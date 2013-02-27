package org.lemsml.jlems.core.run;

import org.lemsml.jlems.core.logging.E;
 
public final class Column {

   int n;
   double[] dat;
   
   public Column(int n0) {
       n = n0;
        dat = new double[n];
   }
   
   public Column(double[] d) {
      dat = d;
      n = d.length;
   }
   
   
   public Column plus(double d) {
      double[] ret = new double[n];
      for (int i = 0; i < n; i++) {
         ret[i] = dat[i] + d;
      }
      return new Column(ret);
   }
   
   public Column times(double d) {
      double[] ret = new double[n];
      for (int i = 0; i < n; i++) {
         ret[i] = dat[i] * d;
      }
      return new Column(ret);
   }
   
   
   public void incrementBy(double d) {
      for (int i = 0; i < n; i++) {
         dat[i] += d;
      }
   }
   

   public void incrementBy(double[] r) {
        for (int i = 0; i < n; i++) {
           dat[i] += r[i];
        }  
   }
   
   public void incrementBy(Column v) {
      for (int i = 0; i < n; i++) {
         dat[i] += v.dat[i];
      }
   }
    
   public void multiuplyBy(double d) {
      for (int i = 0; i < n; i++) {
         dat[i] *= d;
      }
   }

   public double[] getData() {
      return dat;
   }

   public int size() {
         return n;
   }

   public double avgAbs() {
      double r = 0.;
      for (int i = 0; i < n; i++) {
         r += Math.abs(dat[i]);
      }
      r /= n;
      return r;
   }

   
   public void decrementBy(Column v) {
      for (int i = 0; i < n; i++) {
         dat[i] -= v.dat[i];
      }
   }

   public Column copy() {
      double[] d = new double[n];
      for (int i = 0; i < n; i++) {
         d[i] = dat[i];
      }
      return new Column(d);
   }
    
   public void increment(int i, double d) {
      dat[i] += d;
   }

   public Column plus(Column vdc) {
       Column ret = copy();
       ret.incrementBy(vdc);
       return ret;
   }

   public void print() {
    StringBuffer sb = new StringBuffer();
    sb.append("(");
    for (int i = 0; i < n; i++) {
       sb.append("" + dat[i]);
       if (i < n-1) {
          sb.append(", ");
       }
    }
    sb.append(")");
    E.info("column: " + sb.toString());
   }

   public void positivize() {
      for (int i = 0; i < n; i++) {
         if (dat[i] < 0.) {
            dat[i] = 0.;
         }
      }
      
   }
   
  
   public void writeTo(double[] dout) {
      if (dat == dout) {
         // warn?
      } else { 
         for (int i = 0; i < n; i++) {
            dout[i] = dat[i];
         }
      }
   }

   
}
