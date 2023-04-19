import math
from nltk.corpus import wordnet as wn
from EsercizioUno.Shortestpath import shortest_len as sl
from EsercizioUno.Shortestpath import DEPTH_MAX as DEPTH_MAX


def my_lc_similarity(word1, word2):
    max_similarity = 0
    for synset1 in wn.synsets(word1):
        for synset2 in wn.synsets(word2):
            path_correct = sl(synset1, synset2)
            similarity = -math.log((path_correct + 1) / (2 * DEPTH_MAX))
            if similarity > max_similarity:
                max_similarity = similarity
    return max_similarity


w1 = 'tiger'
w2 = 'cat'

print(my_lc_similarity(w1, w2))
