
class Test {
   
   Test() {

      ImageProcessing image = new ImageProcessing("bg1.jpg");
      ImageProcessing bg = new ImageProcessing("bg2.jpg");
      image.imageSubtraction(
         image.imageConvolution(MaskType.spatialWeightedAveraging(5))
      )
      .imageAddition(image)
      .showImage();
      image.imageAddition(bg).showImage();
      image.imageConvolution(MaskType.sharpen(5)).imageAddition(image).showImage();
   }

   static public void main(String args[]) throws Exception {
      Test t = new Test();
   }
}
