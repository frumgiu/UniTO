%||= = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =||
%||= = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =||
%||= = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =||
%||     IALab - Aprile 2023                                                                     ||
%||     Project: CLINGO - Answer Set Programming                                                || 
%||     Title: Champions League                                                                 ||
%||     clingo championsleague.cl --out-ifs='\n' -t 6                                           ||
%||= = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =||
%||     HOW TO RUN:                                                                             ||
%||     - Singolo Thread:        clingo finale2.cl --out-ifs='\n'                               ||
%||     - Multipli Threads:     clingo finale2.cl --out-ifs='\n' -t n   (n = 6 for 6 threads)   ||
%||= = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =||
%||= = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =||
%||= = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =||



%||= = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =||
%||     RICHIESTA 1 (Obbligatorio):                                                             ||        
%||     Sono iscritte 20 squadre                                                                ||
%||= = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =||

% ITALY (Serie A)
squadra("Juventus FC").
squadra("FC Inter").
squadra("AC Milan").
squadra("SSC Napoli").

% SPAIN (La Liga)
squadra("FC Barcelona").
squadra("Real Madrid CdF").
squadra("Atletico de Madrid").
squadra("Sevilla FC").

% ENGLAND (Premeir League)
squadra("Manchester City FC").
squadra("Liverpool FC").
squadra("Chelsea FC").
squadra("Tottenham Hotspur FC").

% GERMANY (Bundesliga)
squadra("FC Bayern Munchen").
squadra("Bayer 04 Leverkusen").
squadra("Borussia 09 Dortmund").
squadra("RasenBallsport Leipzig").

% FRANCE (Ligue 1)
squadra("Paris Saint-Germain FC").
squadra("Olympique de Marseille").
squadra("AS Monaco FC").
squadra("Rennes FC").


%||= = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =||
%||     RICHIESTA 2 (Obbligatorio):                                                             ||
%||     Ogni squadra fa riferimento ad una città, che offre la struttura in cui la squadra      ||
%||     gioca gli incontri in casa                                                              ||
%||= = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =||

% ITALY (Serie A)
stadio("Juventus FC"            ,   "Allianz Stadium - Torino, Italy").
stadio("FC Inter"               ,   "Giuseppe Meazza (San Siro) - Milano, Italy").
stadio("AC Milan"               ,   "Giuseppe Meazza (San Siro) - Milano, Italy").
stadio("SSC Napoli"             ,   "Diego Armando Maradona - Napoli, Italy").

% SPAIN (La Liga)
stadio("FC Barcelona"           ,   "Camp Nou - Barcelona, Spain").
stadio("Real Madrid CdF"        ,   "Estadio Santiago Bernabeu - Madrid, Spain").
stadio("Atletico de Madrid"     ,   "Civitas Metropolitano - Madrid, Spain").
stadio("Sevilla FC"             ,   "Stadio Ramon Sanchez-Pizjuan - Sevilla, Spain").

% ENGLAND (Premeir League)
stadio("Manchester City FC"     ,   "Etihad Stadium - Manchester, England").
stadio("Liverpool FC"           ,   "Anfield - Merseyside, England").
stadio("Chelsea FC"             ,   "Stamford Bridge - London, England").
stadio("Tottenham Hotspur FC"   ,   "Tottenham Hotspur Stadium - London, England").

% GERMANY (Bundesliga)
stadio("FC Bayern Munchen"      ,   "Allianz Arena - Munchen, Germany").
stadio("Bayer 04 Leverkusen"    ,   "BayArena - Leverkusen, Germany").
stadio("Borussia 09 Dortmund"   ,   "Signal Iduna Park - Dortmund, Germany").
stadio("RasenBallsport Leipzig" ,   "Red Bull Arena - Leipzig, Germany").

% FRANCE (Ligue 1)
stadio("Paris Saint-Germain FC" ,   "Parc des Princes - Paris, France").
stadio("Olympique de Marseille" ,   "Stadio Vélodrome - Marseille, France").
stadio("AS Monaco FC"           ,   "Louis II - Monaco, Principality of Monaco").
stadio("Rennes FC"              ,   "Roazhon Park - Rennes, France").


%||= = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =||
%||     COSTANTI varie per definire:                                                            ||
%||     1)  numero di squadre;                                                                  ||
%||     2)  numero di giornate,                                                                 ||
%||     3)  numero di giornate andata / numero di giornate ritorno                              ||
%||     5)  numero di partite per giornata,                                                     ||
%||     6)  distanza tra una parita di andata e di ritorno tra due squadre                      ||
%||= = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =||
%*      1) *% #const num_squadre = 20.
%*      2) *% #const num_giornate = (num_squadre - 1) * 2.      % 20 sq -> 38 giornate
%*      3) *% #const num_giornate_AR = (num_giornate / 2).      % 38 giornate -> 19 A + 19 R
%*      4) *% #const num_partite_giornata = (num_squadre / 2).  % 20 sq -> 10 parite a giornata 
%*      5) *% #const distanza_AR = 10.                              


%||= = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =||
%||     RICHIESTA 3 (Obbligatorio):                                                             ||
%||     Il campionato prevede 38 giornate, 19 di andata e 19 di ritorno NON                     ||
%||     simmetriche, ossia la giornata 1 di ritorno non coincide necessariamente con la         ||
%||     giornata 1 di andata a campi invertiti                                                  ||
%||= = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =||
numero_giornata(1..num_giornate).




%||= = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =||
%||     RICHIESTE 4 e 5 (Obbligatorio):                                                         ||
%||     4)  Ogni squadra affronta due volte tutte le altre squadre, una volta in casa e una     ||
%||         volta fuori casa, ossia una volta nella propria città di riferimento e una volta in ||
%||         quella dell’altra squadra;                                                          ||
%||     5)  Due delle 20 squadre fanno riferimento alla medesima città e condividono la         ||
%||         stessa struttura di gioco, quindi non possono giocare entrambe in casa nella        ||
%||         stessa giornata. Ovviamente, fanno eccezione le due giornate in cui giocano         ||
%||         l’una contro l’altra (derby)                                                        ||
%||= = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =||

% PARTITA
% Una partita è uno scontro tra 2 squadre: S0 ed S1. 
% Le due squadre devono essere necessariamente diverse.
% Nella parita S0,S1 gioca in casa S0; nella parita S1,S0 gioca in casa S1.
partita(S0,S1,Stadio) :- squadra(S0), squadra(S1), S0 <> S1, stadio(S0,Stadio).


% Ogni numero_giornata G è assegnato esattamente a (num_partite_giornata) giornate.
% (ossia: ad ogni numero_giornata G sono giocate esattamente (num_partite_giornata) partite)
% e.g.: 20 Squadre -> num_partite_giornata = 10.
%     16 Squadre -> num_partite_giornata = 8.
%     ...
%     4 Squadre -> num_partite_giornata = 2.
num_partite_giornata 
    {
        giornata(G, S0, S1, Stadio): partita(S0,S1,Stadio) 
    } 
num_partite_giornata 
:- numero_giornata(G).


% Ogni partita tra le squadre S0 ed S1 è assegnata ad UNA E SOLO UNA giornata G
% NB: La partita tra S0 ed S1, e la partita tra S1 ed S0, sono partite diverse.
1 { giornata(G, S0, S1, Stadio): numero_giornata(G)  } 1 :- partita(S0,S1,Stadio).


%||- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -||

% Ogni squadra S0 gioca esattamente UNA e SOLO UNa partita nella giornata G.
% *** NB: ***
% Questo punto è stato sviluppato con 3 diverse soluzioni:

%   ---> SOLUZIONE 1:
1
    { 
        giornata(G, S0, S1, Stadio) : partita(S0, S1, Stadio);
        giornata(G, S1, S0, Stadio) : partita(S1, S0, Stadio) 
    }
1
:- numero_giornata(G), squadra(S0).


% ---> SOLUZIONE 2
% squadra_gioca(G,S) :- numero_giornata(G), squadra(S), giornata(G,S,_,_).
% squadra_gioca(G,S) :- numero_giornata(G), squadra(S), giornata(G,_,S,_).
% :- numero_giornata(G), squadra(S), not squadra_gioca(G,S).


% ---> SOLUZIONE 3
% :- giornata(G, S0, S1,_), giornata(G, S1, S0,_).
% :- giornata(G, S0, S1,_), giornata(G, S0, S2,_), S1 <> S2.
% :- giornata(G, S1, S0,_), giornata(G, S2, S0,_), S1 <> S2.
% :- giornata(G, S1, S0,_), giornata(G, S0, S2,_), S1 <> S2.
% :- giornata(G, S0, S1,_), giornata(G, S2, S0,_), S1 <> S2.


%||- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -||

% Una partita di ANDATA è una partita che ha numero giornata G <= num_giornate_AR
% e.g.: 20 squadre --> 38 giornata --> 19 ANDATA, 19 RITORNO
partita_di_andata(G,S0,S1) :- giornata(G,S0,S1,_), G <= num_giornate_AR.

% Non è possibile che la partita tra S0 ed S1 e la partita tra S1 ed S0 (ossia il viceversa)
% siano entrambe partite di ANDATA
:-  giornata(G1,S0,S1,_),
    giornata(G2,S1,S0,_), 
    partita_di_andata(G1,S0,S1), 
    partita_di_andata(G2,S1,S0).

% NON NECESSARIO.
% Non è necessario 
% partita_di_ritorno(G,S0,S1) :- giornata(G,S0,S1), G > num_giornate_AR.
% :-  giornata(G1,S0,S1,_), 
%     giornata(G2,S1,S0,_), 
%     ritorno(G1,S0,S1), 
%     ritorno(G2,S1,S0).


%||- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -||


% Non voglio che due squadre che condividono lo stadio giochino in casa 
% la stessa giornata.
% Non è possibile che, in una giornata con numero giornata G, due squadre S0 ed S1
% che condividono lo stesso stadio giochino entrambe in casa.
% e.g.: Non è possibile che:
%   giornata(10, "AC Milan", "Juventus FC", "Giuseppe Meazza (San Siro) - Milano, Italy"). 
%   e
%   giornata(10, "FC Inter", "FC Barcelona", "Giuseppe Meazza (San Siro) - Milano, Italy"). 
:-  giornata(G,S0,_,_), 
    giornata(G,S1,_,_),
    S0 <> S1,
    stadio(S0,StadioS0),
    stadio(S1,StadioS1),
    StadioS0 == StadioS1.


%| - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -|
%|  RICHIESTA 6 (Facoltativo):                                                                  |
%|  Ciascuna squadra non deve giocare mai più di due partite consecutive in casa o fuori casa   |
%| - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -|
:- numero_giornata(G), giornata(G,S,_,_), giornata(G+1,S,_,_), giornata(G+2,S,_,_).
:- numero_giornata(G), giornata(G,_,S,_), giornata(G+1,_,S,_), giornata(G+2,_,S,_).


%| - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -|
%|  RICHIESTA 7 (Facoltativo):                                                                  |
%|  La distanza tra una coppia di gare di andata e ritorno è di almeno 10 giornate,             |
%|  ossia se SquadraA vs SquadraB è programmata per la giornata 12, il ritorno                  |
%|  SquadraB vs SquadraA verrà schedulato non prima dalla giornata 22                           |
%| - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -|
:- giornata(G1, S0, S1,_), giornata(G2, S1, S0,_), |G1 - G2| < distanza_AR.


%| - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -|
%|  OUTPUT                                                                                      |
%| - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -|
#show giornata/4.




