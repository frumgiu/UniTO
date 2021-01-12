#include "taxi.h"
#include "Common_IPC.h"
static int move_taxi_y(st_taxip taxi, int distance_x, int distance_y, st_mappap mappa);
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
            if(is_source(&mappa->c[i][j]))
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
static void handler_child (int sig)
{
    if (sig == SIGTSTP)
    {
        /* Ignora il segnale */
    }
}

/* Funzione che fa lo spostamento su asse x */
static int move_taxi_x(st_taxip taxi, int distance_x, int distance_y, st_mappap mappa)
{
    static const struct timespec ripetizione = {0, 100000000};
    static struct timespec mancante = {0, 0};
    static struct timespec movimento = {0, 0};
    static struct timespec pausa = {0, 0};
    int result = 0;
    int step = distance_x > 0 ? -1 : 1;
    st_cellap next;
    /*printf("muove_taxi_x da = %d,%d PID %d \n", taxi->posizione->coordinate.riga, taxi->posizione->coordinate.colonna, getpid());*/
    if (distance_x == 0 && (taxi->posizione->coordinate.colonna == SO_WIDTH))
    {
        step = -1;
    }
    next = return_cell(taxi->posizione->coordinate.riga, taxi->posizione->coordinate.colonna + step, mappa);
    if (!is_hole(next))
    {
        if (enter_cella(next))
        {
            exit_cella(taxi->posizione);
            taxi->posizione = next;
            taxi->strada++;
            movimento.tv_nsec = next->so_timensec;
            nanosleep(&movimento, &pausa);
            time(&taxi->last_move);
            result = 1;
            /*printf("Mosso move_taxi_x in = %d,%d PID %d \n", taxi->posizione->coordinate.riga, taxi->posizione->coordinate.colonna, getpid());*/
        }
        /* ELSE printf("Fermo muove_taxi_x in = %d,%d PID %d \n", taxi->posizione->coordinate.riga, taxi->posizione->coordinate.colonna, getpid());*/
    }
    else     /* else mi muovo su y */
    {
        result = move_taxi_y(taxi, distance_x, distance_y, mappa);
    }
    return result;
}
/* Funzione che fa lo spostamento su asse y */
static int move_taxi_y(st_taxip taxi, int distance_x, int distance_y, st_mappap mappa)
{
    static struct timespec movimento = {0, 0};
    static struct timespec pausa = {0, 0};
    int result = 0;
    int step = distance_y > 0 ? -1 : 1;
    st_cellap next;
    /*printf("muove_taxi_y da = %d,%d PID %d \n", taxi->posizione->coordinate.riga, taxi->posizione->coordinate.colonna, getpid());*/
    if (distance_y == 0 && (taxi->posizione->coordinate.riga == SO_HEIGHT))
    {
        step = -1;
    }
    next = return_cell(taxi->posizione->coordinate.riga + step, taxi->posizione->coordinate.colonna, mappa);
    if (!is_hole(next))
    {
        if (enter_cella(next))
        {
            /*printf("Mosso move_taxi_y da = %d,%d PID %d \n", taxi->posizione->coordinate.riga, taxi->posizione->coordinate.colonna, getpid());*/
            exit_cella(taxi->posizione);
            taxi->posizione = next;
            taxi->strada++;
            movimento.tv_nsec = next->so_timensec;
            nanosleep(&movimento, &pausa);
            time(&taxi->last_move);
            result = 1;
        }
        /* ELSE printf("Fermo move_taxi_y da = %d,%d PID %d \n", taxi->posizione->coordinate.riga, taxi->posizione->coordinate.colonna, getpid());*/
    }
    else     /* else mi muovo su x */
    {
        result = move_taxi_x(taxi, distance_x, distance_y, mappa);
    }
    return result;
}
/* Funzione che muove il taxi */
static void move_taxi(st_taxip taxi, struct coordinate * arrivo, st_mappap mappa)
{
    int distance_x, distance_y;
    struct coordinate * taxi_pos;
    time_t current_time;
    taxi_pos = &taxi->posizione->coordinate;
    distance_x = taxi_pos->colonna - arrivo->colonna;
    distance_y = taxi_pos->riga - arrivo->riga;
    if (distance_x != 0)
    {
        move_taxi_x(taxi, distance_x, distance_y, mappa);
    }
    else if (distance_y != 0)
    {
        move_taxi_y(taxi, distance_x, distance_y, mappa);
    }
    taxi_pos = &taxi->posizione->coordinate;
    /*printf("Sono arrivato in cella = %d,%d PID %d \n", taxi_pos->riga, taxi_pos->colonna, getpid());*/
    if (taxi_pos->colonna == taxi->request.destinazione.colonna && taxi_pos->riga == taxi->request.destinazione.riga)
    {
        taxi->stato = TAXI_STATE_COMPLETED;
    }
    time(&current_time);
    if ((current_time - taxi->last_move) > get_so_timeout())
    {
        taxi->stato = TAXI_STATE_ABORTED;
    }
    /*printf("Finita move_taxi PID %d\n", getpid());*/
}
/* Funzione a stati che dice al taxi cosa fare */
static void run_taxi(st_taxip taxi_p, st_mappap mappa, st_raccoltap raccolta, int sem_id, int shm_id, int shm_stat)
{
    int mex_que_id;
    int parent;
    time_t current_time;
    st_cellap cella_taxi = taxi_p->posizione;

    switch (taxi_p->stato)
    {
        case TAXI_STATE_SEARCHING:
            if (cella_taxi->source == 1)
            {
                int result;
                mex_que_id = cella_taxi->statoCella.queue_id;
                /*printf("Leggo dalla coda %d con PID %d\n", mex_que_id, getpid());*/
                result = msgrcv(mex_que_id, &taxi_p->request, sizeof(st_client), 0, IPC_NOWAIT);
                /*printf("Ho letto dalla coda %d con PID %d numero byte %d\n", mex_que_id, getpid(), result);*/
                if (result == -1 && errno != 42)
                {
                    ERROR;
                }
                else if(result > 0)
                {
                    /*printf("Ricevo richiesta PID %d\n", getpid());*/
                    decrement_sem(sem_id, SEM_MUTEX_STAT);
                    raccolta->viaggi[CLIENT_TOTALI]++;
                    increment_sem(sem_id, SEM_MUTEX_STAT);
                    taxi_p->stato = TAXI_STATE_RUNNING;
                    taxi_p->richieste++;
                    taxi_p->strada = 0;
                    time(&taxi_p->move_client);
                }
                else
                {
                    st_cellap next_source = find_near_source(taxi_p, mappa);
                    /*printf("Nessuna richiesta PID %d\n", getpid());*/
                    move_taxi(taxi_p, &next_source->coordinate, mappa);
                }
            }
            else
            {
                st_cellap next_source = find_near_source(taxi_p, mappa);
                /*printf("Cerca source PID %d\n", getpid());*/
                move_taxi(taxi_p, &next_source->coordinate, mappa);
            }
            break;
        case TAXI_STATE_RUNNING:
            move_taxi(taxi_p, &taxi_p->request.destinazione, mappa);
            break;
        case TAXI_STATE_COMPLETED:
            /*printf("Il taxi  PID %d ha completato la richiesta\n", getpid());*/
            time(&current_time);
            decrement_sem(sem_id, SEM_MUTEX_STAT);
            if (taxi_p->strada > raccolta->strada[1])
            {
                raccolta->strada[0] = getpid();
                raccolta->strada[1] = taxi_p->strada;
            }
            if (taxi_p->move_client > raccolta->vita)
                if ((current_time - taxi_p->move_client) > raccolta->vita)
                {
                    raccolta->pid_taxi = getpid();
                    raccolta->vita = taxi_p->move_client;
                    raccolta->vita = (current_time - taxi_p->move_client);
                }
            raccolta->viaggi[CLIENT_COMPLETED]++;
            increment_sem(sem_id, SEM_MUTEX_STAT);
            taxi_p->stato = TAXI_STATE_SEARCHING;
            taxi_p->request.destinazione.colonna = SEARCHING;
            break;
        case TAXI_STATE_ABORTED:
            parent = getpid();
            /*printf("Il taxi  PID %d e' morto, viene ricreato\n", getpid());*/
            exit_cella(taxi_p->posizione);
            decrement_sem(sem_id, SEM_MUTEX_STAT);
            if (taxi_p->request.destinazione.colonna != SEARCHING)
                raccolta->viaggi[CLIENT_ABORTED]++;
            if (taxi_p->richieste > raccolta->richieste[1])
            {
                raccolta->richieste[0] = getpid();
                raccolta->richieste[1] = taxi_p->richieste;
            }
            increment_sem(sem_id, SEM_MUTEX_STAT);
            shmdt(mappa);
            shmdt(raccolta);
            free(taxi_p);
            /*printf("Sto nuovo taxi  PID %d da %d\n", getpid(), parent);*/
            increment_sem(sem_id, SEM_NUM_TAXI_START); /* senno' non puo' partire */
            init_taxi(sem_id, shm_id, shm_stat);       /* crea un nuovo taxi */
            exit(0);
            break;
    }
}
void init_taxi(int sem_id, int shm_id, int shm_stat)
{
    int child;
    st_cellap c;
    static struct timespec movimento = {0, 100000000};
    static struct timespec pausa = {0, 0};
    st_taxip taxip = malloc(sizeof(st_taxi));
    taxip->stato = TAXI_STATE_SEARCHING;
    taxip->richieste = 0;
    taxip->request.destinazione.colonna = SEARCHING;
    time(&taxip->last_move);
    time(&taxip->move_client);

    child = fork();
    if (child == -1)
    {
        ERROR;
    }
    else if (child == 0)                /* CHILD PROCESS */
    {
        int parent = getpid();
        st_mappap mappa = shmat(shm_id, NULL, 0);
        st_raccoltap raccolta = shmat(shm_stat, NULL, 0);
        struct  sigaction sig;
        sig.sa_handler = handler_child;
        sig.sa_flags = 0;
        sigaction(SIGTSTP, &sig,  NULL);
        srand(getpid());
        while (!enter_cella(c = random_cella(mappa)))
        {
            nanosleep(&movimento, &pausa);
        }
        taxip->posizione = c;
        /*printf("creato taxi con in PID: %d dal padre %d e il semaforo START vale %d\n", getpid(), parent, getval_semaphore(sem_id, SEM_NUM_TAXI_START));*/
        increment_sem(sem_id, SEM_NUM_TAXI_INIT_READY);
        decrement_sem(sem_id, SEM_NUM_TAXI_START);
        /*printf("PID %d sta partendo ed e' prima di semaforo %d che vale %d\n", getpid(), SEM_NUM_TAXI, getval_semaphore(sem_id, SEM_NUM_TAXI));*/
        while (getval_semaphore(sem_id, SEM_NUM_TAXI) == 0)   /* Controllare se il semaforo e' 0 per continuare il processo taxi */
        {
            run_taxi(taxip, mappa, raccolta, sem_id, shm_id, shm_stat);
            nanosleep(&movimento, &pausa);
        }
        /*printf("PID %d voglio chiudere il processo con semaforo %d valore %d\n", getpid(), sem_id, getval_semaphore(sem_id, SEM_NUM_TAXI));*/
        /* TODO: Non mi tornano i numeri */
        decrement_sem(sem_id, SEM_MUTEX_STAT);
        if (taxip->richieste > raccolta->richieste[1])
        {
            raccolta->richieste[0] = getpid();
            raccolta->richieste[1] = taxip->richieste;
        }
        increment_sem(sem_id, SEM_MUTEX_STAT);
        decrement_sem(sem_id, SEM_NUM_TAXI);                  /* se esci perche' semaforo > 0 --> decremento il semaforo di 1 */
        /*printf("PID %d chiudo il processo con semaforo %d valore %d\n", getpid(), sem_id, getval_semaphore(sem_id, SEM_NUM_TAXI));*/
        shmdt(raccolta);
        shmdt(mappa);
        exit(0);
    }
}
/*
* Created by giulia on 23/12/2020.
*/
