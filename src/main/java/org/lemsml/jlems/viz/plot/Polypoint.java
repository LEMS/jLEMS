package org.lemsml.jlems.viz.plot;
 

public class Polypoint {

   public double[] xpts;
   public double[] ypts;
   
   public static final int OPEN = 0;
   public static final int CLOSED = 1;
   public static final int FILLED = 2;
  
   public String closure;
   private int p_closure;
   final public static String[] p_closureNames = { "open", "closed", "filled" };
   
   
   public Polypoint() {
      xpts = new double[2];
      ypts = new double[2];
   }
   
   public Polypoint(double[] xp, double[] yp) {
      this(xp, yp, OPEN);
   }
   
   
   public Polypoint(double[] xp, double[] yp, int icl) {
      xpts = xp;
      ypts = yp;
      p_closure = icl;
   }
   
    
   
   public String exportAsString() {
      StringBuffer sb = new StringBuffer();
      sb.append("pp(" + xpts.length + ": ");
      for (int i = 0; i < xpts.length; i++) {
         sb.append(xpts[i] + ", " + ypts[i] + ", ");
      }
      sb.append(")");
      return sb.toString();
   }
   
   
   
   public int getClosure() {
      return p_closure;
   }
   public void setClosure(int pc) {
      p_closure = pc;
      closure = p_closureNames[p_closure];
   }
  
   public boolean isFilled() {
      return (p_closure == FILLED);
   }
   public boolean isOpen() {
      return (p_closure == OPEN);
   }
   public boolean isClosed() {
      return (p_closure == CLOSED || p_closure == FILLED);
   }


   public void setXpts(double[] d) {
      xpts = d;
   }


   public void setYpts(double[] d) {
      ypts = d;
   }
   
   public double[] getXPts() {
      return xpts;
   }
   
   public double[] getYPts() {
      return ypts;
   }
   

   public Polypoint getCopy() {
      int np = xpts.length;
      double[] xp = new double[np];
      double[] yp = new double[np];
      for (int i = 0; i < np; i++) {
         xp[i] = xpts[i];
         yp[i] = ypts[i];
      }
      return new Polypoint(xp, yp, p_closure);
   }

   
   
   public void translate(Position pos) {
      double dx = pos.getX();
      double dy = pos.getY();
      for (int i = 0; i < xpts.length; i++) {
         xpts[i] += dx;
         ypts[i] += dy;
      }
     }

   public void rotate(Direction dir) {
      double c = dir.getCosine();
      double s = dir.getSine();
      for (int i = 0; i < xpts.length; i++) {
        double x = xpts[i];
        double y = ypts[i];
        xpts[i] = c * x - s * y;
        ypts[i] = s * x + c * y;
      }
       
   }
   
}
