import java.lang.Math;

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