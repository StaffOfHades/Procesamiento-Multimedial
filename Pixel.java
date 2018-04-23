import java.awt.*;
import java.awt.image.*;

import java.io.*;

import javax.imageio.*;
import javax.swing.*;

import java.util.List;
import java.util.Arrays;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import java.lang.Math;


class Pixel {

   private static final String PATH = 
         "C:\\Users\\Mauricio\\Documents\\UDLAP\\Primavera 2018\\Procesamiento Multimedial\\";

   BufferedImage image;
   int width;
   int height;
   
   Pixel() {
      BufferedImage image = loadImage("face.png");
      int[] data = getData(image);
      int[] gray = grayscaleTransformation(data);
      long timeA = 0, timeB = 0;

      int loops = 10;
      for(int i = 0; i < loops; i++) {
         long timeIn = System.currentTimeMillis();
         inverseOperatorA(gray);
         long timeOut = System.currentTimeMillis();
         timeA += timeOut - timeIn;
         timeIn = System.currentTimeMillis();
         inverseOperatorB(gray);
         timeOut = System.currentTimeMillis();
         timeB += timeOut - timeIn;
      }
      
      System.out.println("TimeA: " + (timeA / loops) + ", TimeB: " + (timeB / loops) );
      //setData(image, inverse);
      //showImage(image);
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
   
   private int[] grayscaleTransformation(int[] data) {
      return Arrays
         .stream(data)
         .parallel()
         .map(
            pixel -> {
               return new Pix(pixel).toGrayInt();
            }
         )
         .toArray();
   }

   private int[] inverseOperatorA(int[] data) {
      UnaryOperator<Pix> operator = pix -> {
         pix.red = 0xff - pix.red;
         pix.green = 0xff - pix.green;
         pix.blue = 0xff - pix.blue;
         return pix;
      };
      return process(data, operator);
   }

   private int[] inverseOperatorB(int[] data) {
       return Arrays
         .stream(data)
         .parallel()
         .map(
            pixel -> {
               int a = (pixel >> 24) & 0xff;
               int r = (pixel >> 16) & 0xff;
               int g = (pixel >> 8) & 0xff;
               int b = pixel & 0xff;

               r = 0xff - r;
               g = 0xff - g;
               b = 0xff - b;
               return (a << 24) | (r << 16) | (g << 8) | b;
            }
         )
         .toArray();
   }

   private int[] binaryThresholdInterval(int[] data, int p1, int p2) {
      return Arrays
         .stream(data)
         .parallel()
         .map(
            pixel -> {
               int a = (pixel >> 24) & 0xff;
               int r = (pixel >> 16) & 0xff;
               int g = (pixel >> 8) & 0xff;
               int b = pixel & 0xff;

               r = (r >= p1 && r <= p2) ? 0xff : 0;
               g = (g >= p1 && g <= p2) ? 0xff : 0;
               b = (b >= p1 && b <= p2) ? 0xff : 0;
               return (a << 24) | (r << 16) | (g << 8) | b;
            }
         )
         .toArray();
   }

   private int[] invertedBinaryTresholdOperator(int[] data, int p1, int p2) {
      return Arrays
         .stream(data)
         .parallel()
         .map(
            pixel -> {
               int a = (pixel >> 24) & 0xff;
               int r = (pixel >> 16) & 0xff;
               int g = (pixel >> 8) & 0xff;
               int b = pixel & 0xff;

               r = (r >= p1 && r <= p2) ? 0 : 0xff;
               g = (g >= p1 && g <= p2) ? 0 : 0xff;
               b = (b >= p1 && b <= p2) ? 0 : 0xff;
               return (a << 24) | (r << 16) | (g << 8) | b;
            }
         )
         .toArray();
   }

   private int[] tresholdOperator(int[] data, int p1) {
      return binaryThresholdInterval(data, p1, 0xff);
   }

   private int[] invertedTresholdOperator(int[] data, int p1) {
      return binaryThresholdInterval(data, 0, p1);
   }

   private int[] grayscaleTresholdOperator(int[] data, int p1, int p2) {
      return Arrays
         .stream(data)
         .parallel()
         .map(
            pixel -> {
               int a = (pixel >> 24) & 0xff;
               int r = (pixel >> 16) & 0xff;
               int g = (pixel >> 8) & 0xff;
               int b = pixel & 0xff;

               r = (r >= p1 && r <= p2) ? r : 0;
               g = (g >= p1 && g <= p2) ? g : 0;
               b = (b >= p1 && b <= p2) ? b : 0;
               return (a << 24) | (r << 16) | (g << 8) | b;
            }
         )
         .toArray();
   }

   private int[] inverseGrayscaleTresholdOperator(int[] data, int p1, int p2) {
      return Arrays
         .stream(data)
         .parallel()
         .map(
            pixel -> {
               int a = (pixel >> 24) & 0xff;
               int r = (pixel >> 16) & 0xff;
               int g = (pixel >> 8) & 0xff;
               int b = pixel & 0xff;

               r = (r >= p1 && r <= p2) ? 0xff - r : 0;
               g = (g >= p1 && g <= p2) ? 0xff - g : 0;
               b = (b >= p1 && b <= p2) ? 0xff - b : 0;
               return (a << 24) | (r << 16) | (g << 8) | b;
            }
         )
         .toArray();
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

   static public void main(String args[]) throws Exception {
      Pixel obj = new Pixel();
   }
}

class Pix {

   int alpha;
   int red;
   int green;
   int blue;

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
      int avg = Math.toIntExact(
                  Math.round(
                     0.21 * red + 0.72 * green + 0.07 * blue
                  )
               );
      return (alpha << 24) | (red << 16) | (green << 8) | blue;  
   }

}