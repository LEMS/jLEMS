package org.lemsml.jlems.viz.plot;


 

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import org.lemsml.jlems.core.logging.E;
 


public final class Mouse implements MouseListener, MouseMotionListener, MouseWheelListener {

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

   
   private int scrollUnits;
   
   private boolean down;
  // private boolean onCanvas;

  //  private long timeDown;
 // private long periodDownToDown;
 

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
         canvas.addMouseWheelListener(this);
      }
 
   }

   
   public void setHandler(BaseMouseHandler h) {
	   activeHandler = h;
   }
   

   public void setClickListener(ClickListener cl) {
      clickListener = cl;
   }


   public void detach() {
      canvas.removeMouseListener(this);
      canvas.removeMouseMotionListener(this);
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

      if (motionHandler != null) {
            if (motionHandler.motionChange(this)) {
 
               // TODO this is lazy - the mh should be
               // allowed to say if it wants a complete repaint or
               // just an image without itself to paint on.
               canvas.repaint();

            }
         }
     
   }



   public void mousePressed(MouseEvent e) {
      down = true;
 
      readButton(e);
      readPosition(e);
      readPressPosition(e);

      // long tp = e.getWhen();
     // periodDownToDown = tp - timeDown;
     // timeDown = tp;
 
      if (activeHandler != null) {
    	  activeHandler.init(this);
      }

      if (activeHandler != null) {
         activeHandler.applyOnDown(this);
      }
 

   }



   public void mouseDragged(MouseEvent e) {
      if (!down) {
         return;
      }
      readPosition(e);
 
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
 
      if (activeHandler != null) {
         activeHandler.applyOnRelease(this);
      }

    
      down = false;
      requestRepaint();

      canvas.fixRanges();

      updateCanvasDimensions(); // EFF ?? here
   }


   
   

   @Override	
   public void mouseWheelMoved(MouseWheelEvent e) {
	   // TODO Auto-generated method stub
	   scrollUnits = e.getWheelRotation();
	   readPosition(e);
	    
	      if (activeHandler == null) {
	    	  
	    	  
	      } else {
	    	 updateCanvasDimensions();
	    	  
	    	  activeHandler.applyOnScrollWheel(this);

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

   
   int getScrollUnits() {
	   return scrollUnits;
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
