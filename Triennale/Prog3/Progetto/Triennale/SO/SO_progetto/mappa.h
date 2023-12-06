#ifndef TAXICAB_MAPPA_H
#define TAXICAB_MAPPA_H

#include "cella.h"
#include "configurazione.h"

#define SO_HEIGHT   10                 /* Righe */
#define SO_WIDTH    20                         /* Colonne */
#define DIM_MAPPA SO_WIDTH*SO_HEIGHT   /* Dimensione mappa */
#define DIM_MAPPA_BORDO ((SO_WIDTH + 2)*(SO_HEIGHT + 2))

struct mappa {
    st_cella c[SO_HEIGHT][SO_WIDTH] ;
};

typedef struct mappa st_mappa;
typedef struct mappa * st_mappap;

/* Metodo che inizializza la mappa, gli passo numero di sources e holes che devo mettere */
void init_map(int, int, st_mappap);
/* Controlla che ci sia la possibilita' di creare una mappa funzionante */
int is_dimension_ok(int, int);
/* Controllo che non si creino muri di celle inaccessibili */
int check_hole_ok(st_mappap, int, int);
/* Metodo che stampa la mappa in output */
void print_map(st_mappap);
/* Estrae una cella casualmente dalla mappa */
st_cellap random_cella(st_mappap);
/* Ritorna la cella dalle coordinate */
st_cellap return_cell(int, int, st_mappap);

#endif /* TAXICAB_MAPPA_H */
/*
 * Created by giulia on 11/12/2020.
 */
