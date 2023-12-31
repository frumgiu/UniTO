#include <stdio.h>
#include <stdlib.h>
#include "Common_IPC.h"

#include "cella.h"

int random_num(int a, int b)
{
    /* TODO: chiedere a papa' in una riga */
    int random = 0;
    int minimo = 0, massimo = 0;
    if (a < b)
    {
        minimo = a;
        massimo = b + 1;
    }
    else
    {
        minimo = b;
        massimo = a + 1;
    }
    random = (rand() % (massimo - minimo)) + minimo;
    return random;
}

void create_cella(st_cellap c, int cap_min, int cap_max, int so_time_min, int so_time_max, int source, int hole, int x, int y)
{

    c->so_cap = random_num(cap_min, cap_max);               /* Valori generati randomicamente */
    c->so_timensec = random_num(so_time_min, so_time_max);
    c->source = source;                                     /* 1 se e' una cella generatrice, 0 altrimenti (flag) */
    c->hole = hole;                                         /* 1 se e' una cella inaccessibile, 0 altrimenti (flag) */
    c->coordinate.colonna = x;                              /* Set di coordinate per trovare la cella nella mappa */
    c->coordinate.riga = y;
    c->statoCella.attraversamento = 0;
    c->statoCella.num_taxi = 0;                             /* Le celle vengono create vuote inizialmente */
    c->statoCella.queue_id = -1;                            /* Non punta a nessuna coda all'inizio, si modifica solo se la cella e' source */
    c->statoCella.sem_num = 0;
}

int set_source(int sources, const int probability)
{
    if (sources > 0)
    {
        sources /= 2;
        sources++;
        return sources > random_num(0, probability);
    }
    else
        return 0;
}

int set_holes (int holes, int flag_source)
{
    if (holes > 0 && flag_source == 0)
    {
        return random_num(0, 1);
    }
    else
        return 0;
}

int is_hole (st_cellap c)
{
    return c->hole != 0;
}

int is_source (st_cellap c)
{
    return c->source != 0;
}

static int is_full (st_cellap c)
{
    return (c->statoCella.num_taxi == c->so_cap) ? 1 : 0;
}

void print_cella(st_cellap cellap)
{
    if (is_source(cellap) == 1)
        printf("  S, %d  ", cellap->statoCella.num_taxi);      /* Indica una cella sorgente */
    else if (is_hole(cellap) == 1)
        printf("  X, -  ");                                    /* Indica una cella inaccessibile */
    else
        printf("  ., %d   ", cellap->statoCella.num_taxi);      /* Indica una cella generica */
}

int enter_cella(st_cellap c)
{
    int result = 0;
    /*printf("Entro in cella %d,%d con PID: %d e sem_id %d e sem_num %d\n", c->coordinate.colonna, c->coordinate.riga, getpid(), c->statoCella.sem_set_id, c->statoCella.sem_num);*/
    decrement_sem(c->statoCella.sem_set_id, c->statoCella.sem_num);
    if (!is_full(c))
    {
        c->statoCella.num_taxi++;
        c->statoCella.attraversamento++;
        result = 1;
    }
    increment_sem(c->statoCella.sem_set_id, c->statoCella.sem_num);
    return result;
}

void exit_cella(st_cellap c)
{
    decrement_sem(c->statoCella.sem_set_id, c->statoCella.sem_num);
    c->statoCella.num_taxi--;
    increment_sem(c->statoCella.sem_set_id, c->statoCella.sem_num);
}
/*
* Created by giulia on 11/12/2020.
*/
