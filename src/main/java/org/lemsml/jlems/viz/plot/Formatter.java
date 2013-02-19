package org.lemsml.jlems.viz.plot;

 
public final class Formatter {

	private Formatter() {
		
	}
 
   public static float trim(double a, int itr) {
      double d = 0.0;
      if (Math.abs(a) < 1.0e-300) {
         d = 0.0;
      } else {

         double b = Math.abs(a);
         double mlt = Math.log(b) / Math.log(10.0) - itr;
         int imlt = (int)(mlt + (mlt > 0.0 ? 0.99 : 0.0));
         int ib = (int)(b / Math.pow(10.0, imlt) + 0.5);
         b = ib * Math.pow(10.0, imlt);
         d = (a > 0 ? b : -b);
      }
      return (float)d;
   }


   public static String format(double d) {
      String ret = null;
      if (d > 1.e-3 && d < 10000) {
         ret = "xx" + String.format("%.2f", new Double(d));
      } else {
         ret = String.format("%.2g", new Double(d));
      }
      return ret;
   }
 

   public static String format(double d, double delta) {
      double ad = Math.abs(d);
      double adelta = Math.abs(delta);

      String sret = null;
      if (ad < 1.e-10) {
         sret = "0.0";
      } else {
         int nfig = (int)(Math.log (ad / adelta) / 2.3 + 2.);
         nfig = (nfig < 2 ? 2 : (nfig > 6 ? 6 : nfig));
         
         if (nfig <= 4 && ad> 1.e-3 && ad < 1.e4) {
            sret = String.format("%." + nfig +"f", new Double(d));
         } else {
            sret = String.format("%." + nfig +"g", new Double(d));
         }
      }
      return sret;
   }
}
