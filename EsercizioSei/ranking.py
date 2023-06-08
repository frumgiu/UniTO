from nltk.corpus import wordnet as wn


def get_lemmas_len(lista):
    sense_list = [wn.synsets(l)[0] for l in lista]
    lemma_list = [len(t.lemmas()[0].name()) for t in sense_list]
    return lemma_list


def get_hyponyms(lista):
    result = []
    for i in range(len(lista)):
        syns = wn.synsets(lista[i])
        iponimi = [h.name() for s in syns for h in s.hyponyms()]
        result.append(len(iponimi))
    return result


def get_frequencies(lista):
    sense_list = [wn.synsets(l)[0] for l in lista]
    lemma_list = [t.lemmas()[0].count() for t in sense_list]
    return lemma_list
