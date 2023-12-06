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


def get_random_sentences(num_sentences=50):
    selected_sentences = []
    selected_tag_sentences = []
    max_num = len(semcor.fileids()) - 1
    while len(selected_sentences) < num_sentences:
        name_file_id = exclude_verbs_file(max_num)
        num_sents_file = len(semcor.sents(name_file_id)) - 1
        tag_sentence = []
        s_id = random.randint(0, num_sents_file)

        sentence = " ".join(semcor.sents(name_file_id)[s_id])
        tags = semcor.tagged_sents(name_file_id, tag="sem")[s_id]
        for i in range(len(tags)):
            if isinstance(tags[i], Tree) and isinstance(tags[i][0], str) and isinstance(tags[i].label(), Lemma) and tags[i].label().synset().pos() == 'n':
                tag_sentence.append(Element(tags[i][0], tags[i].label().synset().name()))

        if len(tag_sentence) > 0:
            selected_sentences.append(sentence)
            selected_tag_sentences.append(tag_sentence)

    return selected_sentences, selected_tag_sentences


def exclude_verbs_file(max_num):
    name_file_id = 'brownv'
    while 'brownv' in name_file_id:  # Escludo i file che annotano solo verbi
        num_file_id = random.randint(0, max_num)
        name_file_id = semcor.fileids()[num_file_id]
    return name_file_id


def get_random_word(tagged_line):
    i = len(tagged_line) - 1
    random_num = random.randint(0, i)
    return tagged_line[random_num], (tagged_line[random_num + 1] if random_num < i else '')
