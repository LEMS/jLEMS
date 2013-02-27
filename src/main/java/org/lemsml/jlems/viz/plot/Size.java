package org.lemsml.jlems.viz.plot;


public final class Size {

   double w;
   double h;


   public Size() {
      this(0., 0.);
   }


   public Size(double px, double py) {
      set(px, py);
   }


   public void set(double px, double py) {
      w = px;
      h = py;
   }

   
   public double getWidth() {
      return w;
   }


   public double getHeight() {
      return h;
   }


}
