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
    return preparation(sense.definition() + ' ' + ' '.join(sense.examples()))


def create_context_frame(frame):
    return preparation(frame.definition + ' ' + ' '.join([frame.FE[e].definition for e in frame.FE.keys()]))


def score(context_sense, context_frame):
    return len([value for value in context_sense if value in context_frame]) + 1


def common(context_frame, frame_name):
    best_sense = None
    max_score = 0
    for s in wn.synsets(frame_name):
        context_sense = create_signature_sense(s)
        temp_score = score(context_sense, context_frame)
        if temp_score > max_score:
            max_score = temp_score
            best_sense = s
    return best_sense, max_score


def mapping_framename(frame_name, context_frame):
    best_sense, max_score = common(frame_name, context_frame)
    print('\n____ Frame disambiguato ____')
    print(best_sense, ' ', max_score)


def mapping_fe(frame, context_frame):
    print('\n____ FE disambiguati ____')
    for e in frame.FE.keys():
        best_sense, max_score = common(e, context_frame)
        print(e, ' ', best_sense, ' ', max_score)


def mapping(frame):
    context_frame = create_context_frame(frame)
    frame_name = frame.name.split("_")[0] if "_" in frame.name else frame.name
    mapping_framename(frame_name, context_frame)
    mapping_fe(frame, context_frame)
