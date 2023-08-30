from nltk.corpus import wordnet
from nltk.tokenize import word_tokenize
from def_overlap_funcs import filter_words


# Questo metodo restituisce una lista di sinonimi del termine specificato che sono sostantivi
# Utilizza la libreria wordnet per ottenere i sinonimi
def get_synset_genus(term):
    syn_list = wordnet.synsets(term, pos=wordnet.NOUN)
    return [] if not syn_list else syn_list


# Questo metodo crea un contesto associato a un synset
# Prende in input un oggetto 'h' che rappresenta un synset
# Restituisce una lista di parole che compongono il contesto
def create_context(h):
    word_list = []
    definition_words = word_tokenize(h.definition())
    example_words = [word for example in h.examples() for word in word_tokenize(example)]
    word_list.extend(definition_words)
    word_list.extend(example_words)
    return filter_words(word_list)


# Questo metodo calcola la similarità tra il contesto di un synset e una lista di definizioni
# Prende in input una lista di definizioni 'def_list', un oggetto 'h' che rappresenta un synset e una lista 'h_list'
# contenente coppie di synset e punteggi di similarità
def compute_similarity(def_list, h, h_list):
    sim = len(set(create_context(h)) & set(def_list))
    if (h, sim) not in h_list:
        h_list.append((h, sim))


# Questo metodo restituisce una lista ordinata di parole iponimi basata su termini e definizioni specificati.
# Prende in input una lista di termini 'terms' e una lista di definizioni 'definitions'.
# Restituisce una lista di tuple contenenti la parola iponimo e il punteggio di similarità.
def get_ordered_words(terms, definitions):
    hyponyms = []

    for i in range(len(terms)):
        syn_list = get_synset_genus(terms[i][0])
        for synset in syn_list:
            if synset.max_depth() > 3:
                [compute_similarity(definitions, hyponym, hyponyms) for hyponym in synset.hyponyms()]
                # [find_words(definitions, meronym, hyponyms) for meronym in synset.part_meronyms()]
    hyponyms.sort(key=lambda x: x[1], reverse=True)
    return [(h[0].lemmas()[0].name(), h[1]) for h in hyponyms]


# Questo metodo restituisce una lista ordinata di parole iponimi basata su termini, definizioni e limite di
# profondità specificati. Prende in input una lista di termini 'terms', una lista di definizioni 'definitions' e un
# limite di profondità 'depth_limit'. Restituisce una lista di tuple contenenti la parola iponimo e il punteggio di
# similarità
def get_ordered_words_2(terms, definitions, depth_limit):
    hyponyms = []

    for i in range(len(terms)):
        syn_list = get_synset_genus(terms[i][0])
        for synset in syn_list:
            if synset.max_depth() > 3:
                [compute_similarity(definitions, hyponym, hyponyms) for hyponym in synset.hyponyms()]
    # Dopo aver provato con tutti i sensi degli iponimi del genus provo a fare i sensi degli iponimi
    for i in range(0, depth_limit):
        half = hyponyms[0][1]/2
        for h in hyponyms:
            [compute_similarity(definitions, hyp, hyponyms) for hyp in h[0].hyponyms() if h[1] > half]

    hyponyms.sort(key=lambda x: x[1], reverse=True)
    return [(h[0].lemmas()[0].name(), h[1]) for h in hyponyms]
