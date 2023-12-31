#ifndef TAXICAB_CLIENT_H
#define TAXICAB_CLIENT_H

#include <unistd.h>
#include "mappa.h"

struct client {
    struct coordinate partenza;
    struct coordinate destinazione;
};

typedef struct client st_client;
typedef struct client * st_clientp;

/* Inizializza una coda di messaggi vuota nella cella source */
int init_coda(int);
/* Invia richieste (messaggi) alla coda della cella source */
void new_client(int, int, int, int);
/* Stampa informazioni riguardo al cliente. USATA PER DEBUGGARE IL CODICE */
void print_client(st_clientp, int);

#endif /* TAXICAB_CLIENT_H */

/*
* Created by giulia on 23/12/2020.
*/
