import nltk
import spacy
from nltk.corpus import brown
from nltk.wsd import lesk

parser = spacy.load('en_core_web_sm')
C = ['i', 'you', 'she', 'he', 'we', 'they', 'me', 'yours', 'her', 'him', 'us', 'them', 'who',
     'himself', 'herself', 'ourself', 'yourself', 'myself', 'themselves']
W = ['what', 'where', 'when', 'why', 'which', 'who', 'it', 'itself', 'that']


# Questo metodo estrae frasi contenenti il verbo specificato.
# Prende in input il verbo da cercare 'verb' e il numero massimo di frasi da estrarre 'num_sentences'.
# Restituisce una lista di frasi contenenti il verbo, limitata al numero specificato.
def extract_sentences_with_verb(verb, num_sentences):
    def _is_verb(v, w):
        return w.lower() == v[0] or w.lower() == v[1] or w.lower() == v[2] or w.lower() == v[3] or w.lower() == v[4]

    verb_sentences = []
    verb_pos_tags = {'VB', 'VBD', 'VBG', 'VBN', 'VBP', 'VBZ'}  # PoS tag per i verbi a diversi tempi
    for sentence in brown.sents():
        pos_tags = nltk.pos_tag(sentence)
        if any(tag in verb_pos_tags for (_, tag) in pos_tags):
            for word, pos in pos_tags:
                if (_is_verb(verb, word)) and pos in verb_pos_tags:
                    verb_sentences.append(' '.join(sentence))

        if len(verb_sentences) >= num_sentences:
            break
    return verb_sentences[:num_sentences]


# Questo metodo estrae le coppie soggetto-oggetto da frasi contenenti il verbo specificato.
# Prende in input il verbo da cercare 'verb' e una lista di frasi 'sentences'.
# Restituisce una lista di triple contenenti il supersense del soggetto, il supersense dell'oggetto e la frase stessa.
def extract_subject_object_pairs(verb, sentences):
    def _find_supersense(word, context):
        if word.lower() in C:
            return 'noun.person'
        if word.lower() in W:
            return 'noun.communication'
        else:
            _s = lesk(context, word, 'n')
            return _s.lexname() if _s is not None else _s

    subject_object_pairs = []
    for i in range(0, 3):
        for s in sentences:
            doc = parser(s)

            for token in doc:
                if token.lemma_ == verb[0]:
                    subj = None
                    obj = None
                    # Prima tutte le frasi attive dirette
                    for child in token.children:
                        if child.dep_ == 'nsubj' or child.dep_ == 'nsubjpass':
                            subj = child.text
                        elif child.dep_ == 'dobj':
                            obj = child.text
                    # Frasi attive non dirette
                    if subj is None:
                        for child in token.head.children:
                            if child.dep_ == 'nsubj':
                                subj = child.text
                    if obj is None:
                        for child in token.children:
                            if child.dep_ == 'prep':
                                for c in child.children:
                                    if c.dep_ == 'pobj':
                                        obj = c.text
                                        break
                            elif child.dep_ == 'ccomp':
                                for c in child.children:
                                    if c.dep_ == 'expl':
                                        obj = c.text
                                        break

                    if subj and obj:
                        subject_object_pairs.append((_find_supersense(subj, s), _find_supersense(obj, s), s))
                        sentences.remove(s)

    return [c for c in subject_object_pairs if c[0] is not None and c[1] is not None], sentences


# Questo metodo crea cluster semantici a partire dalle coppie soggetto-oggetto.
# Prende in input una lista di triple contenenti il soggetto, l'oggetto e la frase.
# Restituisce un dizionario di cluster semantici dove la chiave è la coppia soggetto-oggetto
# e il valore è una lista contenente il conteggio delle istanze e le frasi associate al cluster.
def create_semantic_clusters(pairs):
    semantic_clusters = {}

    for subj, obj, sentence in pairs:
        if (subj, obj) in semantic_clusters:
            semantic_clusters[(subj, obj)][0] += 1
            semantic_clusters[(subj, obj)][1].append(sentence)
        else:
            semantic_clusters[(subj, obj)] = [1, [sentence]]

    return semantic_clusters


# Questo metodo analizza i cluster semantici e calcola le frequenze relative.
# Prende in input un dizionario di cluster semantici.
# Restituisce un dizionario di cluster semantici con le frequenze relative calcolate.
def analyze_semantic_clusters(clusters):
    total_instances = sum(v[0] for v in clusters.values())
    cluster_frequencies = {}

    for cluster, count in clusters.items():
        frequency = round(count[0] / total_instances, 6)
        cluster_frequencies[cluster] = frequency

    return cluster_frequencies
