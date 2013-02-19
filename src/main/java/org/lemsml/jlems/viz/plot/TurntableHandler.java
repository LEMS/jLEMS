package org.lemsml.jlems.viz.plot;




final class TurntableHandler extends BaseMouseHandler {

   private int xc;
   private int yc;


   // private boolean continuous;


   final static int NORMAL = 0;
   final static int SIMPLE = 1;

   int actionMode = NORMAL;


 

   public void init(Mouse m) {
      xc = m.getX();
      yc = m.getY();

      if (actionMode == SIMPLE) {
	     if (m.leftButton()) {
	        setClaimIn();

	    } else {
	       setClaimOut();
	    }



    } else {
    	setClaimOut();
	    // needs logic for claiming in multifunction mode
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





   public void advance(Mouse m) {
	   // could work out of we're in the running to take control;
   }




   public void applyOnDown(Mouse m) {
	   m.initializeRotation(m.getX(), m.getY());
   }


   public void applyOnDrag(Mouse m) {
      int x = m.getX();
      int y = m.getY();

      m.dragZRotate(x - xc, y - yc);
      setFullRepaint();
   }



   public void applyOnRelease(Mouse m) {
	   m.restoreAA();
     //  m.permanentPan(xc, yc, x, y);


   }

}


