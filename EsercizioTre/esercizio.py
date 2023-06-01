import matplotlib.pyplot as plt
import nltk
import spacy
from nltk.corpus import brown
from nltk.wsd import lesk

parser = spacy.load('en_core_web_sm')
C = ['i', 'you', 'she', 'he', 'we', 'they', 'me', 'yours', 'her', 'him', 'us', 'them', 'who',
     'himself', 'herself', 'ourself', 'yourself', 'myself', 'themselves']
W = ['what', 'where', 'when', 'why', 'which', 'who', 'it', 'itself', 'that']


def extract_sentences_with_verb(verb, num_sentences):
    verb_sentences = []
    verb_pos_tags = {'VB', 'VBD', 'VBG', 'VBN', 'VBP', 'VBZ'}  # PoS tag per i verbi a diversi tempi

    for sentence in brown.sents():
        pos_tags = nltk.pos_tag(sentence)
        if any(tag in verb_pos_tags for (_, tag) in pos_tags):
            for word, pos in pos_tags:
                if (word.lower() == verb[0] or word.lower() == verb[1] or word.lower() == verb[2] or word.lower() ==
                    verb[3]) and pos in verb_pos_tags:
                    verb_sentences.append(' '.join(sentence))

        if len(verb_sentences) >= num_sentences:
            break
    return verb_sentences[:num_sentences]


def find_supersense(word, context):
    if word.lower() in C:
        return 'noun.person'
    if word.lower() in W:
        return 'noun.communication'
    else:
        s = lesk(context, word, 'n')
        if s is not None:
            return s.lexname()
        else:
            return s


def extract_subject_object_pairs(verb, sentences):
    subject_object_pairs = []
    for s in sentences:
        doc = parser(s)

        for token in doc:
            if token.lemma_ == verb[0]:
                subj = None
                obj = None

                for child in token.children:
                    if child.dep_ == 'nsubj':
                        subj = child.text
                    elif child.dep_ == 'dobj':
                        obj = child.text

                if subj and obj:
                    subject_object_pairs.append((find_supersense(subj, s), find_supersense(obj, s), s))

    return [c for c in subject_object_pairs if c[0] is not None and c[1] is not None]


def create_semantic_clusters(pairs):
    semantic_clusters = {}

    for subj, obj, sentence in pairs:
        if (subj, obj) in semantic_clusters:
            semantic_clusters[(subj, obj)][0] += 1
            semantic_clusters[(subj, obj)][1].append(sentence)
        else:
            semantic_clusters[(subj, obj)] = [1, [sentence]]

    return semantic_clusters


def analyze_semantic_clusters(clusters):
    total_instances = sum(v[0] for v in clusters.values())
    cluster_frequencies = {}

    for cluster, count in clusters.items():
        frequency = round(count[0] / total_instances, 6)
        cluster_frequencies[cluster] = frequency

    return cluster_frequencies


def create_image(cluster_frequencies):
    x_values = []
    y_values = []

    for cluster, frequency in cluster_frequencies.items():
        subj, obj = cluster
        x_values.append(f"{subj} - {obj}")
        y_values.append(frequency)

    # Creazione di una scala di colori in base alle frequenze
    min_freq = min(y_values)
    max_freq = max(y_values)
    norm = plt.Normalize(min_freq, max_freq)
    colors = plt.cm.viridis(norm(y_values))
    # Creazione del grafico a barre
    plt.figure(figsize=(22, 8))
    bars = plt.bar(x_values, y_values, color=colors)
    plt.xticks(rotation=45, ha='right')
    plt.xlabel('Cluster')
    plt.ylabel('Frequenza')
    plt.title('Frequenze dei cluster semantici')

    for i, bar in enumerate(bars):
        plt.text(bar.get_x() + bar.get_width() / 2, bar.get_height(), str(y_values[i]),
                 ha='center', va='bottom', fontsize=6)

    plt.tight_layout()
    # Visualizzazione del grafico
    plt.show()
