from nltk.corpus import wordnet
from def_overlap_funcs import similarity_score


def get_genus(target, r):
    synsets = wordnet.synsets(r[target])
    if not synsets:
        return []
    return synsets[0]  # Prendi il primo synset come target


def get_hyponyms(synset):
    hyponyms = synset.hyponyms()
    return hyponyms


def get_ordered_hyponyms(term, definitions):
    synsets = wordnet.synsets(term, pos=wordnet.NOUN)
    hyponyms = []

    for synset in synsets:
        for hyponym in synset.hyponyms():
            hyponym_words = hyponym.definition().split()
            similarity = len(set(hyponym_words) & set(definitions))
            hyponyms.append((hyponym, similarity))

    hyponyms.sort(key=lambda x: x[1], reverse=True)
    ordered_hyponyms = [hyponym[0] for hyponym in hyponyms]

    return ordered_hyponyms
