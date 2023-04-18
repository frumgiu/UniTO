from nltk.corpus import wordnet as wn
from collections import deque


def get_all_hypernyms(synset):
    list_hyper = set()
    list_hyper.add(synset)
    for hyper in synset.hypernyms():
        list_hyper.add(hyper)
        list_hyper |= set(get_all_hypernyms(hyper))
    return list_hyper


def lowest_common_subsumer(synset1, synset2):
    name = None
    a1 = get_all_hypernyms(synset1)
    a2 = get_all_hypernyms(synset2)
    common_hypernyms = a1.intersection(a2)
    if my_len(common_hypernyms) != 0:      # Per risolvere il problema dei verbi momentaneamente
        lcs = min(common_hypernyms, key=lambda x: x.shortest_path_distance(synset1) + x.shortest_path_distance(synset2))
        name = lcs
    return name


def bfs_wordnet(synset_iniziale, common):
    if synset_iniziale.name() == common.name():
        return {synset_iniziale: 0}
    queue = deque([(synset_iniziale, 0)])
    path = {}
    while queue:
        s, depth = queue.popleft()
        if s in path:
            continue
        path[s] = depth
        if s.name() == common.name():
            return path
        depth += 1
        queue.extend((hyp, depth) for hyp in s.hypernyms())
    return path


def my_len(syn1, syn2):
    lcsname = lowest_common_subsumer(syn1, syn2)
    path1 = bfs_wordnet(syn1, lcsname)
    path2 = bfs_wordnet(syn2, lcsname)
    path = path1[lcsname] + path2[lcsname]
    return path


def my_sp_similarity(word1, word2):
    max_similarity = 0
    for synset1 in wn.synsets(word1):
        for synset2 in wn.synsets(word2):
            similarity = 2.0 * wn.max_depth - my_len(synset1, synset2)
            if similarity > max_similarity:
                print("Senso uno: ", synset1.name(), " Senso due: ", synset2.name())
                max_similarity = similarity
    return max_similarity


w1 = 'girl'
w2 = 'man'
print("synset: ", wn.synsets(w1)[0], wn.synsets(w2)[0])
print("Distanza minima tra due synset: ", (wn.synsets(w1)[0]).shortest_path_distance(wn.synsets(w2)[0]))
# print("Distanza minima MIA: ", my_len(wn.synsets(w1)[0], wn.synsets(w2)[0]))
print(my_sp_similarity(w1, w2))
