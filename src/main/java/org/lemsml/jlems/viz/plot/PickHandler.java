package org.lemsml.jlems.viz.plot;

 

import java.awt.Color;

import org.lemsml.jlems.core.logging.E;
 

public class PickHandler extends BaseMouseHandler {

   int xoff = 0;
   int yoff = 0;

   WorldTransform worldTransform;

   PickStore pickStore;

   Pickable activePick;

   PickListener pickListener;
   ClickListener clickListener;

   Pickable echoItem;

   HoverTimer hTimer;
   Pickable hoverItem;


   boolean pickInTrash;


   public PickHandler(PickStore ps, WorldTransform wt) {
      super();
      pickStore = ps;
      worldTransform = wt;
      hTimer = new HoverTimer(this);
   }



   public void setPickListener(PickListener pl) {
      pickListener = pl;
   }



   public boolean motionAware() {
      return true;
   }


   public boolean motionChange(Mouse m) {
      int xc = m.getX();
      int yc = m.getY();

      double wx = worldTransform.pubWopx(xc);
      double wy = worldTransform.pubWopy(yc);



      boolean changed = false;
      if (echoItem == null) {
         echoItem = pickStore.getClaimant(xc, yc, wx, wy);
         if (echoItem != null) {
            changed = true;
         }

      } else {
         if (pickStore.itemClaims(echoItem, xc, yc, wx, wy)) {
            // nothing to do - same item as before;

         } else {
            Pickable pei = echoItem;
            echoItem = pickStore.getClaimant(xc, yc, wx, wy);
            changed = true;


            if (pei.equals(echoItem)) {
               E.error(" - same claimant but failed to claim");
            }
         }
      }

      if (changed) {
         hoverItem = null;
         if (echoItem == null) {
            hTimer.clear();
         } else {
            hTimer.start();
         }
      }

      return changed;
   }



   public void echoPaint(Painter p, boolean tips) {
      if (echoItem != null) {

         if (echoItem instanceof PickablePoint) {
            p.setColor(Color.white);
            p.drawRectangle(pickStore.getEchoBox((PickablePoint)echoItem));



         } else if (echoItem instanceof PickableRegion) {
            PickableRegion pr = (PickableRegion)echoItem;
            p.setColor(Color.white);
            p.drawPolygon(pr.getXPts(), pr.getYPts());

            if (tips || (hoverItem.equals(echoItem))) {
               String s = pr.getRegionTag();
               if (s != null) {
                  p.drawOffsetCenteredLabel(s, pr.getXReference(), pr.getYReference());
               }
            }
         }

      }
   }



   public void init(Mouse m) {
      int xm = m.getX();
      int ym = m.getY();

      double wx = worldTransform.pubWopx(xm);
      double wy = worldTransform.pubWopy(ym);


      activePick = pickStore.getClaimant(xm, ym, wx, wy);

      // offset between where the mouse was pressed and actual center;
      if (activePick != null) {
         xoff = pickStore.getClaimantRefX() - xm;
         yoff = pickStore.getClaimantRefY() - ym;
      }


      if (activePick == null) {
         setClaimOut();
      } else {
         setClaimIn();
      }

      pickInTrash = false;
   }


   public void advance(Mouse m) {

      // never called since init makes decision?;
      E.warning("advance called in PickHandler? ");

   }


public void missedPress(Mouse m) {
   if (pickListener != null) {
	  
      if (m.getX() > worldTransform.getCanvasWidth() - 20 &&
          m.getY() > worldTransform.getCanvasHeight() - 20) {
         pickListener.trashPressed();

      } else {
         pickListener.backgroundPressed(m.getButton(), m.getX(), m.getY());
      }
   }
}


   public void hovered() {
      hoverItem = echoItem;
     // E.info("set hover item " + hoverItem);
      if (hoverItem != null) {
          pickListener.pickHovered(hoverItem);
       }
   }


   public void applyOnDown(Mouse m) {
      if (pickListener != null) {
         pickListener.pickPressed(activePick, m.getButton(), m.getX(), m.getY());
         // TODO xoff, yoff?
      }
   }


   public void applyOnDrag(Mouse m) {

      if (pickListener != null) {
         int ix = m.getX() + xoff;
         int iy = m.getY() + yoff;
         Position pos = worldTransform.getWorldPosition(ix, iy);
         pickListener.pickDragged(activePick, pos, m.getButton(), ix, iy);


         if (inTrash(ix, iy)) {
            if (pickInTrash) {
               // as ist;
            } else {
               pickInTrash = true;
               pickListener.pickEnteredTrash(activePick);
            }
         } else {
            if (pickInTrash) {
               pickInTrash = false;
               pickListener.pickLeftTrash(activePick);
            }
         }

         setFullRepaint();
      }

   }


   public boolean inTrash(int ix, int iy) {
      return (ix > worldTransform.getCanvasWidth() - 28 && iy > worldTransform.getCanvasHeight() - 28);
   }



   public void applyOnRelease(Mouse m) {

      if (pickListener != null) {
         pickListener.pickReleased(activePick, m.getButton());

         if (pickInTrash) {
            pickListener.pickTrashed(activePick);
         }
      }

   }



   public void setClickListener(ClickListener cl) {
      clickListener = cl;

   }



}
