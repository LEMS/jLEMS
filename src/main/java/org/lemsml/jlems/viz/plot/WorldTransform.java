package org.lemsml.jlems.viz.plot;

import java.util.ArrayList;

import org.lemsml.jlems.core.logging.E;
 


public final class WorldTransform {

   private double wcx;
   private double wcy;

   private int pcx;
   private int pcy;

   private int canvasWidth;
   private int canvasHeight;

   private int dataAreaWidth;
   private int dataAreaHeight;

   private double dpdwx;
   private double dpdwy;


   private int leftMargin = 0;
   private int rightMargin = 0;
   private int topMargin = 0;
   private int bottomMargin = 0;


   private boolean xRescalable;
   private boolean yRescalable;


   private final static int IBIG = 20000;
   private final static double SMALL = 1.e-7;
   private final static double DBIG = 1.e9;

   private boolean recordRange;


   private double aspectRatio;
   private boolean constantAspectRatio;


   // following used for each paint to record limits;
   private double vxmin;
   private double vxmax;
   private double vymin;
   private double vymax;
   // private boolean drawnInXRange;
   // private boolean drawnInYRange;



   // this is inelegant;
   private boolean trialPanning = false;
   private double wcxtp;
   private double wcytp;




   // for three D rotations
 //  private double wcz = 0.;
   private double w2cx, w2cy, w2cz;
   private double w3cx, w3cy, w3cz;
   private double m3xx = 1., m3yy = 1., m3zz = 1.;
   private double m3xy = 0., m3xz = 0., m3yx = 0., m3yz = 0., m3zx = 0., m3zy = 0.;
   private double[][] m3B; // temp array container of above

   // for continuous zoom
   private double zoomCenX, zoomCenY, dpdwx0, dpdwy0, wcx0, wcy0;

   private ArrayList<RangeListener> rangeListeners = new ArrayList<RangeListener>();

   private RotationListener rotationListener;

   private final Size p_pixelSize;


   public WorldTransform() {
      setWidth(100);
      setHeight(100);
      setXRescalable(true);
      setYRescalable(true);

      setAspectRatioFree();

      dpdwx = 1.;
      dpdwy = 1.;

      wcx = 0.;
      wcy = 0.;

   
      p_pixelSize = new Size(0., 0.);
   }


   public void setCanvasSize(int w, int h) {
      setWidth(w);
      setHeight(h);
      System.out.println("SCS " + w + " " + h);
   }

   public int getWidth() {
	   return canvasWidth;
   }

   public int getHeight() {
	   return canvasHeight;
   }

   public int getLeftMargin() {
	   return leftMargin;
   }

   public int getBottomMargin() {
	   return bottomMargin;
   }

   public void setMargins(int l, int r, int b, int t) {
      leftMargin = l;
      rightMargin = r;
      topMargin = t;
      bottomMargin = b;
   }


   public boolean isOnCanvas(double x, double y) {
      return intIsOnCanvas(powx(x), powy(y));
   }


   public boolean intIsOnCanvas(int x, int y) {
      return (x > -10 && x < canvasWidth + 10 && y > -10 && y < canvasHeight + 10);
   }



   public void addRangeListener(RangeListener rl) {
	  rangeListeners.add(rl);
      rl.rangeChanged(getXYXYLimits());
   }


	public void setRotationListener(RotationListener rl) {
   		rotationListener = rl;
   	}



   public void fixRanges() {
      notifyRangeChange();
   }

   public void rangeChange() {
      notifyRangeChange();
   }

 

   public void notifyRangeChange() {
      double[] lims = getXYXYLimits();
      for (RangeListener rl : rangeListeners) {
         rl.rangeChanged(lims);
      }
     //  System.out.println("notified rc " +  lims[0] + " " + lims[2]);
   }


   public void setPixelScalingFromTop(double d) {
      pSetXRange(0., d * getCanvasWidth());
      pSetYRange(-1. * d * getCanvasHeight(), 0.);
      notifyRangeChange();
   }



   public void setAspectRatioFree() {
      constantAspectRatio = false;
   }


   public void setFixedAspectRatio(double f) {
      setAspectRatio(f);
   }


   public void setAspectRatio(double f) {
      aspectRatio = f;
      constantAspectRatio = true;
   }


   public void setXRescalable(boolean b) {
      xRescalable = b;
   }


   public void setYRescalable(boolean b) {
      yRescalable = b;
   }


   void clearRanges() {
      vxmin = DBIG;
      vymin = DBIG;
      vxmax = -1 * DBIG;
      vymax = -1 * DBIG;
      // drawnInXRange = false;
      // drawnInYRange = false;
   }


   void startRangeRecording() {
      clearRanges();
      recordRange = true;
   }


   void stopRangeRecording() {
      recordRange = false;
   }


   public void setWidth(int w) {
	   if (canvasWidth != w) {
		   canvasWidth = w;
		   dataAreaWidth = (canvasWidth - leftMargin - rightMargin) / 2;
		   if (dataAreaWidth < 2) {
			   dataAreaWidth = 2;
		   }
		   pcx = leftMargin + dataAreaWidth;
		   notifyRangeChange();
	   }
   }



   public void setHeight(int h) {
	   if (canvasHeight != h) {
 		   
		   canvasHeight = h;
		   dataAreaHeight = (canvasHeight - topMargin - bottomMargin) / 2;
		   if (dataAreaHeight < 2) {
			   dataAreaHeight = 2;
		   }
		   pcy = bottomMargin + dataAreaHeight;

		   
		   notifyRangeChange();
	   }
   }


   public int getCanvasWidth() {
      return canvasWidth;
   }


   public int getCanvasHeight() {
      return canvasHeight;
   }


   public double getWorldCanvasWidth() {
      return canvasWidth / dpdwx;
   }



   public boolean isShowing(double x, double y) {
      int ix = powx(x);
      int iy = powy(y);
      return (ix > 5 && iy > 5 && ix < canvasWidth - 50 && iy < canvasHeight - 5); // ADHOC
   }


   int[] intDeviceX(double[] wx) {
      int n = wx.length;
      int[] idev = new int[n];
      if (recordRange) {
         for (int i = 0; i < n; i++) {
            idev[i] = qpowx(wx[i]);
         }
      } else {
         for (int i = 0; i < n; i++) {
            idev[i] = powx(wx[i]);
         }

      }
      return idev;
   }


   int[] intDeviceY(double[] wy) {
      int n = wy.length;
      int[] idev = new int[n];
      if (recordRange) {
         for (int i = 0; i < n; i++) {
            idev[i] = qpowy(wy[i]);
         }
      } else {
         for (int i = 0; i < n; i++) {
            idev[i] = powy(wy[i]);
         }

      }
      return idev;
   }



   float[] floatDeviceX(double[] wx) {
      int n = wx.length;
      float[] fdev = new float[n];
      if (recordRange) {
         for (int i = 0; i < n; i++) {
            fdev[i] = qpowx(wx[i]);
         }
      } else {
         for (int i = 0; i < n; i++) {
            fdev[i] = powx(wx[i]);
         }

      }
      return fdev;
   }


   float[] floatDeviceY(double[] wy) {
      int n = wy.length;
      float[] fdev = new float[n];
      if (recordRange) {
         for (int i = 0; i < n; i++) {
            fdev[i] = qpowy(wy[i]);
         }
      } else {
         for (int i = 0; i < n; i++) {
            fdev[i] = powy(wy[i]);
         }

      }
      return fdev;
   }


   public Size getPixelSize() {
      p_pixelSize.set(1. / dpdwx, 1. / dpdwy);
      return p_pixelSize;
   }

   public double getPixelArea() {
	   return 1./ dpdwx * 1. / dpdwy;
   }


   protected double wopx(int x) {
      return wcx + (x - pcx) / dpdwx;
   }


   protected double wopy(int y) {
      return wcy + (canvasHeight - y - pcy) / dpdwy;
   }


   
   protected int xpix(double xr) {
	   return (int)(Math.round(dpdwx * xr));
   }

   protected int ypix(double yr) {
	   return (int)(Math.round(dpdwy * yr));
   }
   

   protected int powx(double xr) {
      double f = dpdwx * (xr - wcx);
      if (f > IBIG) {
         f = IBIG;
      }
      if (f < -IBIG) {
         f = -IBIG;
      }

      int ii = (pcx + (int)f);

      if (recordRange) {
         if (xr > vxmax) {
            vxmax = xr;
         }
         if (xr < vxmin) {
            vxmin = xr;
         }
      }
      if (ii > 0 && ii < canvasWidth) {
         // drawnInXRange = true;
      }
      return ii;
   }


   protected int powy(double yr) {
      double f = dpdwy * (yr - wcy);
      if (f > IBIG) {
         f = IBIG;
      }
      if (f < -IBIG) {
         f = -IBIG;
      }

      int ii = (canvasHeight - (pcy + (int)f));

      if (recordRange) {
         if (yr > vymax) {
            vymax = yr;
         }
         if (yr < vymin) {
            vymin = yr;
         }

      }
      if (ii > 0 && ii < canvasHeight) {
         // drawnInYRange = true;
      }
      return ii;
   }


   
   protected float fpowx(double xr) {
	      float f = (float)(dpdwx * (xr - wcx));
	      return pcx + f;
   }
   
   protected float fpowy(double yr) {
	      float f = (float)(dpdwy * (yr - wcy));
	      float ret = (canvasHeight - (pcy + f));
	      return ret;
}
   
   private int qpowx(double xr) {
      return (int)(pcx + dpdwx * (xr - wcx));
   }


   private int qpowy(double yr) {
      return (int)(canvasHeight - (pcy + dpdwy * (yr - wcy)));
   }



   public int pubPowx(double xr) {
      return powx(xr);
   }


   public int pubPowy(double yr) {
      return powy(yr);
   }


   public Position getWorldPosition(int x, int y) {
      return new Position(wopx(x), wopy(y));
   }


   public double pubWopx(int x) {
      return wopx(x);
   }


   public double pubWopy(int y) {
      return wopy(y);
   }



   public int pubPixDx(double dxr) {
      return (int)(dpdwx * dxr);
   }


   public int pubPixDy(double dyr) {
      return (int)(dpdwy * dyr);
   }


   public double dPdX() {
      return dpdwx;
   }


   public double dPdY() {
      return dpdwy;
   }



   public double pubDyDpix() {
      return 1. / dpdwy;
   }


   public double pubDxDpix() {
      return 1. / dpdwx;
   }



   public double wxLeft() {
      return wcx - dataAreaWidth / dpdwx;
   }


   public double wxRight() {
      return wcx + dataAreaWidth / dpdwx;
   }


   public double wyBottom() {
      return wcy - dataAreaHeight / dpdwy;
   }


   public double wyTop() {
      return wcy + dataAreaHeight / dpdwy;
   }



   private void enforceAspectRatioY() {
      if (xRescalable) {
         dpdwx = 1. / aspectRatio * dpdwy;
      } else if (yRescalable) {
         dpdwy = aspectRatio * dpdwx;
      }
   }


   private void enforceAspectRatioX() {
      if (yRescalable) {
         dpdwy = aspectRatio * dpdwx;
      } else if (xRescalable) {
         dpdwx = 1. / aspectRatio * dpdwy;
      }
   }


   /*
    * public void shiftRanges (double twcx, double twcy) { // set wcx, wcy such
    * that the point that is at xdown, ydown in the; // twcx, twcy coordis is
    * now at x, y;
    *
    * double dx, dy; if (xRescalable) { dx = (ms.px - ms.px0) / dpdwx; wcx =
    * twcx - dx; } if (yRescalable) { dy = (ms.py - ms.py0) / dpdwy; wcy = twcy +
    * dy; } if (constantAspectRatio) enforceAspectRatioY(); recordLimits(); }
    */



   private void zoomAbout(double f, int xc, int yc) {
      zoomAbout(f, f, xc, yc);
   }


   private void zoomAbout(double fx, double fy, int xc, int yc) {
      xZoomAbout(fx, xc);
      yZoomAbout(fy, yc);
      if (constantAspectRatio) {
         enforceAspectRatioY();
      }
      rangeChange();
   }



   private void xZoomAbout(double f, int xc) {
      // NB, here, as everywhere, integer coordinates are measured from;
      // the TOP left corner, so small y is at the top of the window;
      double xWorld = wopx(xc);

      if (xc > leftMargin && xRescalable) {
         wcx = xWorld + f * (wcx - xWorld);
         dpdwx /= f;
         if (dpdwx > 1. / SMALL) {
            dpdwx = 1. / SMALL;
         }
      }
   }


   private void yZoomAbout(double f, int yc) {
      double yWorld = wopy(yc);

      if (yc < canvasHeight - bottomMargin && yRescalable) {
         wcy = yWorld + f * (wcy - yWorld);
         dpdwy /= f;
         if (dpdwy > 1. / SMALL) {
            dpdwy = 1. / SMALL;
         }
      }
   }


   void initializeZoom(int xc, int yc) {
	   zoomCenX = wopx(xc);
	   zoomCenY = wopy(yc);
	   dpdwx0 = dpdwx;
	   dpdwy0 = dpdwy;
	   wcx0 = wcx;
	   wcy0 = wcy;
   }


   void dragZoom(double fxin, double fyin, int xc, int yc) {
	   double fx = fxin;
	   double fy = fyin;
	   if (constantAspectRatio) {
		   fx = fy;
	   }

	   if (xc > leftMargin && xRescalable) {
	         wcx = zoomCenX + fx * (wcx0 - zoomCenX);
	         dpdwx = dpdwx0 / fx;
	         if (dpdwx > 1. / SMALL) {
	            dpdwx = 1. / SMALL;
	         }
	   }
	   if (yc < canvasHeight - bottomMargin && yRescalable) {
	         wcy = zoomCenY + fy * (wcy0 - zoomCenY);
	         dpdwy = dpdwy0 / fy;
	         if (dpdwy > 1. / SMALL) {
	            dpdwy = 1. / SMALL;
	         }
	   }
	   if (constantAspectRatio) {
	         enforceAspectRatioY();
	   }
	   notifyRangeChange();
   }


   /*
    *
    *
    * if (y > topMargin && x < width - rightMargin) {
    *
    *
    *  } if (y < topMargin) { tickGridx = tickGridx + (f < 1. ? -1 : 1); } if (x >
    * width - rightMargin) { tickGridy = tickGridy + (f < 1. ? -1 : 1); }
    *
    * enforceRangeConstraints(); recordLimits(); }
    *
    *
    *
    */



   public void reframe(Box b) {
      setXRange(b.getXmin(), b.getXmax());
      setYRange(b.getYmin(), b.getYmax());
      
      System.out.println("wt set ranges " + b.getXmin() + " " + b.getXmax());
      
      notifyRangeChange();
   }


   public void setXRange(double xl, double xh) {
      pSetXRange(xl, xh);
      notifyRangeChange();
   }


   public void setYRange(double yl, double yh) {
      pSetYRange(yl, yh);
      notifyRangeChange();
   }



   private void pSetXRange(double xlin, double xhin) {
	   double xl = xlin;
	   double xh = xhin;
      if (xh < xl) {
         double xt = xh;
         xh = xl;
         xl = xt;
      }

      if (xRescalable) {
         if (xh <= xl) {
            xh += 0.5;
            xl -= 0.5;
         }
         wcx = 0.5 * (xl + xh);
         if (xh <= xl + SMALL) {
            xh = xl + SMALL;
         }
         dpdwx = 2. * dataAreaWidth / (xh - xl);
      }


      if (constantAspectRatio) {
         enforceAspectRatioX();
      }
   }


   public void ensureCovers(double xl, double yl, double xh, double yh) {
      // set tighter constraint last; other first to get center
      if ((xh - xl) * dataAreaHeight >  (yh - yl) * dataAreaWidth) {
         pSetYRange(yl, yh);
         pSetXRange(xl, xh);

      } else {
         pSetXRange(xl, xh);
         pSetYRange(yl, yh);
      }
      notifyRangeChange();
   }


   private void pSetYRange(double ylin, double yhin) {
	   double yl = ylin;
	   double yh = yhin;
      if (yh < yl) {
         double yt = yh;
         yh = yl;
         yl = yt;
      }

      if (yRescalable) {
         if (yh <= yl) {
            yl -= 0.5;
            yh += 0.5;
         }
         wcy = 0.5 * (yl + yh);
         if (yh <= yl + SMALL) {
            yh = yl + SMALL;
         }
         dpdwy = 2. * dataAreaHeight / (yh - yl);
      }

      if (constantAspectRatio) {
         enforceAspectRatioY();
      }
      rangeChange();
   }



   public double[] getXYXYLimits() {
      double[] range = new double[4];
      range[0] = wxLeft();
      range[1] = wyBottom();
      range[2] = wxRight();
      range[3] = wyTop();
        
      return range;
   }


   public double[] getXRange() {
      double[] d = { wxLeft(), wxRight() };
      return d;
   }


   public double[] getYRange() {
      double[] d = { wyBottom(), wyTop() };
      return d;
   }


   public void setXYXYLimits(double xl, double yl, double xh, double yh) {
      if (constantAspectRatio) {
         // assume aspect ratio is 1 for now!!! ---------- TODO;
         if ((xh - xl) / (canvasWidth - leftMargin - rightMargin) > (yh - yl)
               / (canvasHeight - topMargin - bottomMargin)) {

            pSetYRange(yl, yh);
            pSetXRange(xl, xh);

         } else {
            pSetXRange(xl, xh);
            pSetYRange(yl, yh);
         }

      } else {
         pSetXRange(xl, xh);
         pSetYRange(yl, yh);
      }
      notifyRangeChange();
   }



   void applyRecordedRange() {
      double dx = 0.1 * (vxmax - vxmin);
      double dy = 0.1 * (vymax - vymin);

      double xa = vxmin - dx;
      double xb = vxmax + dx;
      double ya = vymin - dy;
      double yb = vymax + dy;

      if (vxmin > 1.e8 && vxmax < -1.e8) {
         setXYXYLimits(0., 0., 1., 1.);
      } else {
         setXYXYLimits(xa, ya, xb, yb);
      }
   }



   // default mouse coanvas ignores these - subclasses should
   // do something more useful
   public void boxSelected(int x0, int y0, int x1, int y1) {
      pSetXRange(wopx(x0), wopx(x1));
      pSetYRange(wopy(y0), wopy(y1));
      notifyRangeChange();
   }





   void zoom(double fac, int xc, int yc) {
      zoomAbout(fac, xc, yc);
   }


   void zoom(double xfac, double yfac, int xc, int yc) {
      zoomAbout(xfac, yfac, xc, yc);
   }


   void trialPan(int xfrom, int yfrom, int xto, int yto) {
      if (!trialPanning) {
         wcxtp = wcx;
         wcytp = wcy;
         trialPanning = true;
      }

      // not final - should be smarter here - just shift an image EFF;
      wcx = wcxtp - (wopx(xto) - wopx(xfrom));
      wcy = wcytp - (wopy(yto) - wopy(yfrom));
      rangeChange();

   }



   void permanentPan(int xfrom, int yfrom, int xto, int yto) {
      if (!trialPanning) {
         wcxtp = wcx;
         wcytp = wcy;
      }

      wcx = wcxtp - (wopx(xto) - wopx(xfrom));
      wcy = wcytp - (wopy(yto) - wopy(yfrom));


      trialPanning = false;
      rangeChange();
   }


   public int[] getIntPosition(double x, double y) {
     int[] ixy = {powx(x), powy(y)};
     return ixy;
   }




   // 3D transforms and mouse manipulation


   protected double xProj (double x, double y, double z) {
      return w2cx + m3xx * (x - w3cx) + m3xy * (y - w3cy) + m3xz * (z - w3cz);
   }

   protected double yProj (double x, double y, double z) {
      return w2cy + m3yx * (x - w3cx) + m3yy * (y - w3cy) + m3yz * (z - w3cz);
   }

   protected double zProj (double x, double y, double z) {
      return w2cz + m3zx * (x - w3cx) + m3zy * (y - w3cy) + m3zz * (z - w3cz);
   }


   public double[] project(double x, double y, double z) {
      double[] v = {xProj(x,y,z), yProj(x,y,z), zProj(x,y,z)};
      return v;
   }


   public double[] deProject (double x, double y, double z) {
      // deproject the point x, y, w3cz into 3D x,y,z
      double xu = x - w2cx;
      double yu = y - w2cy;
      double zu = z - w2cz;

      double[] v = new double[3];
      double det = (m3xx * (m3yy * m3zz - m3zy * m3yz) -
		    m3xy * (m3yx * m3zz - m3zx * m3yz) +
		    m3xz * (m3yx * m3zy - m3zx * m3yy));



     v[0] =  w3cx + ((m3xy * (m3yz * zu     - m3zz * yu     ) -
		     m3xz * (m3yy * zu     - m3zy * yu     ) +
		     xu    * (m3yy * m3zz  - m3zy * m3yz) )) / det;


     v[1] = w3cy + (-(m3xx * (m3yz * zu     - m3zz *  yu    ) -
		     m3xz * (m3yx * zu     - m3zx *  yu    ) +
		     xu    * (m3yx * m3zz  - m3zx * m3yz) )) / det;

     v[2] =  w3cz + ((m3xx *  (m3yy * zu     - m3zy * yu     ) -
		     m3xy *  (m3yx * zu     - m3zx * yu     ) +
		     xu     * (m3yx * m3zy  - m3zx * m3yy) )) / det;

     double wx = xProj(v[0], v[1], v[2]);
     double wy = yProj(v[0], v[1], v[2]);
     double wz = zProj(v[0], v[1], v[2]);

     if (Math.abs (det - 1.) > 0.001) {
    	 E.warning("rotation determinant != 1. " + det);
      }
      if (Math.abs (x - wx) + Math.abs (y - wy) + Math.abs (z - wz) > 0.001) {
    	  E.info("matrix projection error: ");
    	  E.info("original " + x + " " + y + " " + z);
    	  E.info("deproj   " + v[0] + " " + v[1] + " " + v[2]);
    	  E.info("reproj   " + wx + " " + wy + " " + wz);
      }
      return v;
   }


   public void initializeRotation (int ixcen, int iycen) {
      double x = wopx(ixcen);
      double y = wopy(iycen);
      initializeRotation(x, y, 0.);
   }

   public void initializeRotationLocal(double x, double y, double z) {
	   // x, y, z in the local coordinates (point on the cell)

	   initializeRotation(xProj(x, y, z), yProj(x, y, z), zProj(x, y, z));
   }


   public void initializeRotation(double x, double y, double z) {
      double[][] m3T = {{m3xx, m3xy, m3xz},
		        {m3yx, m3yy, m3yz},
		        {m3zx, m3zy, m3zz}};
      m3B = m3T;
      double[] v = deProject(x, y, z);

      w2cx = x;
      w2cy = y;
      w2cz = 0.;

      w3cx = v[0];
      w3cy = v[1];
      w3cz = v[2];
   }


   void applyRotation(double[][] mr) {
      double[][] m3C = new double[3][3];
      for (int i = 0; i < 3; i++) {
	 for (int j = 0; j < 3; j++) {
	    for (int k = 0; k < 3; k++) {
	       m3C[i][j] += mr[i][k] * m3B[k][j];
	    }
	 }
      }
      m3xx = m3C[0][0]; m3xy = m3C[0][1]; m3xz = m3C[0][2];
      m3yx = m3C[1][0]; m3yy = m3C[1][1]; m3yz = m3C[1][2];
      m3zx = m3C[2][0]; m3zy = m3C[2][1]; m3zz = m3C[2][2];

      if (rotationListener != null) {
    	  rotationListener.rotationChanged();
      }
   }


   void axisRotate (double thax, double thr) {
      // rotate through angle thr about the line in the x-y plane making
      // angle thax with the line y = 0;

      double cf = Math.cos (thax);
      double sf = Math.sin (thax);

      double cr = Math.cos (thr);
      double sr = Math.sin (thr);

      double[][] mr = { {(cf*cf + sf*cr*sf), (cf*sf - sf*cr*cf), (-sr*sf)},
                        {(sf*cf - cf*cr*sf), (sf*sf + cf*cr*cf),  (cf*sr)},
                        {(sr*sf), (-sr*cf), cr}};

      applyRotation(mr);
   }




   public void zRotate (double theta) {
      double cf = Math.cos (theta);
      double sf = Math.sin (theta);
      double[][] mr = { {cf,  sf, 0.},
		        {-sf, cf, 0.},
		        {0.,  0., 1.} };
      applyRotation(mr);
   }



   public void printRot() {
      E.info("rotmat: " + m3xx + " " + m3xy + " " + m3xz);
      E.info("        " + m3yx + " " + m3yy + " " + m3yz);
      E.info("        " + m3zx + " " + m3zy + " " + m3zz);
   }

   public void dragZRotate (int idx, int idy) {
      if (m3B != null) {
      double theta = idy / 60.; // ***
      zRotate (theta);
      }
   }


   public void dragRollRotate (int idx, int idy) {
      if (m3B != null) {

      double thax = Math.atan2(idx, idy);
      double thar =  Math.sqrt (idx*idx + idy*idy) / 60.; // ***
      axisRotate (thax, thar);
      }
   }


public boolean visible3D(double x, double y, double z) {
	// TODO Auto-generated method stub
	  	return (Math.abs (1.5 * dpdwx * (xProj(x,y,z) - wcx)) < canvasWidth &&
	            Math.abs (1.5 * dpdwy * (yProj(x,y,z) - wcy)) < canvasHeight);

}


public double[][] getProjectionMatrix() {
	 double[][] mat = {{m3xx, m3xy, m3xz}, {m3yx, m3yy, m3yz}, {m3zx, m3zy, m3zz}};
	 return mat;
}

public double[] get3Center() {
	double[] ret = {w3cx, w3cy, w3cz};
	return ret;
}

public double[] get2Center() {
	double[] ret = {w2cx, w2cy, w2cz};
	return ret;
}



public void setProjectionMatrix(double[][] pm) {
	m3xx = pm[0][0];
	m3xy = pm[0][1];
	m3xz = pm[0][2];
	m3yx = pm[1][0];
	m3yy = pm[1][1];
	m3yz = pm[1][2];
	m3zx = pm[2][0];
	m3zy = pm[2][1];
	m3zz = pm[2][2];
}



public void set3Center(double[] cen) {
	w3cx = cen[0];
	w3cy = cen[1];
	w3cz = cen[2];
}


public void set2Center(double[] cen) {
	w2cx = cen[0];
	w2cy = cen[1];
}


public void setScale(double sf) {
	dpdwx = sf;
	dpdwy = sf;
}

public double getScale() {
	return Math.sqrt(dpdwx * dpdwy);
}

}



/*
 *
 *
 *
 * public final double[] limits() { return getRange(); }
 *
 * public final double[] getLimits () { double[] da = getRange(); double[] ada =
 * new double[4]; for (int i = 0; i < 4; i++) ada[i] = Math.abs (da[i]); double
 * xm = (ada[1] > ada[0] ? ada[1] : ada[0]); double ym = (ada[3] > ada[2] ?
 * ada[3] : ada[2]); int px = (int) ((Math.log (xm) / - Math.log (Math.abs
 * (da[1] - da[0]))) / Math.log(10.)) + 3; int py = (int) ((Math.log (ym) / -
 * Math.log (Math.abs (da[3] - da[2]))) / Math.log(10.)) + 3; da[0] =
 * Formatter.trim (da[0], px); da[1] = Formatter.trim (da[1], px); da[2] =
 * Formatter.trim (da[2], py); da[3] = Formatter.trim (da[3], py); return da; }
 *
 *
 * public final double[] alims (double[] x, int n) { double a = x[0]; double b =
 * x[0]; for (int i = 0; i < n; i++) { if (x[i] < a) a = x[i]; if (x[i] > b) b =
 * x[i]; } if (a >= b) { a -= 0.5; b += 0.5; } double[] d = {a, b}; return d; }
 *
 * public final void setRangeToFrameData (double[] x, double[] y, int n) {
 * double[] dx = alims (x, n); double[] dy = alims (y, n); setLimits (dx[0],
 * dx[1], dy[0], dy[1]); }
 *
 *
 *  } }
 *
 *
 *
 *
 * public final void forceLimits (double xl, double xh, double yl, double yh) {
 * if (xl > xh) { double t = xl; xl = xh; xh = t; } if (yl > yh) { double t =
 * yl; yl = yh; yh = t; }
 *
 * boolean bxr = xRescalable; xRescalable = true; boolean byr = yRescalable;
 * yRescalable = true;
 *
 * setXrange (xl, xh); setYrange (yl, yh); xRescalable = bxr; yRescalable = byr;
 * if (constantAspectRatio) enforceAspectRatioY(); }
 *
 *
 * public final void setLimits (double[] dd) { if (dd == null || dd.length < 4)
 * return; setLimits (dd[0], dd[1], dd[2], dd[3]); } public final void
 * forceLimits (double[] dd) { if (dd == null || dd.length < 4) return;
 * forceLimits (dd[0], dd[1], dd[2], dd[3]); }
 *
 *  // NB following section contains only occurences of dpdwx
 *
 *
 *
 * public final void recordLimits () { double[] bufl = new double[4]; for (int i =
 * 0; i < 4; i++) bufl[i] = lastLimits[i];
 *
 * lastLimits[0] = wcx - hx / dpdwx; lastLimits[1] = wcx + hx / dpdwx;
 * lastLimits[2] = wcy - hy / dpdwy; lastLimits[3] = wcy + hy / dpdwy;
 *  }
 *
 *
 *
 * public final void gdcSetSize (int w, int h) { width = w; height = h; hx =
 * (width - leftMargin - rightMargin) / 2; hy = (height - topMargin -
 * bottomMargin) / 2; if (hx < 2) hx = 2; if (hy < 2) hy = 2;
 *
 * pcx = leftMargin + hx; pcy = bottomMargin + hy; }
 *
 *  }
 *
 */



