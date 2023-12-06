#include <time.h>
#include "mappa.h"
#include "Common_IPC.h"

void init_map(const int sources, const int holes, st_mappap mappa)
{
    int riga, colonna;
    int flagS, flagH;
    int i, j;
    int probability_source;

    if (is_dimension_ok(sources, holes) == 1)
    {
        i = sources;
        j = holes;
        probability_source = DIM_MAPPA - holes;
        memset(mappa, 0, sizeof(st_mappa));  /* Mette a 0 tutta la memoria che contiene mappa */

        srand(time(NULL));
        while((i > 0 || j > 0))      /* Devo assegnare tutti i source e tutti gli hole, SERVE UN TIMER CHE LO FACCIA FALLIRE SE VA IN LOOP */
        {
            for (riga = 0; riga < SO_HEIGHT; riga++)
            {
                for (colonna = 0; colonna < SO_WIDTH; colonna++)
                {
                    st_cellap cellap = &mappa->c[riga][colonna];
                    if (is_source(cellap) || is_hole(cellap))
                        continue;
                    flagS = set_source(i, probability_source);
                    flagH = set_holes(j, flagS);
                    create_cella(cellap, get_so_cap_min(), get_so_cap_max(),
                                 get_so_timensec_min(), get_so_timensec_max(), flagS, flagH, colonna, riga);
                    if (flagS)
                        i--;
                    if (flagH)
                    {
                        if(check_hole_ok(mappa, riga, colonna))
                            j--;
                        else                /* Non puo' essere un hole */
                            cellap->hole = 0;
                    }
                }
            }
        }
    } else {
        remove_semaphore(semget(SEM_KEY_MUTEX_CELLE, 0, 0));
        fprintf(stderr, "Non e' stato possibile inizializzare la mappa a causa di un problema di dimensioni (init_map).\n");
        exit(1);
    }
}

int is_dimension_ok(int sources, int holes)
{
    if (sources > DIM_MAPPA)
    {
        fprintf(stderr, "Il numero di celle sorgenti, %d, e' maggiore del numero di celle della mappa %d (is_dimension_ok)\n",sources, DIM_MAPPA);
        return 0;
    }
    else if (sources == 0)
    {
        fprintf(stderr, "Siccome ci sono %d sorgenti e' impossibile generare richieste (is_dimension_ok).\n", sources);
        return 0;
    }
        /* TODO: verificare vada bene, mi da errore in casi che potrebbero stare ma rischiano il loop -- DOVREBBE ANDARE */
    else if((holes * 9) - sources >= DIM_MAPPA_BORDO)
    {
        fprintf(stderr, "Il numero di holes richiesti, %d, non e' possibile distribuirlo senza creare muri, per le dimensioni della mappa, %d (is_dimension_ok).\n", holes, DIM_MAPPA);
        return 0;
    }
    else if ((holes + sources) > DIM_MAPPA)
    {
        fprintf(stderr, "Il numero di holes, %d, piu il numero di sorgenti, %d, superano le dimensioni della mappa, %d (is_dimension_ok).\n", holes, sources, DIM_MAPPA);
        return 0;
    }
    else
        return 1;
}

int check_hole_ok(st_mappap m, int r, int c)
{
    int i, j;
    int inizio_r, inizio_c;
    int fine_r, fine_c;

    inizio_r = r == 0 ? 0 : r - 1;
    fine_r = r == SO_HEIGHT ? r : r + 1;
    inizio_c = c == 0 ? 0 : c - 1;
    fine_c = c == SO_WIDTH ? c : c + 1;

    for (i = inizio_r; i <= fine_r; ++i)
    {
        for (j = inizio_c; j <= fine_c; ++j)
        {
            if (is_hole(&m->c[i][j]) == 1 && !(i == r && j == c))
                return 0;
        }
    }
    return 1;
}

void print_map(st_mappap m)
{
    int riga, colonna;
    for(riga = 0; riga < SO_HEIGHT; riga++)
    {
        for(colonna = 0; colonna < SO_WIDTH; colonna++)
        {
            print_cella(&m->c[riga][colonna]);
        }
        printf("\n");        /* Riga dopo */
    }
}

st_cellap random_cella(st_mappap mappa)
{
    st_cellap result;
    do
    {
        result = &mappa->c[random_num(0, SO_HEIGHT-1)][random_num(0, SO_WIDTH-1)];
    }
    while (is_hole(result));
    return result;
}

st_cellap return_cell(int x, int y, st_mappap mappa)
{
    return &mappa->c[x][y];
}

/*
* Created by giulia on 11/12/2020.
*/
