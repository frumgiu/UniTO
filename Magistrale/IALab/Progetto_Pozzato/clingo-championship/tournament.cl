%*
IALAB 2023, Answer Set Programming project.
Authors: Andrea Delmastro, Fabio Ferrero, Giulia Frumento.

Tuornament generator.

To execute: clingo tournament.cl --out-ifs='\n'
- if in anaconda: conda run clingo tournament.cl --out-ifs='\n'
*%

#const teamsNumber    = 20.
#const daysNumber     = (teamsNumber * 2) - 2.
#const legDaysNumber  = daysNumber / 2.
#const rounds         = 2.
#const roundsInterval = 10.

% 20 teams play in the tournament
team(juventusFc         ; lincolnRedImpsFc; asRoma        ; ssLazio;
     kfBallkani         ; usSampdoria     ; acOmonia      ; acfFiorentina;
     heartOfMidlothianFc; fkPartizan      ; fkCrvenaZvezda; fcUnionBerlin;
     rUnionSaintGilloise; fcVaduz         ; scBraga       ; scFreiburg; 
     nottinghamForestFc ; realBetisBp     ; asSaintEtienne; aik);

% Each team is based in a city and plays in a stadium in that city
city(turin       ; gibraltar; rome       ; suhareka; 
     genoa       ; florence ; nicosia    ; edinburgh; 
     belgrade    ; berlin   ; saintGilles; vaduz; 
     braga       ; freiburg ; nottingham ; seville; 
     saintEtienne; stockholm).

homeGround(juventusFc         , "Allianz Stadium"                , turin;
           lincolnRedImpsFc   , "Victoria Stadium"               , gibraltar;
           acOmonia           , "Stadio Neo GSP"                 , nicosia;
           asRoma             , "Stadio Olimpico"                , rome;
           ssLazio            , "Stadio Olimpico"                , rome;
           kfBallkani         , "Stadiumi i Qytetit të Suharekës", suhareka;
           usSampdoria        , "Luigi Ferraris"                 , genoa;
           acfFiorentina      , "Artemio Franchi"                , florence;
           heartOfMidlothianFc, "Tynecastle Stadium"             , edinburgh;
           fkPartizan         , "Partizan Stadium"               , belgrade;
           fkCrvenaZvezda     , "Stadio Rajko Mitić"             , belgrade;
           fcUnionBerlin      , "Alte Försterei"                 , berlin;
           rUnionSaintGilloise, "Joseph Marien"                  , saintGilles;
           fcVaduz            , "Rheinpark Stadion"              , vaduz;
           scBraga            , "Estádio Municipal"              , braga;
           scFreiburg         , "Europa-Park Stadion"            , freiburg;
           nottinghamForestFc , "City Ground"                    , nottingham;
           realBetisBp        , "Stadio Benito Villamarín"       , seville;
           asSaintEtienne     , "Stade Geoffroy-Guichard"        , saintEtienne;
           aik                , "Friends Arena"                  , stockholm).

day(1..daysNumber).

% Each team plays every other team two times, once at home and once away. Valid combinations for
% matches are ordered disjointed pairs of teams.
combination(HomeTeam, AwayTeam)
  :- team(HomeTeam), team(AwayTeam), HomeTeam <> AwayTeam.

%* With aggregates:
(teamsNumber - 1) 
  { combination(HomeTeam, AwayTeam) : team(AwayTeam), AwayTeam <> HomeTeam } 
(teamsNumber - 1) 
  :- team(HomeTeam).*%

% Each combination of teams is assigned to a match day.
1
  { match(Number, combination(HomeTeam, AwayTeam)) : day(Number) }
1
  :- combination(HomeTeam, AwayTeam).

% Each day exactly (teamsNumber / 2) matches are played.
(teamsNumber / 2)
  { match(Number, combination(HomeTeam, AwayTeam)) : combination(HomeTeam, AwayTeam) }
(teamsNumber / 2)
  :- day(Number).

% Each team plays exactly one match per day.
1
  { match(Number, combination(Team, AwayTeam)) : combination(Team, AwayTeam);
    match(Number, combination(HomeTeam, Team)) : combination(HomeTeam, Team) }
1
  :- day(Number), team(Team).

% Each couple of teams face one time per leg:
:- match(Number1, combination(Team1, Team2)),
   match(Number2, combination(Team2, Team1)),
   Number1 <= legDaysNumber,
   Number2 <= legDaysNumber.

% Two teams that share the same stadium in the same city cannot play at home the same day.
:- homeGround(Team1, Ground, City), 
   homeGround(Team2, Ground, City),
   Team1 <> Team2, 
   match(Number, combination(Team1, _)), 
   match(Number, combination(Team2, _)).

% ===== Optional =====

% Each team cannot play more than two consecutive games at home.
:- match(Number, combination(Team1, _)), 
   match(Number + 1, combination(Team1, _)), 
   match(Number + 2, combination(Team1, _)).

% Each team cannot play more than two consecutive games away.
:- match(Number, combination(_, Team1)), 
   match(Number + 1, combination(_, Team1)), 
   match(Number + 2, combination(_, Team1)).

% The first and the second game between two teams must be at least roundsInterval days away.
:- match(Number1, combination(Team1, Team2)), 
   match(Number2, combination(Team2, Team1)), 
   |Number1 - Number2| < roundsInterval.

% ==== Output formatting ====

matchDescription(Number, HomeTeam, AwayTeam, GroundName, GroundCity) 
  :- match(Number, combination(HomeTeam, AwayTeam)), 
     homeGround(HomeTeam, GroundName, GroundCity).

#show matchDescription/5.