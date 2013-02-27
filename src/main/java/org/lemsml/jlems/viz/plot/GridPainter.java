package org.lemsml.jlems.viz.plot;

 

import java.awt.Color;

import org.lemsml.jlems.core.logging.E;
 

public class GridPainter {

   final static int[] intervals = {1, 2, 5};


   Color gridColor;
   Color axisColor;

   private boolean axesOnGrid = false;

   String xlabel = null;


   public void setGridColor(Color c) {
	   gridColor = c;
   }
   public void setAxisColor(Color c) {
	   axisColor = c;
   }

   public void setGridBackground(Color c) {
      int ic = c.getRed() + c.getGreen() + c.getBlue();
      if (ic > 380) {
	 gridColor = ColorUtil.verySlightlyDarker(c);
      } else {
	 gridColor = ColorUtil.verySlightlyBrighter(c);
      }
   }



   public void paint(Painter p) {
      double[] xyxy = p.getXYXYLimits();

      if (gridColor == null) {
    	  setGridBackground(Color.gray);
      }

      int width = p.getCanvasWidth();
      int height = p.getCanvasHeight();

      double dx = xyxy[2] - xyxy[0];
      double dy = xyxy[3] - xyxy[1];

      double[] xpts = getTickValues(6, xyxy[0], xyxy[2]);
      int[] ixpts = getTicks(xpts, xyxy[0], xyxy[2], width);

      double[] ypts = getTickValues(6, xyxy[1], xyxy[3]);
      int[] iypts = getTicks(ypts, xyxy[1], xyxy[3], height);


      p.setColor(gridColor);

      for (int i = 0; i < ixpts.length; i++) {
	       p.drawPixelLine(ixpts[i], 0, ixpts[i], height);
	     }

      for (int i = 0; i < iypts.length; i++) {
	        p.drawPixelLine(0, height - iypts[i], width, height - iypts[i]);
      }

      if (axesOnGrid) {

    	 if (axisColor != null) {
    		 p.setColor(axisColor);
    	 } else {
    		 p.setColor(gridColor);
    	 }


         for (int i = 0; i < ixpts.length; i++) {
            p.drawCenteredString(Formatter.format(xpts[i], dx), ixpts[i], height-10);
         }
          for (int i = 0; i < iypts.length; i++) {
            p.drawString(Formatter.format(ypts[i], dy), 10, height-iypts[i]+4);
         }

          if (xlabel != null) {
             p.drawString(xlabel, width/2, height-2);
          }

      }


   }




   public final static double[] getTickValues(int ntick, double vlow, double vhigh) {

      double dv = 1.5 *  Math.abs(vhigh - vlow) / ntick;

      double log = Math.log (dv) / Math.log(10.);
      double powten = (int) Math.floor(log);
      int iiind = (int) (2.999 * (log - powten));
      if (iiind < 0 || iiind >= 3) {
        E.error (" gdc, 650: " + log + " " + powten +  " " + iiind);
         iiind = 2;
      }
      int ii = intervals[iiind];
      dv = Math.pow(10.0, powten) * ii;

      int i0 = (int)(vlow / dv);
      int i1 = (int)(vhigh / dv);

      int nl = i1 - i0 + 1;

      double[] ret = new double[nl];
      for (int i = 0; i < nl; i++) {

        ret[i] = (i0 + i) * dv;
      }
      return ret;
   }



   public final static int[] getTicks(double[] va,
            double vlow, double vhigh, int range) {
      int nl = va.length;
      int[] ret = new int[nl];

      for (int i = 0; i < nl; i++) {

	         ret[i] = (int) (range * (va[i] - vlow) / (vhigh - vlow));
      }
      return ret;
   }



   public void setOnGridAxes() {
      axesOnGrid = true;
    }



   public void setXAxisLabel(String s) {
      xlabel = s;

   }


}
