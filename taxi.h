#ifndef TAXICAB_TAXI_H
#define TAXICAB_TAXI_H

#include "client.h"

#define TAXI_STATE_SEARCHING    0
#define TAXI_STATE_RUNNING      1
#define TAXI_STATE_ABORTED      2
#define TAXI_STATE_COMPLETED    3

struct taxi {
    st_cellap posizione;    /* dove si trova il taxi al momento */
    st_clientp request;     /* informazioni sulla client del cliente */
    int stato;              /* 0 vuoto, 1 con richiesta, */
    time_t last_move;
};

typedef struct taxi st_taxi;
typedef struct taxi * st_taxip;

/* Crea un taxi senza richieste, nella cella data */
void init_taxi(st_cellap, int, int);
/* Funzione che muove il taxi */
static void run_taxi(st_taxip, st_mappap, int);
/* Funzione che muove il taxi */
static void move_taxi(st_taxip, st_cellap, st_mappap);
/* Funzione che fa lo spostamento su asse x */
static int move_taxi_x(st_taxip , int, int, st_mappap);
/* Funzione che fa lo spostamento su asse y */
static int move_taxi_y(st_taxip, int, int, st_mappap);
/* Funzione che cerca la cella sorgente piu' vicina */
static st_cellap find_near_source(st_taxip taxi, st_mappap mappa);

#endif /* TAXICAB_TAXI_H */
/*
* Created by giulia on 23/12/2020.
*/
