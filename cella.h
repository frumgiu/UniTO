#ifndef TAXICAB_CELLA_H
#define TAXICAB_CELLA_H

#include <stdio.h>
#include <stdlib.h>
#include <time.h>

struct stato_cella {
    int num_taxi;       /* Quanti taxi ci sono in quella cella attualmente */
    int m_id;           /* Id della sua coda di messaggi, se e' una source */
};
struct coordinate {
    int colonna;
    int riga;
};
struct cella {
    int so_cap;                     /* Capacita' della cella */
    int so_timensec;                /* Tempo per ogni taxi di attraversare la cella */
    int source;                     /* 1 se e' sorgente, 0 altrimenti */
    int hole;                       /* 1 se e' inaccessbile, 0 altrimenti */
    struct stato_cella statoCella;
    struct coordinate coordinate;
};

typedef struct cella st_cella;
typedef struct cella * st_cellap;

/* Funzione che genera un numero random compreso nell'intervallo dato */
int random_num(int, int);
/* Funzione che crea una cella e inizializza i parametri */
void create_cella(st_cellap, int, int, int, int, int, int, int, int);
/* Decide se una cella e' generatrice o no */
int set_source(int, int);
/* Decide se una cella e' inaccessibile o no */
int set_holes(int, int);
/* Controlla se una cella e' inaccessibile */
int is_hole(st_cella);
/* Funzione che stampa una cella */
void print_cella(st_cellap);
/* Funzione che incrementa il numero di taxi nella cella */
void enter_cella(st_cellap);
/* Funzione che decrementa il numero di taxi nella cella */
void exit_cella(st_cellap);

#endif /* TAXICAB_CELLA_H */
/*
* Created by giulia on 11/12/2020.
*/
