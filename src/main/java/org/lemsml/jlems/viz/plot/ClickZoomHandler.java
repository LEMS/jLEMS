package org.lemsml.jlems.viz.plot;





final class ClickZoomHandler extends BaseMouseHandler {

   private int xc;
   private int yc;

   double xfac;
   double yfac;


   final static int NORMAL = 1;
   final static int IN = 2;
   final static int OUT = 3;
   final static int INOUT = 4;

   int actionMode;

   boolean dragging = false;

   public ClickZoomHandler() {
	   super();
	   xfac = 1.0;
      yfac = 1.0;
   }



   public void activate() {
      actionMode = NORMAL;
      super.activate();
   }



   public void activateInOut() {
      activate();
      actionMode = INOUT;
   }


   public void activateOut() {
      activate();
      actionMode = OUT;
   }
   public void activateIn() {
	      activate();
	      actionMode = IN;
	   }







   public void init(Mouse m) {
	   dragging = false;
      xc = m.getX();
      yc = m.getY();

      double zfac = 1.0;
      xfac = 1.0;
      yfac = 1.0;

      if (actionMode == IN) {
	 zfac = 0.7;
	 setClaimIn();

      } else if (actionMode == OUT) {
	 zfac = 1. / 0.7;
	 setClaimIn();

      } else if (actionMode == INOUT) {
    	  // we're the only handler, so claim it and zoom on moving up/down or clicking
    	  zfac = 1.;
    	  if (m.leftButton()) {
    		  zfac = 0.7;

    	  } else if (m.rightButton()) {
    		  zfac = 1. / 0.7;
    	  }
    	  setClaimIn();


      } else {
    	  if (m.leftButton()) {
    		  zfac = 0.7;

    	  } else if (m.rightButton()) {
    		  zfac = 1. / 0.7;

    	  } else {
    		  setClaimOut();
    	  }
      }



      if (xc > 30) {
	 xfac = zfac;
      }

      if (yc < m.getCanvasHeight() - 30) {
	 yfac = zfac;
      }

   }


   public void advance(Mouse m) {

      if (actionMode == NORMAL) {
	 int dx = m.getX() - xc;
	 int dy = m.getY() - yc;

	 if (dx * dx + dy * dy > 100) {
	    // not a ckick - a drag - we're out of running;
	    setClaimOut();
	 }
      }
   }


   public void release(Mouse m) {
      setClaimIn();
   }





   public void applyOnDown(Mouse m) {
	   m.initializeZoom(xc, yc);
   }


   public void applyOnDrag(Mouse m) {
      int x = m.getX();
      int y = m.getY();

      double rz = 100.;
      double zx = Math.exp(-(x - xc) / rz);
      double zy = Math.exp((y - yc) / rz);
      if (Math.abs(x - xc) * Math.abs(y - yc) > 5) {
    	  dragging = true;
      }

      m.dragZoom(zx, zy, xc, yc);
      setFullRepaint();
   }





   public void applyOnRelease(Mouse m) {
	   if (!dragging) {
		   m.zoom(xfac, yfac, xc, yc);
	   }
	   m.restoreAA();
   }




}
