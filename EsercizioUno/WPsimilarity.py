from nltk.corpus import wordnet as wn
from Shortestpath import lowest_common_subsumer as lcs_fun


def max_wup_similarity(word1, word2):
    max_similarity = 0
    for synset1 in wn.synsets(word1):
        for synset2 in wn.synsets(word2):
            lcs_syn = lcs_fun(synset1, synset2)
            lcs_depth = 19
            if lcs_syn is not None:
                lcs_depth = lcs_syn.max_depth()
            similarity = (2.0 * lcs_depth) / (synset1.max_depth() + synset2.max_depth())
            if similarity > max_similarity:
                max_similarity = similarity
    return max_similarity

