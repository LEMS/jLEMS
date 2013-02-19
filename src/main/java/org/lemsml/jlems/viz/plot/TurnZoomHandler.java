package org.lemsml.jlems.viz.plot;





public class TurnZoomHandler extends BaseMouseHandler {

   private int xc;
   private int yc;

   private int x0;
   private int y0;

   private int x1;
   private int y1;



   public void init(Mouse m) {
      xc = m.getX();
      yc = m.getY();

      x0 = xc;
      y0 = yc;

      x1 = xc;
      y1 = yc;
   }


   public void advance(Mouse m) {
      int x = m.getX();
      int y = m.getY();
      if (x <= 1 || x >= m.getCanvasWidth() ||
	  y <= 1 || y >= m.getCanvasHeight()) {
	 setClaimIn();
      }
   }




   public void applyOnDrag(Mouse m) {
      int x = m.getX();
      int y = m.getY();

      if ( (x-x1)*(x-x1) + (y-y1)*(y-y1) > 64) {

	 double a = (x1 - x0) * (y - y0) - (y1 - y0) * (x - x0);
	 int d2 = (x1-x0)*(x1-x0) + (y1-y0)*(y1-y0);

	 if (d2 > 32) {
	    a = a / d2;
	    double f = Math.exp (0.16 * a);

	    m.zoom (f, xc, yc);
	 }

	 x0 = x1;
	 x1 = x;

	 y0 = y1;
	 y1 = y;
      }
   }



}

