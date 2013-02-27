package org.lemsml.jlems.viz.plot;

import org.lemsml.jlems.core.logging.E;
 

public final class Position implements XYLocation {

   double x;
   double y;
   
   boolean valid;

   public Position() {
      this(999.9, 999.9);
      valid = false;
   }

   public Position(XYLocation p) {
      set(p.getX(), p.getY());
   }


   public Position(double px, double py) {
      set(px, py);
   }


     public  String toString() {
          return "position(" + x + ", " + y + ")";
    }


   public void set(XYLocation p) {
      set(p.getX(), p.getY());
   }

   public void add(Position p) {
      reportInValid();
      x += p.getX();
      y += p.getY();
   }

   public void subtract(Position p) {
      reportInValid();
      x -= p.getX();
      y -= p.getY();
   }


   public void absolutize(Position porig, double scale, Position prel) {
      // makes thiis position absolute given relative position prel to
      // origin porig at scale scale.
      set(porig.getX() + scale * prel.getX(),
	  porig.getY() + scale * prel.getY());

   }


   public void relativize(Position porig, double scale, Position pabs) {
      // makes this positon relative given two absolute points
      set((pabs.getX() - porig.getX()) / scale,
	  (pabs.getY() - porig.getY()) / scale);
   }


private void reportInValid() {
   if (valid) {

   } else {
      E.warning("using a default position? " + x + " " + y);
   }
}


   public void set(double px, double py) {
      x = px;
      y = py;
      valid = true;
   }


   public double getX() {
      reportInValid();
      return x;
   }


   public double getY() {
      reportInValid();
      return y;
   }



   public static Position aXPlusBY(double a, Position v, double b, Position w) {
      Position ret = new Position(a * v.getX() + b * w.getX(),
				  a * v.getY() + b * w.getY());
      return ret;
   }


   public void shift(double dx, double dy) {
      x += dx;
      y += dy;
   }

   public void shift(Position spos) {
      x += spos.getX();
      y += spos.getY();
   }
   
   
   public boolean isValid() {
      return valid;
   }

   public Position copy() {
      return new Position(this);
   }

   public double distanceFrom(Position p) {
      double dx = p.getX() - x;
      double dy = p.getY() - y;
      return Math.sqrt(dx*dx + dy*dy);
   }

   public double distanceFromOrigin() { 
      return Math.sqrt(x*x + y*y);
   }
   
   
   
   public Position getRelativeToBoxCenter(double[] xyxy) {
      double cx = (xyxy[2] + xyxy[0]) / 2;
      double dx = (xyxy[2] - xyxy[0]) / 2;
      double cy = (xyxy[3] + xyxy[1]) / 2;
      double dy = (xyxy[3] - xyxy[1]) / 2;

      return new Position((x - cx) / dx, (y - cy) / dy);
   }

   public void setX(double d) {
      x = d;
   }
   
   public void setY(double d) {
      y = d;
   }

   public static Position midpoint(Position pa, Position pb) {
         return new Position(0.5 * (pa.x + pb.x), 0.5*(pa.y + pb.y));
   }

   
   public void rotateBy(double rad) {
      double c = Math.cos(rad);
      double s = Math.sin(rad);
      rotateCosSin(c, s);
   }
   
   private void rotateCosSin(double c, double s) {
      double xr = c * x - s * y;
      double yr = s * x + c * y;
      x = xr;
      y = yr;
   }
   
   
   public void rotateTo(Direction dir) {
         double c = dir.getCosine();
         double s = dir.getSine();
         rotateCosSin(c, s);
   }

   
   public void rotateAbout(Position pcen, double rad) {
      double c = Math.cos(rad);
      double s = Math.sin(rad);
      double cx = pcen.getX();
      double cy = pcen.getY();
      double dx = x - cx;
      double dy = y - cy;
      x = cx + c * dx - s * dy;
      y = cy + s * dx + c * dy;
   }

    
}
