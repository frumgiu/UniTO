from nltk.wsd import lesk
from nltk.tokenize import word_tokenize
from LeskAlgorithm import my_lesk


def get_semantic(seq, key_word):
    temp = word_tokenize(seq)
    temp = lesk(temp, key_word)
    return temp


keyword = 'ash'
seq1 = 'The house was burnt to ashes while the owner returned'
seq2 = 'The table is made of ash wood'

print(get_semantic(seq1, keyword))
print(get_semantic(seq2, keyword))
print('------------------------------')
result_one = my_lesk(keyword, seq1)
result_two = my_lesk(keyword, seq2)
print(result_one)
print(result_two)

