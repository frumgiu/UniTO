from LeskAlgorithm import my_lesk, lesk_compose_word
from UtilsDue import get_random_sentences, get_random_word

sentences, sentences_tag = get_random_sentences()

tot = 0
correct = 0
for i in range(len(sentences)):
    tot += 1
    word, next_word = get_random_word(sentences_tag[i])
    if '-' in word.word:
        r = lesk_compose_word(word.word, next_word.word, sentences[i])
        print(word.word, r)
    else:
        r, o = my_lesk(word.word, sentences[i])
    if r is not None and r.name() == word.tag:
        correct += 1

print(correct/tot)
