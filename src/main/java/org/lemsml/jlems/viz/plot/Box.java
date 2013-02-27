
package org.lemsml.jlems.viz.plot;
 



public final class Box {


   double xmin;
   double xmax;
   double ymin;
   double ymax;

   double subdivDx;
   double subdivDy;


   private boolean noXdata;
   private boolean noYdata;

   public Box(Position p) {
      this(p.getX(), p.getY());
   }


   public Box(double x, double y) {
      xmin = x;
      xmax = x;
      ymin = y;
      ymax = y;
      noXdata = false;
      noYdata = false;
   }

   public Box() {
      noXdata = true;
      noYdata = true;
   }


   public Box(double x0, double y0, double x1, double y1) {
      xmin = x0;
      ymin = y0;
      xmax = x1;
      ymax = y1;
      noXdata = false;
      noYdata = false;
   }

   public Box makeCopy() {
      return new Box(xmin, ymin, xmax, ymax);
   }


   public String toString() {
      return " Box x0=" + xmin + " y0=" + ymin + " x1=" + xmax + " y1=" + xmax;
   }


   public void setXMin(double d) {
      xmin = d;
   }
   public void setXMax(double d) {
      xmax = d;
   }

   public void setYMin(double d) {
      ymin = d;
   }

   public void setYMax(double d) {
      ymax = d;
   }

   public double getXmin() {
      return xmin;
   }

   public double getXmax() {
      return xmax;
   }


   public double getYmin() {
      return ymin;
   }

   public double getYmax() {
      return ymax;
   }


   public void subdivide(int n) {
      subdivDx = (xmax - xmin) / n;
      subdivDy = (ymax - ymin) / n;
   }


   public int getXSubdivision(double x) {
      int ret = (int)((x - xmin) / subdivDx);
      return ret;
   }


   public int getYSubdivision(double y) {
      int ret = (int)((y - ymin) / subdivDy);
      return ret;
   }



   public int getXSubdivision(double x, int n) {
      double dx = (xmax - xmin) / n;
      int ret = (int)((x - xmin) / dx);
      return ret;
   }


   public int getYSubdivision(double y, int n) {
      double dy = (ymax - ymin) / n;
      int ret = (int)((y - ymin) / dy);
      return ret;
   }


   public void extendTo(Position p) {
      extendTo(p.getX(), p.getY());
   }


   public void extendTo(Box b) {
         extendTo(b.xmin, b.ymin);
         extendTo(b.xmax, b.ymax);
   }


   public void extendTo(double[] xp, double[] yp) {
      for (int i = 0; i < xp.length; i++) {
         extendTo(xp[i], yp[i]);
      }
   }


   public boolean hasData() {
      return (! (noXdata || noYdata));
   }


   public void extendXTo(double x) {
      if (noXdata) {
       xmin = x;
       xmax = x;
       noXdata = false;
        }

      if (x < xmin) {
       xmin = x;
    }
    if (x > xmax) {
       xmax = x;
    }
   }


   public void extendTo(double x, double y) {
     extendXTo(x);
     extendYTo(y);
   }

   public void extendYTo(double y) {
      if (noYdata) {
	 ymin = y;
	 ymax = y;
    noYdata = false;
      }

	 if (y < ymin) {
	    ymin = y;
	 }

	 if (y > ymax) {
	    ymax = y;
	 }

   }


   public void pad() {
      enlarge(0.1);
   }

   public void enlarge(double f) {

      double dx = f * (xmax - xmin);
      double dy = f * (ymax - ymin);

      xmin -= dx;
      xmax += dx;

      ymin -= dy;
      ymax += dy;
   }


   public void push(double x, double y) {
	   extendTo(x, y);
   }

   public void push(double[] x, double[] y) {
      pushX(x, x.length);
      pushY(y, y.length);
   }

   public void pushX(double[] v) {
	   pushX(v, v.length);
   }

   public void pushX(double[] v, int np) {
      if (np > 0 && noXdata) {
        xmin = v[0];
        xmax = v[0];
         noXdata = false;
      }

      for (int i = 0; i < np; i++) {
         double d = v[i];
         if (xmin > d) {
            xmin = d;
         }
         if (xmax < d) {
            xmax = d;
         }
      }
   }

   public void pushY(double d) {
	   if (noYdata) {
		   ymin = d;
		    ymax = d;
	   } else {
		   if (ymin > d) {
	            ymin = d;
	         }
	         if (ymax < d) {
	            ymax = d;
	         }
	   }
   }

   public void pushY(double[] d) {
	   pushY(d, d.length);
   }

   public void pushY(double[] v, int np) {
         if (np > 0 && noYdata) {
           ymin = v[0];
           ymax = v[0];
            noYdata = false;
         }

      for (int i = 0; i < np; i++) {
         double d = v[i];
         if (ymin > d) {
            ymin = d;
         }
         if (ymax < d) {
            ymax = d;
         }
      }
   }


   public void push(Position position) {
      extendTo(position.getX(), position.getY());
   }


   public boolean differentFrom(Box b, double d) {
      boolean ret = false;

      if (hasData() && b.hasData()) {
         if (rangesDiffer(xmin, xmax, b.xmin, b.xmax, d) ||
             rangesDiffer(ymin, ymax, b.ymin, b.ymax, d)) {
            ret = true;
         }
      }
      return ret;
   }


   private boolean rangesDiffer(double a, double b, double c, double d, double delta) {
      boolean ret = false;
      double u = 0.5 * (b - a + d - c);
      double f1 = (c - a) / u;
      double f2 = (d - b) / u;
      ret = (Math.abs(f1) > delta || Math.abs(f2) > delta);

      if (ret) {
    //     E.info("ranges differ " + a + " " + b + " " + c + " " + d);
      }
      return ret;
   }


   public Position getCenter() {
       double cx = 0.5 * (xmin + xmax);
       double cy = 0.5 * (ymin + ymax);
       return new Position(cx, cy);
   }


   public double getRadius() {
      double dx = xmax - xmin;
      double dy = ymax - ymin;
      double scl = 0.5 * Math.max(dx, dy);
      return scl;
   }


   public void tidyLimits() {
       if (xmin > 0. && (xmax - xmin) / xmin > 5.) {
          xmin = 0.;
       } else if (xmin < 0. && (xmax - xmin) / -xmin > 20.) {
          xmin = 0.;
       }

       if (ymin > 0. && (ymax - ymin) / ymin > 5.) {
          ymin = 0.;
       } else if (ymin < 0. && (ymax - ymin) / -ymin > 20.) {
          ymin = 0.;
       }
   }


public void push(double[] xpts, double[] ypts, int npts) {
	pushX(xpts, npts);
	pushY(ypts, npts);
	
}



}
