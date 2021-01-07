#include "taxi.h"
#include "Common_IPC.h"

static const struct timespec ripetizione = {0, 500000};
static struct timespec mancante = {0, 0};

static void run_taxi(st_taxip taxi_p, st_mappap mappa, int sem_id)
{
    int mex_que_id;
    nanosleep(&ripetizione, &mancante);
        /* TODO: verifico se la cella e' sorgente */
    if (taxi_p->posizione->source == 1)
    {
        mex_que_id = taxi_p->posizione->statoCella.queue_id;            /* Mi serve per accedere in lettura alla coda della source */
        /* TODO: verifico se sulla cella corrente c'e una richiesta */
        if ()
        {/* TODO: prendo la richiesta in carico */
            /* TODO: muovo */
        }
        else
            return;
            /* TODO: esco, aspetto qualche secondo e rincontrollo la cella */
    }
    else
        return;
        /* TODO: cerco una cella sorgente vicino */
}

/* TODO: funzione che muove il taxi da una cella A a una cella B */

void init_taxi(st_cellap c, int sem_id, int shm_id)
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
        st_mappap mappa = shmat(shm_id, NULL, 0);
        decrement_sem(sem_id, SEM_ID_TAXI_START);
        while (getval_semaphore(sem_id, SEM_ID_TAXI) == 0)   /* Controllare se il semaforo e' 0 per continuare il processo taxi */
        {
            run_taxi(taxip, mappa, sem_id);
        }
        decrement_sem(sem_id, SEM_ID_TAXI);                 /* se esci perche' semaforo > 0 --> decremento il semaforo di 1 */
        shmdt(mappa);
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

