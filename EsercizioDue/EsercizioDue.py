from nltk.wsd import lesk
from nltk.tokenize import word_tokenize
# from LeskAlgorithm import my_lesk


def get_semantic(seq, key_word):
    # temp = word_tokenize(seq)
    temp = lesk(temp, key_word)
    return temp
