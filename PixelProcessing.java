
import java.awt.image.BufferedImage;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.function.UnaryOperator;
import java.util.List;
import java.util.stream.Collectors;

class PixelProcessing {

   private int[] pixels;

   PixelProcessing(BufferedImage image) {
      if (image != null) {
         int w = image.getWidth();
         int h = image.getHeight();
         pixels = image.getRGB(0, 0, w, h, null, 0, w);
      }
   }

   PixelProcessing(int[] data) {
      pixels = data;
   }

   PixelProcessing copy() {
      int[] copy = new int[pixels.length];
      for(int i = 0; i < copy.length; i++)
         copy[i] = pixels[i];
      return new PixelProcessing(copy);
   }

   int[] getPixels() {
      return pixels;
   }

   private int[] process(UnaryOperator<Pix> operator) {
      return Arrays
         .stream(pixels)
         .parallel()
         .map(
            pixel -> {
               return operator.apply(new Pix(pixel)).toInt();
            }
         )
         .toArray();
   }

   PixelProcessing grayscaleTransformation() {
      return grayscaleTransformation(GrayscaleType.Luminosity);
   }
   
   PixelProcessing grayscaleTransformation(GrayscaleType type) {
      return new PixelProcessing(
         Arrays
         .stream(pixels)
         .parallel()
         .map(
            pixel -> {
               return new Pix(pixel).toGrayInt(type);
            }
         )
         .toArray()
      );
   }

   PixelProcessing inverseOperator() {
      UnaryOperator<Pix> operator = pix -> {
         pix.red = 0xff - pix.red;
         pix.green = 0xff - pix.green;
         pix.blue = 0xff - pix.blue;
         return pix;
      };
      return new PixelProcessing(
         process(operator)
      );
   }

   PixelProcessing binaryThresholdInterval(int p1, int p2) {
      UnaryOperator<Pix> operator = pix -> {
         pix.red = (pix.red >= p1 && pix.red <= p2) ? 0xff : 0;
         pix.green = (pix.green >= p1 && pix.green <= p2) ? 0xff : 0;
         pix.blue = (pix.blue >= p1 && pix.blue <= p2) ? 0xff : 0;
         return pix;
      };
      return new PixelProcessing(
         process(operator)
      );
   }

   PixelProcessing invertedBinaryTresholdOperator(int p1, int p2) {
      UnaryOperator<Pix> operator = pix -> {
         pix.red = (pix.red >= p1 && pix.red <= p2) ? 0 : 0xff;
         pix.green = (pix.green >= p1 && pix.green <= p2) ? 0 : 0xff;
         pix.blue = (pix.blue >= p1 && pix.blue <= p2) ? 0 : 0xff;
         return pix;
      };
      return new PixelProcessing(
         process(operator)
      );
   }

   PixelProcessing tresholdOperator(int p) {
      return binaryThresholdInterval(p, 0xff);
   }

   PixelProcessing invertedTresholdOperator(int p) {
      return binaryThresholdInterval(0, p);
   }

   PixelProcessing grayscaleTresholdOperator(int p1, int p2) {
      UnaryOperator<Pix> operator = pix -> {
         pix.red = (pix.red >= p1 && pix.red <= p2) ? pix.red : 0;
         pix.green = (pix.green >= p1 && pix.green <= p2) ? pix.green : 0;
         pix.blue = (pix.blue >= p1 && pix.blue <= p2) ? pix.blue : 0;
         return pix;
      };
      return new PixelProcessing(
         process(operator)
      );
   }

   PixelProcessing inverseGrayscaleTresholdOperator(int p1, int p2) {
      UnaryOperator<Pix> operator = pix -> {
         pix.red = (pix.red >= p1 && pix.red <= p2) ? 0xff - pix.red : 0;
         pix.green = (pix.green >= p1 && pix.green <= p2) ? 0xff - pix.green : 0;
         pix.blue = (pix.blue >= p1 && pix.blue <= p2) ? 0xff - pix.blue : 0;
         return pix;
      };
      return new PixelProcessing(
         process(operator)
      );
   }

   PixelProcessing stretchOperator(int p1, int p2) {
      UnaryOperator<Pix> operator = pix -> {
         //((x - p1)*(0xff / (p2 - p1))) if x > p1 and x < p2 else 0

         pix.red = (pix.red > p1 && pix.red < p2) ? (pix.red - p1) * (0xff / (p2 - p1)) : 0;
         pix.green = (pix.green > p1 && pix.green < p2) ? (pix.green - p1) * (0xff / (p2 - p1)) : 0;
         pix.blue = (pix.blue > p1 && pix.blue < p2) ? (pix.blue - p1) * (0xff / (p2 - p1)) : 0;
         return pix;
      };
      return new PixelProcessing(
         process(operator)
      );
   }

   PixelProcessing grayLevelReductionOperator(int[] p, int[] q) {
      UnaryOperator<Pix> operator = pix -> {
         boolean[] over = new boolean[3];
         over[0] = over[1] = over[2] = true;

         for (int i = 0; i < p.length && (over[0] || over[1] || over[2]); i++) {
            if (over[0] && pix.red <= p[i]) {
               over[0] = false;
               pix.red = i == 0 ? 0 : q[i - 1];
            }
            if (over[1] && pix.green <= p[i]) {
               over[1] = false;
               pix.green = i == 0 ? 0 : q[i - 1];
            }
            if (over[2] && pix.blue <= p[i]) {
               over[2] = false;
               pix.blue = i == 0 ? 0 : q[i - 1];
            }
         }
         pix.red = over[0] ? 0xff : pix.red;
         pix.green = over[1] ? 0xff : pix.green;
         pix.blue = over[2] ? 0xff : pix.blue;
         return pix;
      };
      return new PixelProcessing(
         process(operator)
      );
   }

   PixelProcessing grayLevelReductionOperatorByLevel(int levels) {
      if(levels < 2)
         return new PixelProcessing(pixels);

      int[] p = new int[levels];
      for(int i = 0; i < levels; i++) {
         p[i] = 0xff / levels * i;
      }
      levels--;
      int[] q = new int[levels];
      for(int i = 0; i < levels; i++) {
         q[i] = 0xff / levels * i;
      }
      return grayLevelReductionOperator(p , q);
   }

   PixelProcessing rescale(int[] max, int[] min) {
      double[] scalar = new double[3];
      scalar[0] = (255.0 / (Math.abs(min[0]) + Math.abs(max[0])));
      scalar[1] = (255.0 / (Math.abs(min[1]) + Math.abs(max[1])));
      scalar[2] = (255.0 / (Math.abs(min[2]) + Math.abs(max[2])));
      int[] result = Arrays
         .stream(pixels)
         .parallel()
         .map( pixel ->
            {
               Pix pix = new Pix(pixel);
               pix.red = Math.toIntExact(Math.round((pix.red - min[0]) * scalar[0]));
               pix.green = Math.toIntExact(Math.round((pix.green - min[1]) * scalar[1]));
               pix.blue = Math.toIntExact(Math.round((pix.blue - min[2]) * scalar[2]));
               return pix.toInt();
            }
         )
         .toArray();

      return new PixelProcessing(
         result
      );
   }

   List<Tuple<Integer, Integer>> add(PixelProcessing other) {
      return PixelProcessing.add(this, other);
   }

   static List<Tuple<Integer, Integer>> add(PixelProcessing one, PixelProcessing two) {
      List<Tuple<Integer, Integer>> data = new ArrayList<>();
      int[] first = one.getPixels();
      int[] second = two.getPixels();
      for(int i = 0; i < first.length && i < second.length; i++) {
         data.add(new Tuple<Integer, Integer>(first[i], second[i]));
      }
      return data;
   }

}

enum GrayscaleType {
   Luminosity,
   Average;
}