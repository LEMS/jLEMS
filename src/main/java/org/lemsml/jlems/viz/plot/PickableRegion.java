
package org.lemsml.jlems.viz.plot;

import org.lemsml.jlems.core.logging.E;
 


public final class PickableRegion implements Pickable {

   double[] xb;
   double[] yb;
   String label;

   String tag;

   double xref;
   double yref;

   
   Object oref;
   
   int icache;


   // axay should be int[2][npts]
   public PickableRegion(double[][] axay, Object obj) {
      this(axay[0], axay[1], obj);
   }

   

   public PickableRegion(double[] axb, double[] ayb, Object obj) {
      if (axb == null || axb.length < 2) {
         E.error("need at least 2 elts but got " + axb);
      }
      
      if (axb.length < 3) {
	 xb = mmxx(axb);
	 yb = mxxm(ayb);
      } else {
	 xb = axb;
	 yb = ayb;
      }
      oref = obj;


      // reference points for preserving relative offset of mouse click - could 
      // be the center, but shouldn't matter?
      xref = xb[0];
      yref = yb[0];
   }

 

   public double[] getXPts() {
      return xb;
   }

   public double[] getYPts() {
      return yb;
   }


   public boolean contains(double xq, double yq) {
      return Geom.pointIsInside(xb, yb, xq, yq);
   }
   


   public void setReferencePoint(Position p) {
      setReferencePoint(p.getX(), p.getY());
   }


   public Position getReferencePoint() {
      return new Position(xref, yref);
   }
   
   
   public void setPoints(double[][] axy) {
      xb = axy[0];
      yb = axy[1];
   }
   
   public void setPoints(double[] ax, double[] ay) {
      xb = ax;
      yb = ay;
   }

   public void setReferencePoint(double xr, double yr) {
      xref = xr;
      yref = yr;
   }

   public void moveTo(Position pos) {
      moveTo(pos.getX(), pos.getY());
   }

   public void moveTo(double cx, double cy) {
      double dx = cx - xref;
      double dy = cy - yref;
      for (int i = 0; i < xb.length; i++) {
	 xb[i] += dx;
	 yb[i] += dy;
      }
      xref = cx;
      yref = cy;
   }
   
   public void scaleTo(double z) {
         scaleTo(z, z);
   }
   
   public void scaleTo(double xsz, double ysz) {
     MathUtil.scaleRangeTo(2. * xsz, xb);
     MathUtil.scaleRangeTo(2. * ysz, yb);
   }
   
   
   
   public double getXReference() {
      return xref;
   }

   public double getYReference() {
      return yref;
   }


   public Object getRef() {
      return oref;
   }


   public void setCache(int ic) {
      icache = ic;
   }

   public int getCache() {
      return icache;
   }





   public double[] mxxm(double[] v) {
      double min = MathUtil.min(v);
      double max = MathUtil.max(v);
      double[] ret = {min, max, max, min};
      return ret;
   }


   public double[] mmxx(double[] v) {
      double min = MathUtil.min(v);
      double max = MathUtil.max(v);
      double[] ret = {min, min, max, max};
      return ret;
   }



   public void setRegionTag(String s) {
      tag = s;
    }

   public String getRegionTag() {
      return tag;
   }


}
