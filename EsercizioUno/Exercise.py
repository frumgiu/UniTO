import Readcsv as rd
import numpy as np
from SimilarityFunctions import similarity_function as sf
from CorrelationCoeff import pearson_coeff, spearman_coeff

# Read the file
right_values, left_values, human_sim = rd.read_wordsimcsv()

# Calculate similarities WP, LC, SP
result_wp = sf('WP', right_values, left_values)
result_lc = sf('LC', right_values, left_values)
result_sp = sf('SP', right_values, left_values)

# Calculate coefficients
pc_wp = pearson_coeff(np.array(result_wp), human_sim)
print('Pearson coeff for WP: ', pc_wp)
pc_lc = pearson_coeff(np.array(result_lc), human_sim)
print('Pearson coeff for LC: ', pc_lc)
pc_sp = pearson_coeff(np.array(result_sp), human_sim)
print('Pearson coeff for SP: ', pc_sp)
