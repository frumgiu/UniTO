import csv
import numpy as np


def read_wordsimcsv():
    right_value = []
    left_value = []
    human_similarity = []

    with open('combined.csv', mode='r') as file:
        csv_reader = csv.reader(file)
        next(csv_reader)
        for riga in csv_reader:
            right_value.append(riga[0])
            left_value.append(riga[1])
            human_similarity.append(float(riga[2]))
    return np.array(right_value), np.array(left_value), np.array(human_similarity)
