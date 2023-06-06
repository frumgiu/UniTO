from math import sqrt
import numpy as np
from nltk.corpus import stopwords
from nltk.tokenize import word_tokenize
from nltk.stem import PorterStemmer


def tokenization(file_path, w):
    def _clean_tokens(tokens):
        return [token.lower() for token in tokens if token.isalpha()]

    def _remove_stop_word(words):
        return [word for word in words if word not in stopwords.words('english')]

    def _filter_words(words):
        return _remove_stop_word(_clean_tokens(words))

    def _create_token_seq(text_, w_):
        tokens = _clean_tokens(word_tokenize(text_))
        sequences = []
        for i in range(0, len(tokens) - w_ + 1, w_):
            sequences.append(tokens[i:i + w_])
        return sequences

    with open(file_path, 'r', encoding='utf-8') as file:
        text = file.read()
    stemmer = PorterStemmer()
    # Sequenze di token
    tokens_seq = _create_token_seq(text, w)
    tokens_seq_clean = [_remove_stop_word(ts) for ts in tokens_seq]
    tokens_seq_stem = [[stemmer.stem(w) for w in token_seq_clean] for token_seq_clean in tokens_seq_clean]
    # Parole uniche del testo
    unique_words = list(set(stemmer.stem(word) for word in _filter_words(word_tokenize(text))))
    return tokens_seq_stem, unique_words


def create_blocks(token_seq, k):
    blocks = []
    par_tr = []
    for i in range(0, len(token_seq), k):
        blocks.append(token_seq[i: i+k])
        for seq in token_seq[i: i+k]:
            if 'giuliapeppia' in seq:
                par_tr.append(len(blocks) - 1)
    return blocks, par_tr


def lexical_score(blocks_list, word_list):
    def _blocks_score(block1, block2):
        numerator = 0
        sum_sqrt_1 = 0
        sum_sqrt_2 = 0
        for w in word_list:
            block1_count = 0
            block2_count = 0
            for j in range(0, len(block1)):
                block1_count += block1[j].count(w)
            for j in range(0, len(block2)):
                block2_count += block2[j].count(w)
            numerator += block1_count * block2_count
            sum_sqrt_1 += block1_count ** 2
            sum_sqrt_2 += block2_count ** 2
        return numerator / sqrt(sum_sqrt_1 * sum_sqrt_2)

    def _smoothing(ls_score, n_smooth=2, w=4):
        for j in range(0, n_smooth):
            ls_score = np.convolve(ls_score, np.ones(w) / w, mode='same')
        return ls_score

    scores = []
    for i in range(0, len(blocks_list)-1):
        scores.append(_blocks_score(blocks_list[i], blocks_list[i+1]))
    return scores, _smoothing(scores)


def find_boundaries(ls_score, num_cuts):
    def depth_side_score(_ls_score, gap_i, left):
        depth_score_side = 0
        index = gap_i
        while _ls_score[index] - _ls_score[gap_i] >= depth_score_side:
            depth_score_side = _ls_score[index] - _ls_score[gap_i]
            index = index - 1 if left else index + 1
            if (index < 0 and left) or (index == len(_ls_score) and not left):
                break
        return depth_score_side

    def get_depth_score(_ls_score):
        depth_scores = []
        for i, score in enumerate(_ls_score):
            depthScore = depth_side_score(_ls_score, i, True) + depth_side_score(_ls_score, i, False)
            if i != 0 and i != len(_ls_score) - 1:
                depth_scores.append((depthScore, i))
        return depth_scores

    depth_score = sorted(get_depth_score(ls_score), reverse=True)
    return depth_score[0:num_cuts]
