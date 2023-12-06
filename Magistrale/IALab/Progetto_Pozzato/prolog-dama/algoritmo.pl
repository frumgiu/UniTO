% --- Strategia di Ricerca ---
% Algoritmo minmax con potatura alpha-beta 
prova(Mossa) :- 
	write('-- INIZIO SIMULAZIONE --'), nl, nl,
	alphabeta(bianco, -100, 100, Mossa, Val),
	write('-- FINE SIMULAZIONE --'), nl, nl,
	valore_damiera(ValTemp2), write(ValTemp2), nl, nl.

alphabeta(Giocatore, Alpha, Beta, ProssimaMossa, Val, Depth) :- 
	fine_partita(Giocatore),
	write('Per qualche motivo è finita la partita al giocatore '), write(Giocatore), nl, nl,
	valore_damiera(Val), !.

alphabeta(Giocatore, Alpha, Beta, ProssimaMossa, Val) :-
	lista_mosse(Giocatore, ListaMosse),
	bounded_best(Giocatore, Alpha, Beta, ListaMosse, ProssimaMossa, Val), !.

bounded_best(Giocatore, Alpha, Beta, [Mossa|ListaMosse], MigliorMossa, MigliorVal) :-
	dechain(Mossa, MossaFatta),
	prendi_val(MossaFatta, Pos, NomeMossa),
	fai_azione(NomeMossa, Pos, NuovaPos, PedinaRimossa, PosizioneRimossa),
	giocatori_ordine(Giocatore, GiocatoreSucc),
	alphabeta(GiocatoreSucc, Alpha, Beta, _, Val),
	annulla_azione(NomeMossa, NuovaPos, NuovaNuovaPos, PedinaRimossa, PosizioneRimossa),
	good_enough(Giocatore, ListaMosse, Alpha, Beta, MossaFatta, Val, MigliorMossa, MigliorVal).

% Non posso fare altre mosse dopo l'ultima
good_enough(Giocatore, [], _, _, Mossa, Val, Mossa, Val) :- !.

good_enough(Giocatore, _, Alpha, Beta, Mossa, Val, Mossa, Val) :- 
	max_muovere(Giocatore), Val < Alpha, !;
	min_muovere(Giocatore), Val > Beta, !.

good_enough(Giocatore, ListaMosse, Alpha, Beta, Mossa, Val, MigliorMossa, MigliorVal) :- 
	new_bounds(Giocatore, Alpha, Beta, Val, NuovoAlpha, NuovoBeta),
	bounded_best(Giocatore, NuovoAlpha, NuovoBeta, ListaMosse, Mossa1, Val1),
	better_of(Giocatore, Mossa, Val, Mossa1, Val1, MigliorMossa, MigliorVal).

new_bounds(Giocatore, Alpha, Beta, Val, Val, Beta) :-
	min_muovere(Giocatore), 
	Val > Alpha, !.

new_bounds(Giocatore, Alpha, Beta, Val, Alpha, Val) :-
	max_muovere(Giocatore), 
	Val < Beta, !.

new_bounds(Giocatore, Alpha, Beta, _, Alpha, Beta).

better_of(Giocatore, Mossa, Val, Mossa1, Val1, Mossa, Val) :-
	min_muovere(Giocatore),
	Val >= Val1, !;
	max_muovere(Giocatore),
	Val =< Val1, !.

better_of(_, _, _, Mossa1, Val1, Mossa1, Val1).

% Chiedo tutte le mosse che il Giocatore può fare
lista_mosse(Giocatore, ListaMosse) :- 
	findall(Pedine, giocatore_pedina(Giocatore, Pedine, _), PedineListe),					% Ritorno il tipo di pedine associate a quel giocatore							
	findall(Pos, (member(Pedina, PedineListe), occupata(Pos, Pedina)), PosGiocatore),		% Tutte le posizioni con pedine di Giocatore
	findall([Pos, NomeMossa], (member(Pos, PosGiocatore), lista_mosse_valide(Pos, ListaMosse), member(NomeMossa, ListaMosse)), ListaMosse).

% Chiedo tutte le mosse valide che il Giocatore può fare in una data Posizione
lista_mosse_valide(Pos, ListaMosse) :-
	findall(NomeMossa, applicabile(NomeMossa, Pos), NormalMoves),						% Controllo tutte le possibili mosse 
	findall(NomeMossaMangia, applicabile_mangia(NomeMossaMangia, Pos), EatingMoves),	% Controllo tutte le possibili mosse dove mangia
	append(NormalMoves, EatingMoves, ListaMosse).

% Estrae elemento dalla lista
dechain([Move], Move).
dechain([Move|Moves], Last) :- last(Moves, Last).
dechain(Move, Move).

% Serve per estrarre i dati da mettere nel predicato trasforma/3
prendi_val([Pos|TempNome], Pos, NomeMossa) :- last(TempNome, NomeMossa).

% Valore damiera
valore_damiera(Valore) :-
    findall(PesoPedina*PesoCasella, (occupata(pos(X, Y), Pedina), giocatore_pedina(_, Pedina, PesoPedina), peso_casella(pos(X, Y), PesoCasella)), PesoLista),
    sumlist(PesoLista, Valore).

% Fine partita --> un giocatore non ha più pedine o un giocatore non ha più mosse da fare
fine_partita(Giocatore) :- 
	lista_mosse(Giocatore, ListaMosse),
	lista_vuota(ListaMosse).

fine_partita(nero) :- 
	\+occupata(_, pedina_nera),
	\+occupata(_, dama_nera).

fine_partita(bianco) :- 
	\+occupata(_, pedina_bianca),
	\+occupata(_, dama_bianca).

lista_vuota(Lista) :- not(member(_, Lista)).

max_muovere(bianco).
min_muovere(nero).