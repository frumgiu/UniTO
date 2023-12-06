#include "configurazione.h"

int get_so_taxi()
{
    return configuration.so_taxi;
}
int get_so_source()
{
    return configuration.so_source;
}
int get_so_holes()
{
    return configuration.so_holes;
}
int get_so_top_cell()
{
    return configuration.so_top_cell;
}
int get_so_cap_max()
{
    return configuration.so_cap_max;
}
int get_so_cap_min()
{
    return configuration.so_cap_min;
}
int get_so_timensec_max()
{
    return configuration.so_timensec_max;
}
int get_so_timensec_min()
{
    return configuration.so_timensec_min;
}
int get_so_timeout()
{
    return configuration.so_timeout;
}
int get_so_duration()
{
    return configuration.so_duration;
}

static int leggi_parametro(FILE* file_in, char * nome, void * where)
{
    int result;

    result = fscanf(file_in, nome, where);
    if (result < 0)                         /* leggo i parametri e li salvo nella struttura */
        result = 0;
    return result;
}

void read_conf_from_file(st_configurationp conf)
{
    FILE* file_in;
    int result;
    if ((file_in = (fopen(CONFIGURATION_FILE_NAME, "r"))) == NULL)
    {
        fprintf(stderr, "Errore generato dall'apertura del file.\n");
        exit(1);
    }
    while (!feof(file_in))
    {
        result = leggi_parametro(file_in, "SO_TAXI = %d\n",  &conf->so_taxi) &&
                 leggi_parametro(file_in, "SO_SOURCE = %d\n",  &conf->so_source) &&
                 leggi_parametro(file_in, "SO_HOLES = %d\n",  &conf->so_holes) &&
                 leggi_parametro(file_in, "SO_TOP_CELL = %d\n",  &conf->so_top_cell) &&
                 leggi_parametro(file_in, "SO_CAP_MAX = %d\n",  &conf->so_cap_max) &&
                 leggi_parametro(file_in, "SO_CAP_MIN = %d\n",  &conf->so_cap_min) &&
                 leggi_parametro(file_in, "SO_TIMENSEC_MAX = %d\n",  &conf->so_timensec_max) &&
                 leggi_parametro(file_in, "SO_TIMENSEC_MIN = %d\n",  &conf->so_timensec_min) &&
                 leggi_parametro(file_in, "SO_TIMEOUT = %d\n", &conf->so_timeout) &&
                 leggi_parametro(file_in, "SO_DURATION = %d\n",  &conf->so_duration);
        if (result == 0)
        {
            fprintf(stderr, "Errore durante la lettura di un parametro, in configurazione_da_file.c\n");
            exit(1);
        }
    }
    fclose(file_in);
}

void print_conf(st_configurationp conf)
{
    printf("-- info utili configurazione --\n");
    printf("Durata simulazione: %d sec\nNumero di taxi presenti: %d\nNumero di celle generatrici: %d\nNumero di celle inaccessibili: %d\n\n",
           get_so_duration(), get_so_taxi(), get_so_source(), get_so_holes());
}
