import string
from nltk.corpus import stopwords
from nltk.corpus import wordnet as wn
from UtilsDue import get_random_word

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
    best_sense = wn.synsets(word)[0] if len(wn.synsets(word)) > 0 else None
    max_overlap = 0
    context = preparation(sentence)
    for sense in wn.synsets(word):
        signature = create_signature(sense)
        overlap = compute_overlap(signature, context)
        if overlap > max_overlap:
            max_overlap = overlap
            best_sense = sense
    return best_sense, max_overlap


def lesk_compose_word(w, wne, se):
    a1, b1 = my_lesk(w, se)
    a2, b2 = my_lesk(w.replace('-', ''), se)
    a3, b3 = my_lesk(w.replace('-', ' '), se)
    a6, b6 = my_lesk(w.replace('-', '_'), se)
    a4, b4 = my_lesk(w + wne, se)
    a5, b5 = my_lesk(w + ' ' + wne, se)
    a8, b8 = my_lesk(w + '_' + wne, se)
    if a1 is not None and b1 >= b2 and b1 >= b3 and b1 >= b6 and b1 >= b4 and b1 >= b5 and b1 >= b8:
        return a1
    elif a2 is not None and b2 >= b1 and b2 >= b3 and b2 >= b6 and b2 >= b4 and b2 >= b5 and b2 >= b8:
        return a2
    elif a3 is not None and b3 >= b1 and b3 >= b2 and b3 >= b6 and b3 >= b4 and b3 >= b5 and b3 >= b8:
        return a3
    elif a6 is not None and b6 >= b1 and b6 >= b2 and b6 >= b3 and b6 >= b4 and b6 >= b5 and b6 >= b8:
        return a6
    elif a4 is not None and b4 >= b1 and b4 >= b2 and b4 >= b3 and b4 >= b6 and b4 >= b5 and b4 >= b8:
        return a4
    elif a5 is not None and b5 >= b1 and b5 >= b2 and b5 >= b3 and b5 >= b6 and b5 >= b4 and b5 >= b8:
        return a5
    elif a8 is not None and b8 >= b1 and b8 >= b2 and b8 >= b3 and b8 >= b6 and b8 >= b4 and b8 >= b5:
        return a8
    else:
        return None


def run_exercise(num_test, sentences, sentences_tag):
    final_tot = 0
    final_correct = 0
    for j in range(0, num_test - 1):
        tot = 0
        correct = 0
        for i in range(len(sentences)):
            tot += 1
            final_tot += 1
            word, next_word = get_random_word(sentences_tag[i])
            if '-' in word.word:
                r = lesk_compose_word(word.word, next_word.word, sentences[i])
            else:
                r, o = my_lesk(word.word, sentences[i])
            if r is not None and r.name() == word.tag:
                correct += 1
                final_correct += 1

    return round(final_correct/final_tot, 2)
