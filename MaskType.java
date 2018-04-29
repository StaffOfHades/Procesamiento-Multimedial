

class MaskType {
	
	static double[][] blur(int maskSize) {
		switch(maskSize) {
			case 3:
				return new double[][]{
					{  0,1.0/5,  0},
					{1.0/5,1.0/5,1.0/5},
					{  0,1.0/5,  0}
				};
			case 5:
			default:
				return new double[][]{
					{	0,	 0,1.0/13,   0,	0},
					{	0,1.0/13,1.0/13,1.0/13,	0},
					{1.0/13,1.0/13,1.0/13,1.0/13,1.0/13},
					{	0,1.0/13,1.0/13,1.0/13,	0},
					{	0,	 0,1.0/13,   0,	0}
				};
		}
	}

	static double[][] motionBlur() {
		return new double[][]{
			{1.0/9, 0, 0, 0, 0, 0, 0, 0, 0},
			{0, 1.0/9, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 1.0/9, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 1.0/9, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 1.0/9, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 1.0/9, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 1.0/9, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 1.0/9, 0},
			{0, 0, 0, 0, 0, 0, 0, 0, 1.0/9}
		};
	}

	static double[][] spatialAveraging(int maskSize) {
		double[][] mask = new double[maskSize][maskSize];
		for(int i = 0; i < maskSize; i++) {
			for(int j = 0; j < maskSize; j++) {
				mask[i][j] = 1.0/ (maskSize * maskSize);
			}
		}
		return mask;
	}

	static double[][] spatialWeightedAveraging(int maskSize) {
		switch(maskSize) {
			case 3:
				return new double[][]{
					{0,		1.0/ 8, 0	},
					{1.0/ 8, 1.0/ 2, 1.0/ 8},
					{0,		1.0/ 8, 0	}
				};
			case 5:
			default:
				return new double[][]{
					{	  0,		 0, 1.0/ 32,	  	 0,		0},
					{	  0,	1.0/ 32, 1.0/ 16, 1.0/ 32, 	0},
					{1.0/ 32,	1.0/ 16, 1.0/ 2,	1.0/ 16, 1.0/ 32},
					{	  0,	1.0/ 32, 1.0/ 16, 1.0/ 32, 	0},
					{	  0,		 0, 1.0/ 32,		 0,		0}
				};
		}
	}

	static double[][] sharpen(int maskSize) {
		switch(maskSize) {
			case 3:
				return new double[][]{
					{-1, -1, -1},
					{-1,  9, -1},
					{-1, -1, -1}
				};
			case 5:
			default:
				return new double[][]{
					{-1.0/8,-1.0/8,-1.0/8,-1.0/8,-1.0/8},
					{-1.0/8, 1.0/4, 1.0/4, 1.0/4,-1.0/8},
					{-1.0/8, 1.0/4, 1,	   1.0/4,-1.0/8},
					{-1.0/8, 1.0/4, 1.0/4, 1.0/4,-1.0/8},
					{-1.0/8,-1.0/8,-1.0/8,-1.0/8,-1.0/8}
				};
		}
	}

	static double[][] emboss(int maskSize) {
		switch(maskSize) {
			case 3:
				return new double[][]{
					{-1, -1,  0},
					{-1,  0,  1},
					{ 0,  1,  1}
				};
			case 5:
			default:
				return new double[][]{
					{-1, -1, -1, -1,  0},
					{-1, -1, -1,  0,  1},
					{-1, -1,  0,  1,  1},
					{-1,  0,  1,  1,  1},
					{ 0,  1,  1,  1,  1}
				};
		}
	}

	static double[][] horizontalEdges() {
		return new double[][]{
			{0,  0, -1,  0,  0},
			{0,  0, -1,  0,  0},
			{0,  0,  2,  0,  0},
			{0,  0,  0,  0,  0},
			{0,  0,  0,  0,  0}
		};
	}

	static double[][] verticalEdges() {
		return new double[][]{
			{0,  0, -1,  0,  0},
			{0,  0, -1,  0,  0},
			{0,  0,  4,  0,  0},
			{0,  0, -1,  0,  0},
			{0,  0, -1,  0,  0}
		};
	}

	static double[][] diagonalEdges() {
		return new double[][]{
			{-1,  0,  0,  0,  0},
			{ 0, -2,  0,  0,  0},
			{ 0,  0,  6,  0,  0},
			{ 0,  0,  0, -2,  0},
			{ 0,  0,  0,  0, -1}
		};
	}

	static double[][] edgeDetection() {
		return new double[][]{
			{-1, -1, -1},
			{-1,  8, -1},
			{-1, -1, -1}
		};
	}

}