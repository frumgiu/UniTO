import string
from nltk.corpus import stopwords
from nltk.corpus import wordnet as wn

STOPWORDS = set(stopwords.words('english'))
PUNCTUATION = string.punctuation


def preparation(sentence):
    sentence = sentence.lower()
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
    context = preparation(sentence)
    for sense in wn.synsets(word):
        signature = create_signature(sense)
        overlap = compute_overlap(signature, context)
        if overlap > max_overlap:
            max_overlap = overlap
            best_sense = sense
    return best_sense, max_overlap


def lesk_wrapper(sentences):
    result = []
    words = []
    for sentence in sentences:
        sentence = preparation(sentence)
        for word in sentence:
            temp = sentence.copy()
            result.append(my_lesk(word, temp))
            words.append(word)
    return [words, result]


def run_test(w, wne, se):
    a1, b1 = my_lesk(w, se)
    a2, b2 = my_lesk(w.replace('-', ''), se)
    a3, b3 = my_lesk(w.replace('-', ' '), se)
    a4, b4 = my_lesk(w + wne, se)
    a5, b5 = my_lesk(w + ' ' + wne, se)
    a8, b8 = my_lesk(w + '_' + wne, se)
    if a1 is not None and b1 >= b2 and b1 >= b3 and b1 >= b4 and b1 >= b5 and b1 >= b8:
        return a1
    elif a2 is not None and b2 >= b1 and b2 >= b3 and b2 >= b4 and b2 >= b5 and b2 >= b8:
        return a2
    elif a3 is not None and b3 >= b1 and b3 >= b2 and b3 >= b4 and b3 >= b5 and b3 >= b8:
        return a3
    elif a4 is not None and b4 >= b1 and b4 >= b2 and b4 >= b3 and b4 >= b5 and b4 >= b8:
        return a4
    elif a5 is not None and b5 >= b1 and b5 >= b2 and b5 >= b3 and b5 >= b4 and b5 >= b8:
        return a5
    elif a8 is not None and b8 >= b1 and b8 >= b2 and b8 >= b3 and b8 >= b4 and b8 >= b5:
        return a8
    else:
        return None
