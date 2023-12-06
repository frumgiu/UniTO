#include "client.h"
#include "Common_IPC.h"

static void handler_child (int sig)
{
    if (sig == SIGTSTP)
    {
        /* printf("Creo una richiesta in risposta al segnale in PID %d\n", getpid()); */
    }
}

int init_coda(int key)
{
    int mex_id;
    if((mex_id = msgget(key, IPC_CREAT | 0666)) == -1)
        ERROR;
    return mex_id;
}
/* Crea un nuovo cliente, con destinazione generata randomicamente */
static st_clientp init_client(st_cellap source, st_mappap mappa)
{
    st_clientp mex = malloc(sizeof(st_client));
    memcpy(&(mex->partenza), &source->coordinate, sizeof(struct coordinate));
    do                   /* Deve essere diverso dalla cella di partenza */
        memcpy(&(mex->destinazione), &(random_cella(mappa)->coordinate), sizeof(struct coordinate));
    while (mex->destinazione.riga == source->coordinate.riga && mex->destinazione.colonna == source->coordinate.colonna);
    return mex;
}

void new_client (int i, int j, int sem_id, int shm_id)
{
    st_clientp messaggio;
    int child;
    unsigned int timer = 2;

    child = fork();
    if (child == -1)
    {
        ERROR;
    }
    else if (child == 0)        /* CHILD PROCESS */
    {
        st_mappap mappa = shmat(shm_id, NULL, 0);
        struct  sigaction sig;
        sig.sa_handler = handler_child;
        sig.sa_flags = 0;
        sigaction(SIGTSTP, &sig ,  NULL);
        srand(getpid());
        while (getval_semaphore(sem_id, SEM_NUM_CLIENT) == 0)
        {
            messaggio = init_client(&mappa->c[i][j], mappa);    /* Il processo crea un client */
            /*print_client(messaggio, mappa->c[i][j].statoCella.queue_id);*/
            msgsnd(mappa->c[i][j].statoCella.queue_id, messaggio, sizeof(st_client), 0);     /* Invio il messaggio nella coda */
            free(messaggio);
            sleep(timer);
        }
        decrement_sem(sem_id, SEM_NUM_CLIENT);
        shmdt(mappa);
        exit(0);
    }
}

void print_client(st_clientp c, int queue_id )
{
    printf("Il cliente parte da: (%d,%d)\n", c->partenza.riga, c->partenza.colonna);
    printf("Vuole arrivare a: (%d,%d)\n", c->destinazione.riga, c->destinazione.colonna);
    printf("Inserito in coda ID: %d\n\n", queue_id);
}
/*
* Created by giulia on 23/12/2020.
*/
