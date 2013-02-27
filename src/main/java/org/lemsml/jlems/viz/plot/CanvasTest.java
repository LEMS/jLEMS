package org.lemsml.jlems.viz.plot;
 

import javax.swing.JFrame;


public class CanvasTest implements PaintInstructor {


   double[] xdat;
   double[] ydat;

   public CanvasTest() {

      int n = 1000;
      xdat = new double[n];
      ydat = new double[n];
      for (int i = 0; i < n; i++) {
	 xdat[i] = (10. * i) / n;
	 ydat[i] = 10. * Math.cos(50. * xdat[i]);
      }
      
   }




  public static void main(String[] argv) {
      JFrame f = new JFrame();
      WorldCanvas wc = new WorldCanvas();
      f.getContentPane().add(wc);
      f.pack();
      f.setVisible(true);

      wc.setPaintInstructor(new CanvasTest());

      
   }




   public void instruct(Painter p) {
      p.drawWhiteLine(3., xdat, ydat);
   }

   

   public boolean antialias() {
      return true;
   }




   public Box getLimitBox() {
      // TODO Auto-generated method stub
      return null;
   }


}
