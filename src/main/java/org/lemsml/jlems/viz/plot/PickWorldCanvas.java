package org.lemsml.jlems.viz.plot;
 

import java.awt.Color;
import java.awt.Graphics2D;

import org.lemsml.jlems.core.logging.E;
 

public class PickWorldCanvas extends WorldCanvas {

   static final long serialVersionUID = 1001;

   PickStore pickStore;
   Builder builder;
   PickHandler pickHandler;

   BuildPaintInstructor buildPaintInstructor;

   GridPainter gridPainter;
   
   PaintListener paintListener = null;

   boolean drawGrid = true;


   public PickWorldCanvas(boolean interact) {
      super(interact);

      pickStore = new PickStore();
      builder = new Builder(getPainter(), pickStore);
      pickHandler = new PickHandler(pickStore, getWorldTransform());
     
      // prependHandler(pickHandler);
      
      gridPainter = new GridPainter();

   }


   public void setXAxisLabel(String s) {
      gridPainter.setXAxisLabel(s);
   }
   
   
   public void setBg(String str) {
	   SColor sc = new SColor(str);
	   setBg(sc.getColor());
   }

   
   public void setBg(Color c) {
      super.setBg(c);
      if (gridPainter != null) {
         gridPainter.setGridBackground(c);
      }
   }


   public void setNoGrid() {
      gridPainter = null;
   }

   public void setShowGrid(boolean b) {
	   drawGrid = b;
   }


   public void setBuildPaintInstructor(BuildPaintInstructor pi) {
      buildPaintInstructor = pi;
      if (pi instanceof PickListener) {
         setPickListener((PickListener)pi);
      }
   }


   public void setPickListener(PickListener pl) {
      pickHandler.setPickListener(pl);
   }


   public void setGridColor(Color c) {
	   if (gridPainter != null) {
		   gridPainter.setGridColor(c);
	   }
   }

   public void setAxisColor(Color c) {
	   if (gridPainter != null) {
		   gridPainter.setAxisColor(c);
	   }
   }


   public void prePaint(Graphics2D g) {
      builder.clear();
      builder.setCanvasSize(getWidth(), getHeight());

      if (drawGrid && gridPainter != null) {
         gridPainter.paint(painter);
      }

   }


   public void postPaint(Graphics2D g) {
      if (mouse.isDown()) {

      } else {
         pickHandler.echoPaint(painter, showToolTips());
      }
   }


   public void paint2D(Graphics2D g) {

      applyAAPreference(g);

      if (buildPaintInstructor != null) {
         buildPaintInstructor.instruct(painter, builder);

      } else if (paintInstructor != null) {
         paintInstructor.instruct(painter);
      }
      
      if (paintListener != null) {
    	  paintListener.painted();
      }
   }


   public void attach(Object obj) {
      boolean done = false;

      if (obj instanceof BuildPaintInstructor) {
         setBuildPaintInstructor((BuildPaintInstructor)obj);
         done = true;

      } else if (obj instanceof PaintInstructor) {
         setPaintInstructor((PaintInstructor)obj);
         done = true;
      }

      if (obj instanceof PickListener) {
         setPickListener((PickListener)obj);
         done = true;
      }

      if (!done) {
         E.error(" - cant attach " + obj + " to a PickWorldCanvas");
      }

   }


   public void setOnGridAxes() {
      gridPainter.setOnGridAxes();
   }


   public void reframe() {
      if (buildPaintInstructor != null) {
    	  Box box = buildPaintInstructor.getLimitBox(painter);
          frameToBox(box);
      
      } else if (paintInstructor != null) {
    	  Box box = paintInstructor.getLimitBox();
          frameToBox(box);
      
      } else {
        E.shortWarning("no paint instructor?");
      } 
    
   }


public void addPaintListener(PaintListener pl) {
	paintListener = pl;
	
}

 




}
