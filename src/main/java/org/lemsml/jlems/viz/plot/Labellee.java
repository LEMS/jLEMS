package org.lemsml.jlems.viz.plot;

import java.awt.Color;


public final class Labellee {

   double[] xpts;
   double[] ypts;
   String label;
   Color color;


   double xlab;
   double ylab;



   public Labellee(double[] xp, double[] yp, String s, Color c) {
      update(xp, yp, s, c);
   }


   public void update(double[] xp, double[] yp, String s, Color c) {
      xpts = xp;
      ypts = yp;
      label = s;
      color = c;
   }


   public void setLabelPosition(double x, double y) {
      xlab = x;
      ylab = y;
   }


   public void instruct(Painter p) {
      p.drawLabel(label, xlab, ylab, color);
   }



}
