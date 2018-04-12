import numpy as np
import time
from scipy import misc
import matplotlib.pyplot as plt

def openImage(path, gray=False):
	return misc.imread(path, gray)

def saveImage(image, path):
	misc.imsave(path, image) # uses the Image module (PIL)

def imageIterator(image):
	for i in range(len(image)):
		for j in range(len(image[i])):
			try:
				max_k = len(image[i][j])
				for k in range(max_k):
					yield((i, j, k), image[i][j][k])
			except:
				yield ((i, j), image[i][j])

def grayscaleTransformation(image):
	try:
		len(image[0][0])
		iterator = imageIterator(image)
		while iterator:
			try:
				pos, R = next(iterator)
				pos, G = next(iterator)
				pos, B = next(iterator)
				pos = pos[:2]
				value = 0.21*R + 0.72*G + 0.07*B
				image[pos] = value
			except StopIteration:
				break
		return image
	except:
		return image

def inverseOperator(image):
	iterator = imageIterator(image)
	while iterator:
		try:
			pos, value = next(iterator)
			image[pos] = abs(value - 255)
		except StopIteration:
			break
	return image

def binaryThresholdInterval(image, p1, p2):
	iterator = imageIterator(image)
	while iterator:
		try:
			pos, value = next(iterator)
			if value > p1 and value < p2:
				image[pos] = 255
			else:
				image[pos] = 0
		except StopIteration:
			break
	return image

def tresholdOperator(image, p1):
	return binaryThresholdInterval(image, p1, 255)

def invertedTresholdOperator(image, p1):
	return binaryThresholdInterval(image, 0, p1)

def showImage(image):
	plt.imshow(face)
	plt.show()

if __name__ == "__main__":
	face = openImage("face.png")