package org.lemsml.jlems.viz.plot;



import java.awt.Graphics2D;



public class BaseMouseHandler {

   final static int NONE = 0;
   final static int BUFFERED = 1;
   final static int FULL = 2;
   private int repaintStatus = NONE;

   private boolean active;


   private final static int OUT = -1;
   private final static int UNDECIDED = 0;
   private final static int IN = 1;
   private int claimStatus = UNDECIDED;




   BaseMouseHandler() {
      active = true;
      setClaimUndecided();
   }


   public void activate() {
      active = true;
   }

   public void deactivate() {
      active = false;
   }


   public boolean isActive() {
      return active;
   }



   public boolean motionAware() {
	   return false;
   }
   
   public boolean motionChange(Mouse m) {
      return false;
   }

   int getRepaintStatus() {
      return repaintStatus;
   }


   void setRepaintStatus(int i) {
      repaintStatus = i;
   }

   void setFullRepaint() {
      repaintStatus = FULL;
   }




   final void setClaimUndecided() {
      claimStatus = UNDECIDED;
   }

   void setClaimIn() {
      claimStatus = IN;
   }

   void setClaimOut() {
      claimStatus = OUT;
   }



   boolean isIn() {
      return (claimStatus == IN);
   }

   boolean isUndecided() {
      return (claimStatus == UNDECIDED);
   }

   boolean isOut() {
      return (claimStatus == OUT);
   }





   // used to decide whether it has control

   void clear() {
	   // ignore
   }

   void init(Mouse m) {
	   // ignore
   }

   void advance(Mouse m) {
	   // ignore
   }

   void release(Mouse m) {
	   // ignore
   }





   void echoPaint(Graphics2D g) {
	   // ignore
   }




   // if handler has won control, then the following are called
   void missedPress(Mouse m) {
	   // ignore
   }
   
   void applyOnDown(Mouse m) {
	   // ignore
   }

   void applyOnDrag(Mouse m) {
	   // ignore
   }

   void applyOnRelease(Mouse m) {
	   // ingore
   }


   public void applyOnScrollWheel(Mouse mouse) {
 	
   }

}
