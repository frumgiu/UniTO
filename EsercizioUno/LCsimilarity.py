import math
from nltk.corpus import wordnet as wn
from Shortestpath import shortest_len as sl
from Shortestpath import DEPTH_MAX as DEPTH_MAX


def my_lc_similarity(word1, word2):
    max_similarity = 0
    for synset1 in wn.synsets(word1):
        for synset2 in wn.synsets(word2):
            path_correct = sl(synset1, synset2)
            if path_correct == 0:
                path_correct = 1

            similarity = -math.log(path_correct / (2 * DEPTH_MAX))
            if similarity > max_similarity:
                max_similarity = similarity
    return max_similarity
