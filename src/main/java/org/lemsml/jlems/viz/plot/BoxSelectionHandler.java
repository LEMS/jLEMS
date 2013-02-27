package org.lemsml.jlems.viz.plot;



import java.awt.Color;
import java.awt.Graphics2D;


final class BoxSelectionHandler extends BaseMouseHandler {

   private boolean vtl;
   private boolean vtr;
   private boolean vbl;
   private boolean vbr;
   

   private int x0;
   private int y0;
   private int x1; 
   private int y1;

   private int[] xyxy;

   final static int NORMAL = 0;
   final static int SIMPLE = 1;

   int actionMode;



   public BoxSelectionHandler() {
	   super();
      clear();
      actionMode = NORMAL;
      xyxy = new int[4];
      setRepaintStatus(BaseMouseHandler.NONE);
   }



   public void activate() {
      actionMode = NORMAL;
      super.activate();
   }



   public void simpleActivate() {
      activate();
      actionMode = SIMPLE;
   }




   public void clear() {
      vtl = false;
      vtr = false;
      vbl = false;
      vbr = false;
   }


   public void init(Mouse m) {
      clear();
      x0 = m.getX();
      y0 = m.getY();

      if (actionMode == SIMPLE) {
	 setClaimIn();
      }
   }


   public void advance(Mouse m) {
      if (isUndecided()) {
	 checkActivate(m.getX(), m.getY());
      }
      setRepaintStatus(BaseMouseHandler.BUFFERED);
   }




   // if this handler has won control, then the following are called:

   void echoPaint(Graphics2D g) {

      g.setColor(Color.red);
      g.drawLine(x0, y0, x1, y0);
      g.drawLine(x0, y0, x0, y1);
      g.drawLine(x1, y1, x1, y0);
      g.drawLine(x1, y1, x0, y1);
   }





   public void applyOnDown(Mouse m) {
      // ignore
   }


   public void applyOnDrag(Mouse m) {
      readPosition(m);

      setRepaintStatus(BaseMouseHandler.BUFFERED);
   }


   public void applyOnRelease(Mouse m) {
      readPosition(m);
      int xa = (x0 < x1 ? x0 : x1);
      int ya = (y0 < y1 ? y0 : y1);
      int xb = (x0 < x1 ? x1 : x0);
      int yb = (y0 < y1 ? y1 : y0);

      m.boxSelected(xa, ya, xb, yb);
   }

   

   void readPosition(Mouse m) {
      x1 = m.getX();
      y1 = m.getY();
   }




   public int[] getXYXY() {
      xyxy[0] = x0;
      xyxy[1] = y0;
      xyxy[2] = x1;
      xyxy[3] = y1;
      return xyxy;
   }
   



   private void checkActivate(int x, int y) {
      int dx = x - x0;
      int dy = y - y0;

      int thresh = 8;
      int pt = thresh;
      int mt = -1 * thresh;

      vtl = (vtl || (dx < mt && dy < mt));
      vtr = (vtr || (dx > pt && dy < mt));
      vbl = (vbl || (dx < mt && dy > pt));
      vbr = (vbr || (dx > pt && dy > pt));


      if ( (vtl && dx > 0 && dy > 0) ||
	   (vtr && dx < 0 && dy > 0) ||
	   (vbl && dx > 0 && dy < 0) ||
	   (vbr && dx < 0 && dy < 0) ) {

	 setClaimIn();

	 x1 = x;
	 y1 = y;
      }
   }
   



}
