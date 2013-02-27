package org.lemsml.jlems.viz.plot;

import org.lemsml.jlems.core.logging.E;
 

public class Direction {

   double vx;
   double vy;
   
   double cosine;
   double sine;
   
   
   public Direction(Direction d) {
     this(d.getXCpt(), d.getYCpt());
   }
   
   
   
   public Direction(double a, double b) {
      vx = a; 
      vy = b;
      double d = Math.sqrt(vx * vx + vy * vy);
      if (d == 0.) {
         d = 1.;
         vx = 1;
         E.warning("created direction with zero vector");
      }
      cosine = vx / d;
      sine = vy / d;
   }
   
   
   public double getCosine() {
        return cosine;
   }
   
   public double getSine() {
        return sine;
   }
   
    
   public double getXCpt() {
      return vx;
   }
   
   
   public double getYCpt() {
      return vy;
   }


   public Position destination(double d) {
      return new Position(d * cosine, d* sine);
   }
   
   
   public static Direction fromTo(Position pa, Position pb) {
      return new Direction(pb.getX() - pa.getX(), pb.getY() - pa.getY());
   }
   
}
