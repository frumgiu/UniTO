% --- Strategia di Ricerca ---

% Algoritmo minmax con potatura alpha-beta - prova(Depth, MossaMigliore)
% L'algoritmo calcola la mossa migliore per il giocatore Bianco, in base alla configurazione della damiera
prova(Mossa) :- 
	write('-- INIZIO SIMULAZIONE --'), nl, nl,
	Depth is 6,
	write('Depth predefinita: '), write(Depth), nl, nl,
	alphabeta(bianco, -300, 300, Mossa, _, Depth),
	stampa(),
	write('-- FINE SIMULAZIONE --'), nl, nl.

% Algoritmo minmax con potatura alpha-beta - prova(Depth, MossaMigliore)
% L'algoritmo calcola la mossa migliore per il giocatore Bianco, in base alla configurazione della damiera
prova_depth(Depth, Mossa) :- 
	write('-- INIZIO SIMULAZIONE --'), nl, nl,
	write('Depth scelta: '), write(Depth), nl,
	alphabeta(bianco, -300, 300, Mossa, _, Depth),
	write('-- FINE SIMULAZIONE --'), nl, nl.

alphabeta(Giocatore, _, _, _, Val, Depth) :- 
	Depth > 0,
	fine_partita(Giocatore),
	valore_damiera(Val), !.

alphabeta(Giocatore, Alpha, Beta, ProssimaMossa, Val, Depth) :-
	Depth > 0,
	NewDepth is Depth - 1,
	lista_mosse(Giocatore, ListaMosse),
	bounded_best(Giocatore, Alpha, Beta, ListaMosse, ProssimaMossa, Val, NewDepth), !.

alphabeta(_, _, _, _, Val, _) :- valore_damiera(Val), !.

bounded_best(Giocatore, Alpha, Beta, [[Pos, NomeMossa]|ListaMosse], MigliorMossa, MigliorVal, Depth) :-
	fai_azione(NomeMossa, Pos, NuovaPos, PedinaRimossa, PosizioneRimossa),								% Giocatore fa la mossa scelta
	giocatori_ordine(Giocatore, GiocatoreSucc),															% Tocca al giocatore successivo
	alphabeta(GiocatoreSucc, Alpha, Beta, _, Val, Depth),												% Chiamo l'algoritmo per il GiocatoreSucc
	annulla_azione(NomeMossa, NuovaPos, _, PedinaRimossa, PosizioneRimossa),				% Torno allo stato precedente alla mossa
	good_enough(Giocatore, ListaMosse, Alpha, Beta, [Pos, NomeMossa], Val, MigliorMossa, MigliorVal, Depth).	% Valuto la mossa fatta

% Non posso fare altre mosse
good_enough(_, [], _, _, Mossa, Val, Mossa, Val, _) :- !.

good_enough(Giocatore, _, Alpha, Beta, Mossa, Val, Mossa, Val, _) :- 						% Val deve essere contenuto tra Alpha e Beta per essere considerato
	min_muovere(Giocatore), Val < Alpha;														% Val è minore di Alpha
	max_muovere(Giocatore), Val > Beta, !.														% Val maggiore di Beta

good_enough(Giocatore, ListaMosse, Alpha, Beta, Mossa, Val, MigliorMossa, MigliorVal, Depth) :- 
	new_bounds(Giocatore, Alpha, Beta, Val, NuovoAlpha, NuovoBeta),										% Riscrivo i valori dell'intervallo
	bounded_best(Giocatore, NuovoAlpha, NuovoBeta, ListaMosse, Mossa1, Val1, Depth),
	better_of(Giocatore, Mossa, Val, Mossa1, Val1, MigliorMossa, MigliorVal).

% Aggiorno il valore di Alpha
new_bounds(Giocatore, Alpha, Beta, Val, Val, Beta) :-
	max_muovere(Giocatore), 
	Val > Alpha, !.

% Aggiorno il valore di Beta
new_bounds(Giocatore, Alpha, Beta, Val, Alpha, Val) :-
	min_muovere(Giocatore), 
	Val < Beta, !.

% Restano uguali
new_bounds(_, Alpha, Beta, _, Alpha, Beta).

better_of(Giocatore, Mossa, Val, _, Val1, Mossa, Val) :-
	max_muovere(Giocatore),
	Val > Val1, !;
	min_muovere(Giocatore),
	Val < Val1, !.

better_of(_, _, _, Mossa1, Val1, Mossa1, Val1).