import numpy as np


def pearson_coeff(x, y):
    covariance = np.mean(x * y) - np.mean(x) * np.mean(y)  # covariance = (np.sum(x*y) / 4) - np.mean(x)*np.mean(y)
    return round((covariance / (np.std(x) * np.std(y))), 4)


def spearman_coeff(x, y):
    x_ranks = np.argsort(np.argsort(x))
    y_ranks = np.argsort(np.argsort(y))
    return round((pearson_coeff(x_ranks, y_ranks)), 4)
