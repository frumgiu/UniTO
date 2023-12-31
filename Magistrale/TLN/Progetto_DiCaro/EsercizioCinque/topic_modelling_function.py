import csv
from nltk.tokenize import RegexpTokenizer
from nltk.stem.wordnet import WordNetLemmatizer
from gensim.models import Phrases
from gensim.models import LdaModel


# Questo metodo estrae i documenti da un file CSV.
# Prende in input il percorso del file CSV 'file_path' e il nome della colonna 'col_name' contenente i documenti.
# Restituisce una lista contenente i documenti estratti.
def extract_documents_csv(file_path="spacenews-december-2022.csv", col_name='content'):
    rows = []
    with open(file_path, 'r') as csvfile:
        reader = csv.DictReader(csvfile)
        for row in reader:
            rows.append(row[col_name])
    return rows


# Questo metodo effettua il pre-processing dei documenti.
# Prende in input una lista di documenti 'docs'.
# Restituisce i documenti pre-processati.
def pre_processing(docs):
    lemmatizer = WordNetLemmatizer()
    tokenizer = RegexpTokenizer(r'\w+')
    for idx in range(len(docs)):
        docs[idx] = docs[idx].lower()  # Convert to lowercase.
        docs[idx] = tokenizer.tokenize(docs[idx])  # Split into words.
    # Remove numbers, but not words that contain numbers.
    docs = [[token for token in doc if not token.isnumeric()] for doc in docs]
    # Remove words that are only one character.
    docs = [[token for token in doc if len(token) > 1] for doc in docs]
    # Lemmatizzazione
    docs = [[lemmatizer.lemmatize(token) for token in doc] for doc in docs]
    return docs


# Questo metodo trova i bigrammi all'interno dei documenti.
# Prende in input i documenti pre-processati 'docs'.
# Restituisce i documenti con i bigrammi aggiunti.
def find_bigrams(docs):
    # Add bigrams and trigrams to docs (only ones that appear 20 times or more).
    bigram = Phrases(docs, min_count=20)
    for idx in range(len(docs)):
        for token in bigram[docs[idx]]:
            if '_' in token:
                docs[idx].append(token)
    return docs


def training(corpus, dictionary, num_topics=10, chunksize=200, passes=20, iterations=400, eval_every=None):
    # Make an index to word dictionary.
    temp = dictionary[0]  # This is only to "load" the dictionary.
    id2word = dictionary.id2token

    model = LdaModel(
        corpus=corpus,
        id2word=id2word,
        chunksize=chunksize,
        alpha='auto',
        eta='auto',
        iterations=iterations,
        num_topics=num_topics,
        passes=passes,
        eval_every=eval_every
    )

    top_topics = model.top_topics(corpus)

    # Average topic coherence is the sum of topic coherences of all topics, divided by the number of topics.
    avg_topic_coherence = sum([t[1] for t in top_topics]) / num_topics
    print('Average topic coherence: %.4f.' % avg_topic_coherence)
    return top_topics, avg_topic_coherence
