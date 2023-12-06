#ifndef TAXICAB_CONFIGURAZIONE_H
#define TAXICAB_CONFIGURAZIONE_H

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "configurazione_private.h"
#define CONFIGURATION_FILE_NAME     "Parametri_taxi.txt"

st_configuration configuration;

/* metodi che mi servono per leggere i parametri di configurzione.
 * ogni parametro ha un metodo get dedicato */
int get_so_taxi();
int get_so_source();
int get_so_holes();
int get_so_top_cell();
int get_so_cap_max();
int get_so_cap_min();
int get_so_timensec_max();
int get_so_timensec_min();
int get_so_timeout();
int get_so_duration();

static int leggi_parametro(FILE*, char *, void *);
/* definisco il puntatore alla funzione di lettura al file con i parametri
 * di configurazione, in modo da usarlo nel modulo di configurazione
 * (piu' velcoe che riscrivere tutto) */
/* legge il testo del file passato per define */
void read_conf_from_file(st_configurationp);
void print_conf(st_configurationp);
#endif /* TAXICAB_CONFIGURAZIONE_H */
