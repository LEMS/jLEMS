package org.lemsml.jlemsviz.plot;


 

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import org.lemsml.jlems.logging.E;
 


public final class Mouse implements MouseListener, MouseMotionListener {

   public final static int LEFT = 1;
   public final static int MIDDLE = 2;
   public final static int RIGHT = 3;
   private int button;

   private int canvasWidth;
   private int canvasHeight;

   private int xDown;
   private int yDown;

   private int xCurrent;
   private int yCurrent;

   private boolean down;
  // private boolean onCanvas;

  //  private long timeDown;
 // private long periodDownToDown;

   private int nHandler;
   private BaseMouseHandler[] handlers;


   private BaseMouseHandler activeHandler;
   private BaseMouseHandler motionHandler;


   private final WorldCanvas canvas;


   private ClickListener clickListener;


   public Mouse(WorldCanvas c, boolean interactive) {
      super();
      canvas = c;

      if (interactive) {
         canvas.addMouseListener(this);
         canvas.addMouseMotionListener(this);
      }

      handlers = new BaseMouseHandler[10];
   }


   public void setClickListener(ClickListener cl) {
      clickListener = cl;
   }


   public void detach() {
      canvas.removeMouseListener(this);
      canvas.removeMouseMotionListener(this);
   }


   public void addHandler(BaseMouseHandler h) {
      if (nHandler >= handlers.length) {
         E.error("Mouse handler array too small");
      } else {
         handlers[nHandler++] = h;
      }
   }


   public void prependHandler(BaseMouseHandler h) {
      if (nHandler >= handlers.length) {
         E.error("Mouse handler array too small");
      } else {
         for (int i = nHandler; i > 0; i--) {
            handlers[i] = handlers[i - 1];
         }
         handlers[0] = h;
         nHandler += 1;
      }
   }



   private void requestRepaint() {
      canvas.repaint();
   }


   boolean leftButton() {
      return (button == LEFT);
   }


   boolean middleButton() {
      return (button == MIDDLE);
   }


   boolean rightButton() {
      return (button == RIGHT);
   }


   public void updateCanvasDimensions() {
      canvasWidth = canvas.getWidth();
      canvasHeight = canvas.getHeight();
   }


   int getCanvasWidth() {
      return canvasWidth;
   }


   int getCanvasHeight() {
      return canvasHeight;
   }



   public void mouseEntered(MouseEvent e) {
      // onCanvas = true;
   }


   public void mouseExited(MouseEvent e) {
      // onCanvas = false;
   }


   public void mouseClicked(MouseEvent e) {
      readPosition(e);

      if (clickListener != null) {
         clickListener.pointClicked(e.getX(), e.getY(), MouseUtil.getButton(e));
      }
   }



   public void mouseMoved(MouseEvent e) {
      if (down) {
         // should only get dragged events when down;
         E.shortWarning("mouse moved when down?? " + e);
         down = false;
         return;
      }

      readPosition(e);


      for (int i = 0; i < nHandler; i++) {
         BaseMouseHandler mh = handlers[i];
         if (mh.isActive() && mh.motionAware()) {
            if (mh.motionChange(this)) {

               motionHandler = mh;

               // TODO this is lazy - the mh should be
               // allowed to say if it wants a complete repaint or
               // just an image without itself to paint on.
               canvas.repaint();

            }
         }
      }
   }



   public void mousePressed(MouseEvent e) {
      down = true;

      motionHandler = null;

      readButton(e);
      readPosition(e);
      readPressPosition(e);

      // long tp = e.getWhen();
     // periodDownToDown = tp - timeDown;
     // timeDown = tp;

      activeHandler = null;

      for (int i = 0; i < nHandler; i++) {
         BaseMouseHandler mh = handlers[i];

         if (mh.isActive()) {

            mh.setClaimUndecided();

            mh.init(this);
            if (mh.isIn()) {
               activeHandler = mh;
               break;
            }
         }
      }

      if (activeHandler != null) {
         activeHandler.applyOnDown(this);
      }

      for (BaseMouseHandler mh : handlers) {
            if (mh == activeHandler) {

            } else if (mh != null) {
               mh.missedPress(this);
            }
      }

   }



   public void mouseDragged(MouseEvent e) {
      if (!down) {
         return;
      }
      readPosition(e);

      if (activeHandler == null) {
         for (int i = 0; i < nHandler; i++) {
            BaseMouseHandler mh = handlers[i];

            if (mh.isActive()) {
               if (mh.isOut()) {
                  // eliminated itself;

               } else {
                  mh.advance(this);
                  if (mh.isIn()) {
                     activeHandler = mh;
                     break;
                  }
               }
            }
         }
      }

      if (activeHandler != null) {
         activeHandler.applyOnDrag(this);

         if (activeHandler.getRepaintStatus() == BaseMouseHandler.FULL) {
            requestRepaint();

         } else if (activeHandler.getRepaintStatus() == BaseMouseHandler.BUFFERED) {
            // should do some ting more economical here EFF
            requestRepaint();

         } else {
            // nothing to do...
         }
      }
   }



   public void mouseReleased(MouseEvent e) {
      if (!down) {
         return;
      }

      readPosition(e);

      if (activeHandler == null) {
         for (int i = 0; i < nHandler; i++) {
            BaseMouseHandler mh = handlers[i];
            if (mh.isActive()) {
               if (mh.isOut()) {

               } else {
                  mh.release(this);
                  if (mh.isIn()) {
                     activeHandler = mh;
                     break;
                  }
               }
            }
         }
      }

      if (activeHandler != null) {
         activeHandler.applyOnRelease(this);
      }

      activeHandler = null;
      down = false;
      requestRepaint();

      canvas.fixRanges();

      updateCanvasDimensions(); // EFF ?? here
   }



   private void readPosition(MouseEvent e) {
      xCurrent = e.getX();
      yCurrent = e.getY();
   }


   private void readPressPosition(MouseEvent e) {
      xDown = e.getX();
      yDown = e.getY();
   }



   private void readButton(MouseEvent e) {
      button = MouseUtil.getButton(e);
   }



   public int getButton() {
      return button;
   }


   public boolean isDown() {
      return down;
   }


   int getX() {
      return xCurrent;
   }


   int getY() {
      return yCurrent;
   }


   int getXDown() {
      return xDown;
   }


   int getYDown() {
      return yDown;
   }



   void echoPaint(Graphics2D g) {
      if (activeHandler != null) {

         activeHandler.echoPaint(g);

         activeHandler.setRepaintStatus(BaseMouseHandler.NONE);

      } else if (motionHandler != null) {
         motionHandler.echoPaint(g);

         // activeHandler.setRepaintStatus(MouseHandler.NONE);

      }

   }



   // TODO should these go via mouse??
   void boxSelected(int x0, int y0, int x1, int y1) {
      canvas.boxSelected(x0, y0, x1, y1);
   }


   void initializeZoom(int xc, int yc) {
	   canvas.initializeZoom(xc, yc);
   }

   void dragZoom(double fx, double fy, int xc, int yc) {
	   canvas.dragZoom(fx, fy, xc, yc);
   }

   void zoom(double fac, int xc, int yc) {
      canvas.zoom(fac, xc, yc);
   }


   void zoom(double xfac, double yfac, int xc, int yc) {
      canvas.zoom(xfac, yfac, xc, yc);
   }



   void trialPan(int xfrom, int yfrom, int xto, int yto) {
      canvas.trialPan(xfrom, yfrom, xto, yto);
   }


   void permanentPan(int xfrom, int yfrom, int xto, int yto) {
      canvas.permanentPan(xfrom, yfrom, xto, yto);
   }


   public void dragRollRotate(int pdx, int pdy) {
	  canvas.dragRollRotate(pdx, pdy);
   }

   public void dragZRotate(int pdx, int pdy) {
		  canvas.dragZRotate(pdx, pdy);
   }

   public void initializeRotation(int ix, int iy) {
	   canvas.initializeRotation(ix, iy);
   }

   public void initializeRotation(double x, double y, double z) {
	   canvas.initializeRotation(x, y, z);
   }

   public void restoreAA() {
	   canvas.restoreAA();
   }


}
