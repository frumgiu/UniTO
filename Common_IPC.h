#ifndef TAXICAB_COMMON_IPC_H
#define TAXICAB_COMMON_IPC_H

#include <time.h>
#include <sys/ipc.h>
#include <sys/sem.h>    /* Semafori */
#include <sys/msg.h>	/* Coda di messaggi */
#include <sys/shm.h>	/* Shared Memory */
#include <errno.h>

#define SEM_ID_CLIENT        0
#define SEM_ID_TAXI          1
#define SEM_ID_TAXI_START    2
#define SEM_KEY_SOURCE      0x1111  /* key per il set di semafori per i porcessi source e taxi */
#define SEM_MUTEX           0x2222  /* key per il mutex per la shared memory */
#define MAPPA_KEY           51213

/* union per il semaforo */
union semun {
    int val;
    struct semid_ds *buf;
    unsigned short  *array;
    struct seminfo  *__buf;
};

int create_semaphore(int, int);
int setval_semaphore(int, int, int);
int getval_semaphore(int, int);
int remove_semaphore(int);
int increment_sem (int, int);
int decrement_sem (int, int);

#endif /* TAXICAB_COMMON_IPC_H */
/*
* Created by giulia on 03/01/2021.
*/
