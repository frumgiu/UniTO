import random
from nltk import Tree
from nltk.corpus.reader.wordnet import Lemma
from nltk.corpus import semcor


class Element:
    def __init__(self, word, tag):
        self.word = word
        self.tag = tag

    def __str__(self):
        return f"{self.word} {self.tag}"


def test(num_sentences=50):
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


def get_random_sentences(num_sentences=50):
    selected_sentences = []
    selected_tag_sentences = []
    max_num = len(semcor.sents()) - 1
    while len(selected_sentences) < num_sentences:
        selected_id = random.randint(0, max_num)
        sentence = " ".join(semcor.sents()[selected_id])
        tag_sentence = []
        tags = semcor.tagged_sents(tag="sem")[selected_id]
        for i in range(len(tags) - 1):
            if isinstance(tags[i], Tree) and isinstance(tags[i][0], str) and isinstance(tags[i].label(), Lemma) \
                    and tags[i].label().synset().pos() == 'n':
                tag_sentence.append(Element(tags[i][0], tags[i].label().synset().name()))
        if len(tag_sentence) > 0:
            selected_sentences.append(sentence)
            selected_tag_sentences.append(tag_sentence)
    return selected_sentences, selected_tag_sentences


def get_random_word(tagged_line):
    i = len(tagged_line) - 1
    random_num = random.randint(0, i)
    return tagged_line[random_num]
