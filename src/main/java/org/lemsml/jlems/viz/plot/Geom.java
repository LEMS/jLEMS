package org.lemsml.jlems.viz.plot;
 

public final class Geom {


	private Geom() {
		
	}

   public static boolean pointIsInside(double[] xb, double[] yb, int x, int y) {
      return pointIsInside(xb, yb, (double)x, (double)y);
   }


   public static boolean pointIsInside(double[] xb, double[] yb, double x, double y) {
      int n = xb.length;
      int iwn = 0;
      for (int i = 0; i < n; i++) {
         int idir = 0;
         int p = (i + 1) % n;
         if (yb[i] <= y && yb[p] > y) {
            idir = 1;
         }
         
         if (yb[i] > y && yb[p] <= y) {
            idir = -1;
         }
         
         if (idir != 0) {
            double f = (y - yb[i]) / (yb[p] - yb[i]);
            double xc = f * xb[p] + (1. - f) * xb[i];
            int isid = (xc > x ? 1 : -1);
            iwn += isid * idir;
         }

      }
      return (iwn != 0);
   }



   public static double[][] makeLineBoundary(double[] xpts, double[] ypts) {
      int np = xpts.length;
      double[][] ret = new double[2][4 + 3 * (np - 2)];
      double[] xret = ret[0];
      double[] yret = ret[1];

      int nr = xret.length - 1;

     

      int ic = 0;
      int iac = 0;

      double f = 0.15;

      for (int i = 0; i < np; i++) {
         int ifa = i - 1;
         if (ifa < 0) {
            ifa = 0;
         }
         int ifb = i + 1;
         if (ifb >= np - 1) {
            ifb = np - 1;
         }

         double dxa = xpts[ifa] - xpts[ifa + 1];
         double dya = ypts[ifa] - ypts[ifa + 1];
         double dxb = xpts[ifb] - xpts[ifb - 1];
         double dyb = ypts[ifb] - ypts[ifb - 1];

         double la = Math.sqrt(dxa * dxa + dya * dya) + 1.e-8;
         double lb = Math.sqrt(dxb * dxb + dyb * dyb) + 2.e-8; // ADHOC

         double vxa = dxa / la;
         double vya = dya / la;
         double vxb = dxb / lb;
         double vyb = dyb / lb;

         double cosTheta = vxa * vxb + vya * vyb;
         double sinHalfTheta = Math.sqrt((1. - cosTheta) / 2.);

         double vecp = vxa * vyb - vxb * vya;
         
         double vxo = vya - vyb;
         double vyo = -vxa + vxb;
         double lo = Math.sqrt(vxo * vxo + vyo * vyo);
         vxo /= lo;
         vyo /= lo;

         double fe = 1 / (sinHalfTheta);

         fe *= f;

         double xc = xpts[i];
         double yc = ypts[i];
         
         if (i == 0 || i == np - 1) {
            xret[ic] = xc +  fe * vxo;
            yret[ic] = yc + fe * vyo;
            ic += 1;

            xret[nr - iac] = xc - fe * vxo;
            yret[nr - iac]  = yc - fe * vyo;
            iac += 1;

         } else if (vecp < 0) {
            xret[ic] = xc + fe * vxo;
            yret[ic] = yc + fe * vyo;
            ic += 1;

            xret[nr - iac] =xc - f * vya;
            yret[nr - iac] = yc + f * vxa;
            iac += 1;

            xret[nr - iac] = xc + f * vyb;
            yret[nr - iac] = yc - f * vxb;
            iac += 1;

         } else {
            xret[ic] = xc + f * vya;
            yret[ic] = yc - f * vxa;
            ic += 1;

            xret[ic] = xc - f * vyb;
            yret[ic] = yc + f * vxb;
            ic += 1;

            xret[nr - iac] = xc - fe * vxo;
            yret[nr - iac] = yc - fe * vyo;
            iac += 1;

         }

      }

      return ret;
   }


   public static Position centerOfGravity(double[] xpts, double[] ypts) {
      int np = xpts.length;
      double xc = 0.;
      double yc = 0;
      for (int i = 0; i < np; i++) {
         xc += xpts[i];
         yc += ypts[i];
      }
      xc /= np;
      yc /= np;
      return new Position(xc, yc);
   }


   public static double distanceBetween(Position a, Position b) {
       double dx = b.getX() - a.getX();
       double dy = b.getY() - a.getY();
       double ret = Math.sqrt(dx*dx + dy*dy);
       return ret;
   }


   
   // TODO - smarter (eg pickable region code)
   public static double[][] innerPolygon(double[] x, double[] y) {
      int n = x.length;
      double[][] ret = new double[2][n];
      double cx = 0.;
      double cy = 0.;
      for (int i = 0; i < n; i++) {
         cx += x[i];
         cy += y[i];
      }
      cx /= n;
      cy /= n;
      for (int i = 0; i < n; i++) {
         ret[0][i] = cx + 0.9 * (x[i] - cx);
         ret[1][i] = cy + 0.9 * (y[i] - cy);
      }
      return ret;
   }


}
