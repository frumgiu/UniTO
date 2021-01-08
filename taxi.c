#include "taxi.h"
#include "Common_IPC.h"

static const struct timespec ripetizione = {0, 500000};
static struct timespec mancante = {0, 0};

static void run_taxi(st_taxip taxi_p, st_mappap mappa, int sem_id)
{
    int mex_que_id;

    st_cellap cella_taxi = taxi_p->posizione;
    switch (taxi_p->stato)
    {
        case TAXI_STATE_SEARCHING:
            if (cella_taxi->source == 1)
            {
                int result;
                mex_que_id = cella_taxi->statoCella.queue_id;
                result = msgrcv(mex_que_id, &taxi_p->request, sizeof(&taxi_p->request), 0, IPC_NOWAIT);
                if (result == -1)
                {
                    ERROR;
                }
                else if(result > 0)
                {
                    taxi_p->stato = TAXI_STATE_RUNNING;
                }
                else
                {
                    /* TODO: cerco una cella_taxi sorgente vicino */
                    /* mi avvicino alla nuova sorgente */
                }
            }
            else
            {
                /* TODO: cerco una cella_taxi sorgente vicino */
                /* mi avvicino alla nuova sorgente */
            }
            break;
        case TAXI_STATE_RUNNING:
            /* TODO: controllo se sono arrivato */
            /* muovo il taxi verso il risultato */
            /* controllo finale */
            break;
        case TAXI_STATE_COMPLETED:
            taxi_p->stato = TAXI_STATE_SEARCHING;
            taxi_p->request = NULL;
            break;
        case TAXI_STATE_ABORTED:
            /* TODO: muore e creo un nuovo processo taxi */
            break;
    }
}

static void move_taxi(st_taxip taxi)
{
    /* TODO: funzione che muove il taxi da una cella A a una cella B */
    /* Controllo che la destinazione non sia una delle celle adiacenti */
    /* se si mi sposto di uno sull'asse x/y */
    /* senno' inizio a spostarmi sull'asse x, se incontro un holes mi sposto di uno su y verso la destinazione */
    /* se ho finito di muovere x, mi sposto verso y */
    /* se arrivo a destinazione taxi->stato = completed, request->stato = completed */
}

static st_cellap near_source(st_taxip taxi)
{
    int i, j, min_distance;
    st_cellap min_source;
    min_distance = DIM_MAPPA; /* La distanza piu' grande che posso avere */
    /* Controllo la distanza tra la sua posizione e le celle sorgenti sulla mappa */
    for (i = 0; i < SO_HEIGHT; i++)
    {
        for (j = 0; j < SO_WIDTH; j++)
        {
            if(is_source(/* cella della mappa su cui sono */))
                /* calcolo la distanza e la confronto con min_distance */
        }
    }
    /* salvo quella con distanza minore */
    /* restituisco il puntatore a quella sorgente */
    return min_source;
}

void init_taxi(st_cellap c, int sem_id, int shm_id)
{
    int child;
    st_taxip taxip = malloc(sizeof(st_taxi));
    taxip->posizione = c;
    taxip->stato = 0;
    enter_cella(c);

    child = fork();
    if (child == -1)
    {
        ERROR;
    }
    else if (child == 0)                /* CHILD PROCESS */
    {
        st_mappap mappa = shmat(shm_id, NULL, 0);
        decrement_sem(sem_id, SEM_ID_TAXI_START);
        while (getval_semaphore(sem_id, SEM_ID_TAXI) == 0)   /* Controllare se il semaforo e' 0 per continuare il processo taxi */
        {
            run_taxi(taxip, mappa, sem_id);
            nanosleep(&ripetizione, &mancante);
        }
        decrement_sem(sem_id, SEM_ID_TAXI);                  /* se esci perche' semaforo > 0 --> decremento il semaforo di 1 */
        shmdt(mappa);
        exit(0);
    }
}

/*
* Created by giulia on 23/12/2020.
*/
