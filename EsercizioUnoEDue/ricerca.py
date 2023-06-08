from nltk.corpus import wordnet
from nltk.tokenize import word_tokenize
from def_overlap_funcs import filter_words


def get_genus(term):
    syn_list = wordnet.synsets(term, pos=wordnet.NOUN)
    return [] if not syn_list else syn_list


def create_context(h):
    word_list = []
    definition_words = word_tokenize(h.definition())
    example_words = [word for example in h.examples() for word in word_tokenize(example)]
    word_list.extend(definition_words)
    word_list.extend(example_words)
    return filter_words(word_list)


def get_ordered_words(terms, definitions):
    def _find_similarity(def_list, h, h_list):
        similarity = len(set(create_context(h)) & set(def_list))
        if (h.lemmas()[0].name(), similarity) not in h_list:
            h_list.append((h.lemmas()[0].name(), similarity))

    hyponyms = []
    for i in range(len(terms)):
        syn_list = get_genus(terms[i][0])

        for synset in syn_list:
            if synset.max_depth() > 6:
                for hyponym in synset.hyponyms():
                    _find_similarity(definitions, hyponym, hyponyms)
                for meronym in synset.part_meronyms():
                    _find_similarity(definitions, meronym, hyponyms)

    hyponyms.sort(key=lambda x: x[1], reverse=True)
    return hyponyms

