#include "Common_IPC.h"
#include "taxi.h"

static struct timespec so_duration = {0, 0};
static const struct timespec wait_cycle = {0, 400000};
static struct timespec mancante = {0, 0};

static st_mappap mappa;     /* Puntatore alla zona della mappa */

static void inizializza_configurazione() {
    printf("1. Lettura dal file .txt dei parametri di configurazione\n");
    read_conf_from_file(&configuration);
    so_duration.tv_sec = get_so_duration();  /* Mi serve per settare il tempo della nanosleep */
    print_conf(&configuration);
}

static int inizializza_mappa(int sem_celle_id)
{
    int shm_id;
    int i, j;

    printf("2. Inizializzazione della mappa, di dimensione %dx%d, e prima stampa\n", SO_HEIGHT, SO_WIDTH);
    if ((shm_id = shmget(MAPPA_KEY, sizeof(struct cella) * (DIM_MAPPA), IPC_CREAT | 0666)) == -1)
        ERROR;

    mappa = shmat(shm_id, NULL, 0);
    init_map(get_so_source(), get_so_holes(), mappa);
    /* Devo settare tutti i mutex nelle celle, assegno il numero di semaforo nelle celle (tranne holes) */
    for (i = 0; i < SO_HEIGHT; ++i) {
        for (j = 0; j < SO_WIDTH; ++j) {
            int sem_num = 0;
            if (is_hole(mappa->c[i][j]) == 0)           /* Escludo celle inaccessibili */
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
            if (is_source(mappa->c[i][j]) == 1)
                mappa->c[i][j].statoCella.queue_id = init_coda(IPC_PRIVATE);
        }
    }
}

static void remove_queue_source() {
    int i, j;
    printf("8. Chiusura delle code di messaggi delle celle source\n");
    /* creo una coda di messaggi per ogni cella source */
    for (i = 0; i < SO_HEIGHT; ++i) {
        for (j = 0; j < SO_WIDTH; ++j) {
            if (is_source(mappa->c[i][j]) == 1)
                msgctl(mappa->c[i][j].statoCella.queue_id, IPC_RMID, 0);
        }
    }
}

static void create_taxi(int sem_id, int valore, int shm_id) {
    int n;
    printf("4. Creazione di %d taxi per servire le richeste\n", get_so_taxi());
    setval_semaphore(sem_id, SEM_ID_TAXI, valore);
    setval_semaphore(sem_id, SEM_ID_TAXI_START, valore);
    n = get_so_taxi();                                   /* Numero di taxi che devo creare */
    while (n > 0) {
        init_taxi(random_cella(mappa), sem_id, shm_id);  /* Nella funzione sono gia' escluse le celle holes, non serve ricontrollare */
        --n;
    }
    print_map(mappa);
}

static void create_client(int sem_id, int valore, int shm_id) {
    int i, j;
    printf("5. I source prcocessi incominciano a creare richieste e i taxi a servirle\n");
    if (setval_semaphore(sem_id, SEM_ID_CLIENT, valore) == 0)
        for (i = 0; i < SO_HEIGHT; ++i) {
            for (j = 0; j < SO_WIDTH; ++j) {
                if (is_source(mappa->c[i][j]) == 1)
                    new_client(i, j, sem_id, shm_id);
            }
        }
}

static void chiudi_processi_child(int sem_id, int semnum, int valore, char* child) {
    printf("7. Terminazione dei processi %s\n", child);
    setval_semaphore(sem_id, semnum, valore);
    while (semctl(sem_id, semnum, GETVAL) > 0)
        nanosleep(&wait_cycle, &mancante);
}

static void termina_specifiche() {
    printf("6. Stampa delle specifiche richieste a termine simulazione\n");
    printf("Numero di viaggi (completi, inevasi e abortiti): %d\n", 0);
    printf("Processo taxi che ha fatto piu' strada: %d\n", 0);                              /* Stampo il pid del processo */
    printf("Processo taxi che ha fatto il tempo piu' lungo per una client: %d\n", 0);
    printf("Processo taxi che ha servito piu' clienti: %d\n", 0);
    printf("Mappa con evidenziate le sorgenti e le %d celle piu' attraversate\n", get_so_top_cell());
    /* TODO: Funzione per la stampa di questa mappa finale */
}

int main(int argc, char **argv)
{
    int sem_set_id;
    int sem_mutex_id;
    int shm_id;
    int num_sem_mutex;

    printf("-- INIZIO SIMULAZIONE --\n\n");
    inizializza_configurazione();
    num_sem_mutex = (DIM_MAPPA - get_so_holes());
    sem_mutex_id = create_semaphore(SEM_MUTEX, num_sem_mutex);          /* Creo set semaforo per le cell (non holes) lo metto a 1 (aperto) */
    shm_id = inizializza_mappa(sem_mutex_id);                           /* Salvo l'ID della SM creata nell'inizializzazione della mappa */
    create_source_queue();                                              /* Creo code di messaggi nelle celle source */
    sem_set_id = create_semaphore(SEM_KEY_SOURCE, 3);                   /* Creo set di 3 semafori, 1 per source, 2 per taxi */
    /* -- SEZIONE CRITICA -- i taxi aggiorneranno la loro posizione */
    create_taxi(sem_set_id, 0, shm_id);
    create_client(sem_set_id, 0, shm_id);
    setval_semaphore(sem_set_id, SEM_ID_TAXI_START, get_so_taxi());                     /* Faccio partire la corsa dei processi taxi */
    /* TODO: Ogni secondo deve stampare la mappa aggiornata, per vedere i taxi muoversi */
    /* alarm(get_so_duration); */
    /* sigaction(); */
    nanosleep(&so_duration, &mancante);                                                  /* Il padre dorme per il tempo SO_DURATION */
    /* -- FINE SEZIONE CRITICA -- */
    /* TODO: appena si azzera posso svegliare gli altri nanosleep e interromperli, per finire subito */
    printf("\n-- finito tempo simulazione --\n");
    termina_specifiche();
    chiudi_processi_child(sem_set_id, SEM_ID_CLIENT, get_so_source(), "source");    /* Chiusura processi source */
    chiudi_processi_child(sem_set_id, SEM_ID_TAXI, get_so_taxi(), "taxi");          /* Chiusura processi taxi */
    printf("9. Chiudo i set di semafori creati\n");
    remove_semaphore(sem_set_id);                                                        /* Chiusura dei due set di semafori */
    remove_semaphore(sem_mutex_id);
    remove_queue_source();                                                               /* Chiudo le code di messaggi */
    shmdt(mappa);                                                                        /* Stacco la shared memory dal processo master */
    printf("10. Elimino l'area di memoria condivisa\n");
    if (shmctl(shm_id, IPC_RMID, 0) < 0)                                             /* Cancello area memoria condivisa */
        ERROR;
    printf("\n-- FINE SIMULAZIONE --");

    exit(0);
}
