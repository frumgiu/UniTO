#ifndef TAXICAB_CONFIGURAZIONE_PRIVATE_H
#define TAXICAB_CONFIGURAZIONE_PRIVATE_H

/* struttura dove salvero' una copia dei dati configurazione,
 * che usero' poi per strutturare la simulazione */
struct configuration {
    int so_taxi;
    int so_source;
    int so_holes;
    int so_top_cell;
    int so_cap_max;
    int so_cap_min;
    int so_timensec_max;
    int so_timensec_min;
    int so_timeout;
    int so_duration;
};

typedef struct configuration st_configuration;
typedef struct configuration * st_configurationp;

#endif /*TAXICAB_CONFIGURAZIONE_PRIVATE_H */
