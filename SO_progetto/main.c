#include <time.h>
#include <signal.h>
#include "Common_IPC.h"
#include "taxi.h"

static st_mappap mappa;     /* Puntatore alla zona della mappa */
static st_raccoltap statistic;
static int stop_print = 0;

static void inizializza_configurazione() {
    printf("1. Lettura dal file .txt dei parametri di configurazione\n");
    read_conf_from_file(&configuration);
    print_conf(&configuration);
}

static int inizializza_mappa(int sem_celle_id)
{
    int shm_id, i, j, sem_num = 0;
    printf("2. Inizializzazione della mappa, di dimensione %dx%d, e prima stampa\n", SO_HEIGHT, SO_WIDTH);

    shm_id = create_shmemory(MAPPA_KEY, sizeof(struct cella) * (DIM_MAPPA));
    mappa = shmat(shm_id, NULL, 0);
    init_map(get_so_source(), get_so_holes(), mappa);

    for (i = 0; i < SO_HEIGHT; ++i) {    /* Devo settare tutti i mutex nelle celle, assegno il numero di semaforo nelle celle (tranne holes) */
        for (j = 0; j < SO_WIDTH; ++j) {
            if (!is_hole(&mappa->c[i][j]))
            {
                setval_semaphore(sem_celle_id, sem_num, 1);
                mappa->c[i][j].statoCella.sem_set_id = sem_celle_id;
                mappa->c[i][j].statoCella.sem_num = sem_num;
                ++sem_num;
            }
        }
    }
    print_map(mappa);
    printf("\n-- Legenda --\nS = cella sorgente\nX = cella inaccessibile\n"
           ". = cella normale\nI numeri nelle celle corrisondono ai taxi presenti in esse\n\n");

    return shm_id;
}

static void create_source_queue() {
    int i, j;
    printf("3. Inizializzazione delle code di messaggi (vuote) nelle %d celle source\n", get_so_source());
    for (i = 0; i < SO_HEIGHT; ++i) {        /* creo una coda di messaggi per ogni cella source */
        for (j = 0; j < SO_WIDTH; ++j) {
            if (is_source(&mappa->c[i][j]))
                mappa->c[i][j].statoCella.queue_id = init_coda(IPC_PRIVATE);
        }
    }
}

static void remove_queue_source() {
    int i, j;
    printf("8. Chiusura delle code di messaggi delle celle source\n");
    for (i = 0; i < SO_HEIGHT; ++i) {
        for (j = 0; j < SO_WIDTH; ++j) {
            if (is_source(&mappa->c[i][j]) == 1)
                msgctl(mappa->c[i][j].statoCella.queue_id, IPC_RMID, 0);
        }
    }
}

static void create_taxi(int sem_id, int valore, int shm_id, int shm_stat) {
    int n;
    printf("4. Creazione di %d taxi per servire le richeste\n", get_so_taxi());
    setval_semaphore(sem_id, SEM_NUM_TAXI, valore);
    setval_semaphore(sem_id, SEM_NUM_TAXI_START, valore);
    n = get_so_taxi();
    while (n > 0) {
        init_taxi(sem_id, shm_id, shm_stat);      /* Nella funzione sono gia' escluse le celle holes, non serve ricontrollare */
        --n;
    }
}

static void create_client(int sem_id, int valore, int shm_id) {
    int i, j;
    printf("5. I source processi incominciano a creare richieste e i taxi a servirle\n");
    if (setval_semaphore(sem_id, SEM_NUM_CLIENT, valore) == 0)
        for (i = 0; i < SO_HEIGHT; ++i) {
            for (j = 0; j < SO_WIDTH; ++j) {
                if (is_source(&mappa->c[i][j]))
                    new_client(i, j, sem_id, shm_id);
            }
        }
}

static void chiudi_processi_child(int sem_id, int semnum, int valore, char* child) {
    printf("6. Terminazione dei processi %s\n", child);
    setval_semaphore(sem_id, semnum, valore);
    while (getval_semaphore(sem_id, semnum) > 0) {
        sleep(1);           /* Aspetto un attimo prima di ricontrollare */
    }
}

static void termina_specifiche()
{
    int inevasi = statistic->viaggi[CLIENT_TOTALI] - (statistic->viaggi[CLIENT_COMPLETED] + statistic->viaggi[CLIENT_ABORTED]);

    printf("7. Stampa delle specifiche richieste a termine simulazione\n");
    printf("Numero di viaggi totali: %d\n- completati %d\n- abortiti %d\n- inevasi %d\n",
           statistic->viaggi[CLIENT_TOTALI], statistic->viaggi[CLIENT_COMPLETED], statistic->viaggi[CLIENT_ABORTED], inevasi);
    printf("PID processo taxi che ha fatto piu' strada per servire un cliente: %d, %d celle attraversate\n",
           statistic->strada[0], statistic->strada[1]);
    printf("PID processo taxi che ha fatto il tempo piu' lungo per una richiesta: %d, %ld secondi\n", statistic->pid_taxi, statistic->vita);
    printf("PID processo taxi che ha raccolto piu' richieste: %d, %d richieste\n", statistic->richieste[0], statistic->richieste[1]);
    printf("Mappa con evidenziate le sorgenti e le %d celle piu' attraversate\n", get_so_top_cell());
    /* TODO: Funzione per la stampa di questa mappa finale */
}

static void handler_close (int sig)
{
    if (sig == SIGALRM) {
        stop_print = 1;
        printf("-- finito tempo simulazione --\n");
    }
    else if (sig == SIGINT)
    {
        printf("Ricevuto segnale da tastiera (CTRL Z)di generare una richiesta\n");
    }
}

int main(int argc, char **argv)
{
    int sem_set_utility_id, sem_mutex_celle_id, num_sem_mutex, shm_id;
    int shm_id_stat;
    unsigned int timer;
    struct sigaction new;
    /* Come gestire il segnale SIGALRM ~ usato per terminare la simulazione */
    new.sa_flags = 0;
    new.sa_handler = handler_close;
    sigaction(SIGALRM, &new, NULL);
    sigaction(SIGTSTP, &new, NULL);

    printf("-- INIZIO PREPARAZIONE SIMULAZIONE --\n\n");
    /* Inizio dalla lettura del file di configurazione ~ usare file allegato in cartella */
    inizializza_configurazione();
    timer = get_so_duration();

    /* Creazione memoria condivisa per raccolta delle statistiche ~ condiviso solo con i taxi */
    shm_id_stat = create_shmemory(STAT_KEY, sizeof(st_raccolta));
    statistic = shmat(shm_id_stat, NULL, 0);
    memset(statistic, 0, sizeof(st_raccolta));

    /* Preparazione mappa e allocazione in memoria condivisa */
    num_sem_mutex = (DIM_MAPPA - get_so_holes());
    sem_mutex_celle_id = create_semaphore(SEM_KEY_MUTEX_CELLE, num_sem_mutex);      /* Creo set semaforo per le celle (non holes) lo metto a 1 (aperto) */
    shm_id = inizializza_mappa(sem_mutex_celle_id);                           /* Salvo l'ID della SM creata nell'inizializzazione della mappa */

    /* Creazione code di messaggi, processi source e taxi ~ settaggio dei semafori */
    create_source_queue();
    sem_set_utility_id = create_semaphore(SEM_KEY_UTILITY, NUM_SEM);
    create_taxi(sem_set_utility_id, 0, shm_id, shm_id_stat);
    set_semop(sem_set_utility_id, SEM_NUM_TAXI_INIT_READY, (0 - get_so_taxi()));  /* Ogni taxi inizilizzato incrementa il semaforo, se non arriva a 0 manca qualcuno */
    setval_semaphore(sem_set_utility_id, SEM_NUM_MUTEX_STAT, 1);

    /* Dove parte la simulazione ~ movimento taxi e creazione richieste */
    printf("-- inizio simulazione --\n");
    create_client(sem_set_utility_id, 0, shm_id);
    setval_semaphore(sem_set_utility_id, SEM_NUM_TAXI_START, get_so_taxi());

    alarm(timer);
    while (!stop_print)
    {
        print_map(mappa);
        printf("--------------------------------------------------------------------------------------------------------------------------------\n");
        sleep(1);
    }

    chiudi_processi_child(sem_set_utility_id, SEM_NUM_TAXI, get_so_taxi(), "taxi");      /* Chiusura processi taxi */
    chiudi_processi_child(sem_set_utility_id, SEM_NUM_CLIENT, get_so_source(), "source");/* Chiusura processi source */
    /* Si e' chiusa la simulazione ~ timer scaduto e cancello tutte le strutture IPC */
    termina_specifiche();
    remove_queue_source();                                                /* Chiudo le code di messaggi */

    printf("9. Chiudo i set di semafori creati\n");
    remove_semaphore(sem_set_utility_id);                                         /* Chiusura dei set di semafori */
    remove_semaphore(sem_mutex_celle_id);

    printf("10. Elimino l'area di memoria condivisa\n");
    shmdt(mappa);                                                         /* Stacco le shared memory dal processo master */
    shmdt(statistic);
    if (shmctl(shm_id, IPC_RMID, 0) < 0)                              /* Cancello aree di memoria condivisa */
        ERROR;
    if (shmctl(shm_id_stat, IPC_RMID, 0) < 0)
        ERROR;
    printf("\n-- FINE --");

    exit(0);
}
