

package org.lemsml.jlems.viz.plot;
 

import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import org.lemsml.jlems.core.logging.E;
 

public class YAxisCanvas extends BaseCanvas
   implements RangeListener {
   static final long serialVersionUID = 1001;

   double ylow;
   double yhigh;

   int ntick = 5;

   String labY;

   final static int[] intervals = {1, 2, 5};

   AffineTransform upTransform;
   AffineTransform normalTransform;


   public YAxisCanvas() {
      super();

      upTransform = new AffineTransform();
      upTransform.setToRotation(-1 * Math.PI/2.);
      normalTransform = new AffineTransform();
   }


   public void setLabel(String s) {
      labY = s;
   }


   public void rangeChanged(double[] xyxylims) {
     
	 ylow = xyxylims[1];
	 yhigh = xyxylims[3];
	 repaint();
 
   }


   public void paint2D(Graphics2D g) {
      drawAxis(g);
   }




   public final void drawAxis(Graphics2D g) {
      FontMetrics fm = g.getFontMetrics();


      g.setColor (getNormalForeground());

      int width = getWidth();
      int height = getHeight();

      double yran = Math.abs(yhigh - ylow);
      double dy = 1.5 * yran  / ntick;

      double log = Math.log (dy) / Math.log(10.);
      double powten = (int) Math.floor(log);
      int iiind = (int) (2.999 * (log - powten));
      if (iiind < 0 || iiind >= 3) {
	E.error ("error gdc, 650: " + log + " " + powten +  " " + iiind);
	 iiind = 2;
      }
      int ii = intervals[iiind];
      dy = Math.pow(10.0, powten) * ii;

      int i0 = (int)(ylow / dy);
      int i1 = (int)(yhigh / dy);

      for (int i = i0; i <= i1; i++) {
	 double yy = i * dy;
	 String lab = "0";
	 if (i == 0) {
	    // OK;

	 } else if (dy >= 0.999 && dy < 1.e4) {
	    lab = String.valueOf((int)(yy));
	 } else {
	    lab = String.valueOf((float)(yy));
	 }

	 int iy = height - (int) (height * (yy - ylow) / (yhigh - ylow));

	 int off = fm.stringWidth(lab);

	 g.drawString(lab, width - 12 - off, iy + 4);
	 g.drawLine (width - 5, iy, width, iy);

	 if (labY != null) {
	    int ilx = 18;
	    int ily = height / 2 + fm.stringWidth(labY) / 2;


	    AffineTransform at = g.getTransform();

	    g.translate((double)ilx, (double)ily);
	    g.rotate(-1 * Math.PI / 2);

	    g.drawString(labY, 0, 0);



	    g.setTransform(at);
	 }
      }
   }






}
