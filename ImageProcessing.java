import java.awt.*;
import java.awt.image.*;

import java.io.*;

import javax.imageio.*;
import javax.swing.*;

import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.UnaryOperator;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;


class ImageProcessing {

   private static final String PATH = 
         "C:\\Users\\Mauricio\\Documents\\UDLAP\\Primavera 2018\\Procesamiento Multimedial\\";

   private BufferedImage image;
   private PixelProcessing data;

   ImageProcessing(String name) {
      loadImage(name);
   }

   ImageProcessing(BufferedImage image) {
      this.image = image;
   }
   
   void loadImage(String name) {
      try {
         File file = new File(PATH + name);
         image = ImageIO.read(file);
         data = new PixelProcessing(image);
      } catch ( Exception e ) {
           e.printStackTrace();
      }
   }

   void saveImage(String name, String format) {
      try {
         File file = new File(PATH + name);
         ImageIO.write(image, format, file);
      } catch ( Exception e ) {
         e.printStackTrace();
      }
   }

   void showImage() {
      SwingUtilities.invokeLater(new Runnable()
         {
            public void run() {
               JFrame editorFrame = new JFrame("Image Demo");
               editorFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); 
               ImageIcon imageIcon = new ImageIcon(image);
               JLabel jLabel = new JLabel();
               jLabel.setIcon(imageIcon);
               editorFrame.getContentPane().add(jLabel, BorderLayout.CENTER);

               editorFrame.pack();
               editorFrame.setLocationRelativeTo(null);
               editorFrame.setVisible(true);
            }
         }
      );
   }

   private void setData() {
      int w = image.getWidth();
      int h = image.getHeight();
      image.setRGB(0, 0, w, h, data.getPixels(), 0, w);
   }

   BufferedImage getImage() {
      return image;
   }

   void setPixels(PixelProcessing pixels) {
      data = pixels;
      setData();
   }

   PixelProcessing getPixels() {
      return data;
   }

   private ImageProcessing copy(PixelProcessing data) {
      ColorModel model = image.getColorModel();
      boolean isAlphaPremultiplied = model.isAlphaPremultiplied();
      WritableRaster raster = image.copyData(image.getRaster().createCompatibleWritableRaster());
      BufferedImage copyImage = new BufferedImage(model, raster, isAlphaPremultiplied, null);
      ImageProcessing copy = new ImageProcessing(copyImage);
      copy.setPixels(data.copy());
      return copy;
   }

   int[] getMax(List<Pix> pixs) {
      int[] max = new int[3];
      for(int i = 0; i < max.length; i++) {
         final int type = i;
         max[i] = pixs
            .stream()
            .mapToInt( pix -> {
               switch(type) {
                  case 0:
                     return pix.red;
                  case 1:
                     return pix.green;
                  case 2:
                  default:
                     return pix.blue;
               }
            })
            .max()
            .getAsInt();
      }
      return max;
   }

   int[] getMin(List<Pix> pixs) {
      int[] min = new int[3];
      for(int i = 0; i < min.length; i++) {
         final int type = i;
         min[i] = pixs
            .stream()
            .mapToInt( pix -> {
               switch(type) {
                  case 0:
                     return pix.red;
                  case 1:
                     return pix.green;
                  case 2:
                  default:
                     return pix.blue;
                  }
            })
            .min()
            .getAsInt();
      }
      return min;
   }

   private List<Pix> process(PixelProcessing other, BinaryOperator<Pix> operator) {
      List<Tuple<Integer, Integer>> tuples = data.add(other);
      return tuples
         .parallelStream()
         .map(
            tuple -> {
               return operator.apply(new Pix(tuple.first), new Pix(tuple.second));
            }
         )
         .collect(Collectors.toList());
   }

   private PixelProcessing processPixels(PixelProcessing other, BinaryOperator<Pix> operator) {
      return new PixelProcessing(
         process(other, operator)
         .parallelStream()
         .mapToInt(
            tuple -> {
               return tuple.toInt();
            }
         )
         .toArray()
      );
   }

   ImageProcessing grayscaleTransformation() {
      return grayscaleTransformation(GrayscaleType.Luminosity);
   }

   ImageProcessing grayscaleTransformation(GrayscaleType type) {
      return copy(
         data.grayscaleTransformation(type)
      );
   }

   ImageProcessing inverseOperator() {
      return copy(
         data.inverseOperator()
      );
   }

   ImageProcessing binaryThresholdInterval(int p1, int p2) {
      return copy(
         data.binaryThresholdInterval(p1, p2)
      );
   }

   ImageProcessing invertedBinaryTresholdOperator(int p1, int p2) {
      return copy(
         data.invertedBinaryTresholdOperator(p1, p2)
      );
   }

   ImageProcessing tresholdOperator(int p) {
      return copy(
         data.tresholdOperator(p)
      );
   }

   ImageProcessing invertedTresholdOperator(int p) {
      return copy(
         data.invertedTresholdOperator(p)
      );
   }

   ImageProcessing grayscaleTresholdOperator(int p1, int p2) {
      return copy(
         data.grayscaleTresholdOperator(p1, p2)
      );
   }

   ImageProcessing inverseGrayscaleTresholdOperator(int p1, int p2) {
      return copy(
         data.inverseGrayscaleTresholdOperator(p1, p2)
      );
   }

   ImageProcessing stretchOperator(int p1, int p2) {
      return copy(
         data.stretchOperator(p1, p2)
      );
   }

   ImageProcessing grayLevelReductionOperator(int[] p, int[] q) {
      return copy(
         data.grayLevelReductionOperator(p, q)
      );
   }

   ImageProcessing grayLevelReductionOperatorByLevel(int levels) {
      return copy(
         data.grayLevelReductionOperatorByLevel(levels)
      );
   }

   ImageProcessing imageAddition(ImageProcessing other) {
      return imageAddition(other, false);
   }

   ImageProcessing imageAddition(ImageProcessing other, boolean withRescaling) {
         return withRescaling ? imageAdditionRescaling(other) : imageAdditionAverage(other, 2);
   }

   ImageProcessing imageAdditionAverage(ImageProcessing other, double scalingFactor) {
      BinaryOperator<Pix> operator = (pix1, pix2) -> {
         Pix pix = new Pix();
         pix.red = (int) Math.ceil( (pix1.red + pix2.red) / scalingFactor);
         pix.green = (int) Math.ceil( (pix1.green + pix2.green) / scalingFactor);
         pix.blue = (int) Math.ceil( (pix1.blue + pix2.blue) / scalingFactor);
         return pix;
      };
      return copy(
         processPixels(other.getPixels(), operator)
      );
   }

   ImageProcessing imageAdditionRescaling(ImageProcessing other) {
      BinaryOperator<Pix> operator = (pix1, pix2) -> {
         Pix pix = new Pix();
         pix.red = pix1.red + pix2.red;
         pix.green = pix1.green + pix2.green;
         pix.blue = pix1.blue + pix2.blue;   
         return pix;
      };
      List<Pix> additionData = process(other.getPixels(), operator);

      int[] max = getMax(additionData);
      int[] min = getMin(additionData);

      double[] scalar = new double[3];
      scalar[0] = (255.0 / (Math.abs(min[0]) + Math.abs(max[0])));
      scalar[1] = (255.0 / (Math.abs(min[1]) + Math.abs(max[1])));
      scalar[2] = (255.0 / (Math.abs(min[2]) + Math.abs(max[2])));

      int[] results = additionData
         .parallelStream()
         .mapToInt( pix ->
            {
               pix.red = Math.toIntExact(Math.round((pix.red - min[0]) * scalar[0]));
               pix.green = Math.toIntExact(Math.round((pix.green - min[1]) * scalar[1]));
               pix.blue = Math.toIntExact(Math.round((pix.blue - min[2]) * scalar[2]));
               return pix.toInt();
            }
         )
         .toArray();
      return copy(
         new PixelProcessing(results)
      );
   }

   ImageProcessing imageSubtraction(ImageProcessing other) {
       BinaryOperator<Pix> operator = (pix1, pix2) -> {
         Pix pix = new Pix();
         pix.red = pix1.red - pix2.red;
         pix.green = pix1.green - pix2.green;
         pix.blue = pix1.blue - pix2.blue;   
         return pix;
      };
      List<Pix> subtractedData = process(other.getPixels(), operator);

      int[] max = getMax(subtractedData);
      int[] min = getMin(subtractedData);

      double[] scalar = new double[3];
      scalar[0] = (255.0 / (Math.abs(min[0]) + Math.abs(max[0])));
      scalar[1] = (255.0 / (Math.abs(min[1]) + Math.abs(max[1])));
      scalar[2] = (255.0 / (Math.abs(min[2]) + Math.abs(max[2])));

      int[] results = subtractedData
         .parallelStream()
         .mapToInt( pix ->
            {
               pix.red = Math.toIntExact(Math.round((pix.red - min[0]) * scalar[0]));
               pix.green = Math.toIntExact(Math.round((pix.green - min[1]) * scalar[1]));
               pix.blue = Math.toIntExact(Math.round((pix.blue - min[2]) * scalar[2]));
               return pix.toInt();
            }
         )
         .toArray();
      return copy(
         new PixelProcessing(results)
      );
   }

   ImageProcessing imageMultiplcation(ImageProcessing other) {
      BinaryOperator<Pix> operator = (pix1, pix2) -> {
         Pix pix = new Pix();
         pix.red = Math.toIntExact(Math.round(pix1.red + (pix1.red * (pix2.red / 10.0))));
         pix.green = Math.toIntExact(Math.round(pix1.green + (pix1.green * (pix2.green / 10.0))));
         pix.blue = Math.toIntExact(Math.round(pix1.blue + (pix1.blue * (pix2.blue / 10.0))));
         pix.red = Math.max(pix.red, 0xff);
         pix.green = Math.max(pix.green, 0xff);
         pix.blue = Math.max(pix.blue, 0xff);
         return pix;
      };

      return copy(
         processPixels(other.getPixels(), operator)
      );
   }

   ImageProcessing imageConvolution(double[][] mask) {
      return imageConvolution(mask, 1, 0);
   }

   ImageProcessing imageConvolution(double[][] mask, double scalingFactor) {
      return imageConvolution(mask, scalingFactor, 0);
   }

   ImageProcessing imageConvolution(double[][] mask, int bias) {
      return imageConvolution(mask, 1.0, bias);
   }

   ImageProcessing imageConvolution(double[][] mask, double scalingFactor, int bias) {
      BufferedImage convoluted = copy(getPixels()).getImage();
      int width = convoluted.getWidth();
      int height = convoluted.getHeight();

      for(int x = 0; x < width; x++) {
         for(int y = 0; y < height; y++) {
            Pix output = new Pix();
            Pix input = new Pix( image.getRGB(x, y) );
            output.alpha = input.alpha;
            double red, green, blue;
            red = green = blue = 0;
            for (int xi = 0; xi < mask.length; xi++) {
               for(int yi = 0; yi < mask[xi].length; yi++) {
                  int xj = (x - mask.length / 2 + xi + width) % width;
                  int yj = (y - mask[xi].length / 2 + yi + height) % height;

                  Pix pix = new Pix(image.getRGB(xj, yj));
                  red += pix.red * mask[xi][yi] / scalingFactor;
                  green += pix.green * mask[xi][yi] / scalingFactor;
                  blue += pix.blue * mask[xi][yi] / scalingFactor;
               }
            }
            red = Math.min(Math.max(red + bias, 0), 255);
            green = Math.min(Math.max(green + bias, 0), 255);
            blue = Math.min(Math.max(blue + bias, 0), 255);
            output.red = Math.toIntExact(Math.round(red));
            output.green = Math.toIntExact(Math.round(green));
            output.blue = Math.toIntExact(Math.round(blue));
            convoluted.setRGB(x, y, output.toInt());
         }
      }
      return copy(
         new PixelProcessing(convoluted)
      );
   }

}