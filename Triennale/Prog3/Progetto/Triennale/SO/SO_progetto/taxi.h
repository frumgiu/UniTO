#ifndef TAXICAB_TAXI_H
#define TAXICAB_TAXI_H

#include "client.h"

#include "client.h"

#define SEARCHING              (-1)

#define TAXI_STATE_SEARCHING    0
#define TAXI_STATE_RUNNING      1
#define TAXI_STATE_ABORTED      2
#define TAXI_STATE_COMPLETED    3

struct taxi {
    st_cellap posizione;    /* dove si trova il taxi al momento */
    st_client request;      /* informazioni sulla client del cliente */
    int stato;
    int strada;
    int richieste;
    time_t last_move;
    time_t move_client;
};

typedef struct taxi st_taxi;
typedef struct taxi * st_taxip;

/* Crea un taxi senza richieste in una cella casuale */
void init_taxi(int, int, int);

#endif /* TAXICAB_TAXI_H */
/*
* Created by giulia on 23/12/2020.
*/
