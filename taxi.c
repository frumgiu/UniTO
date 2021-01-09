#include "taxi.h"
#include "Common_IPC.h"

static const struct timespec ripetizione = {0, 100000000};
static struct timespec mancante = {0, 0};

static struct timespec movimento = {0, 0};
static struct timespec pausa = {0, 0};

static void run_taxi(st_taxip taxi_p, st_mappap mappa, int sem_id, int shm_id)
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
                    st_cellap next_source = find_near_source(taxi_p, mappa);
                    move_taxi(taxi_p, next_source, mappa);
                }
            }
            else
            {
                st_cellap next_source = find_near_source(taxi_p, mappa);
                move_taxi(taxi_p, next_source, mappa);
            }
            break;
        case TAXI_STATE_RUNNING:
            move_taxi(taxi_p, taxi_p->request->destinazione, mappa);
            break;
        case TAXI_STATE_COMPLETED:
            taxi_p->stato = TAXI_STATE_SEARCHING;
            taxi_p->request = NULL;         /* Per sicurezza gli rimuovo la richiesta appena conclusa */
            break;
        case TAXI_STATE_ABORTED:
            shmdt(mappa);
            free(taxi_p);
            setval_semaphore(sem_id, SEM_ID_TAXI_START, 1); /* senno' non puo' partire */
            init_taxi(random_cella(mappa), sem_id, shm_id); /* crea un nuovo taxi */
            break;
    }
}

static void move_taxi(st_taxip taxi, st_cellap arrivo, st_mappap mappa)
{
    int distance_x, distance_y;
    struct coordinate taxi_pos;
    taxi_pos = taxi->posizione->coordinate;
    distance_x = taxi_pos.colonna - arrivo->coordinate.colonna;
    distance_y = taxi_pos.riga - arrivo->coordinate.riga;
    if (distance_x != 0)
    {
        if (move_taxi_x(taxi, distance_x, distance_y, mappa) == 0)
            return;
    }
    else if (distance_y != 0)
    {
        if (move_taxi_y(taxi, distance_x, distance_y, mappa) == 0)
            return;
    }
    if (taxi->posizione == taxi->request->destinazione)
    {
        taxi->stato = TAXI_STATE_COMPLETED;
    }
}

static int move_taxi_x(st_taxip taxi, int distance_x, int distance_y, st_mappap mappa)
{
    int result = 0;
    int step = distance_x > 0 ? -1 : 1;
    st_cellap next;

    if (distance_x == 0 && (taxi->posizione->coordinate.riga == SO_HEIGHT))
    {
        step = -1;
    }
    next = return_cell(taxi->posizione->coordinate.colonna + step, taxi->posizione->coordinate.riga, mappa);
    if (!is_hole(*next))
    {
        if (enter_cella(next) == 1)
        {
            exit_cella(taxi->posizione);
            taxi->posizione = next;
            time(&taxi->last_move);
            movimento.tv_nsec = next->so_timensec;
            nanosleep(&movimento, &pausa);
            result = 1;
        }
        else
        {
            if (taxi->last_move >= get_so_timeout())
            {
                taxi->stato = TAXI_STATE_ABORTED;
            }
        }
        nanosleep(&ripetizione, &pausa);
    }
    else    /* else mi muovo su y */
    {
       result = move_taxi_y(taxi, distance_x, distance_y, mappa);
    }
    return result;
}

static int move_taxi_y(st_taxip taxi, int distance_x, int distance_y, st_mappap mappa)
{
    int result = 0;
    int step = distance_y > 0 ? -1 : 1;
    st_cellap next;
    if (distance_y == 0 && (taxi->posizione->coordinate.colonna == SO_WIDTH))
    {
        step = -1;
    }
    next = return_cell(taxi->posizione->coordinate.colonna, taxi->posizione->coordinate.riga + step, mappa);
    if (!is_hole(*next))
    {
        if (enter_cella(next) == 1)
        {
            exit_cella(taxi->posizione);
            taxi->posizione = next;
            time(&taxi->last_move);
            movimento.tv_nsec = next->so_timensec;
            nanosleep(&movimento, &pausa);
            result = 1;
        }
        else
        {
            if (taxi->last_move >= get_so_timeout())
            {
                taxi->stato = TAXI_STATE_ABORTED;
            }
        }
        nanosleep(&ripetizione, &pausa);
    }
    else    /* else mi muovo su x */
    {
        result = move_taxi_x(taxi, distance_x, distance_y, mappa);
    }
    return result;
}

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
                distance = abs(taxi_pos.colonna - source_pos.colonna) + abs(taxi_pos.riga - source_pos.riga);
                if (distance < min_distance)
                {
                    min_distance = distance;
                    min_source = &mappa->c[i][j];
                }
            }
        }
    }
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
            run_taxi(taxip, mappa, sem_id, shm_id);
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
