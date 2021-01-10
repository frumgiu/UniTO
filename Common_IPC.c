#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "Common_IPC.h"

static union semun sem;

int create_semaphore(int key, int num_sem)
{
    int sem_id;
    if ((sem_id = semget(key, num_sem, 0666 | IPC_CREAT)) < 0)
        ERROR;
    return sem_id;
}

int setval_semaphore (int sem_id, int semnum, int value)
{
    int result;
    sem.val = value;
    if ((result = semctl(sem_id, semnum, SETVAL, sem.val)) < 0)
        ERROR;
    return result;
}

int getval_semaphore (int sem_id, int sem_num)
{
    int valore;
    if((valore = semctl(sem_id, sem_num, GETVAL)) < 0)
        ERROR;
    return valore;
}

int remove_semaphore (int sem_id)
{
    int result;
    if ((result = semctl(sem_id, 0, IPC_RMID, 0)) < 0)
        ERROR;
    return result;
}

int set_semop (int semId, int semNum, int value)
{
    int result;
    struct sembuf sops;

    sops.sem_num = semNum;
    sops.sem_op = value;
    sops.sem_flg = 0;
    if ((result = semop(semId, &sops, 1)) < 0)
        ERROR;
    printf("Esco decremento con PID %d\n", getpid());
    return result;
}

int increment_sem (int semId, int semNum)
{
    printf("Incrementa con PID %d per il sem_id %d sem_num %d\n", getpid(), semId, semNum);
   return set_semop(semId, semNum, +1);
}

int decrement_sem (int semId, int semNum)
{
    printf("Decremento con PID %d per il sem_id %d sem_num %d\n", getpid(), semId, semNum);
    return set_semop(semId, semNum, -1);
}

/*
* Created by giulia on 05/01/2021.
*/
