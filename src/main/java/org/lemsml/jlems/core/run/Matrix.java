package org.lemsml.jlems.core.run;

import org.lemsml.jlems.core.logging.E;
 

public final class Matrix extends Object implements Cloneable {

   private double a[][];
   private double ws[];
   private final int n;
   private int perm[];
   private int sign;

   private int n1 = 0;
   private int n2 = 0;


   public Matrix(int nnn) {
      super();
	   n1 = n2 = n = nnn;
      a = new double[n][n];
      perm = new int[n];
      ws = new double[n];
   }


   public Matrix(double[][] a) {
      super();
	   this.a = a;
      n1 = n2 = n = a.length;
      perm = new int[n];
      ws = new double[n];
   }

   
   public double[][] getData() {
	   return a;
   }
   
   
   public double[] flatten() {
      double[] d = new double[n1 * n2];
      for (int i = 0; i < n; i++) {
         for (int j = 0; j < n; j++) {
            d[n * i + j] = a[i][j];
         }
      }
      return d;
   }


   public void sysPrint(final String s) throws MatrixException {
     	throw new MatrixException(s);
   }


   public int dim() {
      return n;
   }


   public Matrix copy() {
      Matrix m = new Matrix(n);

      for (int i = 0; i < n; i++) {
         for (int j = 0; j < n; j++) {
            m.a[i][j] = a[i][j];
         }
         m.perm[i] = perm[i];
      }

      m.sign = sign;
      m.setDims(n1, n2);
      return m;
   }


   public void setDims(int d1, int d2) {
      n1 = d1;
      n2 = d2;
   }


   public void identise() {
      for (int i = 0; i < n; i++) {
         for (int j = 0; j < n; j++) {
            a[i][j] = 0.;
         }
         a[i][i] = 1.;
      }
   }


   public void randomise() {
      for (int i = 0; i < n; i++) {
         for (int j = 0; j < n; j++) {
            a[i][j] = Math.random();
         }
      }
   }


   public void zero() {
      for (int i = 0; i < n; i++) {
         for (int j = 0; j < n; j++) {
            a[i][j] = 0.;
         }
      }
   }



   public Matrix identity() {
      Matrix m = copy();
      m.identise();
      return m;
   }


   public Matrix random() {
      Matrix m = copy();
      m.randomise();
      return m;
   }



   public void add(double d) {
      for (int i = 0; i < n; i++) {
         for (int j = 0; j < n; j++) {
            a[i][j] += d;
         }
      }
   }



   public static Matrix[] average(Matrix[] ma, Matrix[] mb, double f) {
      int n = ma.length;
      Matrix[] res = new Matrix[n];
      for (int i = 0; i < n; i++) {
         res[i] = average(ma[i], mb[i], f);
      }
      return res;
   }


   public static Matrix average(Matrix ma, Matrix mb, double f) {
      double g = 1. - f;
      int n = ma.n;
      Matrix res = new Matrix(n);
      for (int i = 0; i < n; i++) {
         for (int j = 0; j < n; j++) {
            res.a[i][j] = f * mb.a[i][j] + g * ma.a[i][j];
         }
      }
      return res;
   }



   public void add(Matrix m) throws MatrixException {
      if (m.n != n) {
         sysPrint("incompativle dims in Matrix.mplyBy " + n + " " + m.n);

      } else {
         for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
               a[i][j] += m.a[i][j];
            }
         }
      }
   }


   public Matrix sum(Matrix m) throws MatrixException {
      Matrix mr = copy();
      mr.zero();
      if (m.n != n) {
         sysPrint("incompativle dims in Matrix.mplyBy " + n + " " + m.n);

      } else {
         for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
               mr.a[i][j] = a[i][j] + m.a[i][j];
            }
         }
      }
      return mr;
   }


   public void mpyBy(double d) {
      for (int i = 0; i < n; i++) {
         for (int j = 0; j < n; j++) {
            a[i][j] *= d;
         }
      }
   }


   public void mpyBy(Matrix m) throws MatrixException {
      a = prod(m).a;
   }


   public Matrix prod(Matrix m) throws MatrixException {
      Matrix mr = copy();
      mr.zero();

      if (m.n != n) {
         sysPrint("incompativle dims in Matrix.mplyBy " + n + " " + m.n);

      } else {
         for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
               for (int k = 0; k < n; k++) {
                  mr.a[i][j] += a[i][k] * m.a[k][j];
               }
            }
         }
      }
      return mr;
   }


   public double[] lvprod(double[] v) throws MatrixException {
      double[] r = new double[n];
      if (v.length != n) {
         sysPrint("incompatible dimensions in lvprod");
      } else {
         for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
               r[j] += v[i] * a[i][j];
            }
         }
      }
      return r;
   }


   public Column times(Column v) throws MatrixException {
      return new Column(rvprod(v.getData()));
   }

   public double[] rvprod(double[] v) throws MatrixException {
      double[] r = new double[n];
      if (v.length != n) {
         sysPrint("incompatible dimensions in lvprod");
      } else {
         for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
               r[i] += a[i][j] * v[j];
            }
         }
      }
      return r;
   }



   public void multiplyInto(double[] v) {
      for (int i = 0; i < n; i++) {
         ws[i] = 0.;
         for (int j = 0; j < n; j++) {
            ws[i] += a[i][j] * v[j];
         }
      }
      for (int i = 0; i < n; i++) {
         v[i] = ws[i];
      }
   }


   public void rect2rvprod(double[] v, double[] r1, double[] r2) {
      for (int i = 0; i < n1; i++) {
         ws[i] = 0.0;
         for (int j = 0; j < n1; j++) {
            ws[i] += a[i][j] * v[j];
         }
      }

      for (int i = 0; i < n2 - n1; i++) {
         r2[i] = 0.0;
         for (int j = 0; j < n1; j++) {
            r2[i] += a[i + n1][j] * v[j];
         }
      }
      for (int i = 0; i < n1; i++) {
         r1[i] = ws[i];
      }


   }



   public double rvprodOneElt(double[] v, int elt) {
      double r = 0.0;
      for (int j = 0; j < n; j++) {
         r += a[elt][j] * v[j];
      }
      return r;
   }


   public Matrix transpose() {
      Matrix m = copy();
      for (int i = 0; i < n; i++) {
         for (int j = 0; j < n; j++) {
            m.a[i][j] = a[j][i];
         }
      }
      return m;
   }


   public double det() throws MatrixException {
      Matrix t = copy();
      t.lu();
      double d = 1.0 * t.sign;
      for (int i = 0; i < n; i++) {
         d *= t.a[i][i];
      }
      return d;
   }



   public double[][] copyMat() {
      double[][] ar = new double[n][n];
      for (int i = 0; i < n; i++) {
         for (int j = 0; j < n; j++) {
            ar[i][j] = a[i][j];
         }
      }
      return ar;
   }



   public void lu() throws MatrixException {
      int i, imax, j, k;
      double big, sum;
      double vv[] = new double[n];
      double TINY = 1.0e-20;

      sign = 1;

      imax = -1;
      for (i = 0; i < n; i++) {
         big = 0.0;
         for (j = 0; j < n; j++) {
            double temp = Math.abs(a[i][j]);
        	 if (temp > big) {
               big = temp;
            }
         }
         if (big == 0.0) {
            sysPrint("Singular Matrix in routine LUDCMP");
         }
         vv[i] = 1.0 / big;
      }

      for (j = 0; j < n; j++) {
         for (i = 0; i < j; i++) {
            sum = a[i][j];
            for (k = 0; k < i; k++) {
               sum -= a[i][k] * a[k][j];
            }
            a[i][j] = sum;
         }
         big = 0.0;
         for (i = j; i < n; i++) {
            sum = a[i][j];
            for (k = 0; k < j; k++) {
               sum -= a[i][k] * a[k][j];
            }
            a[i][j] = sum;
            double dum = vv[i] * Math.abs(sum);
            if (dum >= big) {
               big = dum;
               imax = i;
            }
         }
         if (j != imax) {
            for (k = 0; k < n; k++) {
               double dum = a[imax][k];
               a[imax][k] = a[j][k];
               a[j][k] = dum;
            }
            sign = -sign;
            vv[imax] = vv[j];
         }
         perm[j] = imax;
         if (a[j][j] == 0.0) {
            a[j][j] = TINY;
         }
         if (j != n) {
            double dum = 1.0 / (a[j][j]);
            for (i = j + 1; i < n; i++) {
               a[i][j] *= dum;
            }
         }
      }
   }



   public Matrix inverse() throws MatrixException {
      Matrix t, r;
      t = copy();
      r = copy();
      t.lu();

      double[] c = new double[n];
      for (int j = 0; j < n; j++) {
         for (int i = 0; i < n; i++) {
            c[i] = 0.0;
         }
         c[j] = 1.0;
         t.lubksb(c);
         for (int i = 0; i < n; i++) {
            r.a[i][j] = c[i];
         }
      }
      return r;
   }



   public static double[] luSolve(double[][] m, double[] R) throws MatrixException {

      Matrix M = new Matrix(m);
      M.lu();
      double[] W = M.lubksb(R);
      return W;
   }


   public Column luSolve(Column r) throws MatrixException {
      Matrix m = copy();
      m.lu();
      double[] w = m.lubksb(r.getData());
      return new Column(w);
   }



   public void invert() throws MatrixException {
      a = inverse().a;
   }


   public Column lubksb(Column v) {
      double[] d = lubksb(v.getData());
      return new Column(d);
   }

   public double[] lubksb(double[] b) {
      int ip;
      int ii = -1;
      double sum;

      for (int i = 0; i < n; i++) {
         ip = perm[i];
         sum = b[ip];
         b[ip] = b[i];
         if (ii >= 0) {
            for (int j = ii; j < i; j++) {
               sum -= a[i][j] * b[j];
            }
         } else if (sum != 0.0) {
            ii = i;
         }
         b[i] = sum;
      }

      for (int i = n - 1; i >= 0; i--) {
         sum = b[i];
         for (int j = i + 1; j < n; j++) {
            sum -= a[i][j] * b[j];
         }
         b[i] = sum / a[i][i];
      }
      return b;
   }


   public void round(double d) {
      for (int i = 0; i < n; i++) {
         for (int j = 0; j < n; j++) {
            if (Math.abs(a[i][j]) < d) {
               a[i][j] = 0.0;
            }
         }
      }
   }



   public void round() {
      round(1.0e-15);
   }



   public String print() {

      String[] sa = new String[n];
      for (int i = 0; i < n; i++) {
         sa[i] = " ";
         for (int j = 0; j < n; j++) {
            sa[i] += (" " + a[i][j]);
         }
      }
      StringBuilder sb = new StringBuilder();
      sb.append(" n1: " + n1 + " n2: " + n2 + "\n");
      for (int i = 0; i < sa.length; i++) {
        sb.append("" + i + " " + sa[i] + "\n");
      }
      return sb.toString();
   }



   public double maxAbsElt() {
      double d = 0.0;
      for (int i = 0; i < n; i++) {
         for (int j = 0; j < n; j++) {
            if (Math.abs(a[i][j]) > d) {
               d = Math.abs(a[i][j]);
            }
         }
      }
      return d;
   }



   public Matrix power(int p) throws MatrixException {
      Matrix mr = identity();
      int pg = 0;
      int pl = 1;
      Matrix mp = copy();
      while (pg < p) {
         if ((p & pl) > 0) {
            pg += pl;
            mr = mr.prod(mp);
         }
         pl *= 2;
         mp = mp.prod(mp);
      }
      if (pg != p) {
         sysPrint("got Matrix power wrong: " + p + " " + pg + " " + pl);
      }
      return mr;
   }


   public Matrix crudeExpOf(double t) throws MatrixException {
      Matrix m = copy();
      m.mpyBy(t);
      double eps = 1.0e-8;

      double d = m.maxAbsElt();
      int p = 0;
      double f = 1;
      for (p = 0; d * f > eps; f *= 0.5, p++) {
         ;
      }

      m.mpyBy(f);
      m.add(m.identity());

      for (; p > 0; p--) {
         m.mpyBy(m);
      }
      return m;
   }



   public Matrix expOf(double t) throws MatrixException {
      Matrix m = copy();
      m.mpyBy(t);
      double eps = 1.0e-12;

      double d = m.maxAbsElt();
      int p = 0;
      double f = 1;
      for (p = 0; d * f > eps; f *= 0.5, p++) {
         ;
      }

      m.mpyBy(f);
      // now we want to calculate (I + m)^p
      // without doing the obvious

      for (; p > 0; p--) {
         Matrix u = m.copy();
         u.mpyBy(u);
         m.add(m);
         m.add(u);
      }
      m.add(m.identity());
      return m;
   }



   public int randomIndexFromColumn(int c) {
      return randomIndexFromColumn(c, Math.random());
   }


   public int randomIndexFromColumn(int c, double rin) {
      double r = rin;
	  int ir = 0;
      while ((r -= a[ir][c]) > 0) {
         ir++;
      }
      return ir;
   }



   // should bufer this ***;
   public double[] getColumn(int ic) {
      double[] c = new double[n2];
      for (int i = 0; i < n2; i++) {
         c[i] = a[i][ic];
      }
      return c;
   }



   public int randomIndexFromOffsetColumn(int c, int off) {
      double r = Math.random();
      int ir;
      for (ir = off; (r -= a[ir][c]) > 0; ir++) {
         ;
      }
      return ir;
   }



   public double[] ev1vec(int np) throws MatrixException {
      // find the vector with eigenvalue 1., assuming it exists... or
      // equivalently the null space of M-I, which is assumed to have
      // dimension 1;
      // actually just take a large power of the Matrix ***

      Matrix q = copy();
      for (int i = 0; i < np; i++) {
         q = q.prod(q);
      }

      double[] s = new double[n];
      for (int i = 0; i < n; i++) {
         s[i] = 1. / n;
      }
      s = q.rvprod(s);

      double t = 0.0;
      for (int i = 0; i < n; i++) {
         t += s[i];
      }
      for (int i = 0; i < n; i++) {
         s[i] /= t;
      }
      if (Math.abs(t - 1.) > 0.01) {
         E.warning("ev1vec in class Matrix chnaged size " + t);
      }
      return s;
   }


   public void multiplyBy(double dt) {
      for (int i = 0; i < n1; i++) {
            for (int j = 0; j < n2; j++) {
               a[i][j] *= dt;
            }
      }
   }


   public void subtractIdentity() {
      for (int i = 0; i < n1; i++) {
            a[i][i] -= 1.;
         }
   }


   public void negate() {
      for (int i = 0; i < n1; i++) {
         for (int j = 0; j < n2; j++) {
            a[i][j] = -a[i][j];
         }
   }

   }


   public void set(int i, int j, double d) {
      a[i][j] = d;
   }


   public String dump() {
      StringBuffer sb = new StringBuffer();
      for (int i = 0; i < n1; i++) {
         for (int j = 0; j < n2; j++) {
            sb.append(a[i][j]);
            sb.append("   ");
         }
         sb.append("\n");
      }
      return sb.toString();
   }


   public double[][] getArray() {
		return a;
   }


public double[] getFlat() {
	double[] ret = new double[n1 * n2];
	for (int i = 0; i < n1; i++) {
		for (int j = 0; j < n2; j++) {
			ret[i * n2 + j] = a[i][j];
		}
	}
	return ret;
}


public String printDiag() {
	 StringBuffer sb = new StringBuffer();
	 for (int i = 0; i < n; i++) {
		 sb.append("" + a[i][i]); // String.format("%g8.4", a[i][i]));
		 sb.append(", ");
	 }
	 return sb.toString();
}


}
