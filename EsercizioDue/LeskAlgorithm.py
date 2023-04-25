import string
from nltk.corpus import stopwords
from nltk.corpus import wordnet as wn

STOPWORDS = set(stopwords.words('english'))
PUNCTUATION = string.punctuation.replace('-', '')


def preparation(sentence):
    # sentence = sentence.lower()
    for p in PUNCTUATION:
        sentence = sentence.replace(p, ' ')
    sentence = sentence.split()
    sentence = [s for s in sentence if s.lower() not in STOPWORDS]
    return sentence


def create_signature(sense):
    result = sense.definition()
    for e in sense.examples():
        result += ' '
        result += e
    return preparation(result)


def compute_overlap(signature, context):
    shared = [value for value in signature if value in context]
    return len(shared)


def my_lesk(word, sentence):
    best_sense = None
    max_overlap = 0
    # context = preparation(sentence)
    for sense in wn.synsets(word):
        signature = create_signature(sense)
        overlap = compute_overlap(signature, sentence)
        for h in sense.hyponyms():
            signature_h = create_signature(h)
            overlap += compute_overlap(signature_h, sentence)
        if overlap > max_overlap:
            max_overlap = overlap
            best_sense = sense
    return best_sense


def my_corpus_lesk(word, sentence):
    best_sense = None
    max_overlap = 0
    # context = preparation(sentence)
    for sense in wn.synsets(word):
        signature = create_signature(sense)
        overlap = compute_overlap(signature, sentence)
        for h in sense.hyponyms():
            signature_h = create_signature(h)
            overlap += compute_overlap(signature_h, sentence)
        if overlap > max_overlap:
            max_overlap = overlap
            best_sense = sense
    return best_sense

def lesk_wrapper(sentences):
    result = []
    words = []
    for sentence in sentences:
        sentence = preparation(sentence)
        for word in sentence:
            temp = sentence.copy()
            # temp.remove(word)
            result.append(my_lesk(word, temp))
            words.append(word)
    return [words, result]
