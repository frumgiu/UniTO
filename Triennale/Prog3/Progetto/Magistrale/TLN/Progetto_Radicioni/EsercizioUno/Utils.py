from collections import deque
DEPTH_MAX = 19


# Ritorna tutti gli iperonimi di un senso in una lista
def get_all_hypernyms(synset):
    list_hyper = set()
    for hyper in synset.hypernyms():
        list_hyper.add(hyper)
        list_hyper |= set(get_all_hypernyms(hyper))
    for hyper in synset.instance_hypernyms():
        list_hyper.add(hyper)
        list_hyper |= set(get_all_hypernyms(hyper))
    list_hyper.add(synset)
    return list_hyper


# Calcola l'antenato comune ai due sensi con percorso più corto
# tra i due sensi in questione
def lowest_common_subsumer(synset1, synset2):
    lcs = None
    a1 = get_all_hypernyms(synset1)     # Prendo gli iperonimi del senso 1
    a2 = get_all_hypernyms(synset2)     # Prendo gli iperonimi del senso 2
    common_hypernyms = a1 & a2          # Prendo solo quelli in comune
    if len(common_hypernyms) != 0:
        temp_lcs = min(common_hypernyms, key=lambda x: x.shortest_path_distance(synset1) + x.shortest_path_distance(synset2))
        lcs = temp_lcs
    return lcs


# Eseguo una visita dell'albero e ritorno il percorso svolto
def bfs_wordnet(synset_starter, synset_common):
    if synset_starter.name() == synset_common.name():
        return {synset_starter: 0}
    queue = deque([(synset_starter, 0)])
    path = {}
    while queue:
        s, depth = queue.popleft()
        if s in path:
            continue
        path[s] = depth
        if s.name() == synset_common.name():
            return path
        depth += 1
        queue.extend((hyp, depth) for hyp in s.hypernyms())             # Quando finisco di visitare un nodo metto
        queue.extend((hyp, depth) for hyp in s.instance_hypernyms())    # nella coda i suoi iperonimi
    return path


def shortest_len(syn1, syn2):
    lcs = lowest_common_subsumer(syn1, syn2)
    if lcs is None:
        return DEPTH_MAX            # Se non hanno antenati in comune allora prendo la distanza massima
    path1 = bfs_wordnet(syn1, lcs)
    path2 = bfs_wordnet(syn2, lcs)
    path = path1[lcs] + path2[lcs]
    return path
