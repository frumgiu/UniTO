(defmodule AGENT (import MAIN ?ALL) (import ENV ?ALL) (export ?ALL))

;; Tengo una copia su cui lavorare di k-per-row e k-per-col;
;; così posso modificare il valore dello slot num
(deftemplate k-left-per-row
	(slot row)
	(slot num)
)

(deftemplate k-left-per-col
	(slot col)
	(slot num)
)

;; Uso per trovare l'indice maggiore delle righe e delle colonne
(deftemplate row-idx
  (multislot xs)
)

(deftemplate col-idx
  (multislot ys)
)

;; Coordinate di k-left-per-* in cui andare a decrementare di 1 lo slot num
;; delle coordinate x e y
(deftemplate dec
  (slot x)
  (slot y)
)

(deftemplate guess
  (slot x)
  (slot y)
)

(deffacts init
  (row-idx)
  (col-idx)
)

;; Creo una copia di k-per-row che posso modificare
(defrule init-k-left-per-row (declare (salience 20))
  (k-per-row (row ?x) (num ?nx))
=>
  (assert (k-left-per-row (row ?x) (num ?nx)))
)

;; Creo una copia di k-per-col che posso modificare
(defrule init-k-left-per-col (declare (salience 20))
  (k-per-col (col ?y) (num ?ny))
=>
  (assert (k-left-per-col (col ?y) (num ?ny)))
)

;; Creo una lista con i numeri delle righe
(defrule init-row-idx (declare (salience 20))
  (k-per-row (row ?x))
  ?r-idx <- (row-idx (xs $?xs&:(not (member$ ?x $?xs))))
=>
  (modify ?r-idx (xs (insert$ $?xs (+ (length$ $?xs) 1) ?x)))
)

;; Creo una lista con i numeri delle colonne
(defrule init-col-idx (declare (salience 20))
  (k-per-col (col ?y))
  ?c-idx <- (col-idx (ys $?ys&:(not (member$ ?y $?ys))))
=>
  (modify ?c-idx (ys (insert$ $?ys (+ (length$ $?ys) 1) ?y)))
)

;; Se ho sparato e non ho beccato acqua, devo decrementare k-left-per-row
;; e k-left-per-col delle coordinate in cui ho sparato
(defrule assert-dec-k-left-fire (declare (salience 10))
  (k-cell (x ?x) (y ?y) (content ?c&:(neq ?c water)))
=>
  (assert (dec (x ?x) (y ?y)))
)

;; Decremento di uno alle coordinate x e y
(defrule dec-k-left (declare (salience 10))
  ?dec <- (dec (x ?x) (y ?y))
  ?klr <- (k-left-per-row (row ?x) (num ?nx))
  ?klc <- (k-left-per-col (col ?y) (num ?ny))
=>
  (modify ?klr (num (- ?nx 1)))
  (modify ?klc (num (- ?ny 1)))
  (retract ?dec)
)

(defrule assert-guess-fire-ok (declare (salience 10))
 (k-cell (x ?x) (y ?y) (content ?c&:(neq ?c water))) 
=>
  (assert (guess (x ?x) (y ?y)))
)

(defrule guess-fire-ok (declare (salience 10))
  (status (step ?s) (currently running))
  ?g <- (guess (x ?x) (y ?y))
=>
  (assert (exec (step ?s) (action guess) (x ?x) (y ?y)))
  (retract ?g)
  (pop-focus)
)

;; Inizio il gioco usando tutte e 5 le fire.
;; Sparo nei punti più promettenti, ovvero le celle dove il valore num di k-left-per-row
;; e il valore num di k-left-per-col sono quelli massimi di righe e colonne.
;; La cella su cui sparo deve essere sconosciuta, quindi non deve esistere un fatto k-cell 
;; con quelle coordinate.
(defrule max-fire
  (status (step ?s) (currently running))
  (moves (fires ?nf &:(> ?nf 0)))
  (row-idx (xs $? ?x $?))
  (col-idx (ys $? ?y $?))
  (not (k-cell (x ?x) (y ?y)))
  (not (exec (action guess) (x ?x) (y ?y)))
  (k-left-per-row (row ?x) (num ?nx))
  (k-left-per-col (col ?y) (num ?ny))
  (not 
    (exists (k-left-per-row (row ?x1) (num ?nx1))
      (exists (k-left-per-col (col ?y1) (num ?ny1))
        (not (k-cell (x ?x1) (y ?y1)))
        (not (exec (action guess) (x ?x1) (y ?y1)))
        (test (> (+ ?nx1 ?ny1) (+ ?nx ?ny)))
      )
    )
  )
=>
  (assert (exec (step ?s) (action fire) (x ?x) (y ?y)))
  (pop-focus)
)

;; REGOLE CERTE DI MOVIMENTO -> Sicuramente colpisco un pezzo di nave
;; Ho una k-cell con contenuto left ==> metto una guess a destra
(defrule guess-right-sure (declare (salience 5))
  (status (step ?s) (currently running))
  (k-cell (x ?x) (y ?y) (content left)) ;;eq ?c 
  (not (exec (action guess) (x ?x) (y ?y1&:(eq ?y1 (+ ?y 1)))))
  (not (k-cell (x ?x) (y ?y1&:(eq ?y1 (+ ?y 1)))))
=>
  (assert (exec (step ?s) (action guess) (x ?x) (y (+ ?y 1))))
  (assert (dec (x ?x) (y (+ ?y 1))))
  (pop-focus)
)

;; Ho una k-cell con contenuto right ==> metto una guess a sinistra
(defrule guess-left-sure (declare (salience 5))
  (status (step ?s) (currently running))
  (k-cell (x ?x) (y ?y) (content right))
  (not (exec (action guess) (x ?x) (y ?y1&:(eq ?y1 (- ?y 1)))))
  (not (k-cell (x ?x) (y ?y1&:(eq ?y1 (- ?y 1)))))
=>
  (assert (exec (step ?s) (action guess) (x ?x) (y (- ?y 1))))
  (assert (dec (x ?x) (y (- ?y 1))))
  (pop-focus)
)

;; Ho una k-cell con contenuto top ==> metto una guess sotto
(defrule guess-bot-sure (declare (salience 5))
  (status (step ?s) (currently running))
  (k-cell (x ?x) (y ?y) (content top))
  (not (exec (action guess) (x ?x1&:(eq ?x1 (+ ?x 1))) (y ?y)))
  (not (k-cell (x ?x1&:(eq ?x1 (+ ?x 1))) (y ?y)))
=>
  (assert (exec (step ?s) (action guess) (x (+ ?x 1)) (y ?y)))
  (assert (dec (x (+ ?x 1)) (y ?y)))
  (pop-focus)
)

;; Ho una k-cell con contenuto bot ==> metto una guess sopra
(defrule guess-top-sure (declare (salience 5))
  (status (step ?s) (currently running))
  (k-cell (x ?x) (y ?y) (content bot))
  (not (exec (action guess) (x ?x1&:(eq ?x1 (- ?x 1))) (y ?y)))
  (not (k-cell (x ?x1&:(eq ?x1 (- ?x 1))) (y ?y)))
=>
  (assert (exec (step ?s) (action guess) (x (- ?x 1)) (y ?y)))
  (assert (dec (x (- ?x 1)) (y ?y)))
  (pop-focus)
)

;; Ho una k-cell con contenuto mid e sono su una "parete" verticale 
;; ==> metto una guess sotto
(defrule guess-mid-ver-wall-bot-sure (declare (salience 5))
  (status (step ?s) (currently running))
  (col-idx (ys $?ys))
                                            ;; la max di una lista vuota non è definita
  (k-cell (x ?x) (y ?y&:(or (eq ?y 0) (eq ?y (max -1 (expand$ $?ys))))) (content middle)) ;; TODO: si può fare meglio?
  (not (exec (action guess) (x ?x1&:(eq ?x1 (+ ?x 1))) (y ?y)))
  (not (k-cell (x ?x1&:(eq ?x1 (+ ?x 1))) (y ?y)))
=>
  (assert (exec (step ?s) (action guess) (x (+ ?x 1)) (y ?y)))
  (assert (dec (x (+ ?x 1)) (y ?y)))
  (pop-focus)
)

;; Ho una k-cell con contenuto mid e sono su una "parete" verticale 
;; ==> metto una guess sopra
(defrule guess-mid-ver-wall-top-sure (declare (salience 5))
  (status (step ?s) (currently running))
  (col-idx (ys $?ys))
                                            ;; la max di una lista vuota non è definita
  (k-cell (x ?x) (y ?y&:(or (eq ?y 0) (eq ?y (max -1 (expand$ $?ys))))) (content middle)) ;; TODO: si può fare meglio?
  (not (exec (action guess) (x ?x1&:(eq ?x1 (- ?x 1))) (y ?y)))
  (not (k-cell (x ?x1&:(eq ?x1 (- ?x 1))) (y ?y)))
=>
  (assert (exec (step ?s) (action guess) (x (- ?x 1)) (y ?y)))
  (assert (dec (x (- ?x 1)) (y ?y)))
  (pop-focus)
)

;; Ho una k-cell con contenuto mid e sono su una "parete" orizzontale 
;; ==> metto una guess a destra
(defrule guess-mid-hor-wall-right-sure (declare (salience 5))
  (status (step ?s) (currently running))
  (row-idx (xs $?xs))
                                            ;; la max di una lista vuota non è definita
  (k-cell (x ?x&:(or (eq ?x 0) (eq ?x (max -1 (expand$ $?xs))))) (y ?y) (content middle)) ;; TODO: si può fare meglio?
  (not (exec (action guess) (x ?x) (y ?y1&:(eq ?y1 (+ ?y 1)))))
  (not (k-cell (x ?x) (y ?y1&:(eq ?y1 (+ ?y 1)))))
=>
  (assert (exec (step ?s) (action guess) (x ?x) (y (+ ?y 1))))
  (assert (dec (x ?x) (y (+ ?y 1))))
  (pop-focus)
)

;; Ho una k-cell con contenuto mid e sono su una "parete" orizzontale 
;; ==> metto una guess a sinistra
(defrule guess-mid-hor-wall-left-sure (declare (salience 5))
  (status (step ?s) (currently running))
  (row-idx (xs $?xs))
                                            ;; la max di una lista vuota non è definita
  (k-cell (x ?x&:(or (eq ?x 0) (eq ?x (max -1 (expand$ $?xs))))) (y ?y) (content middle)) ;; TODO: si può fare meglio?
  (not (exec (action guess) (x ?x) (y ?y1&:(eq ?y1 (- ?y 1)))))
  (not (k-cell (x ?x) (y ?y1&:(eq ?y1 (- ?y 1)))))
=>
  (assert (exec (step ?s) (action guess) (x ?x) (y (- ?y 1))))
  (assert (dec (x ?x) (y (- ?y 1))))
  (pop-focus)
)

;; TODO: in teoria se la riga dove si trova il mid ha num == 0 la direzione della barca sarà verticale
;; Ho una k-cell con contenuto mid e o la colonna a sinistra o la colonna a destra ha k-left-per-col 0
;; ==> metto una guess sotto
(defrule guess-mid-ver-bot-sure (declare (salience 5))
  (status (step ?s) (currently running))
  (k-cell (x ?x) (y ?y) (content middle))
  (or
    (k-left-per-col (col ?y1&:(eq ?y1 (- ?y 1))) (num 0))
    (k-left-per-col (col ?y2&:(eq ?y2 (+ ?y 1))) (num 0))
    (k-left-per-row (row ?x) (num 0)) ;; NUOVO, dal TODO
  )
  (not
    (or
      (exec (action guess) (x ?x) (y ?y3&:(eq ?y3 (- ?y 1))))
      (exec (action guess) (x ?x) (y ?y4&:(eq ?y4 (+ ?y 1))))
      (k-cell (x ?x) (y ?y5&:(eq ?y5 (- ?y 1))))
      (k-cell (x ?x) (y ?y6&:(eq ?y6 (+ ?y 1))))
    )
  )
  (not (exec (action guess) (x ?x1&:(eq ?x1 (+ ?x 1))) (y ?y)))
  (not (k-cell (x ?x1&:(eq ?x1 (+ ?x 1))) (y ?y)))
=>
  (assert (exec (step ?s) (action guess) (x (+ ?x 1)) (y ?y)))
  (assert (dec (x (+ ?x 1)) (y ?y)))
  (pop-focus)
)

;; Ho una k-cell con contenuto mid e o la colonna a sinistra o la colonna a destra ha k-left-per-col 0
;; ==> metto una guess sotto
(defrule guess-mid-ver-top-sure (declare (salience 5))
  (status (step ?s) (currently running))
  (k-cell (x ?x) (y ?y) (content middle))
  (or
    (k-left-per-col (col ?y1&:(eq ?y1 (- ?y 1))) (num 0))
    (k-left-per-col (col ?y2&:(eq ?y2 (+ ?y 1))) (num 0))
    (k-left-per-row (row ?x) (num 0)) ;; NUOVO, dal TODO
  )
  (not
    (or
      (exec (action guess) (x ?x) (y ?y3&:(eq ?y3 (- ?y 1))))
      (exec (action guess) (x ?x) (y ?y4&:(eq ?y4 (+ ?y 1))))
      (k-cell (x ?x) (y ?y5&:(eq ?y5 (- ?y 1))))
      (k-cell (x ?x) (y ?y6&:(eq ?y6 (+ ?y 1))))
    )
  )
  (not (exec (action guess) (x ?x1&:(eq ?x1 (- ?x 1))) (y ?y)))
  (not (k-cell (x ?x1&:(eq ?x1 (- ?x 1))) (y ?y)))
=>
  (assert (exec (step ?s) (action guess) (x (- ?x 1)) (y ?y)))
  (assert (dec (x (- ?x 1)) (y ?y)))
  (pop-focus)
)

;; Ho una k-cell con contenuto mid e o la riga sopra o la riga sotto ha k-left-per-row 0
;; ==> metto una guess a sinistra
(defrule guess-mid-hor-right-sure (declare (salience 5))
  (status (step ?s) (currently running))
  (k-cell (x ?x) (y ?y) (content middle))
  (or
    (k-left-per-row (row ?x1&:(eq ?x1 (- ?x 1))) (num 0))
    (k-left-per-row (row ?x2&:(eq ?x2 (+ ?x 1))) (num 0))
    (k-left-per-col (col ?y) (num 0)) ;; NUOVO, dal TODO
  )
  (not
    (or
      (exec (action guess) (x ?x3&:(eq ?x3 (- ?x 1))) (y ?y))
      (exec (action guess) (x ?x4&:(eq ?x4 (+ ?x 1))) (y ?y))
      (k-cell (x ?x5&:(eq ?x5 (- ?x 1))) (y ?y))
      (k-cell (x ?x6&:(eq ?x6 (+ ?x 1))) (y ?y))
    )
  )
  (not (exec (action guess) (x ?x) (y ?y1&:(eq ?y1 (+ ?y 1)))))
  (not (k-cell (x ?x) (y ?y1&:(eq ?y1 (+ ?y 1)))))
=>
  (assert (exec (step ?s) (action guess) (x ?x) (y (+ ?y 1))))
  (assert (dec (x ?x) (y (+ ?y 1))))
  (pop-focus)
)

;; Ho una k-cell con contenuto mid e o la riga sopra o la riga sotto ha k-left-per-row 0
;; ==> metto una guess a sinistra
(defrule guess-mid-hor-left-sure (declare (salience 5))
  (status (step ?s) (currently running))
  (k-cell (x ?x) (y ?y) (content middle))
  (or
    (k-left-per-row (row ?x1&:(eq ?x1 (- ?x 1))) (num 0))
    (k-left-per-row (row ?x2&:(eq ?x2 (+ ?x 1))) (num 0))
    (k-left-per-col (col ?y) (num 0)) ;; NUOVO, dal TODO
  )
  (not
    (or
      (exec (action guess) (x ?x3&:(eq ?x3 (- ?x 1))) (y ?y))
      (exec (action guess) (x ?x4&:(eq ?x4 (+ ?x 1))) (y ?y))
      (k-cell (x ?x5&:(eq ?x5 (- ?x 1))) (y ?y))
      (k-cell (x ?x6&:(eq ?x6 (+ ?x 1))) (y ?y))
    )
  )
  ;; (not (exec (action guess|fire) (x ?x) (y ?y1&:(eq ?y1 (- ?y 1))))) ;; TODO: problema!! Risolto.
  (not (exec (action guess) (x ?x) (y ?y1&:(eq ?y1 (- ?y 1)))))
  (not (k-cell (x ?x) (y ?y1&:(eq ?y1 (- ?y 1)))))
=>
  (assert (exec (step ?s) (action guess) (x ?x) (y (- ?y 1))))
  (assert (dec (x ?x) (y (- ?y 1))))
  (pop-focus)
)

;; Ho una k-cell con contenuto mid che ha una k-cell con contenuto mid sotto
;; ==> metto una guess ancora sotto
(defrule guess-mid-mid-ver-bot-sure (declare (salience 5))
  (status (step ?s) (currently running))
  (k-cell (x ?x) (y ?y) (content middle))
  (k-cell (x ?x1&:(eq ?x1 (+ ?x 1))) (y ?y) (content middle))
  (not (exec (action guess) (x ?x2&:(eq ?x2 (+ ?x 2))) (y ?y)))
  (not (k-cell (x ?x2&:(eq ?x2 (+ ?x 2))) (y ?y)))
=>
  (assert (exec (step ?s) (action guess) (x (+ ?x 2)) (y ?y)))
  (assert (dec (x (+ ?x 2)) (y ?y)))
  (pop-focus)
)

;; Ho una k-cell con contenuto mid che ha una k-cell con contenuto mid sopra
;; ==> metto una guess ancora sopra
(defrule guess-mid-mid-ver-top-sure (declare (salience 5))
  (status (step ?s) (currently running))
  (k-cell (x ?x) (y ?y) (content middle))
  (k-cell (x ?x1&:(eq ?x1 (- ?x 1))) (y ?y) (content middle))
  (not (exec (action guess) (x ?x2&:(eq ?x2 (- ?x 2))) (y ?y)))
  (not (k-cell (x ?x2&:(eq ?x2 (- ?x 2))) (y ?y)))
=>
  (assert (exec (step ?s) (action guess) (x (- ?x 2)) (y ?y)))
  (assert (dec (x (- ?x 2)) (y ?y)))
  (pop-focus)
)

;; Ho una k-cell con contenuto mid che ha una k-cell con contenuto mida sinistra
;; ==> metto una guess ancora a destra
(defrule guess-mid-mid-hor-right-sure (declare (salience 5))
  (status (step ?s) (currently running))
  (k-cell (x ?x) (y ?y) (content middle))
  (k-cell (x ?x) (y ?y1&:(eq ?y1 (+ ?y 1))) (content middle))
  (not (exec (action guess) (x ?x) (y ?y2&:(eq ?y2 (+ ?y 2)))))
  (not (k-cell (x ?x) (y ?y2&:(eq ?y2 (+ ?y 2)))))
=>
  (assert (exec (step ?s) (action guess) (x ?x) (y (+ ?y 2))))
  (assert (dec (x ?x) (y (+ ?y 2))))
  (pop-focus)
)

;; Ho una k-cell con contenuto mid che ha una k-cell con contenuto mid a destra
;; ==> metto una guess ancora a sinistra
(defrule guess-mid-mid-hor-right-sure (declare (salience 5))
  (status (step ?s) (currently running))
  (k-cell (x ?x) (y ?y) (content middle))
  (k-cell (x ?x) (y ?y1&:(eq ?y1 (- ?y 1))) (content middle))
  (not (exec (action guess) (x ?x) (y ?y2&:(eq ?y2 (- ?y 2)))))
  (not (k-cell (x ?x) (y ?y2&:(eq ?y2 (- ?y 2)))))
=>
  (assert (exec (step ?s) (action guess) (x ?x) (y (- ?y 2))))
  (assert (dec (x ?x) (y (- ?y 2))))
  (pop-focus)
)

;; REGOLE INCERTE DI MOVIMENTO
;; Se non ho alternative ==> metto una guess nella cella di punteggio massimo (riga/colonna)
(defrule max-guess (declare (salience -5))
  (status (step ?s) (currently running))
  (moves (guesses ?ng &:(> ?ng 0)))
  (row-idx (xs $? ?x $?))
  (col-idx (ys $? ?y $?))
  (not (exec (action guess) (x ?x) (y ?y)))
  (not (k-cell (x ?x) (y ?y)))
  (k-left-per-row (row ?x) (num ?nx))
  (k-left-per-col (col ?y) (num ?ny))
  (not 
    (exists (k-left-per-row (row ?x1) (num ?nx1))
      (exists (k-left-per-col (col ?y1) (num ?ny1))
        (not (exec (action guess) (x ?x1) (y ?y1)))
        (not (k-cell (x ?x1) (y ?y1)))
        (test (> (+ ?nx1 ?ny1) (+ ?nx ?ny)))
      )
    )
  )
=>
  (assert (exec (step ?s) (action guess) (x ?x) (y ?y)))
  ;; Riga 425 commentare o scommentare in base a che strategia vuoi usare
  ;;(assert (dec (x ?x) (y ?y)))
  (pop-focus)
)

(defrule exec-solve (declare (salience -10))
  (status (step ?s) (currently running))
=>
  (assert (exec (step ?s) (action solve)))
)