#ifndef TAXICAB_CELLA_H
#define TAXICAB_CELLA_H

struct stato_cella {
    int attraversamento;
    int num_taxi;       /* Quanti taxi ci sono in quella cella attualmente */
    int queue_id;       /* Id della sua coda di messaggi, se e' una source */
    int sem_set_id;
    int sem_num;
};

struct coordinate {
    int riga;
    int colonna;
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
/* Controlla se una cella e' inaccessibile (1) 0 no (0) */
int is_hole(st_cellap);
/* Controlla se una cella e' sorgente (1) 0 no (0) */
int is_source(st_cellap);
/* Funzione che stampa una cella */
void print_cella(st_cellap);
/* Funzione che incrementa il numero di taxi nella cella */
int enter_cella(st_cellap);
/* Funzione che decrementa il numero di taxi nella cella */
void exit_cella(st_cellap);

#endif /* TAXICAB_CELLA_H */
/*
* Created by giulia on 11/12/2020.
*/
