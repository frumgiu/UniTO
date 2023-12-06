% --- Regole dama ---

% Controllo movimento pedina NERA e DAMA - applicabile(Azione, Stato)
applicabile(giudx, pos(Riga, Colonna)) :- 
    \+occupata(pos(Riga, Colonna), pedina_bianca),
    num_righe(NR),
    Riga < NR,                                              % Controllo se posso andare giù
    num_colonne(NC),
    Colonna < NC,                                           % Controllo se posso andare a dx
    RigaSotto is Riga+1, ColonnaDx is Colonna+1,
    \+occupata(pos(RigaSotto, ColonnaDx), _).               % Mi muovo sempre in diagonale

applicabile(giusx, pos(Riga, Colonna)) :- 
    \+occupata(pos(Riga, Colonna), pedina_bianca),
    num_righe(NR),
    Riga < NR,                                              % Controllo se posso andare giù
    Colonna > 1,                                            % Controllo se posso andare a sx
    RigaSotto is Riga+1, ColonnaSx is Colonna-1,
    \+occupata(pos(RigaSotto, ColonnaSx), _).               

% Controllo movimento pedina BIANCA E DAMA - applicabile(Azione, Stato)
applicabile(sudx, pos(Riga, Colonna)) :- 
    \+occupata(pos(Riga, Colonna), pedina_nera),            
    Riga > 1,                                               % Controllo se posso andare su
    num_colonne(NC),
    Colonna < NC,                                           % Controllo se posso andare a dx
    RigaSopra is Riga-1, ColonnaDx is Colonna+1,
    \+occupata(pos(RigaSopra, ColonnaDx), _).               

applicabile(susx, pos(Riga, Colonna)) :- 
    \+occupata(pos(Riga, Colonna), pedina_nera),           
    Riga > 1,                                               % Controllo se posso andare su
    Colonna > 1,                                            % Controllo se posso andare a sx
    RigaSopra is Riga-1, ColonnaSx is Colonna-1,
    \+occupata(pos(RigaSopra, ColonnaSx), _).               

% Controllo promozioni possibili - applicabile(Azione, Stato)
applicabile(promuovinera, pos(Riga, Colonna)) :-            % Promuovo pedina nera in dama nera
    num_righe(NR),
    Riga = NR,
    occupata(pos(Riga, Colonna), pedina_nera).

applicabile(promuovibianca, pos(Riga, Colonna)) :-          % Promuovo pedina bianca in dama bianca
    Riga = 1,
    occupata(pos(Riga, Colonna), pedina_bianca).

% Controllo movimento pedina NERA e DAMA nel mangiare - applicabile(Azione, Stato)
applicabile_mangia(mangiagiudx, pos(Riga, Colonna)) :-
    \+occupata(pos(Riga, Colonna), pedina_bianca),
    num_righe(NR), NRnew is NR-1,
    Riga < NRnew,
    num_colonne(NC), NCnew is NC-1,
    Colonna < NCnew,
    RigaSotto is Riga+1, ColonnaDx is Colonna+1,            
    nemico(pos(Riga, Colonna), pos(RigaSotto, ColonnaDx)),
    RigaSottoSotto is Riga+2, ColonnaDxDx is Colonna+2, 
    \+occupata(pos(RigaSottoSotto, ColonnaDxDx), _).

applicabile_mangia(mangiagiusx, pos(Riga, Colonna)) :-
    \+occupata(pos(Riga, Colonna), pedina_bianca),
    num_righe(NR), NRnew is NR-1,
    Riga < NRnew,
    Colonna > 2,
    RigaSotto is Riga+1, ColonnaSx is Colonna-1,
    nemico(pos(Riga, Colonna), pos(RigaSotto, ColonnaSx)),
    RigaSottoSotto is Riga+2, ColonnaSxSx is Colonna-2, 
    \+occupata(pos(RigaSottoSotto, ColonnaSxSx), _).        

% Controllo movimento pedina BIANCA e DAMA nel mangiare - applicabile(Azione, Stato)
applicabile_mangia(mangiasudx, pos(Riga, Colonna)) :-
    \+occupata(pos(Riga, Colonna), pedina_nera),
    Riga > 2,
    num_colonne(NC), NCnew is NC-1,
    Colonna < NCnew,
    RigaSopra is Riga-1, ColonnaDx is Colonna+1,
    nemico(pos(Riga, Colonna), pos(RigaSopra, ColonnaDx)),
    RigaSopraSopra is Riga-2, ColonnaDxDx is Colonna+2, 
    \+occupata(pos(RigaSopraSopra, ColonnaDxDx), _).

applicabile_mangia(mangiasusx, pos(Riga, Colonna)) :-
    \+occupata(pos(Riga, Colonna), pedina_nera),
    Riga > 2,
    Colonna > 2,
    RigaSopra is Riga-1, ColonnaSx is Colonna-1,
    nemico(pos(Riga, Colonna), pos(RigaSopra, ColonnaSx)),
    RigaSopraSopra is Riga-2, ColonnaSxSx is Colonna-2, 
    \+occupata(pos(RigaSopraSopra, ColonnaSxSx), _).    

% Eseguo movimento pedina - trasforma(Azione, PosizioneOld, PosizioneNew)
trasforma(giudx, pos(Riga, Colonna), pos(RigaSotto, ColonnaDx)) :- 
    RigaSotto is Riga+1,
    ColonnaDx is Colonna+1,
    retract(occupata(pos(Riga, Colonna), Pedina)),
    assert(occupata(pos(RigaSotto, ColonnaDx), Pedina)).

trasforma(giusx, pos(Riga, Colonna), pos(RigaSotto, ColonnaSx)) :- 
    RigaSotto is Riga+1,
    ColonnaSx is Colonna-1,
    retract(occupata(pos(Riga, Colonna), Pedina)),
    assert(occupata(pos(RigaSotto, ColonnaSx), Pedina)).

trasforma(sudx, pos(Riga, Colonna), pos(RigaSopra, ColonnaDx)) :- 
    RigaSopra is Riga-1,
    ColonnaDx is Colonna+1,
    retract(occupata(pos(Riga, Colonna), Pedina)),
    assert(occupata(pos(RigaSopra, ColonnaDx), Pedina)).

trasforma(susx, pos(Riga, Colonna), pos(RigaSopra, ColonnaSx)) :- 
    RigaSopra is Riga-1,
    ColonnaSx is Colonna-1,
    retract(occupata(pos(Riga, Colonna), Pedina)),
    assert(occupata(pos(RigaSopra, ColonnaSx), Pedina)).

trasforma(promuovinera, pos(Riga, Colonna), pos(Riga, Colonna)) :-
    retract(occupata(pos(Riga, Colonna), pedina_nera)),                 % Rimuovo la pedina
    assert(occupata(pos(Riga, Colonna), dama_nera)).                    % Creo la posizione con la dama

trasforma(promuovibianca, pos(Riga, Colonna), pos(Riga, Colonna)) :-
    retract(occupata(pos(Riga, Colonna), pedina_bianca)),               % Rimuovo la pedina
    assert(occupata(pos(Riga, Colonna), dama_bianca)).                  % Creo la posizione con la dama

% Eseguo movimento e mangiata pedina - trasforma(Azione, PosizioneOld, PosizioneNew)
trasforma(mangiagiudx, pos(Riga, Colonna), pos(RigaSotto, ColonnaDx)) :- 
    RigaSotto is Riga+2,
    ColonnaDx is Colonna+2,
    retract(occupata(pos(Riga, Colonna), Pedina)),
    assert(occupata(pos(RigaSotto, ColonnaDx), Pedina)).

trasforma(mangiagiusx, pos(Riga, Colonna), pos(RigaSotto, ColonnaSx)) :- 
    RigaSotto is Riga+2,
    ColonnaSx is Colonna-2,
    retract(occupata(pos(Riga, Colonna), Pedina)),
    assert(occupata(pos(RigaSotto, ColonnaSx), Pedina)).

trasforma(mangiasudx, pos(Riga, Colonna), pos(RigaSopra, ColonnaDx)) :- 
    RigaSopra is Riga-2,
    ColonnaDx is Colonna+2,
    retract(occupata(pos(Riga, Colonna), Pedina)),
    assert(occupata(pos(RigaSopra, ColonnaDx), Pedina)).

trasforma(mangiasusx, pos(Riga, Colonna), pos(RigaSopra, ColonnaSx)) :- 
    RigaSopra is Riga-2,
    ColonnaSx is Colonna-2,
    retract(occupata(pos(Riga, Colonna), Pedina)),
    assert(occupata(pos(RigaSopra, ColonnaSx), Pedina)).

% fai_azione(Mossa, Posizione, NuovaPosizione, PedinaEliminata, PosizioneEliminata)
% Gli ulitmi due attributi mi servono per annullare la mossa, nel caso questa fosse del tipo mangiaqualcosa
fai_azione(promuovinera, Pos, NuovaPos, pedina_nera, Pos) :-  
    trasforma(promuovinera, Pos, NuovaPos).

fai_azione(promuovibianca, Pos, NuovaPos, pedina_bianca, Pos) :- 
    trasforma(promuovibianca, Pos, NuovaPos).

fai_azione(mangiagiudx, pos(Riga, Colonna), NuovaPos, PedinaRetract, pos(RigaRetract, ColonnaRetract)) :- 
    trasforma(mangiagiudx, pos(Riga, Colonna), NuovaPos),
    RigaRetract is Riga+1, ColonnaRetract is Colonna+1,
    retract(occupata(pos(RigaRetract, ColonnaRetract), PedinaRetract)). 

fai_azione(mangiagiusx, pos(Riga, Colonna), NuovaPos, PedinaRetract, pos(RigaRetract, ColonnaRetract)) :- 
    trasforma(mangiagiusx, pos(Riga, Colonna), NuovaPos),
    RigaRetract is Riga+1, ColonnaRetract is Colonna-1,
    retract(occupata(pos(RigaRetract, ColonnaRetract), PedinaRetract)).

fai_azione(mangiasudx, pos(Riga, Colonna), NuovaPos, PedinaRetract, pos(RigaRetract, ColonnaRetract)) :- 
    trasforma(mangiasudx, pos(Riga, Colonna), NuovaPos),
    RigaRetract is Riga-1, ColonnaRetract is Colonna+1,
    retract(occupata(pos(RigaRetract, ColonnaRetract), PedinaRetract)). % Rimuovo la pedina mangiata

fai_azione(mangiasusx, pos(Riga, Colonna), NuovaPos, PedinaRetract, pos(RigaRetract, ColonnaRetract)) :- 
    trasforma(mangiasusx, pos(Riga, Colonna), NuovaPos),
    RigaRetract is Riga-1, ColonnaRetract is Colonna-1,
    retract(occupata(pos(RigaRetract, ColonnaRetract), PedinaRetract)). 

fai_azione(Mossa, Pos, NuovaPos, _, Pos) :- 
    trasforma(Mossa, Pos, NuovaPos).

% Torna indietro sulla Mossa fatta precentemente, ripristinando lo stato
% annulla_azione(Mossa, Posizione, NuovaPosizione, PedinaEliminata, PosizioneEliminata)
% Gli ulitmi due attributi mi servono per annullare la mossa, nel caso questa fosse del tipo mangiaqualcosa o promuoviqualcosa
annulla_azione(promuovinera, Pos, Pos, pedina_nera, Pos) :-
    retract(occupata(pos(Riga, Colonna), dama_nera)),
    assert(occupata(pos(Riga, Colonna), pedina_nera)).

annulla_azione(promuovibianca, Pos, Pos, pedina_bianca, Pos) :-
    retract(occupata(pos(Riga, Colonna), dama_bianca)),
    assert(occupata(pos(Riga, Colonna), pedina_bianca)).

annulla_azione(giudx, Pos, NuovaPos, _, _) :- 
    trasforma(susx, Pos, NuovaPos). 

annulla_azione(giusx, Pos, NuovaPos, _, _) :- 
    trasforma(sudx, Pos, NuovaPos).

annulla_azione(sudx, Pos, NuovaPos, _, _) :- 
    trasforma(giusx, Pos, NuovaPos).

annulla_azione(susx, Pos, NuovaPos, _, _) :- 
    trasforma(giudx, Pos, NuovaPos). 

annulla_azione(mangiagiudx, Pos, NuovaPos, PedinaRetract, pos(RigaRetract, ColonnaRetract)) :- 
    trasforma(mangiasusx, Pos, NuovaPos),
    assert(occupata(pos(RigaRetract, ColonnaRetract), PedinaRetract)). 

annulla_azione(mangiagiusx, Pos, NuovaPos, PedinaRetract, pos(RigaRetract, ColonnaRetract)) :- 
    trasforma(mangiasudx, Pos, NuovaPos),
    assert(occupata(pos(RigaRetract, ColonnaRetract), PedinaRetract)).

annulla_azione(mangiasudx, Pos, NuovaPos, PedinaRetract, pos(RigaRetract, ColonnaRetract)) :- 
    trasforma(mangiagiusx, Pos, NuovaPos),
    assert(occupata(pos(RigaRetract, ColonnaRetract), PedinaRetract)). % Rimuovo la pedina mangiata

annulla_azione(mangiasusx, Pos, NuovaPos, PedinaRetract, pos(RigaRetract, ColonnaRetract)) :- 
    trasforma(mangiagiudx, Pos, NuovaPos),
    assert(occupata(pos(RigaRetract, ColonnaRetract), PedinaRetract)). 

% Controllo che la pedina da mangiare sia nemica - nemico(Posizione, PosizioneNuova)
nemico(Pos, PosNew) :-
    occupata(Pos, Pedina), giocatore_pedina(Giocatore, Pedina, _),              % Mi dice il giocatore attuale
    giocatori_ordine(Giocatore, GiocatoreDopo),                                 % Mi dice chi gioca dopo Giocatore
    occupata(PosNew, PedinaNew), giocatore_pedina(GiocatoreDopo, PedinaNew, _). % Se la pedina è del giocatore dopo posso mangiarla