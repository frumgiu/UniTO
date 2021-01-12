#ifndef TAXICAB_COMMON_IPC_H
#define TAXICAB_COMMON_IPC_H

#include <time.h>
#include <sys/ipc.h>
#include <sys/sem.h>    /* Semafori */
#include <sys/msg.h>	/* Coda di messaggi */
#include <sys/shm.h>	/* Shared Memory */
#include <signal.h>     /* Segnali */
#include <errno.h>

#define ERROR if (errno) {fprintf(stderr, "Errore in file: %s, riga: %d durante processo (pid) %ld; errno: %d (%s)\n",  \
              __FILE__, __LINE__, (long) getpid(), errno, strerror(errno)); exit(1);}

#define ERROR_SEMA(sem_set_id, sem_num) if (errno) {fprintf(stderr, "Errore in file: %s, riga: %d durante processo (pid) %ld per sema  %d semnum %d; errno: %d (%s)\n",  \
              __FILE__, __LINE__, (long) getpid(),sem_set_id, sem_num, errno, strerror(errno));}

#define CLIENT_TOTALI           0
#define CLIENT_COMPLETED        1
#define CLIENT_ABORTED          2

#define NUM_SEM                 5
#define SEM_NUM_CLIENT          0
#define SEM_NUM_TAXI            1
#define SEM_NUM_TAXI_START      2
#define SEM_NUM_TAXI_INIT_READY 3
#define SEM_NUM_MUTEX_STAT          4

#define SEM_KEY_UTILITY         0x1111  /* key per il set di semafori per i porcessi source e taxi */
#define SEM_KEY_MUTEX_CELLE     0x2222  /* key per il mutex per la shared memory */
#define MAPPA_KEY               51213
#define STAT_KEY                92345

struct raccolta
{
    int viaggi[3];     /* Numero di viaggi serviti (completate, abortite, inevase) */
    int strada[2];     /* Taxi che ha fatto piu' strada (come numero di celle) */
    int richieste[2];  /* Taxi che ha preso in carico piu' richieste */
    int pid_taxi;      /* Taxi che ha fatto il tempo piu' lungo per servire una richiesta */
    time_t vita;       /* Tempo che ci ha messo a servirla */
};
/* union per il semaforo */
union semun {
    int val;
    struct semid_ds *buf;
    unsigned short  *array;
    struct seminfo  *__buf;
};

typedef struct raccolta st_raccolta;
typedef struct raccolta * st_raccoltap;

int create_shmemory (int, size_t);
int create_semaphore (int, int);
int setval_semaphore (int, int, int);
int getval_semaphore (int, int);
int remove_semaphore (int);
int set_semop (int, int, int);
int increment_sem (int, int);
int decrement_sem (int, int);

#endif /* TAXICAB_COMMON_IPC_H */

/*
* Created by giulia on 03/01/2021.
*/
