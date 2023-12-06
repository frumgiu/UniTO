from nltk.corpus import stopwords


def _remove_stop_words(sentence):
    return sentence.difference(stopwords.words('english'))


def _clean_tokens(sentence):
    return set([word.lower() for word in sentence if word.isalpha()])


def filter_words(sentence):
    return _remove_stop_words(_clean_tokens(sentence))


def similarity_score(overlap, len1, len2):
    return overlap / min([len1, len2])
