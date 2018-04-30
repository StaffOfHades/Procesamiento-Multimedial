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

   ImageProcessing(File file) {
      loadImage(file);
   }

   // Inicia una nueva clase dado el nombre de una imagen local con tipo
   ImageProcessing(String name, String format) {
      loadImage(name, format);
   }

   // Inicia una nueva clase dado una copia
   ImageProcessing(BufferedImage image) {
      this.image = image;
   }
   
   // Carga una imagen de disco local dado el nombre y formato
   void loadImage(String name, String format) {
      loadImage(new File(PATH + name + "." + format));
   }

   // Carga una imagen de disco
   void loadImage(File file) {
      try {
         image = ImageIO.read(file);
         data = new PixelProcessing(image);
      } catch ( Exception e ) {
           e.printStackTrace();
      }
   }

   // Guarda una imagen a disco
   void saveImage(String name, String format) {
      try {
         File file = new File(PATH + name + "." + format);
         ImageIO.write(image, format, file);
      } catch ( Exception e ) {
         e.printStackTrace();
      }
   }

   // Muestra la imagen actual
   void showImage() {
      SwingUtilities.invokeLater(new Runnable()
         {
            public void run() {
               JFrame editorFrame = new JFrame("Image");
               //editorFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); 
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

   ImageProcessing copy() {
      return copy(data);
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

   // Regresa una imagen en escala de gris con el metodo de luminosidad
   ImageProcessing grayscaleTransformation() {
      return grayscaleTransformation(GrayscaleType.Luminosity);
   }

   // Regresa una imagen en escala de gris con el metodo dado
   ImageProcessing grayscaleTransformation(GrayscaleType type) {
      return copy(
         data.grayscaleTransformation(type)
      );
   }

   // Regresa una imagen invertida en colores
   ImageProcessing inverseOperator() {
      return copy(
         data.inverseOperator()
      );
   }

   // Regresa una imagen con los valores fuera de p1 y p2 suprimidos
   ImageProcessing binaryThresholdInterval(int p1, int p2) {
      return copy(
         data.binaryThresholdInterval(p1, p2)
      );
   }

   // Regresa una imagen con los valores dentro de p1 y p2 invertidos,
   // y fuera de ella suprimidos
   ImageProcessing invertedBinaryTresholdOperator(int p1, int p2) {
      return copy(
         data.invertedBinaryTresholdOperator(p1, p2)
      );
   }

   // Regresa una imagen donde los valores mayores a p son blancos
   // y los valores menores son negros
   ImageProcessing tresholdOperator(int p) {
      return copy(
         data.tresholdOperator(p)
      );
   }

   // Regresa una imagen donde los valores mayores a p son negros
   // y los valores menores son blanvos
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

   // Regresa una imagen donde se extiende el rango de valores
   // de 0 a 255
   ImageProcessing stretchOperator(int p1, int p2) {
      return copy(
         data.stretchOperator(p1, p2)
      );
   }

   // Limita el numero de colores a q + 2, de 0 a 255, dado 
   // las posiciones p
   ImageProcessing grayLevelReductionOperator(int[] p, int[] q) {
      return copy(
         data.grayLevelReductionOperator(p, q)
      );
   }

   // Limita el numero de colores a n numero de niveles si
   // la imagen esta en escala de gris,
   // o n * 3 si esta en colores.
   ImageProcessing grayLevelReductionOperatorByLevel(int levels) {
      return copy(
         data.grayLevelReductionOperatorByLevel(levels)
      );
   }

   // Suma a esta imagen otra imagen
   ImageProcessing imageAddition(ImageProcessing other) {
      return imageAddition(other, false);
   }

   // Suma a esta imagen otra imagen
   // (Se puede utilizar averaging o rescaling)
   ImageProcessing imageAddition(ImageProcessing other, boolean withRescaling) {
         return withRescaling ? imageAdditionRescaling(other) : imageAdditionAverage(other, 2);
   }

   // Suma a esta imagen otra imagen donde el valor de un pixel es la suma de ambos
   // entre el factor escala (por defecto 2 si el argumento se omite)
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

   // Suma a esta imagen otra imagen donde se suman los valores
   // y despues se redistribuyen de 0 a 255
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

   // Resta a esta imagen otra imagen donde el valor de un pixel es la resta de esta menos
   // la otra imagen y se aplica un rescalamiento
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

   // Se multiplica a esta imagen otra
   ImageProcessing imageMultiplication(ImageProcessing other) {
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

   // Se aplica esta imagen una mascara por convolucion
   ImageProcessing imageConvolution(double[][] mask) {
      return imageConvolution(mask, 1, 0);
   }

   // Se aplica esta imagen una mascara por convolucion,
   // donde la mascara no viene ya con una suma total igual a 1
   // y es necesario escalar el resultado por ende.
   ImageProcessing imageConvolution(double[][] mask, double scalingFactor) {
      return imageConvolution(mask, scalingFactor, 0);
   }

   // Se aplica esta imagen una mascara por convolucion,
   // y adicionalmente, se aplica un bias que altera todos los pixeles
   // con la suma de ese valor.
   ImageProcessing imageConvolution(double[][] mask, int bias) {
      return imageConvolution(mask, 1.0, bias);
   }

   // Se aplica esta imagen una mascara por convolucion,
   // donde la mascara no viene ya con una suma total igual a 1
   // y es necesario escalar el resultado por ende.
   // Adicionalmente, se aplica un bias que altera todos los pixeles
   // con la suma de ese valor.
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