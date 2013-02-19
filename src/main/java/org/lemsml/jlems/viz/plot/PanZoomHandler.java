package org.lemsml.jlems.viz.plot;



final class PanZoomHandler extends BaseMouseHandler {

   private int xc;
   private int yc;

   private boolean zoomX;
   private boolean zoomY;

   
   boolean dragZoom = false;
   
   boolean movedMouse = false;
   
   
   
   public void init(Mouse m) {
      xc = m.getX();
      yc = m.getY();
      
      setClaimIn();
	  
      dragZoom = false;
      movedMouse = false;
     readZoomConditions(m);
    
   }

   
   public void readZoomConditions(Mouse m) {
	   zoomX = true;
	      zoomY = true;
	      
	      
	      
	      if (m.getX() < 30) {
	    	  zoomX = false;
	      }

	      if (m.getY() > m.getCanvasHeight() - 30) {
	    	  zoomY = false;
	      
	      }
	 
   }
   
   

   public void activate() {
      super.activate();
   }

   public void applyOnDown(Mouse m) {
	   m.initializeZoom(xc, yc);
   }

   
   public void advance(Mouse m) {
	   setClaimIn();
   }

   public void applyOnDrag(Mouse m) {
      int x = m.getX();
      int y = m.getY();

      int dx = x - xc;
      int dy = y - yc;
      if (dx * dx + dy * dy > 50) {
     	 movedMouse = true;
       }
      
      if (m.leftButton()) {
    	  m.trialPan(xc, yc, x, y);
    	  setFullRepaint();
    	  
    	  if (!movedMouse) {
    		  
    	  }
    	  
    	  
      } else if (m.rightButton()) {
    	  double rz = 100.;
          double zx = Math.exp(-(x - xc) / rz);
          double zy = Math.exp((y - yc) / rz);
          if (Math.abs(x - xc) * Math.abs(y - yc) > 5) {
        	  dragZoom = true;
          }

          if (!zoomX) {
        	  zx = 1.;
          }
          if (!zoomY) {
        	  zy = 1.;
          }
          m.dragZoom(zx, zy, xc, yc);
           
          setFullRepaint();
      }
   }

   public void applyOnScrollWheel(Mouse m) {
	 	int units = m.getScrollUnits();
	 	double rz = 7.;
	 	double zx = Math.exp(units / rz);
	 	double zy = Math.exp(units / rz);
	 	  	
	 	readZoomConditions(m);
	 	
	 	if (!zoomX) {
	 		zx = 1.;
	 	}
	 	if (!zoomY) {
	 		zy = 1.;
	 	}
	 	m.zoom(zx, zy, m.getX(), m.getY());
           
	 	setFullRepaint();
	 	
   }


   public void applyOnRelease(Mouse m) {
      int x = m.getX();
      int y = m.getY();
     
      double zf = 0.7;
      
      
      if (m.leftButton()) {
    	  m.permanentPan(xc, yc, x, y);
    	  
    	  if (!movedMouse) {
    		  m.zoom((zoomX ? zf : 1.), (zoomY ? zf : 1.), xc, yc);
    	  }

      } else if (m.rightButton()) {
    	  if (!movedMouse) {
    		  m.zoom((zoomX ? 1./zf : 1.), (zoomY ? 1./zf : 1.), xc, yc);
    	  }
      }
    	  
      m.restoreAA();
   }



}

 