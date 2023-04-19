from collections import deque
DEPTH_MAX = 19


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


def lowest_common_subsumer(synset1, synset2):
    lcs = None
    a1 = get_all_hypernyms(synset1)
    a2 = get_all_hypernyms(synset2)
    common_hypernyms = a1 & a2
    if len(common_hypernyms) != 0:         # Per risolvere il problema dei verbi momentaneamente
        temp_lcs = min(common_hypernyms, key=lambda x: x.shortest_path_distance(synset1) + x.shortest_path_distance(synset2))
        lcs = temp_lcs
    return lcs


def bfs_wordnet(synset_iniziale, synset_common):
    if synset_iniziale.name() == synset_common.name():
        return {synset_iniziale: 0}
    queue = deque([(synset_iniziale, 0)])
    path = {}
    while queue:
        s, depth = queue.popleft()
        if s in path:
            continue
        path[s] = depth
        if s.name() == synset_common.name():
            return path
        depth += 1
        queue.extend((hyp, depth) for hyp in s.hypernyms())
        queue.extend((hyp, depth) for hyp in s.instance_hypernyms())
    return path


def shortest_len(syn1, syn2):
    lcs = lowest_common_subsumer(syn1, syn2)
    if lcs is None:
        return DEPTH_MAX
    path1 = bfs_wordnet(syn1, lcs)
    path2 = bfs_wordnet(syn2, lcs)
    path = path1[lcs] + path2[lcs]
    return path
