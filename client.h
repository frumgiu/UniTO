#ifndef TAXICAB_CLIENT_H
#define TAXICAB_CLIENT_H

#include <unistd.h>
#include "mappa.h"

struct client {
    st_cellap partenza;
    st_cellap destinazione;
};

typedef struct client st_client;
typedef struct client * st_clientp;

/* Inizializza una coda di messaggi vuota nella cella source */
int init_coda(int);
/* Crea un nuovo cliente, con destinazione generata randomicamente */
st_clientp init_client(st_cellap, st_mappap);
/* Invia clienti (messaggi) alla coda della cella source */
void new_client(int, int, int, int);
/* Stampa informazioni riguardo al cliente. USATA PER DEBUGGARE IL CODICE */
void print_client(st_clientp, st_cellap);

#endif /* TAXICAB_CLIENT_H */

/*
* Created by giulia on 23/12/2020.
*/
