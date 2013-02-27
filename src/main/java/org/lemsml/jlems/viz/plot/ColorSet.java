package org.lemsml.jlems.viz.plot;


import java.awt.Color;



public class ColorSet {
   
   Color background;  // GETSET
   Color foreground;  // GETSET

   Color dataBackground;  // GETSET
   Color dataForeground;  // GETSET



   public ColorSet() {
      background = new Color(80, 80, 140);
      foreground = Color.white; // new Color(0, 0, 0);
      
      dataBackground = new Color(20, 20, 70); //  new Color(90, 90, 90);
      dataForeground = Color.white; // new Color(0, 0, 0);

   }
   
   
   public ColorSet copy() {
      ColorSet ret = new ColorSet();
      ret.background = background;
      ret.foreground = foreground;
      ret.dataBackground = dataBackground;
      ret.dataForeground = dataForeground;
      return ret;
   }
   
   

   public void setGray() {
      foreground = Color.black;
      background = new Color(0xe0e0e0);
   }

   
   public Color getBackground() {
      return background;
   }

   public Color getBg() {
      return background;
   }
   
   public void setBackground(Color background) {
      this.background = background;
   }

   
   public Color getDataBackground() {
      return dataBackground;
   }

   
   public void setDataBackground(Color dataBackground) {
      this.dataBackground = dataBackground;
   }

   
   public Color getDataForeground() {
      return dataForeground;
   }

   
   public void setDataForeground(Color dataForeground) {
      this.dataForeground = dataForeground;
   }

   
   public Color getForeground() {
      return foreground;
   }

   public Color getFg() {
      return foreground;
   }
   
   public void setForeground(Color foreground) {
      this.foreground = foreground;
   }

   public void setBg(Color color) {
     background = color;
      
   }

}
