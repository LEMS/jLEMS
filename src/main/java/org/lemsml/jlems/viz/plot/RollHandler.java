package org.lemsml.jlems.viz.plot;




final class RollHandler extends BaseMouseHandler {

   private int xc;
   private int yc;

   private double rcx = Double.NaN;
   private double rcy = Double.NaN;
   private double rcz = Double.NaN;

   // private boolean continuous;


   final static int NORMAL = 0;
   final static int SIMPLE = 1;

   int actionMode = NORMAL;

   boolean aaCache;


 

   public void init(Mouse m) {
      xc = m.getX();
      yc = m.getY();

      if (actionMode == SIMPLE) {
	     if (m.leftButton()) {
	        setClaimIn();

	    } else {
	    	setClaimOut();
	    }



    } else if (m.rightButton()) {
    	// setClaimOut();
	    // needs logic for claiming in multifunction mode
      }
   }

   public void advance(Mouse m) {
	  if (actionMode == NORMAL && m.leftButton()) {

		 int dx = m.getX() - xc;
		 int dy = m.getY() - yc;

		 if (dx * dx + dy * dy > 100) {
			 // not a click -dragging with left means us
		    setClaimIn();
		    applyOnDown(m); // TODO shouldn't manager call this once we win?
		 }
	   }
	}


   public void activate() {
      actionMode = NORMAL;
      super.activate();
   }



   public void simpleActivate() {
      activate();
      actionMode = SIMPLE;
   }


   public void setRollCenter(double x, double y, double z) {
	   rcx = x;
	   rcy = y;
	   rcz = z;
   }



   public void applyOnDown(Mouse m) {
	   if (Double.isNaN(rcx)) {
		   m.initializeRotation(m.getX(), m.getY());

	   } else {
		   m.initializeRotation(rcx, rcy, rcz);
	   }
   }

   public void applyOnDrag(Mouse m) {
      int x = m.getX();
      int y = m.getY();

      m.dragRollRotate(x - xc, y - yc);
      setFullRepaint();
   }



   public void applyOnRelease(Mouse m) {
	   m.restoreAA();
     //  m.permanentPan(xc, yc, x, y);


   }

}


