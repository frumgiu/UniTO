#include "client.h"
#include "Common_IPC.h"

static const struct timespec ripetizione = {1, 0};
static struct timespec mancante = {0, 0};

int init_coda(int key)
{
    int mex_id;
    if((mex_id = msgget(key, IPC_CREAT | 0666)) == -1)
        ERROR;
    return mex_id;
}

st_clientp init_client(st_cellap source, st_mappap mappa)
{
    st_clientp mex = malloc(sizeof(st_client));

    mex->partenza = source;
    mex->destinazione = random_cella(mappa);
    while ( mex->destinazione == source)                   /* Deve essere diverso dalla cella di partenza */
        mex->destinazione = random_cella(mappa);
    return mex;
}

void new_client (int i, int j, int sem_id, int shm_id)
{
    st_clientp messaggio;
    int child;

    child = fork();
    if (child == -1)
    {
        ERROR;
    }
    else if (child == 0)        /* CHILD PROCESS */
    {
        st_mappap mappa = shmat(shm_id, NULL, 0);
        srand(getpid());
        while (getval_semaphore(sem_id, SEM_NUM_CLIENT) == 0)
        {
            /* Metto mutex? */
            messaggio = init_client(&mappa->c[i][j], mappa);                                          /* Il processo crea un client */
            msgsnd(mappa->c[i][j].statoCella.queue_id, &messaggio, sizeof(&messaggio), 0);     /* Invio il messaggio nella coda */
            nanosleep(&ripetizione, &mancante);
        }
        decrement_sem(sem_id, SEM_NUM_CLIENT);
        shmdt(mappa);
        exit(0);
    }
}

void print_client(st_clientp c, st_cellap cp)
{
    printf("Il cliente parte da: (%d,%d)\n", c->partenza->coordinate.riga, c->partenza->coordinate.colonna);
    printf("Vuole arrivare a: (%d,%d)\n", c->destinazione->coordinate.riga, c->destinazione->coordinate.colonna);
    printf("Inserito in coda ID: %d\n\n", cp->statoCella.queue_id);
}
/*
* Created by giulia on 23/12/2020.
*/
