from nltk.corpus import wordnet
from nltk.tokenize import word_tokenize
from def_overlap_funcs import filter_words


def get_genus(term):
    synsets = wordnet.synsets(term, pos=wordnet.NOUN)
    if not synsets:
        return []
    return synsets


def create_context(h):
    word_list = []
    definition_words = word_tokenize(h.definition())
    example_words = [word for example in h.examples() for word in word_tokenize(example)]
    word_list.extend(definition_words)
    word_list.extend(example_words)
    return filter_words(word_list)


def get_ordered_hyponyms(term, definitions):
    synsets = get_genus(term)
    hyponyms = []

    for synset in synsets:
        # print(synset, synset.max_depth())
        if synset.max_depth() > 6:              # Che valore usare
            for hyponym in synset.hyponyms():
                word_list = create_context(hyponym)
                similarity = len(set(word_list) & set(definitions))
                hyponyms.append((hyponym, similarity))

    hyponyms.sort(key=lambda x: x[1], reverse=True)
    ordered_hyponyms = [hyponym[0] for hyponym in hyponyms]

    return ordered_hyponyms
