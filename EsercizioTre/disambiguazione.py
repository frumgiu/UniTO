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


def create_context_sense(sense):
    return preparation(sense.definition() + ' ' + ' '.join(sense.examples()))


def create_context_frame(frame):
    return preparation(frame.definition + ' ' + ' '.join([frame.FE[e].definition for e in frame.FE.keys()]))


def score(context_sense, context_frame):
    return len([value for value in context_sense if value in context_frame]) + 1


def mapping_framename(frame):
    context_frame = create_context_frame(frame)
    frame_name = frame.name.split("_")[0] if "_" in frame.name else frame.name
    best_sense = None
    max_score = 0
    for s in wn.synsets(frame_name):
        context_sense = create_context_sense(s)
        temp_score = score(context_sense, context_frame)
        if temp_score > max_score:
            max_score = temp_score
            best_sense = s
    print('\n____ Frame Name disambiguato ____')
    print(best_sense, ' ', max_score)


def mapping_fe(frame):
    print('\n____ FE disambiguati ____')
    for e in frame.FE.keys():
        context_frame = preparation(frame.FE[e].definition)
        best_sense = None
        max_score = 0
        for s in wn.synsets(e):
            context_sense = create_context_sense(s)
            temp_score = score(context_sense, context_frame)
            if temp_score > max_score:
                max_score = temp_score
                best_sense = s
        print(e, ' ', best_sense, ' ', max_score)


def mapping_lu(frame):
    print('\n____ LU disambiguati ____')
    for e in frame.lexUnit.keys():
        lu_name = e.split(".")[0]
        context_frame = preparation(frame.lexUnit[e].definition)
        best_sense = None
        max_score = 0
        for s in wn.synsets(lu_name):
            if e in s.name():
                context_sense = create_context_sense(s)
                temp_score = score(context_sense, context_frame)
                if temp_score > max_score:
                    max_score = temp_score
                    best_sense = s
        print(e, ' ', best_sense, ' ', max_score)


def mapping(frame):
    mapping_framename(frame)
    mapping_fe(frame)
    mapping_lu(frame)
