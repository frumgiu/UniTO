import string
from nltk.corpus import wordnet as wn
from nltk.corpus import stopwords


STOPWORDS = set(stopwords.words('english'))
PUNCTUATION = string.punctuation


def preparation(sentence):
    sentence = sentence.lower()
    for p in PUNCTUATION:
        sentence = sentence.replace(p, ' ')
    sentence = sentence.split()
    sentence = [s for s in sentence if s not in STOPWORDS]
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
    context = preparation(sentence)
    for sense in wn.synsets(word):
        signature = create_signature(sense)
        overlap = compute_overlap(signature, context)
       # for h in sense.hypernyms():
       #     signature_h = create_signature(h)
       #     overlap += compute_overlap(signature_h, context)
        if overlap > max_overlap:
            max_overlap = overlap
            best_sense = sense
    return best_sense
