
package org.lemsml.jlems.viz.plot;



public final class MathUtil {

	
	private MathUtil() {
		
	}
	
   public static double min(double[] v) {
      double ret = v[0];
      for (int i = 1; i < v.length; i++) {
	 if (v[i] < ret) {
	    ret = v[i];
	 }
      }
      return ret;
   }



   public static double max(double[] v) {
      double ret = v[0];
      for (int i = 1; i < v.length; i++) {
	 if (v[i] > ret) {
	    ret = v[i];
	 }
      }
      return ret;
   }



   public static void scaleRangeTo(double d, double[] v) {
      double a = min(v);
      double b = max(v);
      double f = d  / (b - a);
      double c = (a + b) / 2.;
      for (int i = 0; i < v.length; i++) {
         v[i] = c + f * (v[i] - c);
      }
   }

}
