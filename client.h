#ifndef TAXICAB_CLIENT_H
#define TAXICAB_CLIENT_H

#include <unistd.h>
#include <wait.h>
#include "Common_IPC.h"
#include "mappa.h"

/* Valori che puo' assumere la variabile stato */
enum status {Pending, Running, Aborted, Completed};

struct viaggio {
    st_cellap partenza;
    st_cellap destinazione;
};

struct client {
    long mex_tipo;
    struct viaggio itinerario;      /* Partenza e destinazione della client */
    enum status stato;              /* Stato della client */
};

typedef struct client st_client;
typedef struct client * st_clientp;

/* Inizializza una coda di messaggi vuota nella cella source */
int init_coda(int);
/* Crea un nuovo cliente, con destinazione generata randomicamente */
st_clientp init_client(st_cellap, st_mappap);
/* Invia clienti (messaggi) alla coda della cella source */
void new_client(int, int, int, int);
/* Per settare lo stato di client durante la sua vita */
void set_status_request(st_clientp);
/* Stampa informazioni riguardo al cliente. USATA PER DEBUGGARE IL CODICE */
void print_client(st_clientp, st_cellap);

#endif /* TAXICAB_CLIENT_H */

/*
* Created by giulia on 23/12/2020.
*/
