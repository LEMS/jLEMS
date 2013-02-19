package org.lemsml.jlems.viz.plot;



final class PanHandler extends BaseMouseHandler {

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
	   //  continuous = true;

	 } else {
	    setClaimOut();
	 }



      } else {

	 if (m.leftButton()) {
	    //	 continuous = true;
	    setClaimOut();
	    // used to remain undecided - could be tuyrn zoom ,oucld be pan
	    // - reinstate??? TODO

	 } else if (m.rightButton()) {
	   // continuous = true;

	 } else {
	    setClaimOut();
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





   public void advance(Mouse m) {

      int dx = m.getX() - xc;
      int dy = m.getY() - yc;

      if (dx * dx + dy * dy > 100) {
	 // moved ten pixels - claim mouse for pan if we can;
	 setClaimIn();
      }

   }


   public void applyOnDrag(Mouse m) {
      int x = m.getX();
      int y = m.getY();

      // always pan on drag?? *** (no non-continuous mode?)
      m.trialPan(xc, yc, x, y);
      setFullRepaint();
   }



   public void applyOnRelease(Mouse m) {
      int x = m.getX();
      int y = m.getY();
      m.permanentPan(xc, yc, x, y);
      m.restoreAA();

   }



}


/*


   package void applyOnRelease() {

      } else {
	 boolean longClick = (ms.button == ms.RIGHTBUTTON ||
			      ms.tUp - ms.tDown > 500);

	 if (dropEmptyClick) {
	    dropEmptyClick = false;
	 } else {
	   if (panZoomable) panZoom(longClick);
	 }
	 repaint ();

   }

}

*/
