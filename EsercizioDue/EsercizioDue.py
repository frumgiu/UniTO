from nltk.wsd import lesk
from nltk.tokenize import word_tokenize
from LeskAlgorithm import my_lesk
from UtilsDue import get_random_sentences

sentences, sentences_tag = get_random_sentences()
print(sentences)
