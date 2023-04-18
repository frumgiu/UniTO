from nltk.corpus import wordnet as wn


def max_wup_similarity(word1, word2):
    max_similarity = 0
    for synset1 in wn.synsets(word1):
        for synset2 in wn.synsets(word2):
            # print('Senso 1 ', synset1, ' senso 2', synset2)
            # print(synset1.lowest_common_hypernyms(synset2)[0].max_depth())
            similarity = wn.wup_similarity(synset1, synset2)  # calcola la similarità di Wu e Palmer
            if similarity > max_similarity:  # aggiorna il massimo se la similarità è
                # maggiore
                max_similarity = similarity
    return max_similarity


w1 = 'girl'
w2 = 'cat'
print("Mia similarity: ", max_wup_similarity(w1, w2))
