% -- FATTI DEL DOMINIO --

:- dynamic occupata/2.
% Dimensione damiera
num_righe(6).
num_colonne(6). 

% STATO INIZIALE
occupata(pos(1,2), pedina_nera).
occupata(pos(3,2), pedina_nera).

occupata(pos(4,1), pedina_bianca).
occupata(pos(4,3), dama_bianca).


% Tipi di pedine per ogni giocatore con rispettivo peso
% giocatore_pedina(Giocatore, Pedina, Peso)
giocatore_pedina(nero, pedina_nera, -2).
giocatore_pedina(nero, dama_nera, -5).
giocatore_pedina(bianco, pedina_bianca, 2).
giocatore_pedina(bianco, dama_bianca, 5).

% Ordine di gioco - giocatori_ordine(GiocAttuale, GiocSuccessivo)
giocatori_ordine(bianco, nero).
giocatori_ordine(nero, bianco).

max_muovere(bianco).
min_muovere(nero).

% Pesi delle caselle - peso_casella(Posizione, Pedina, Peso)
% PESI PER LE PEDINE
peso_casella(pos(1, 2), pedina_nera, 2).
peso_casella(pos(1, 4), pedina_nera, 3).
peso_casella(pos(1, 6), pedina_nera, 2).
peso_casella(pos(2, 1), pedina_nera, 3).
peso_casella(pos(2, 3), pedina_nera, 4).
peso_casella(pos(2, 5), pedina_nera, 3).
peso_casella(pos(3, 2), pedina_nera, 5).
peso_casella(pos(3, 4), pedina_nera, 6).
peso_casella(pos(3, 6), pedina_nera, 5).
peso_casella(pos(4, 1), pedina_nera, 5).
peso_casella(pos(4, 3), pedina_nera, 6).
peso_casella(pos(4, 5), pedina_nera, 5).
peso_casella(pos(5, 2), pedina_nera, 6). % 5
peso_casella(pos(5, 4), pedina_nera, 5).
peso_casella(pos(5, 6), pedina_nera, 6).
peso_casella(pos(6, 1), pedina_nera, 8).
peso_casella(pos(6, 3), pedina_nera, 7).
peso_casella(pos(6, 5), pedina_nera, 7).

peso_casella(pos(1, 2), pedina_bianca, 7).
peso_casella(pos(1, 4), pedina_bianca, 7).
peso_casella(pos(1, 6), pedina_bianca, 8).
peso_casella(pos(2, 1), pedina_bianca, 6).
peso_casella(pos(2, 3), pedina_bianca, 5).
peso_casella(pos(2, 5), pedina_bianca, 6). % 5
peso_casella(pos(3, 2), pedina_bianca, 5).
peso_casella(pos(3, 4), pedina_bianca, 6).
peso_casella(pos(3, 6), pedina_bianca, 5).
peso_casella(pos(4, 1), pedina_bianca, 5).
peso_casella(pos(4, 3), pedina_bianca, 6).
peso_casella(pos(4, 5), pedina_bianca, 5). 
peso_casella(pos(5, 2), pedina_bianca, 3).
peso_casella(pos(5, 4), pedina_bianca, 4).
peso_casella(pos(5, 6), pedina_bianca, 3).
peso_casella(pos(6, 1), pedina_bianca, 2).
peso_casella(pos(6, 3), pedina_bianca, 3).
peso_casella(pos(6, 5), pedina_bianca, 2).

% PESI PER LE DAME
peso_casella(pos(_, _), dama_bianca, 1).
peso_casella(pos(_, _), dama_nera, 1).

% Valore damiera
valore_damiera(Valore) :-
    findall(PesoPedina*PesoCasella, (occupata(pos(X, Y), Pedina), giocatore_pedina(_, Pedina, PesoPedina), peso_casella(pos(X, Y), Pedina, PesoCasella)), PesoLista),
    sumlist(PesoLista, Valore).

% Chiedo tutte le mosse che il Giocatore può fare
lista_mosse(Giocatore, ListaMosse) :- 
	findall(Pedine, giocatore_pedina(Giocatore, Pedine, _), PedineListe),					% Ritorno il tipo di pedine associate a quel giocatore							
	findall(Pos, (member(Pedina, PedineListe), occupata(Pos, Pedina)), PosGiocatore),		% Tutte le posizioni con pedine di Giocatore
	findall([Pos, NomeMossa], (member(Pos, PosGiocatore), lista_mosse_valide(Pos, ListaMosse), member(NomeMossa, ListaMosse)), ListaMosse).

% Chiedo tutte le mosse valide che il Giocatore può fare in una data Posizione
lista_mosse_valide(Pos, NormalMoves) :-
	findall(NomeMossaMangia, applicabile_mangia(NomeMossaMangia, Pos), EatingMoves),	% Controllo tutte le possibili mosse dove mangia
	lista_vuota(EatingMoves),
	findall(NomeMossa, applicabile(NomeMossa, Pos), NormalMoves).						% Controllo tutte le possibili mosse 

% Chiedo tutte le mosse valide che il Giocatore può fare in una data Posizione
lista_mosse_valide(Pos, EatingMoves) :-
	findall(NomeMossaMangia, applicabile_mangia(NomeMossaMangia, Pos), EatingMoves).	% Controllo tutte le possibili mosse dove mangia


% Fine partita --> un giocatore non ha più pedine o un giocatore non ha più mosse da fare
fine_partita(Giocatore) :- 
	lista_mosse(Giocatore, ListaMosse),
	lista_vuota(ListaMosse);
	giocatori_ordine(Giocatore, GiocatoreSucc),
	lista_mosse(GiocatoreSucc, ListaMosse),
	lista_vuota(ListaMosse), !.

fine_partita(_) :- 
	\+occupata(_, pedina_nera),
	\+occupata(_, dama_nera);
	\+occupata(_, pedina_bianca),
	\+occupata(_, dama_bianca).

lista_vuota(Lista) :- not(member(_, Lista)).