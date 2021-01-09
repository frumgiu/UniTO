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
                    /* Ha una richiesta da servire */
                    taxi_p->stato = TAXI_STATE_RUNNING;
                }
                else
                {
                    find_near_source(taxi_p, mappa);
                    /* mi avvicino alla nuova sorgente */
                }
            }
            else
            {
                find_near_source(taxi_p, mappa);
                /* mi avvicino alla nuova sorgente */
            }
            break;
        case TAXI_STATE_RUNNING:
            /* TODO: controllo se sono arrivato */
            /* FALSE: muovo il taxi verso il risultato finche' o si completa o muore */
            /* TRUE: controllo tipo di finale */
            break;
        case TAXI_STATE_COMPLETED:
            taxi_p->stato = TAXI_STATE_SEARCHING;
            taxi_p->request = NULL;         /* Cosi' cancella la richiesta conslusa dalla sua 'memoria' */
            break;
        case TAXI_STATE_ABORTED:
            /* TODO: muore e creo un nuovo processo taxi */
            break;
    }
}

static void move_taxi(st_taxip taxi, st_cellap arrivo)
{
    /* TODO: funzione che muove il taxi da una cella A a una cella B */
    int distance_x, distance_y;
    st_cellap next;
    struct coordinate taxi_pos;
    taxi_pos = taxi->posizione->coordinate;
    distance_x = taxi_pos.colonna - arrivo->coordinate.colonna;
    if(distance_x > 0) /* muoversi verso sinistra */
    {
        next->coordinate.colonna = taxi_pos.colonna - 1;
        next->coordinate.riga = taxi_pos.riga;
        if (!is_hole(*next)) { /* controllo se la cella dopo non e' piena */
            if () {
                /* TRUE: aspetta che si liberi un posto, entro timeout */
            }
            else {
                enter_cella(next);
                exit_cella(taxi->posizione);
                taxi->posizione = next; /* FALSE: Il taxi si sposta sulla cella vicina */

            }
        }
    }
    else /* muovo verso destra */
    /* inizio a spostarmi sull'asse x, se incontro un holes mi sposto di uno su y verso la destinazione */
    /* se ho finito di muovere x, mi sposto verso y */
    /* se arrivo a destinazione taxi->stato = completed */
}

static void move_taxi_x(st_cellap taxi_pos, int distance_x, int distance_y)
{

}

static void move_taxi_y(st_taxip taxi)
{}

static st_cellap find_near_source(st_taxip taxi, st_mappap mappa)
{
    int i, j, min_distance, distance;
    st_cellap min_source;
    struct coordinate taxi_pos, source_pos;
    min_distance = DIM_MAPPA; /* La distanza piu' grande che posso avere */
    /* Controllo la distanza tra la sua posizione e le celle sorgenti sulla mappa */
    for (i = 0; i < SO_HEIGHT; i++)
    {
        for (j = 0; j < SO_WIDTH; j++)
        {
            taxi_pos = taxi->posizione->coordinate;
            source_pos = mappa->c[i][j].coordinate;
            if(is_source(mappa->c[i][j]))
            {
                /* calcolo la distanza e la confronto con min_distance */
                distance = abs(taxi_pos.colonna - source_pos.colonna) + abs(taxi_pos.riga - source_pos.riga);
                /* la confronto con min_distance */
                /* TRUE: assegno la distance a dist_min e min_source */
                if (distance < min_distance)
                {
                    min_distance = distance;
                    min_source = &mappa->c[i][j];
                }
                /* FALSE: resta uguale */
            }
        }
    }
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
