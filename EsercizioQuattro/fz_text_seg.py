from math import sqrt
import numpy as np
from nltk.corpus import stopwords
from nltk.tokenize import word_tokenize
from nltk.stem import PorterStemmer


def tokenization(file_path, w):
    def create_token_seq(text_, w_):
        tokens = word_tokenize(text_)
        sequences = []
        for i in range(len(tokens) - w_ + 1):
            sequence = tokens[i:i + w_]
            sequences.append(sequence)
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
        denominator = sqrt(block1_sum * block2_sum)
        return numerator/denominator

    ls_blocks_score = []
    for index in range(1, len(token_seq)):  # da 1 a numero di sequenze che ho
        # Devo tenere conto che la finestra non sarà sempre = k, dipende da quante sequenze mi sono rimaste
        window_size = min(index, k, len(token_seq) - index)
        block1 = token_seq[index - window_size: index]
        block2 = token_seq[index: index + window_size]
        ls_blocks_score.append(block_score(block1, block2, word_list, window_size))
    return ls_blocks_score


def getDepthLeftRight(lexScores, i, left):
    depthScore = 0
    lr = i
    while lexScores[lr] - lexScores[i] >= depthScore:
        depthScore = lexScores[lr] - lexScores[i]
        lr = lr - 1 if left else lr + 1
        if (lr < 0 and left) or (lr == len(lexScores) and not left):
            break
    return depthScore


def getGapBoundaries(lexScores):
    boundaries = []
    cutoff = np.mean(lexScores) - np.std(lexScores)

    for i, score in enumerate(lexScores):
        depthLeftScore = getDepthLeftRight(lexScores, i, True)
        depthRightScore = getDepthLeftRight(lexScores, i, False)

        depthScore = depthLeftScore + depthRightScore
        if depthScore >= cutoff:
            boundaries.append(i)
    return boundaries


def getBoundaries(lexScores, pLocs, w):
    # convert boundaries from gap indices to token indices
    gapBoundaries = getGapBoundaries(lexScores)
    tokBoundaries = [w * (gap + 1) for gap in gapBoundaries]

    # do not allow duplicates of boundaries
    parBoundaries = set()
    # convert raw token boundary index to closest index where paragraph occurs
    for i in range(1, len(tokBoundaries)):
        parBoundaries.add(min(pLocs, key=lambda b: abs(b - tokBoundaries[i])))

    return sorted(list(parBoundaries))
