import numpy as np
import time
from scipy import misc
import matplotlib.pyplot as plt
import math

def openImage(path, gray=False):
	return misc.imread(path, gray)

def saveImage(image, path):
	misc.imsave(path, image) # uses the Image module (PIL)

def grayscaleTransformation(image):
	it = np.nditer(image, flags=["multi_index"])
	i = 0
	RGB = [0, 0, 0]
	while not it.finished:
		RGB[i] = it[0]
		i = i + 1
		if i > 2:
			image[it.multi_index[:2]] = 0.21*RGB[0] + 0.72*RGB[1] + 0.07*RGB[2]
			i = 0
		it.iternext()
	return image

def inverseOperator(image):
	for x in np.nditer(image, op_flags=["readwrite"]): 
		x[...] = 255 - x
	return image

def binaryThresholdInterval(image, p1, p2):
	for x in np.nditer(image, op_flags=["readwrite"]): 
		x[...] = 0 if x > p1 and x < p2 else 255
	return image

def invertedBinaryTresholdOperator(image, p1, p2):
	for x in np.nditer(image, op_flags=["readwrite"]): 
		x[...] = 255 if x > p1 and x < p2 else 0
	return image

def tresholdOperator(image, p1):
	return binaryThresholdInterval(image, p1, 255)

def invertedTresholdOperator(image, p1):
	return binaryThresholdInterval(image, 0, p1)

def grayscaleTresholdOperator(image, p1, p2):
	for x in np.nditer(image, op_flags=["readwrite"]): 
		x[...] = x if x > p1 and x < p2 else 0
	return image

def invertedGrayscaleTresholdOperator(image, p1, p2):
	for x in np.nditer(image, op_flags=["readwrite"]): 
		x[...] = (255 - x) if x > p1 and x < p2 else 0
	return image

def stretchOperator(image, p1, p2):
	for x in np.nditer(image, op_flags=["readwrite"]): 
		x[...] = ((x - p1)*(255 / (p2 - p1))) if x > p1 and x < p2 else 0
	return image

def grayLevelReductionOperator(image, p, q):
	for x in np.nditer(image, op_flags=["readwrite"]):
		more = False
		for i, p_ in enumerate(p):
			if x <= p_:
				less = True
				if i == 0:
					x[...] = 0
				else: 
					x[...] = q[i-1]
				break
		if more:
			x[...] = 255
	return image

def grayLevelReductionOperatorByLevel(image, div):
	p = [(255 / div) * i for i in range(div)]
	div = div - 1
	q = [(255 / div) * i for i in range(div)]
	return grayLevelReductionOperator(image, p, q)


def imageAddition(image1, image2, scalingFactor = 2):
	it1 = np.nditer(image1, flags=["multi_index"])
	it2 = np.nditer(image2, flags=["multi_index"])
	while not it1.finished and not it2.finished:
		image1[it1.multi_index] = math.ceil( (it1[0] + it2[0]) / scalingFactor)
		it1.iternext()
		it2.iternext()
	return image1

def imageSubtraction(image1, image2):
	it1 = np.nditer(image1, flags=["multi_index"])
	it2 = np.nditer(image2, flags=["multi_index"])
	while not it1.finished and not it2.finished:
		image1[it1.multi_index] = it1[0] - it2[0]
		it1.iternext()
		it2.iternext()

	max = np.max(image1)
	min = np.min(image1)

	for x in np.nditer(image1, op_flags=["readwrite"]): 
		x[...] = (x - min) * (255 / (abs(min) + abs(max))) 
	return image1


def showImage(image):
	plt.imshow(image)
	plt.show()

if __name__ == "__main__":
	image1 = openImage("bg1.jpg")
	image2 = openImage("bg2.jpg")
	showImage(grayLevelReductionOperatorByLevel(grayscaleTransformation(image1), 3))