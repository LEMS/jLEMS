package org.lemsml.jlems.viz.plot;


public final class IntPosition {

   int x;
   int y;


   public IntPosition() {
      this(0, 0);
   }

   public IntPosition(IntPosition p) {
      this(p.getX(), p.getY());
   }


   public IntPosition(int px, int py) {
      set(px, py);
   }


   public void set(IntPosition p) {
      set(p.getX(), p.getY());
   }


   public void set(int px, int py) {
      x = px;
      y = py;
   }

   public void shift(int dx, int dy) {
      x += dx;
      y += dy;
   }

   
   public int getX() {
      return x;
   }


   public int getY() {
      return y;
   }

   public void subtract(IntPosition sm) {
      x -= sm.getX();
      y -= sm.getY();
      
   }


}
