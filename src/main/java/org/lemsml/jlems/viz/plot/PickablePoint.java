package org.lemsml.jlems.viz.plot;

 

import java.awt.Color;



public class PickablePoint implements Pickable {

   // NB are relative to an origin and scale set by the owner of the point
   // (which may be the natural origin and identity scale of course)
   Position p_position;

   Color p_color;
   String p_tip;

   int p_size;
   int p_range;


   final static int SQUARE = 1;
   final static int CIRCLE = 2;
   int p_style;
   int p_icache;

   Object p_ref;

   private AbsLocated p_absLocated;


   public PickablePoint() {
      this(0.0, 0.0, null);
   }


   public PickablePoint(Object obj) {
      this(0., 0., obj);
   }


   public PickablePoint(double cx, double cy) {
      this(cx, cy, null);
   }


   public PickablePoint(Position p, Object obj) {
      this(p.getX(), p.getY(), obj);
   }


   public PickablePoint(Position p, Object obj, int icol) {
      this(p.getX(), p.getY(), obj);
      p_color = new Color(icol);
   }


   public PickablePoint(double cx, double cy, Object oref) {
      this(cx, cy, Color.red, oref);
   }


   public PickablePoint(double cx, double cy, Color col, Object oref) {
      this(cx, cy, col, oref, 4, 8);
   }


   public PickablePoint(double cx, double cy, Color col, Object oref, int isize, int irange) {
      p_position = new Position(cx, cy);
        p_color = col;
      p_ref = oref;

      if (oref instanceof AbsLocated) {
         p_absLocated = (AbsLocated)oref;
      }


      p_size = isize;
      p_range = irange;
   }


   public void moveTo(double x, double y) {
	   p_position.set(x, y);
   }


   public Object getRef() {
      return p_ref;
   }


   public void setCache(int i) {
      p_icache = i;
   }


   public int getCache() {
      return p_icache;
   }


   public void moveTo(Position pos) {
      setPosition(pos);
   }


   public void setPosition(Position p) {
      setPosition(p.getX(), p.getY());
   }


   public void setPosition(double x, double y) {
      p_position.set(x, y);
   }


   public int getRange() {
      return p_range;
   }


   public int getSize() {
      return p_size;
   }




   public void setColor(int icol) {
      setColor(new Color(icol));
   }



   public void setColor(Color c) {
      p_color = c;
   }


   public void setSize(int sz) {
      p_size = sz;
   }


   public void setRange(int rn) {
      p_range = rn;
   }


   public Position getPosition() {
      Position ret = null;
      if (p_absLocated == null) {
         ret = p_position;
      } else {
         ret = p_absLocated.getAbsolutePosition();
      }
      return ret;
   }



   public Color getColor() {
      return p_color;
   }


}
