package org.lemsml.jlems.viz.plot;

 


import java.awt.Color;

import org.lemsml.jlems.core.logging.E;
 

public final class ColorUtil {
 

   static String[] stdColorNames =  {"white", "black",
                                     "red", "green", "blue",
                                     "magenta", "cyan", "yellow",
                                     "orange", "pink", "gray"};
   static Color[] stdColors = {Color.white, Color.black,
                               Color.red, Color.green, Color.blue,
                               Color.magenta, Color.cyan, Color.yellow,
                               Color.orange, Color.pink, Color.gray};


   private ColorUtil() {
	   
   }
   
   
    public static Color getSequentialColour(int cn) {
    	int colNumber = cn;
    	if (colNumber < 0) {
        	colNumber = colNumber*-1;
        }
        int neuCol = colNumber%stdColors.length;
        return stdColors[neuCol];
    }

   public static Color brighter(Color c) {
      return linMod(c, 30);
   }


   public static Color darker(Color c) {
      return linMod(c, -30);
   }


   public static Color slightlyBrighter(Color c) {
      return linMod(c, 16);
   }


   public static Color slightlyDarker(Color c) {
      return linMod(c, -16);
   }


   public static Color verySlightlyBrighter(Color c) {
      Color ret = linMod(c, 8);
      return ret;
   }



   public static Color verySlightlyDarker(Color c) {
      Color ret = linMod(c, -8);
      return ret;
   }


   public static Color myBrighter(Color c) {
      return linMod(c, 35);
   }


   public static Color myDarker(Color c) {
      return linMod(c, -35);
   }


   public static Color linMod(Color c, int d) {
      int r = c.getRed();
      int g = c.getGreen();
      int b = c.getBlue();

      r += d;
      g += d;
      b += d;
      r = (r > 0 ? (r < 255 ? r : 255) : 0);
      g = (g > 0 ? (g < 255 ? g : 255) : 0);
      b = (b > 0 ? (b < 255 ? b : 255) : 0);
      return new Color(r, g, b);
   }



   public static Color deserialize(String s) {
      return parseColor(s);
   }


   public static Color parseColor(String s) {
      Color cret = Color.red;

      if (s == null || s.length() == 0) {
         cret = Color.orange;
         E.warning("asked to parse empty color");

      } else if (s.startsWith("#")) {
          cret = parseHexColor(s);

      } else {

         cret = simpleLookup(s);

         if (cret == null) {

       
            cret = parseHexColor(s);
       
         }
      }
      return cret;
   }


   private static Color simpleLookup(String s) {
      Color ret = null;
      for (int i = 0; i < stdColors.length; i++) {
         if (s.equals(stdColorNames[i])) {
            ret = stdColors[i];
            break;
         }

      }
      return ret;
   }



   public static Color parseHexColor(String s) {
      Color cret = null;
      try {
         int ic = Integer.decode(s).intValue();
         cret = new Color(ic);

      } catch (NumberFormatException ex) {
         E.error("cant decode color string " + s);
         cret = Color.red;
      }
      return cret;
   }


   public static Color oldParseColor(String s) {
      Color ret = Color.black;
      if (s != null && s.startsWith("#") && s.length() == 7) {
         String s1 = s.substring(1, s.length());
         int icol = Integer.parseInt(s1, 16);
         ret = new Color(icol);

      } else {
         E.error(" - cant read color " + s);

      }
      return ret;
   }


   // following two same;
   public static String serializeColor(Color c) {
      String ret = null;
      if (c == null) {
         ret = "#000000";
      } else {
         int rgb = c.getRGB();
         // to HexString leaves off leading zeroes if it can;

         int xrgb = rgb | 0xff000000;

         String fullhex = Integer.toHexString(xrgb);
         ret = "#" + fullhex.substring(2, 8);
      }
      return ret;
   }


   public static String hexString(Color c) {
      return serializeColor(c);
      /*
       * following prints "ff0000" as "ff 0 0" String sr =
       * String.format("#%2x%2x%2x", c.getRed(), c.getGreen(), c.getBlue());
       * E.info("color to string got : " + sr); return sr;
       */
   }



}
