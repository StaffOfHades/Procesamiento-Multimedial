
class Test {
   
   Test() {

      ImageProcessing image = new ImageProcessing("bg1", "jpg");
      ImageProcessing bg = new ImageProcessing("bg2", "jpg");
      // Low-pass High-pass Crispen
      image.imageSubtraction(
         image.imageConvolution(MaskType.spatialWeightedAveraging(5))
      )
      .imageAddition(image)
      .showImage();
      // Image addition normal
      image.imageAddition(bg).showImage();

      // Sharpen
      image.imageConvolution(MaskType.sharpen(5)).imageAddition(image).showImage();

      // Noise
      image.grayscaleTransformation().imageSubtraction(image.grayscaleTransformation().grayLevelReductionOperatorByLevel(8)).showImage();
   }

   static public void main(String args[]) throws Exception {
      Test t = new Test();
   }
}
