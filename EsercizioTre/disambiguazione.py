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


def create_signature_sense(sense):
    result = sense.definition()
    for e in sense.examples():
        result += ' '
        result += e
    return preparation(result)


def create_context_frame(frame):
    result = frame.definition
    for e in frame.FE.keys():
        result += ' '
        result += frame.FE[e].definition
    return preparation(result)


def score(sense, frame):
    context_sense = create_signature_sense(sense)
    context_frame = create_context_frame(frame)
    result = [value for value in context_sense if value in context_frame]
    return len(result) + 1


def mapping_framename(frame):
    best_sense = None
    max_score = 0
    primo_termine = frame.name.split("_")[0] if "_" in frame.name else frame.name
    for s in wn.synsets(primo_termine):
        temp_score = score(s, frame)
        if temp_score > max_score:
            max_score = temp_score
            best_sense = s
    return best_sense, max_score
