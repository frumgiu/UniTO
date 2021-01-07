#ifndef TAXICAB_TAXI_H
#define TAXICAB_TAXI_H

#include "client.h"

struct taxi {
    st_cellap posizione;    /* dove si trova il taxi al momento */
    st_clientp request;     /* informazioni sulla client del cliente */
};

typedef struct taxi st_taxi;
typedef struct taxi * st_taxip;

/* Crea un taxi senza richieste, nella cella data */
void init_taxi(st_cellap, int, int);
/* Funzione che muove il taxi */
static void run_taxi(st_taxip, st_mappap, int);
/* Per vedere se il taxi e' occupato da un cliente (1) o e' libero (0) */
int is_busy(st_taxip);

#endif /* TAXICAB_TAXI_H */
/*
* Created by giulia on 23/12/2020.
*/
