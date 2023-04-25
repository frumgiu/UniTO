import random
from nltk import Tree
from nltk.corpus.reader.wordnet import Lemma
from nltk.corpus import semcor


def get_random_sentences(num_sentences=50):
    selected_ids = random.sample(list(range(0, len(semcor.fileids()) - 1)), num_sentences)
    selected_sentences = []
    tag_words = []
    tag_synset = []
    for sentence_id in selected_ids:
        sentence = " ".join(semcor.sents()[sentence_id])
        tags = semcor.tagged_sents(tag="sem")[sentence_id]
        for i in range(len(tags)):
            if isinstance(tags[i], Tree) and isinstance(tags[i][0], str) and isinstance(tags[i].label(), Lemma):
                tag_words.append(tags[i][0])
                tag_synset.append(tags[i].label().synset())
        selected_sentences.append(sentence)
    return selected_sentences, [tag_words, tag_synset]


def matching_synset(expected, actual):
    count = 0
    common_word = 0
    for i in range(len(expected[0])):
        for j in range(len(actual[0])):
            if expected[0][i] == actual[0][j]:
                common_word += 1
                if expected[1][i] is None or actual[1][j] is None or expected[1][i] != actual[1][j]:
                    break
                elif expected[1][i] == actual[1][j]:
                    count += 1
                    break
    return count, common_word
