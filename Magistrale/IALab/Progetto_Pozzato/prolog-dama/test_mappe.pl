% SERVE A ME PER RICORDARE LE CASELLE NERE POSSIBILI
damiera([	(1,2), (1,4), (1,6),
		 (2,1), (2,3), (2,5),
  		 	(3,2), (3,4), (3,6),
      	 (4,1), (4,3), (4,5),
      	 	(5,2), (5,4), (5,6),
      	 (6,1), (6,3), (6,5)]).

% Mappa 1 - Pesi uguali per ogni dama
% Mossa migliore ottenuta DEPTH 3: 	[pos(4, 3), sudx]
% Mossa migliore ottenuta DEPTH 6: 	[pos(4, 3), giudx]
occupata(pos(1,2), pedina_nera).
occupata(pos(3,2), pedina_nera).

occupata(pos(4,1), pedina_bianca).
occupata(pos(4,3), dama_bianca).

% Mappa 2
% Mossa migliore ottenuta DEPTH 3: 	[pos(4, 3), sudx]
% Mossa migliore ottenuta DEPTH 6: 	[pos(4, 3), giudx]
occupata(pos(1,2), pedina_nera).
occupata(pos(1,4), pedina_nera).
occupata(pos(2,5), pedina_nera).
occupata(pos(3,2), pedina_nera).

occupata(pos(1,6), dama_bianca).
occupata(pos(6,1), pedina_bianca).

% Mappa 3
% Mossa migliore ottenuta DEPTH 3: 	[pos(5, 4), sudx]
% Mossa migliore ottenuta DEPTH 6: 	[pos(1, 6), mangiagiusx]
occupata(pos(1,2), pedina_nera).
occupata(pos(1,4), pedina_nera).
occupata(pos(2,1), pedina_nera).
occupata(pos(2,5), dama_nera).
occupata(pos(3,2), pedina_nera).

occupata(pos(1,6), dama_bianca).
occupata(pos(4,1), pedina_bianca).
occupata(pos(5,4), pedina_bianca).
occupata(pos(6,1), pedina_bianca).
occupata(pos(6,3), pedina_bianca).

% Mappa 4
% Mossa migliore ottenuta DEPTH 3: 	[pos(6, 3), susx]
% Mossa migliore ottenuta DEPTH 6: 	[pos(1, 6), mangiagiusx]
occupata(pos(1,2), pedina_nera).
occupata(pos(1,4), pedina_nera).
occupata(pos(2,5), dama_nera).
occupata(pos(3,2), pedina_nera).

occupata(pos(1,6), dama_bianca).
occupata(pos(6,3), pedina_bianca).

% PESI PER LE DAME
peso_casella(pos(1, 1), _, 1).
peso_casella(pos(1, 2), _, 3).
peso_casella(pos(1, 3), _, 4).
peso_casella(pos(1, 4), _, 4).
peso_casella(pos(1, 5), _, 3).
peso_casella(pos(1, 6), _, 1).

peso_casella(pos(2, 1), _, 2).
peso_casella(pos(2, 2), _, 3).
peso_casella(pos(2, 3), _, 5).
peso_casella(pos(2, 4), _, 5).
peso_casella(pos(2, 5), _, 3).
peso_casella(pos(2, 6), _, 2).

peso_casella(pos(5, 1), _, 2).
peso_casella(pos(5, 2), _, 3).
peso_casella(pos(5, 3), _, 5).
peso_casella(pos(5, 4), _, 5).
peso_casella(pos(5, 5), _, 3).
peso_casella(pos(5, 6), _, 2).

peso_casella(pos(6, 1), _, 1).
peso_casella(pos(6, 2), _, 3).
peso_casella(pos(6, 3), _, 4).
peso_casella(pos(6, 4), _, 4).
peso_casella(pos(6, 5), _, 3).
peso_casella(pos(6, 6), _, 1).

peso_casella(pos(_, 1), _, 4).
peso_casella(pos(_, 2), _, 5).
peso_casella(pos(_, 3), _, 7).
peso_casella(pos(_, 4), _, 7).
peso_casella(pos(_, 5), _, 5).
peso_casella(pos(_, 6), _, 4).