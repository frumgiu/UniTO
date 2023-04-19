import numpy as np
import SimilarityFunctions as sf


def pearson_index(x, y):
    return np.cov(x, y) / (np.std(x) * np.std(y))


w1 = 'love'
w2 = 'sex'
w3 = 'tiger'
w4 = 'cat'
result = sf.my_lc_similarity(w1, w2)
result2 = sf.max_wup_similarity(w1, w2)
print('Result LC similarity: ', result)
print('Result WP similarity: ', result2)
x_array = np.array([result, result2])
y_array = np.array([6.77])
# print('Pearson index: ', pearson_index(x_array, y_array))
# print('Pearson index numpy: ', np.corrcoef(x_array, y_array))

