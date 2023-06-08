from nltk.corpus import wordnet
from nltk.tokenize import word_tokenize
from def_overlap_funcs import filter_words


def get_synset_genus(term):
    syn_list = wordnet.synsets(term, pos=wordnet.NOUN)
    return [] if not syn_list else syn_list


def create_context(h):
    word_list = []
    definition_words = word_tokenize(h.definition())
    example_words = [word for example in h.examples() for word in word_tokenize(example)]
    word_list.extend(definition_words)
    word_list.extend(example_words)
    return filter_words(word_list)


def compute_similarity(def_list, h, h_list):
    sim = len(set(create_context(h)) & set(def_list))
    if (h, sim) not in h_list:
        h_list.append((h, sim))


def get_ordered_words(terms, definitions):
    hyponyms = []

    for i in range(len(terms)):
        syn_list = get_synset_genus(terms[i][0])
        for synset in syn_list:
            if synset.max_depth() > 6:
                [compute_similarity(definitions, hyponym, hyponyms) for hyponym in synset.hyponyms()]
                # [find_words(definitions, meronym, hyponyms) for meronym in synset.part_meronyms()]
    hyponyms.sort(key=lambda x: x[1], reverse=True)
    return [(h[0].lemmas()[0].name(), h[1]) for h in hyponyms]


def get_ordered_words_2(terms, definitions, depth_limit):
    hyponyms = []

    for i in range(len(terms)):
        syn_list = get_synset_genus(terms[i][0])
        for synset in syn_list:
            if synset.max_depth() > 6:
                [compute_similarity(definitions, hyponym, hyponyms) for hyponym in synset.hyponyms()]
    # Dopo aver provato con tutti i sensi degli iponimi del genus provo a fare i sensi degli iponimi
    for i in range(0, depth_limit):
        half = hyponyms[0][1]/2
        for h in hyponyms:
            [compute_similarity(definitions, hyp, hyponyms) for hyp in h[0].hyponyms() if h[1] > half]

    hyponyms.sort(key=lambda x: x[1], reverse=True)
    return [(h[0].lemmas()[0].name(), h[1]) for h in hyponyms]
