import Readcsv as rd
import numpy as np
from SimilarityFunctions import similarity_function as sf
from CorrelationCoeff import pearson_coeff, spearman_coeff
from scipy.stats import pearsonr, spearmanr

# Read the file
right_values, left_values, human_sim = rd.read_wordsimcsv()

# Calculate similarities WP, LC, SP
result_wp = sf('WP', right_values, left_values)
result_lc = sf('LC', right_values, left_values)
result_sp = sf('SP', right_values, left_values)

# Calculate coefficients
pc_wp = pearson_coeff(np.array(result_wp), human_sim)
print('Pearson coeff for WP: ', pc_wp)
print('Pearson coeff LIB for WP: ', pearsonr(np.array(result_wp), human_sim))
pc_lc = pearson_coeff(np.array(result_lc), human_sim)
print('Pearson coeff for LC: ', pc_lc)
print('Pearson coeff LIB for LC: ', pearsonr(np.array(result_lc), human_sim))
pc_sp = pearson_coeff(np.array(result_sp), human_sim)
print('Pearson coeff for SP: ', pc_sp)
print('Pearson coeff LIB for SP: ', pearsonr(np.array(result_sp), human_sim), '\n\n')

sc_wp = spearman_coeff(np.array(result_wp), human_sim)
print('Spearman coeff for WP: ', sc_wp)
print('Spearman coeff LIB for WP: ', spearmanr(np.array(result_wp), human_sim))
sc_lc = spearman_coeff(np.array(result_lc), human_sim)
print('Spearman coeff for LC: ', sc_lc)
print('Spearman coeff LIB for LC: ', spearmanr(np.array(result_lc), human_sim))
sc_sp = spearman_coeff(np.array(result_sp), human_sim)
print('Spearman coeff for SP: ', sc_sp)
print('Spearman coeff LIB for SP: ', spearmanr(np.array(result_sp), human_sim))

