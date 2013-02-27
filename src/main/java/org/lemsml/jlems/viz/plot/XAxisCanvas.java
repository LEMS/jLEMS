


package org.lemsml.jlems.viz.plot;
 

import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;

import org.lemsml.jlems.core.logging.E;
 

public class XAxisCanvas extends BaseCanvas implements RangeListener {
   static final long serialVersionUID = 1001;
   
   double xlow;
   double xhigh;

   String labX;


   final static int[] intervals = {1, 2, 5};
   int ntick = 5;


   public XAxisCanvas() {
      super();
   }


   public void setLabel(String s) {
      labX = s;
   }


   public void rangeChanged(double[] xyxylims) {
	   xlow = xyxylims[0];
	   xhigh = xyxylims[2];
	   repaint();
   }


   public void paint2D(Graphics2D g) {
      drawAxis(g);
   }

   
   public final void drawAxis(Graphics g) {
      g.setColor(getNormalForeground());


      int width = getWidth();
      int height = getHeight();

      double xran = Math.abs(xhigh - xlow);
      double dx = 1.5 * xran  / ntick;

      double log = Math.log (dx) / Math.log(10.); 
      double powten = (int) Math.floor(log);
      int iiind = (int) (2.999 * (log - powten));
      if (iiind < 0 || iiind >= 3) {
	 E.error ("error gdc, 650: " + log + " " + powten +  " " + iiind);
	 iiind = 2;
      }
      int ii = intervals[iiind]; 
      dx = Math.pow(10.0, powten) * ii;

      int i0 = (int)(xlow / dx); 
      int i1 = (int)(xhigh / dx);

      for (int i = i0; i <= i1; i++) {
	 double xx = i * dx;
	 String lab = "0";
	 if (i == 0) {
	    // OK;
	    
	 } else if (dx >= 0.999 && dx < 1.e4) {
	    lab = String.valueOf((int)(xx));
	 } else {
	    lab = String.valueOf((float)(xx));
	 }
	 int off = lab.length();
	 off = 1 - 4 * off;
	 if (i*dx < 0.0) {
	    off -= 4;
	 }

	 int ix = (int) (width * (xx - xlow) / (xhigh - xlow));
	 
	 g.drawString(lab, ix+off, 20);
	 g.drawLine (ix, 0, ix, 5);
	    
	 if (labX != null) {
	    FontMetrics fm = g.getFontMetrics();
	    int ilx = width / 2 - fm.stringWidth(labX) / 2;
	    int ily = height - 3;
	    g.drawString(labX, ilx, ily);
	 }
      }
   }





}
