
package org.lemsml.jlems.viz.plot;
 


import java.awt.Color;

import org.lemsml.jlems.core.logging.E;



/*
lots of objects need a data type for color but shouldn't have a dependence on java.awt
So they use SColor instead (maybe should remove the awt dependence here too?)
This also serialises and deserialises nicely to standard #ffffff format.
*/


public class SColor { 

   public String string;

   private Color p_color;



   public SColor() {
      p_color = Color.red;
   }

   public SColor(String s) {
      string = s;
      p_color = parseColor(s);
   }


   public SColor(Color c) {
      p_color = c;
   }


   public String toString() {
      return serializeColor(p_color);
   }

   public void deReference() {
      string = serializeColor(p_color);
   }


   public void reReference() {
      p_color = parseColor(string);
   }



   public Color getColor() {
      return p_color;
   }



   public static Color parseColor(String sin) {
	   String s = sin;
      Color cret = null;
      s = s.trim();
      if (!s.startsWith("#")) {
         s = ColorNames.getHexValue(s);
      }

      if (s == null) {
         E.error("cant get color " + s);
      } else {
            try {
               int ic = Integer.decode(s).intValue();
               cret = new Color(ic);

            } catch (NumberFormatException ex) {
               E.error(" - cant decode color string " + s);
               cret = Color.red;
            }
      }
      return cret;
   }



   // NB duplicated with color util to avoide dependency
   public static String serializeColor(Color c) {
      int rgb = c.getRGB();
      // to HexString leaves off leading zeroes if it can;

      int xrgb  = rgb | 0xff000000;

      String fullhex = Integer.toHexString(xrgb);
      String ret = "#" + fullhex.substring(2, 8);

      return ret;
   }






}
