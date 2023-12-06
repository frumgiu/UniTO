from nltk.corpus import wordnet as wn


# Questo metodo restituisce la lunghezza dei lemmi per una lista di parole.
# Prende in input una lista di parole 'lista'.
# Restituisce una lista con la lunghezza dei lemmi corrispondenti.
def get_lemmas_len(lista):
    sense_list = [wn.synsets(l)[0] for l in lista]
    lemma_list = [len(t.lemmas()[0].name()) for t in sense_list]
    return lemma_list


# Questo metodo calcola la media della lunghezza dei lemmi per una lista di parole.
# Prende in input una lista di parole 'lista'.
# Restituisce una lista con la media della lunghezza dei lemmi corrispondenti.
def get_lemmas_len_avg(lista):
    result = []
    for el in lista:
        sense_list = wn.synsets(el)
        lemma_list = [len(t.name()) for s in sense_list for t in s.lemmas()]
        result.append(round(sum(lemma_list)/len(lemma_list), 4))
    return result


# Questo metodo restituisce il numero di iponimi per una lista di parole.
# Prende in input una lista di parole 'lista'.
# Restituisce una lista con il numero di iponimi corrispondenti per ogni parola.
def get_hyponyms(lista):
    result = []
    for i in range(len(lista)):
        syns = wn.synsets(lista[i])
        iponimi = [h.name() for s in syns for h in s.hyponyms()]
        result.append(len(iponimi))
    return result


# Questo metodo restituisce le frequenze dei lemmi per una lista di parole.
# Prende in input una lista di parole 'lista'.
# Restituisce una lista con le frequenze dei lemmi corrispondenti.
def get_frequencies(lista):
    sense_list = [wn.synsets(l)[0] for l in lista]
    lemma_list = [t.lemmas()[0].count() for t in sense_list]
    return lemma_list
