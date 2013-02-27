package org.lemsml.jlems.core.run;

 
 

public final class SMatrix {

	
	private SMatrix() {
		
	}

	
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



   public static double[][] mMmultiply(double[][] a, double[][] b) throws MatrixException {
      int nx = a.length;
      int ny = a[0].length;

      int nu = b.length;
      int nv = b[0].length;
      if (ny != nu) {
         throw new MatrixException("matrix dims incompatible " + 
			    nx + " " + ny + "   *   " + ny + " " + nv);
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




   public static double[] mvMultiply(double[][] a, double[] b) throws MatrixException {
      int nx = a.length;
      int ny = a[0].length;
      
      int nu = b.length;
      if (ny != nu) {
    	  throw new MatrixException("MVMultiply : matrix dimensions  are incompatible " + 
			    nx + " " + ny + "   *   " + ny);
      }
      
      double[] r = new double[nx];
      for (int i = 0; i < nx; i ++) {
	 for (int k = 0; k < ny; k++) {
	    r[i] += a[i][k] * b[k];
	 }
      }
      return r;
   }



   public static double[] luSolve (double[][] m, double[] R) throws MatrixException {
      
      Matrix M = new Matrix (m);
      M.lu();
      double[] W = M.lubksb(R);
      return W;
   }
   



}
