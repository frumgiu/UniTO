#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <errno.h>
#include "Common_IPC.h"

static union semun sem;

int create_semaphore(int key, int num_sem)
{
    int sem_id;
    if ((sem_id = semget(key, num_sem, 0666 | IPC_CREAT)) < 0)
    {
        fprintf(stderr, "Errore '%s' nella creazione del semaforo per le code\n", strerror(errno));
        exit(1);
    }
    return sem_id;
}

int setval_semaphore (int sem_id, int semnum, int value)
{
    int result;
    sem.val = value;
    if ((result = semctl(sem_id, semnum, SETVAL, sem.val)) < 0)
    {
        fprintf(stderr, "Errore '%s' nell'inizializzazione del valore del semaforo\n", strerror(errno));
        exit(1);
    }
    return result;
}

int getval_semaphore (int sem_id, int sem_num)
{
    int valore;
    if((valore = semctl(sem_id, sem_num, GETVAL)) < 0)
    {
        fprintf(stderr, "Errore '%s' nella lettura del valore del semaforo\n", strerror(errno));
        exit(1);
    }
    return valore;
}

int remove_semaphore (int sem_id)
{
    int result;
    if ((result = semctl(sem_id, 0, IPC_RMID, 0)) < 0)
    {
        fprintf(stderr, "Errore '%s' nella rimozione del semaforo\n", strerror(errno));
        exit(1);
    }
    return result;
}

int increment_sem (int semId, int semNum)
{
    int result;
    struct sembuf sops;
    sops.sem_num = semNum;
    sops.sem_op = 1;
    sops.sem_flg = 0;
    if ((result = semop(semId, &sops, 1)) < 0)
    {
        fprintf(stderr, "Errore '%s' nel incrementare il semaforo\n", strerror(errno));
        exit(1);
    }
    return result;
}

int decrement_sem (int semId, int semNum)
{
    int result;
    struct sembuf sops;
    sops.sem_num = semNum;
    sops.sem_op = -1;
    sops.sem_flg = 0;
    if ((result = semop(semId, &sops, 1)) < 0)
    {
        fprintf(stderr, "Errore '%s' nel decrementare il semaforo\n", strerror(errno));
        exit(1);
    }
    return result;
}

/* TODO: funzione che legge dalla coda di mex */

/*
* Created by giulia on 05/01/2021.
*/
