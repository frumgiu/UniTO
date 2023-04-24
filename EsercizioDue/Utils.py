import random
from nltk import Tree
from nltk.corpus.reader.wordnet import Lemma
from nltk.corpus import semcor


def get_random_sentences(num_sentences=50):
    selected_ids = random.sample(list(range(0, len(semcor.fileids()) - 1)), num_sentences)
    selected_sentences = []
    tag_selected_sentences = []
    for sentence_id in selected_ids:
        sentence = " ".join(semcor.sents()[sentence_id])
        tags = semcor.tagged_sents(tag="sem")[sentence_id]
        sentence_with_tags = []
        for i in range(len(tags)):
            if isinstance(tags[i], Tree) and isinstance(tags[i][0], str) and isinstance(tags[i].label(), Lemma):
                sentence_with_tags.append((tags[i][0], tags[i].label().synset()))
        selected_sentences.append(sentence)
        tag_selected_sentences.append(sentence_with_tags)
    return selected_sentences, tag_selected_sentences
