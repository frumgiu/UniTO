#include "taxi.h"

static const struct timespec ripetizione = {0, 500000};
static struct timespec mancante = {0, 0};

static void run_taxi(st_taxip taxi_p, int sem_id)
{
    nanosleep(&ripetizione, &mancante);
        /* TODO: verifico se la cella e' sorgente */
        /* TODO: verifico se sulla cella corrente c'e una richiesta */
        /* TODO: muovo */
}

void init_taxi(st_cellap c, int sem_id)
{
    int child;
    st_taxip taxip = malloc(sizeof(st_taxi));
    taxip->posizione = c;
    taxip->request = NULL;
    enter_cella(c);

    child = fork();
    if (child == -1)
    {
        fprintf(stderr, "Errore '%s' nell'esecuzione della fork nei taxi\n", strerror(errno));
        exit(1);
    }
    else if (child == 0)                /* CHILD PROCESS */
    {
        decrement_sem(sem_id, SEM_ID_TAXI_START);
        while (getval_semaphore(sem_id, SEM_ID_TAXI) == 0)   /* Controllare se il semaforo e' 0 per continuare il processo taxi */
        {
            run_taxi(taxip, sem_id);
        }
        decrement_sem(sem_id, SEM_ID_TAXI);                 /* se esci perche' semaforo > 0 --> decremento il semaforo di 1 */
        exit(0);
    }
}
int is_busy(st_taxip taxip)
{
    return (taxip->request == NULL ? 0 : 1);
}
/*
* Created by giulia on 23/12/2020.
*/

