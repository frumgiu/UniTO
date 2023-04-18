from nltk.corpus import wordnet as wn


def get_all_hypernyms(synset):
    list_hyper = set()
    list_hyper.add(synset)
    for hyper in synset.hypernyms():
        list_hyper.add(hyper)
        list_hyper |= set(get_all_hypernyms(hyper))
    return list_hyper


def lowest_common_subsumer(synset1, synset2):
    result = 0
    a1 = get_all_hypernyms(synset1)
    a2 = get_all_hypernyms(synset2)
    common_hypernyms = a1.intersection(a2)
    if len(common_hypernyms) != 0:      # Per risolvere il problema dei verbi momentaneamente
        lcs = min(common_hypernyms, key=lambda x: x.shortest_path_distance(synset1) + x.shortest_path_distance(synset2))
        # lcs = max(common_hypernyms, key=lambda x: x.max_depth())
        result = lcs.max_depth()
    return result


def max_wup_similarity(word1, word2):
    max_similarity = 0
    for synset1 in wn.synsets(word1):
        for synset2 in wn.synsets(word2):
            lcs = lowest_common_subsumer(synset1, synset2)
            similarity = 2.0 * lcs/(synset1.max_depth() + synset2.max_depth())
            if similarity > max_similarity:
                print("Senso uno: ", synset1.name(), " Senso due: ", synset2.name())
                print('LCS depth usata: ', lcs)
                max_similarity = similarity
    return max_similarity


w1 = 'girl'
w2 = 'man'
print('Date le parole: ', w1, ' e ', w2)
print("Max similarity: ", max_wup_similarity(w1, w2))
