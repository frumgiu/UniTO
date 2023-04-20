import numpy as np
from scipy.stats import pearsonr
import SimilarityFunctions as sf


def pearson_index(x, y):
    covariance = np.mean(x*y) - np.mean(x)*np.mean(y) # covariance = (np.sum(x*y) / 4) - np.mean(x)*np.mean(y)
    return covariance / (np.std(x) * np.std(y))


w1 = 'love'
w2 = 'sex'
w3 = 'tiger'
w4 = 'cat'
w5 = 'book'
w6 = 'paper'
result = sf.my_lc_similarity(w1, w2)
result2 = sf.my_lc_similarity(w3, w4)
result3 = sf.my_lc_similarity(w3, w3)
result4 = sf.my_lc_similarity(w5, w6)
print('Result LC similarity: ', result)
print('Result2 LC similarity: ', result2)
print('Result3 LC similarity: ', result3)
print('Result4 LC similarity: ', result4)
x_array = np.array([result, result2, result3, result4])
y_array = np.array([6.77, 7.35, 10.00, 7.46])
print('Pearson index: ', pearson_index(x_array, y_array))
print('Pearson index numpy: ', pearsonr(x_array, y_array))
