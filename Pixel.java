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

import java.lang.Math;


class Pixel {

   private static final String PATH = 
         "C:\\Users\\Mauricio\\Documents\\UDLAP\\Primavera 2018\\Procesamiento Multimedial\\";
   
   Pixel() {
      BufferedImage image = loadImage("face.png");
      int[] data = getData(image);
      int[] avg = grayscaleTransformation(data, GrayscaleType.Average);
      saveImage(setData(image, avg), "face_average_grayscale.png", "png");
      int[] lum = grayscaleTransformation(data, GrayscaleType.Luminosity);
      saveImage(setData(image, lum), "face_luminosity_grayscale.png", "png");
      showImage(setData(image, imageSubtraction(avg, lum)));
   }

   private BufferedImage loadImage(String name) {
      try {
         File file = new File(PATH + name);
         return ImageIO.read(file);
      } catch ( Exception e ) {
           e.printStackTrace();
      }
      return null;
   }

   private void saveImage(BufferedImage image, String name, String format) {
      try {
         File file = new File(PATH + name);
         ImageIO.write(image, format, file);
      } catch ( Exception e ) {
         e.printStackTrace();
      }
   }

   private int[] getData(BufferedImage image) {
      if (image == null)
         return new int[0];
      int w = image.getWidth();
      int h = image.getHeight();
      return image.getRGB(0, 0, w, h, null, 0, w);
   }

   private BufferedImage setData(BufferedImage image, int[] data) {
      if (image == null || data.length <= 0)
         return image;
      int w = image.getWidth();
      int h = image.getHeight();
      image.setRGB(0, 0, w, h, data, 0, w);
      return image;
   }

   private void showImage(BufferedImage image) {
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

   private int[] process(int[] data, UnaryOperator<Pix> operator) {
      return Arrays
         .stream(data)
         .parallel()
         .map(
            pixel -> {
               return operator.apply(new Pix(pixel)).toInt();
            }
         )
         .toArray();
   }

   private int[] process(int[] data1, int[] data2, BinaryOperator<Pix> operator) {
      List<Tuple<Integer, Integer>> data = new ArrayList<>();
      for(int i = 0; i < data1.length && i < data2.length; i++) {
         data.add(new Tuple<Integer, Integer>(data1[i], data2[i]));
      }
      return data
         .parallelStream()
         .mapToInt(
            tuple -> {
               return operator.apply(new Pix(tuple.first), new Pix(tuple.second)).toInt();
            }
         )
         .toArray();
   }

   private int[] grayscaleTransformation(int[] data) {
      return grayscaleTransformation(data, GrayscaleType.Luminosity);
   }
   
   private int[] grayscaleTransformation(int[] data, GrayscaleType type) {
      return Arrays
         .stream(data)
         .parallel()
         .map(
            pixel -> {
               return new Pix(pixel).toGrayInt(type);
            }
         )
         .toArray();
   }

   private int[] inverseOperator(int[] data) {
      UnaryOperator<Pix> operator = pix -> {
         pix.red = 0xff - pix.red;
         pix.green = 0xff - pix.green;
         pix.blue = 0xff - pix.blue;
         return pix;
      };
      return process(data, operator);
   }

   private int[] binaryThresholdInterval(int[] data, int p1, int p2) {
      UnaryOperator<Pix> operator = pix -> {
         pix.red = (pix.red >= p1 && pix.red <= p2) ? 0xff : 0;
         pix.green = (pix.green >= p1 && pix.green <= p2) ? 0xff : 0;
         pix.blue = (pix.blue >= p1 && pix.blue <= p2) ? 0xff : 0;
         return pix;
      };
      return process(data, operator);
   }

   private int[] invertedBinaryTresholdOperator(int[] data, int p1, int p2) {
      UnaryOperator<Pix> operator = pix -> {
         pix.red = (pix.red >= p1 && pix.red <= p2) ? 0 : 0xff;
         pix.green = (pix.green >= p1 && pix.green <= p2) ? 0 : 0xff;
         pix.blue = (pix.blue >= p1 && pix.blue <= p2) ? 0 : 0xff;
         return pix;
      };
      return process(data, operator);
   }

   private int[] tresholdOperator(int[] data, int p1) {
      return binaryThresholdInterval(data, p1, 0xff);
   }

   private int[] invertedTresholdOperator(int[] data, int p1) {
      return binaryThresholdInterval(data, 0, p1);
   }

   private int[] grayscaleTresholdOperator(int[] data, int p1, int p2) {
      UnaryOperator<Pix> operator = pix -> {
         pix.red = (pix.red >= p1 && pix.red <= p2) ? pix.red : 0;
         pix.green = (pix.green >= p1 && pix.green <= p2) ? pix.green : 0;
         pix.blue = (pix.blue >= p1 && pix.blue <= p2) ? pix.blue : 0;
         return pix;
      };
      return process(data, operator);
   }

   private int[] inverseGrayscaleTresholdOperator(int[] data, int p1, int p2) {
      UnaryOperator<Pix> operator = pix -> {
         pix.red = (pix.red >= p1 && pix.red <= p2) ? 0xff - pix.red : 0;
         pix.green = (pix.green >= p1 && pix.green <= p2) ? 0xff - pix.green : 0;
         pix.blue = (pix.blue >= p1 && pix.blue <= p2) ? 0xff - pix.blue : 0;
         return pix;
      };
      return process(data, operator);
   }


   private int[] stretchOperator(int[] data, int p1, int p2) {
      UnaryOperator<Pix> operator = pix -> {
         //((x - p1)*(255 / (p2 - p1))) if x > p1 and x < p2 else 0

         pix.red = (pix.red > p1 && pix.red < p2) ? (pix.red - p1) * (0xff / (p2 - p1)) : 0;
         pix.green = (pix.green > p1 && pix.green < p2) ? (pix.green - p1) * (0xff / (p2 - p1)) : 0;
         pix.blue = (pix.blue > p1 && pix.blue < p2) ? (pix.blue - p1) * (0xff / (p2 - p1)) : 0;
         return pix;
      };
      return process(data, operator);
   }

   private int[] grayLevelReductionOperator(int[] data, int[] p, int[] q) {
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
         if (over[0]) {
            pix.red = 0xff;
         }
         if (over[1]) {
            pix.green = 0xff;
         }
         if (over[2]) {
            pix.blue = 0xff;
         }
         return pix;
      };
      return process(data, operator);
   }

   private int[] grayLevelReductionOperatorByLevel(int[] data, int levels) {
      if(levels < 2)
         return data;

      int[] p = new int[levels];
      for(int i = 0; i < levels; i++) {
         p[i] = 0xff / levels * i;
      }
      levels--;
      int[] q = new int[levels];
      for(int i = 0; i < levels; i++) {
         q[i] = 0xff / levels * i;
      }
      return grayLevelReductionOperator(data, p , q);
   }

   private int[] imageAddition(int[] data1, int[] data2) {
      return imageAddition(data1, data2, 2);
     }

   private int[] imageAddition(int[] data1, int[] data2, double scalingFactor) {
      BinaryOperator<Pix> operator = (pix1, pix2) -> {
         Pix pix = new Pix();
         pix.red = (int) Math.ceil( (pix1.red + pix2.red) / scalingFactor);
         pix.green = (int) Math.ceil( (pix1.green + pix2.green) / scalingFactor);
         pix.blue = (int) Math.ceil( (pix1.blue + pix2.blue) / scalingFactor);
         return pix;
      };

      return process(data1, data2, operator);
   }

   private int[] imageSubtraction(int[] data1, int[] data2) {
      List<Tuple<Integer, Integer>> data = new ArrayList<>();
      for(int i = 0; i < data1.length && i < data2.length; i++)
         data.add(new Tuple<Integer, Integer>(data1[i], data2[i]));
      
      List<Pix> subtractedData = data
         .parallelStream()
         .map(
            tuple -> {
               Pix pix1 = new Pix(tuple.first);
               Pix pix2 = new Pix(tuple.second);
               Pix pix = new Pix();
               pix.red = pix1.red - pix2.red;
               pix.green = pix1.green - pix2.green;
               pix.blue = pix1.blue - pix2.blue;   
               return pix;
            }
         )
         .collect(Collectors.toList());

      int[] max = new int[3];
      int[] min = new int[3];

      max[0] = subtractedData
         .stream()
         .mapToInt( pix -> pix.red)
         .max()
         .getAsInt();
      max[1] = subtractedData
         .stream()
         .mapToInt( pix -> pix.green)
         .max()
         .getAsInt();
      max[2] = subtractedData
         .stream()
         .mapToInt( pix -> pix.blue)
         .max()
         .getAsInt();
      min[0] = subtractedData
         .stream()
         .mapToInt( pix -> pix.red)
         .min()
         .getAsInt();
      min[1] = subtractedData
         .stream()
         .mapToInt( pix -> pix.green)
         .min()
         .getAsInt();
      min[2] = subtractedData
         .stream()
         .mapToInt( pix -> pix.blue)
         .min()
         .getAsInt();

      double[] scalar = new double[3];
      scalar[0] = (255.0 / (Math.abs(min[0]) + Math.abs(max[0])));
      scalar[1] = (255.0 / (Math.abs(min[1]) + Math.abs(max[1])));
      scalar[2] = (255.0 / (Math.abs(min[2]) + Math.abs(max[2])));

      return subtractedData
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
   }

   private int[] imageMultiplcation(int[] data1, int[] data2) {
      BinaryOperator<Pix> operator = (pix1, pix2) -> {
         Pix pix = new Pix();
         pix.red = Math.toIntExact(Math.round(pix1.red + (pix1.red * (pix2.red / 10.0))));
         pix.green = Math.toIntExact(Math.round(pix1.green + (pix1.green * (pix2.green / 10.0))));
         pix.blue = Math.toIntExact(Math.round(pix1.blue + (pix1.blue * (pix2.blue / 10.0))));
         pix.red = pix.red > 255 ? 255 : pix.red;
         pix.green = pix.green > 255 ? 255 : pix.green;
         pix.blue = pix.blue > 255 ? 255 : pix.blue;
         return pix;
      };

      return process(data1, data2, operator);
   }


   static public void main(String args[]) throws Exception {
      Pixel obj = new Pixel();
   }
}

class Pix {

   int alpha;
   int red;
   int green;
   int blue;

   Pix() {}

   Pix(int pixel) {
      alpha = (pixel >> 24) & 0xff;
      red = (pixel >> 16) & 0xff;
      green = (pixel >> 8) & 0xff;
      blue = pixel & 0xff;               
   }

   int toInt() {
      return (alpha << 24) | (red << 16) | (green << 8) | blue;
   }

   int toGrayInt() {
      return toGrayInt(GrayscaleType.Luminosity);
   }

   int toGrayInt(GrayscaleType type) {
      final double value;
      switch (type) {
         case Average:
            value = (red + green + blue) / 3.0;
            break;
         case Luminosity:
         default:
            value = 0.21 * red + 0.72 * green + 0.07 * blue;
      }

      int avg = Math.toIntExact(Math.round(value));
      return (alpha << 24) | (avg << 16) | (avg << 8) | avg;  
   }

}

enum GrayscaleType {
   Luminosity,
   Average;
}

class Tuple<E, F> {
   final E first;
   final F second;

   Tuple(E e, F f) {
      first = e;
      second = f;
   }
}