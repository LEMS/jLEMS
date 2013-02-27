package org.lemsml.jlems.viz.plot;
 


public class Demo1 implements PaintInstructor {


   double[] xdat;
   double[] ydat;

   public Demo1() {

      int n = 1000;
      xdat = new double[n];
      ydat = new double[n];
      for (int i = 0; i < n; i++) {
	 xdat[i] = (10. * i) / n;
	 ydat[i] = 10. * Math.cos(2. * xdat[i]);
      }
      
   }



   public void instruct(Painter p) {
      p.drawWhiteLine(3., xdat, ydat);
   }

   

   public boolean antialias() {
      return true;
   }



   public Box getLimitBox() {
	   Box ret = new Box();
	   ret.push(xdat, ydat);
	   return ret;
   }


}
