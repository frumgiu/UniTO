import math
from nltk.corpus import wordnet as wn
from Shortestpath import shortest_len as sl
from Shortestpath import DEPTH_MAX as DEPTH_MAX
from Shortestpath import lowest_common_subsumer as lcs_fun


def max_wup_similarity(word1, word2):
    max_similarity = 0
    for synset1 in wn.synsets(word1):
        for synset2 in wn.synsets(word2):
            lcs_syn = lcs_fun(synset1, synset2)
            lcs_depth = 0
            if lcs_syn is not None:
                lcs_depth = lcs_syn.max_depth()
            similarity = (2.0 * lcs_depth) / (synset1.max_depth() + synset2.max_depth())
            if similarity > max_similarity:
                # print(synset1, synset2)
                max_similarity = similarity
    return max_similarity


def my_sp_similarity(word1, word2):
    max_similarity = 0
    for synset1 in wn.synsets(word1):
        for synset2 in wn.synsets(word2):
            similarity = 2 * DEPTH_MAX - sl(synset1, synset2)
            if similarity > max_similarity:
                max_similarity = similarity
    return max_similarity


def my_lc_similarity(word1, word2):
    max_similarity = 0
    for synset1 in wn.synsets(word1):
        for synset2 in wn.synsets(word2):
            path_correct = sl(synset1, synset2)
            similarity = -math.log((path_correct + 1) / (2 * DEPTH_MAX))
            if similarity > max_similarity:
                max_similarity = similarity
    return max_similarity
