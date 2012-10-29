package org.lemsml.jlemsviz.plot;



import java.awt.Graphics2D;



abstract class MouseHandler {

   final static int NONE = 0;
   final static int BUFFERED = 1;
   final static int FULL = 2;
   private int repaintStatus = NONE;

   private boolean active;


   private final static int OUT = -1;
   private final static int UNDECIDED = 0;
   private final static int IN = 1;
   private int claimStatus = UNDECIDED;




   MouseHandler() {
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



   boolean motionAware() {
      return false;
   }
   boolean motionChange(Mouse m) {
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




   void setClaimUndecided() {
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
   }

   void init(Mouse m) {
   }

   void advance(Mouse m) {
   }

   void release(Mouse m) {
   }





   void echoPaint(Graphics2D g) {
   }




   // if handler has won control, then the following are called
   void missedPress(Mouse m) {

   }
   void applyOnDown(Mouse m) {
   }

   void applyOnDrag(Mouse m) {
   }
   void applyOnRelease(Mouse m) {
   }

}
