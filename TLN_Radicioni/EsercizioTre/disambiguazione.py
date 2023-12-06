import string
from nltk.corpus import stopwords
from nltk.corpus import wordnet as wn
from nltk.corpus import framenet as fn
from annotazioneTarget import bail_target, partitive_target, receiving_target, speed_target, mining_target

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


def score(context_sense, context_frame):
    return len([value for value in context_sense if value in context_frame]) + 1


def mapping_framename(frame, result):
    context_frame = preparation(frame.definition + ' ' + ' '.join([frame.FE[e].definition for e in frame.FE.keys()]))
    frame_name = frame.name.split("_")[0] if "_" in frame.name else frame.name
    best_sense = None
    max_score = 0
    for s in wn.synsets(frame_name):
        context_sense = create_context_sense(s)
        temp_score = score(context_sense, context_frame)
        if temp_score > max_score:
            max_score = temp_score
            best_sense = s
    result[frame.name] = best_sense.name() if best_sense is not None else None


def mapping_fe(frame, result):
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
        result[e] = best_sense.name() if best_sense is not None else None


def mapping_lu(frame, result):
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
        result[e] = best_sense.name() if best_sense is not None else None


def mapping_frame(frame):
    result = {}
    mapping_framename(frame, result)
    mapping_fe(frame, result)
    mapping_lu(frame, result)
    return result


def mapping():
    result_one = mapping_frame(fn.frame(1010)).items() & partitive_target.items()
    result_two = mapping_frame(fn.frame(966)).items() & speed_target.items()
    result_three = mapping_frame(fn.frame(2285)).items() & mining_target.items()
    result_four = mapping_frame(fn.frame(411)).items() & receiving_target.items()
    result_five = mapping_frame(fn.frame(121)).items() & bail_target.items()
    correct = len(result_one) + len(result_two) + len(result_three) + len(result_four) + len(result_five)
    total = len(partitive_target) + len(speed_target) + len(mining_target) + len(receiving_target) + len(bail_target)
    return correct/total
