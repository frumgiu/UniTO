gioca(Giocatore) :-
	fine_partita(Giocatore),
	write('---- Non giocabile ----').

gioca(Giocatore) :- 
	write('---- INIZIO PARTITA ----'), nl, nl,
	stampa(),
	turno(Giocatore),
	write('---- FINE PARTITA ----'), nl,
	stampa().

turno(Giocatore) :-
	fine_partita(Giocatore).

turno(Giocatore) :-
	turno_giocatore(Giocatore, _),
	giocatori_ordine(Giocatore, GiocatoreSucc),
	turno(GiocatoreSucc), !.

turno_giocatore(bianco, _) :-
	write('--------------------'), nl,
	write('TURNO GIOCATORE BIANCO'), nl,
	write('--------------------'), nl, nl,
	prova_partita(Mossa),
	write('Esegue mossa: '), write(Mossa), nl, nl,
	estrai(Mossa, PosBianco, NomeMossaBianco),
	fai_azione(NomeMossaBianco, PosBianco, _, _, _),
	stampa(), !.

turno_giocatore(nero, Scelta) :-
	write('--------------------'), nl,
	write('TURNO GIOCATORE NERO'), nl,
	write('--------------------'), nl, nl,
	lista_mosse(nero, ListaMosse),
	write('Mosse possibili che puoi fare:'), nl, write(ListaMosse), nl, nl,
	write('Scegli una mossa: '), nl,
	read(Scelta),
	estrai(Scelta, PosNero, NomeMossaNero),
	fai_azione(NomeMossaNero, PosNero,  _, _, _),
	stampa(), !.

estrai([Pos, Nome], Pos, Nome).

prova_partita(Mossa) :- 
	Depth is 6,
	alphabeta(bianco, -300, 300, Mossa, _, Depth).

stampa() :- 
	write('        1       2       3       4       5       6 '), nl,
	stampa_damiera(1), nl, nl, !.

stampa_damiera(7).

stampa_damiera(Riga) :-
	write(Riga), stampa_riga(Riga, 1), write(' '), nl,
	RigaDopo is Riga + 1,
	stampa_damiera(RigaDopo).

stampa_riga(_, 7).

stampa_riga(Riga, Colonna) :-
	write('       '), ottieni_pezzo(pos(Riga, Colonna), Simbolo), write(Simbolo),
	ColonnaDopo is Colonna + 1,
	stampa_riga(Riga, ColonnaDopo).

ottieni_pezzo(Pos, '-') :- 
	\+occupata(Pos, _).

ottieni_pezzo(Pos, n) :- 
	occupata(Pos, pedina_nera).

ottieni_pezzo(Pos, 'N') :- 
	occupata(Pos, dama_nera).

ottieni_pezzo(Pos, b) :- 
	occupata(Pos, pedina_bianca).

ottieni_pezzo(Pos, 'B') :- 
	occupata(Pos, dama_bianca).