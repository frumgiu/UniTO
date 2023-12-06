import math
from nltk.corpus import wordnet as wn
from Utils import shortest_len as sl
from Utils import DEPTH_MAX as DEPTH_MAX
from Utils import lowest_common_subsumer as lcs_fun


# Mi serve per il main per non esportare tutte e tre le funzioni
def similarity_function(name_function, list_words_one, list_words_two):
    index = len(list_words_one)
    result = []
    for i in range(0, index):
        if name_function == 'WP':
            result.append(my_wup_similarity(list_words_one[i], list_words_two[i]))
        elif name_function == 'LC':
            result.append(my_lc_similarity(list_words_one[i], list_words_two[i]))
        elif name_function == 'SP':
            result.append(my_sp_similarity(list_words_one[i], list_words_two[i]))
        else:
            print('Unknown name function, please retry.')
    return result


def my_wup_similarity(word1, word2):
    max_similarity = 0
    if word1 == word2:
        return 1
    for synset1 in wn.synsets(word1):
        for synset2 in wn.synsets(word2):
            lcs_syn = lcs_fun(synset1, synset2)
            lcs_depth = 0
            if lcs_syn is not None:
                lcs_depth = lcs_syn.max_depth()
            temp = (synset1.max_depth() + synset2.max_depth())
            if temp > 0:
                similarity = (2.0 * lcs_depth) / temp
            else:
                similarity = (2.0 * lcs_depth) / 2
            if similarity > max_similarity:
                max_similarity = similarity
    return round(max_similarity, 2)


def my_sp_similarity(word1, word2):
    max_similarity = 0
    for synset1 in wn.synsets(word1):
        for synset2 in wn.synsets(word2):
            similarity = 2 * DEPTH_MAX - sl(synset1, synset2)
            if similarity > max_similarity:
                max_similarity = similarity
    return round(max_similarity, 2)


def my_lc_similarity(word1, word2):
    max_similarity = 0
    for synset1 in wn.synsets(word1):
        for synset2 in wn.synsets(word2):
            path_correct = sl(synset1, synset2)
            similarity = -math.log((path_correct + 1) / (2 * DEPTH_MAX))
            if similarity > max_similarity:
                max_similarity = similarity
    return round(max_similarity, 2)
