#include "cella.h"

int random_num(int a, int b)
{
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
    c->so_cap = random_num(cap_min, cap_max);               /* Valori generato randomicamente */
    c->so_timensec = random_num(so_time_min, so_time_max);
    c->source = source;                                     /* 1 se e' una cella generatrice, 0 altrimenti (flag) */
    c->hole = hole;                                         /* 1 se e' una cella inaccessibile, 0 altrimenti (flag) */
    c->coordinate.colonna = x;                              /* Set di coordinate per trovare la cella nella mappa */
    c->coordinate.riga = y;
    c->statoCella.num_taxi = 0;                             /* Le celle vengono create vuote inizialmente */
    c->statoCella.m_id = -1;                                /* Non punta a nessuna coda all'inizio, si modifica solo se la cella e' source */
}
int set_source(int sources, const int probability)
{
    if (sources > 0)
    {
        return random_num(0, probability);
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
int is_hole (st_cella c)
{
    if (c.hole == 1)
        return 1;
    else
        return 0;
}
void print_cella(st_cellap cellap)
{
    if (cellap->source == 1)
        printf("  S, %d  ", cellap->statoCella.num_taxi);      /* Indica una cella sorgente */
    else if (cellap->hole == 1)
        printf("  X, -  ");                                     /* Indica una cella inaccessibile */
    else
        printf("  N, %d  ", cellap->statoCella.num_taxi);      /* Indica una cella generica */
}
void enter_cella(st_cellap c)
{
    c->statoCella.num_taxi++;
}
void exit_cella(st_cellap c)
{
    c->statoCella.num_taxi--;
}
/*
* Created by giulia on 11/12/2020.
*/
