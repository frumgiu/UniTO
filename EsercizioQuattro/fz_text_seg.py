from math import sqrt
import numpy as np
from nltk.corpus import stopwords
from nltk.tokenize import word_tokenize
from nltk.stem import PorterStemmer


def tokenization(file_path, w):
    def create_token_seq(text_, w_):
        tokens = word_tokenize(text_)
        sequences = []
        # TODO: sbagliato penso
        for i in range(0, len(tokens) - w_ + 1, w):
            sequences.append(tokens[i:i + w_ - 1])
        return sequences

    def find_paragraph_breaks(text_):
        paragraph_breaks = []
        for i in range(len(text_)):
            if text_[i:i + 2] == '\n\n':
                paragraph_breaks.append(i)
        return paragraph_breaks

    with open(file_path, 'r', encoding='utf-8') as file:
        text = file.read()

    tokens_seq = create_token_seq(text, w)
    stemmer = PorterStemmer()
    unique_words = [word.lower() for word in word_tokenize(text) if word not in stopwords.words('english')]
    unique_words = list(set(stemmer.stem(word) for word in unique_words))

    token_seq_clean = [[w.lower() for w in ts if w not in stopwords.words('english')] for ts in tokens_seq]
    token_seq_stem = [[stemmer.stem(w) for w in word] for word in token_seq_clean]
    return token_seq_stem, unique_words, find_paragraph_breaks(text)


def lexical_score(token_seq, word_list, k):
    def block_score(block_1, block_2, words_list, w_size):
        numerator = 0
        block1_sum = 0
        block2_sum = 0
        for w in words_list:
            block1_cnt_word = 0
            block2_cnt_word = 0
            for i in range(0, w_size):
                block1_cnt_word = block_1[i].count(w)
                block2_cnt_word = block_2[i].count(w)
            numerator += block1_cnt_word * block2_cnt_word
            block1_sum += block1_cnt_word ** 2
            block2_sum += block2_cnt_word ** 2
        return numerator / sqrt(block1_sum * block2_sum)

    ls_blocks_score = []
    for index in range(k, len(token_seq)):  # da k a numero di sequenze che ho
        # Devo tenere conto che la finestra non sarà sempre = k, dipende da quante sequenze mi sono rimaste
        window_size = min(k, len(token_seq) - index)  # index,
        block1 = token_seq[index - window_size: index]
        block2 = token_seq[index: index + window_size]
        ls_blocks_score.append(block_score(block1, block2, word_list, window_size))
        window = np.ones(window_size) / window_size
        # Smoothing
        result = np.convolve(ls_blocks_score, window, mode='same')
        print(result)
    return ls_blocks_score


def depth_cutoff(ls_score, liberal=True):
    if liberal is True:
        # Lower precision, higher recall
        return np.mean(ls_score) - np.std(ls_score)
    else:
        # Higher precision, lower recall
        return np.mean(ls_score) - np.std(ls_score) / 2


def depth_left_score(ls_score, gap_i):
    depth_score_side = 0
    index = gap_i
    return 0


def depth_score(ls_score):
    # Calcolo il depth_cutoff
    cutoff = depth_cutoff(ls_score)
    # Calcolo il left_score del gap
    # Calcolo il right_score del gap
    # depth_score(i) = (left_score - i_score) + (right_score - i_score)
    # Faccio il taglio solo se il depth_score(i) > depth_cutoff(i)
    return
