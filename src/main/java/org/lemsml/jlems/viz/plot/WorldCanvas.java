package org.lemsml.jlems.viz.plot;
 

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import org.lemsml.jlems.core.logging.E;
 

public class WorldCanvas extends BaseCanvas implements ModeSettable {

   static final long serialVersionUID = 1001;


   public static final String PAN = "pan";
   public static final String ALL = "all";
   public static final String EZOOM = "zoom";
   public static final String BOX = "box";
   public static final String ROLL = "roll";
   public static final String ETURN = "turn";
   public static final String MULTI = "all";
   
   public static final String ZOOMIN = "zoomIn";
   public static final String ZOOMOUT = "zoomOut";
   

   protected WorldTransform wt;
   protected Painter painter;
   protected PaintInstructor paintInstructor;
   protected Mouse mouse;

 

   private boolean antialias = false;
   private boolean tooltips = true;

   Box reframeBox;

   private boolean userAntialias;


   public WorldCanvas() {
      this(true);
   }


   public WorldCanvas(boolean interactive) {
      super();


      mouse = new Mouse(this, interactive);


      wt = new WorldTransform();
   

      painter = new Painter(wt);
 
      setMouseMode("pan");
   }


   public boolean isAntialiasing() {
	   return antialias;
   }

   public void setAntialias(boolean b) {
	   userAntialias = b;
	   antialias = b;
	   repaint();
   }

   public void restoreAA() {
	  	antialias = userAntialias;
   }


   public void setMode(String dom, String mod) {

      if (dom.equals("antialias")) {
         antialias = mod.equals("true");
         userAntialias = antialias;
    
         
      } else if (dom.equals("mouse")) {
         setMouseMode(mod);

      } else {
         E.error("unrecognized mode " + dom);
      }
      repaint();
   }



   public void setMode(String dom, boolean b) {
       if (dom.equals("antialias")) {
         antialias = b;
         userAntialias = b;
      
      } else if (dom.equals("labels")) {
         tooltips = b;

      } else if (dom.equals("showAll")) {
         reframe();

      } else {
         E.error("unrecognized mode " + dom);
      }
      repaint();
   }

 
   public void reluctantReframe() {
       if (reframeBox == null) {
          reframe();
       } else if (paintInstructor != null) {
          Box box = paintInstructor.getLimitBox();
          if (box != null && box.differentFrom(reframeBox, 0.15)) {
             frameToBox(box);
          }
       }
   }


   public void reframe() {
      if (paintInstructor == null) {
        E.shortWarning("no paint instructor?");
      } else {
      Box box = paintInstructor.getLimitBox();
      frameToBox(box);
   }
   }


   protected void frameToBox(Box box) {
      if (box == null) {
         E.warning("no limit box returned from " + paintInstructor.getClass().getName());


      } else if (box.hasData()) {
    	  System.out.println("framing " + getHeight());
    	  syncSize();
    	  
    	  reframeBox = box.makeCopy();
         box.pad();
         painter.reframe(box);
         repaint();
         
      } else {
         // ok? - mesage? TODO
      }

   }



   public boolean showToolTips() {
      return tooltips;
   }


   public void setMode(int imode) {
      E.missing();
   }


   public void addRangeListener(RangeListener rl) {
      wt.addRangeListener(rl);
   }



   public Painter getPainter() {
      return painter;
   }


   WorldTransform getWorldTransform() {
      return wt;
   }

 

   public void setClickListener(ClickListener cl) {
      mouse.setClickListener(cl);
   }

   	public void setRotationListener(RotationListener rl) {
   		wt.setRotationListener(rl);
   	}



   



   public final void setMouseMode(String s) {
     
 
      if (s.equals(PAN) || s.equals(ALL)) {
    	 setMouseHandler(new PanZoomHandler());
     

      } else if (s.equals(EZOOM)) {
    	  setMouseHandler(new ClickZoomHandler());

      } else if (s.equals(BOX)) {
    	  setMouseHandler(new BoxSelectionHandler());
    	  
      } else if (s.equals(ROLL)) {
    	setMouseHandler(new RollHandler());

      } else if (s.equals(ETURN)) {
         setMouseHandler(new TurnZoomHandler());

      } else if (s.equals(ZOOMIN)) {
    	  ClickZoomHandler czh = new ClickZoomHandler();
    	  setMouseHandler(czh);
    	  czh.activateIn();
    	  
      } else if (s.equals(ZOOMOUT)) {
    	  ClickZoomHandler czh = new ClickZoomHandler();
    	  setMouseHandler(czh);
    	  czh.activateOut();
      
      }  
   }


   public void setXRange(double[] lh) {
      setXRange(lh[0], lh[1]);
   }


   public void setYRange(double[] lh) {
      setYRange(lh[0], lh[1]);
   }



   public void ensureCovers(double[] xr, double[] yr) {
      wt.ensureCovers(xr[0], yr[0], xr[1], yr[1]);
   }


   public void setXRange(double low, double high) {
      wt.setXRange(low, high);
    }


   public void setYRange(double low, double high) {
      wt.setYRange(low, high);
     }


   public void setFixedAspectRatio(double ar) {
      wt.setFixedAspectRatio(ar);
   }


   public double[] getXRange() {
      return wt.getXRange();
   }


   public double[] getYRange() {
      return wt.getYRange();
   }


   public void viewAction(String s) {
      if (s.equals("frame")) {
         reframe();
      }
   }



   public final void setMouseHandler(BaseMouseHandler mh) {
      mouse.setHandler(mh);
   }


   public void fixRanges() {
      wt.fixRanges();
   }



   public void setPaintInstructor(PaintInstructor pi) {
      paintInstructor = pi;
   }

   
   public void syncSize() {
	   wt.setWidth(getWidth());
	   wt.setHeight(getHeight());
   }
   
    

   public void paintComponent(Graphics g0) {
	   syncSize();
	   
      g0.setColor(getDataBackground());
      g0.fillRect(0, 0, getWidth(), getHeight());
      Graphics2D g = (Graphics2D)g0;


      painter.setGraphics(g);

      prePaint(g);

      paint2D(g);

      postPaint(g);

      mouse.echoPaint(g);
 
   }


   public void prePaint(Graphics2D g) {
	   // nothing to do
   }

   public void postPaint(Graphics2D g) {
	   // nothing to do
   }


   public void applyAAPreference(Graphics2D g) {
	   if (antialias) {
         g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      } else {
         g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
      }

   }


   public void paint2D(Graphics2D g) {

      E.info("pw painting ");

      if (paintInstructor != null) {

         applyAAPreference(g);

         paintInstructor.instruct(painter);
      }

   }



   void boxSelected(int x0, int y0, int x1, int y1) {
      wt.setCanvasSize(getWidth(), getHeight());
      wt.boxSelected(x0, y0, x1, y1);
      repaint();
   }

   void initializeZoom(int xc, int yc) {
	   wt.initializeZoom(xc, yc);
   }

   void dragZoom(double fx, double fy, int xc, int yc) {
	   antialias = false;
	   wt.dragZoom(fx, fy, xc, yc);
   }


   void zoom(double fac, int xc, int yc) {
      wt.zoom(fac, xc, yc);
      repaint();
   }


   void zoom(double xfac, double yfac, int xc, int yc) {
      wt.zoom(xfac, yfac, xc, yc);
      repaint();
   }


   void trialPan(int xfrom, int yfrom, int xto, int yto) {
	   antialias = false;
      wt.trialPan(xfrom, yfrom, xto, yto);
      repaint();
   }


   void permanentPan(int xfrom, int yfrom, int xto, int yto) {
      wt.permanentPan(xfrom, yfrom, xto, yto);
      repaint();
   }


   void dragRollRotate(int px, int py) {
	   antialias = false;
	      wt.dragRollRotate(px, py);
	      repaint();
	   }

   void dragZRotate(int px, int py) {
	   antialias = false;
	   wt.dragZRotate(px, py);
	      repaint();
	   }

   public void turn(double d) {
	  wt.initializeRotation(getWidth() / 2, getHeight() / 2);
	  wt.zRotate(d / 180. * Math.PI);
	  repaint();
   }

   void initializeRotation(int px, int py) {
	   wt.initializeRotation(px, py);
   }


   void initializeRotation(double x, double y, double z) {
	   wt.initializeRotationLocal(x, y, z);
   }


   public void setColorRange(double cmin, double cmax) {
      painter.setColorRange(cmin, cmax);
   }

   public void setColorTable(Color[] ac) {
      painter.setColorTable(ac);
   }


   public int[] getIntPosition(Position pos) {
     return wt.getIntPosition(pos.getX(), pos.getY());
   }


   public double[][] getProjectionMatrix() {
	   return wt.getProjectionMatrix();
	}

   public void setRollCenter(double x, double y, double z) {
	   // rollHandler.setRollCenter(x, y, z);
   }


   public double[] get2Center() {
	   return wt.get2Center();
   }

   public double[] get3Center() {
	   return wt.get3Center();
   }



   // first four elements should be top row of matrix;
   public double[] getFourMatrix() {
	   double[][] pm = wt.getProjectionMatrix();

//	   double[] cen = wt.get3Center();

	   double[] cen2 = wt.get2Center();

	   double[] ret = new double[16];

	   	double sf = 3. * wt.getScale();

	   for (int i = 0; i < 3; i++) {
		   for (int j = 0; j < 3; j++) {
			   ret[4 * i + j] = sf * pm[i][j];
		   }
		   ret[4 * i + 3] = cen2[i];
	   }
	   ret[15] = 1.;
	   return ret;
   }


   public void setFourMatrix(double[] fm) {
	  double[][] pm = new double[3][3];
	  double[] cen = new double[3];

	  for (int i = 0; i < 3; i++) {
		   for (int j = 0; j < 3; j++) {
			   pm[i][j]= fm[4 * i + j];
		   }
		   cen[i] = fm[4 * i + 3];
	   }

	  double m3xx, m3xy, m3xz, m3yx, m3yy, m3yz, m3zx, m3zy, m3zz;
	  m3xx = pm[0][0]; m3xy = pm[0][1]; m3xz = pm[0][2];
      m3yx = pm[1][0]; m3yy = pm[1][1]; m3yz = pm[1][2];
      m3zx = pm[2][0]; m3zy = pm[2][1]; m3zz = pm[2][2];

	  double det = (m3xx * (m3yy * m3zz - m3zy * m3yz) -
			    m3xy * (m3yx * m3zz - m3zx * m3yz) +
			    m3xz * (m3yx * m3zy - m3zx * m3yy));

	  double rdet = Math.pow(det, 1./3.);
	   for (int i = 0; i < 3; i++) {
		   for (int j = 0; j < 3; j++) {
			   pm[i][j] /= rdet;
		   }
	   }

	   double[] c0 = {0., 0., 0.};

	   for (int i = 0; i < 3; i++) {
		   cen[i] /= rdet;
	   }

	   wt.setProjectionMatrix(pm);
	   wt.set3Center(c0);
	   wt.set2Center(cen);

	   wt.setScale(0.3 * rdet); // ADHOC

	   repaint();

   }
}
