package org.lemsml.run;

import org.lemsml.util.E;
 
 

public final class SMatrix {



   public static double[][] transpose(double[][] a) {
      int nx = a.length;
      int ny = a[0].length;
      double[][] r = new double[ny][nx];
      for (int i = 0; i < nx; i++) {
	 for (int j = 0; j < ny; j++) {
	    r[j][i] = a[i][j];
	 }
      }
      return r;

   }



   public static double[][] MMmultiply(double[][] a, double[][] b) {
      int nx = a.length;
      int ny = a[0].length;

      int nu = b.length;
      int nv = b[0].length;
      if (ny != nu) {
         E.error("matrix dims incompatible " + 
			    nx + " " + ny + "   *   " + ny + " " + nv);
	 return null;
      }
      
      double[][] r = new double[nx][nv];
      for (int i = 0; i < nx; i ++) {
	 for (int j = 0; j < nv; j++) {
	    for (int k = 0; k < ny; k++) {
	       r[i][j] += a[i][k] * b[k][j];
	    }
	 }
      }
      return r;
   }




   public static double[] MVmultiply(double[][] a, double[] b) {
      int nx = a.length;
      int ny = a[0].length;
      
      int nu = b.length;
      if (ny != nu) {
	 E.error("MVMultiply : matrix dimensions " + 
			    " are incompatible " + 
			    nx + " " + ny + "   *   " + ny);
	 return null;
      }
      
      double[] r = new double[nx];
      for (int i = 0; i < nx; i ++) {
	 for (int k = 0; k < ny; k++) {
	    r[i] += a[i][k] * b[k];
	 }
      }
      return r;
   }



   public static double[] LUSolve (double[][] m, double[] R) {
      
      Matrix M = new Matrix (m);
      M.LU();
      double[] W = M.lubksb(R);
      return W;
   }
   



}
