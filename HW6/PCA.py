import csv
import numpy as np
from numpy import linalg as LA
import math
import matplotlib.pyplot as plt

def calcMags (matrix):
    mags = []

    for row in matrix:
        total = 0

        for i in row:
            total = total + math.pow(float(i), 2)
        mags.append(math.sqrt(total))

    return mags

def main():
    # matrix = np.loadtxt(open("cardata.csv", "rb"), delimiter=",", skiprows=1, usecols = (2,3,4,5,6,7,8,9,10,11,12))
    # tMatrix = matrix.transpose()
    #
    # # mean mu
    # mu = []
    # for vec in tMatrix:
    #     mu.append(np.mean(vec))
    #
    # # standard deviation
    # sd = []
    # for vec in tMatrix:
    #     sd.append(np.std(vec, ddof=1))
    #
    # # centreting
    # i = 0
    # for row in tMatrix:
    #     for j in range(len(row)):
    #         tMatrix[i][j] = (tMatrix[i][j] - mu[i]) / sd[i]
    #     i = i + 1
    #
    # matrix = tMatrix.transpose()
    # coMatrix = np.cov(matrix.transpose())
    #
    # w, v = LA.eig(coMatrix)
    # map = {}
    # i = 0
    # for a in w:
    #     map[a] = v[:, i]
    #     i = i + 1
    #
    # w = np.sort(w)
    # a = np.dot(matrix[0], map[w[-1]])
    # b = np.dot(matrix[1], map[w[-1]])
    # print(a)
    # print(b)

    matrix = np.loadtxt(open("cardata.csv", "rb"), delimiter=",", skiprows=1, usecols = (2,3,4,5,6,7,8,9,10,11,12))
    f = open("cardata.csv","r")

    index = []
    category = []
    i = 0
    for row in f:
        token = row.split(",")
        if token[1] != "minivan" and token[1] != "sedan" and token[1] != "suv":
            index.append(i)

        elif token[1] == "minivan":
            category.append(1)
        elif token[1] == "sedan":
            category.append(2)
        elif token[1] == "suv":
            category.append(3)
        i = i + 1

    tMatrix = matrix.transpose()

    # mean mu
    mu = []
    for vec in tMatrix:
        mu.append(np.mean(vec))

    # standard deviation
    sd = []
    for vec in tMatrix:
        sd.append(np.std(vec, ddof=1))

    # centreting
    i = 0
    for row in tMatrix:
        for j in range(len(row)):
            tMatrix[i][j] = (tMatrix[i][j] - mu[i]) / sd[i]
        i = i + 1



    matrix = tMatrix.transpose()
    # matrix = np.delete(matrix, index, axis=0)
    coMatrix = np.cov(matrix.transpose())

    w, v = LA.eig(coMatrix)
    map = {}
    i = 0
    for a in w:
        map[a] = v[:, i]
        i = i + 1

    w = np.sort(w)
    # proj_matrix = np.hstack((map[w[-1]].reshape(11,1), map[w[-2]].reshape(11,1)))
    #
    # subspace = matrix.dot(proj_matrix)
    # print(v)
    #
    # i = 0
    # for point in subspace:
    #     if category[i] == 1:  # minivan
    #         plt.scatter(point[0], point[1], color = "r")
    #     if category[i] == 2:  # sedan
    #         plt.scatter(point[0], point[1], color = "g")
    #     if category[i] == 3:  # suv
    #         plt.scatter(point[0], point[1], color = "b")
    #     i = i + 1
    # plt.show()


if __name__ == "__main__":
    main();
