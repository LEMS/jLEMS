package org.lemsml.jlems.viz.plot;

  
 
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;

import org.lemsml.jlems.core.logging.E;
 

public final class Painter {


   private WorldTransform worldTransform;
   private Graphics2D g;


   private final BasicStroke normalStroke;
   private BasicStroke dashedStroke;

   private final PointPainter pointPainter;

   /*
    * private LinePainter lp = new LinePaniter(); private AreaPainter ap = new
    * AreaPaniter(); private TextPainter tp = new TextPaniter();
    */



   AffineTransform upTransform;
   AffineTransform normalTransform;


   private Color[] colorTable;
   private double ctMin;
   private double ctMax;


   private Box wkBox;


   public Painter(WorldTransform transform) {
      super();
      setWorldTransform(transform);
      pointPainter = new PointPainter();

      normalStroke = new BasicStroke((float)1.);

      upTransform = new AffineTransform();
      upTransform.setToRotation(Math.PI / 2.);
      normalTransform = new AffineTransform();

   }


   PointPainter getPointPainter() {
      return pointPainter;
   }


   public boolean isShowing(double x, double y) {
      return worldTransform.isShowing(x, y);
   }


   public void reframe(Box box) {
      worldTransform.reframe(box);
   }


   public int getCanvasWidth() {
      return worldTransform.getCanvasWidth();
   }

   public double getWorldCanvasWidth() {
      return worldTransform.getWorldCanvasWidth();
   }

   public int getCanvasHeight() {
      return worldTransform.getCanvasHeight();
   }


   public Size getPixelSize() {
      return worldTransform.getPixelSize();
   }

   
   public double getPixelArea() {
	   return worldTransform.getPixelArea();
   }



   public boolean isOnCanvas(Position p) {
      return worldTransform.isOnCanvas(p.getX(), p.getY());
   }


   public boolean isOnCanvas(double x, double y) {
      return worldTransform.isOnCanvas(x, y);
   }


   public boolean intIsOnCanvas(int x, int y) {
      return worldTransform.intIsOnCanvas(x, y);
   }


   public void setPixelScalingFromTop(double d) {
      worldTransform.setPixelScalingFromTop(d);
   }


   public double[] getXYXYLimits() {
      return worldTransform.getXYXYLimits();
   }


   void setWorldTransform(WorldTransform transform) {
      worldTransform = transform;
   }


   public void setGraphics(Graphics2D g2d) {
      g = g2d;
      pointPainter.setGraphics(g2d);
   }

   public void setAntialias(boolean b) {
	   if (b) {
		   g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	   }
   }
   

   public Graphics2D getGraphics() {
      return g;
   }



   public IntPosition pow(Position p) {
      return new IntPosition(powx(p.getX()), powy(p.getY()));
   }


   public double dxydp() {
      return worldTransform.pubDxDpix();
   }


   public int powx(double wx) {
      return worldTransform.powx(wx);
   }


   public int powy(double wy) {
      return worldTransform.powy(wy);
   }
   
   public float fpowx(double wx) {
	   return worldTransform.fpowx(wx);
   }


   public float fpowy(double wy) {
	   return worldTransform.fpowy(wy);
   }
	      


   public Position wop(IntPosition ip) {
      return new Position(wopx(ip.getX()), wopy(ip.getY()));
   }


   public double wopx(int x) {
      return worldTransform.wopx(x);
   }


   public double wopy(int y) {
      return worldTransform.wopy(y);
   }



   void setBasicStroke(double w) {
      if (w != 1.0) {
         g.setStroke(new BasicStroke((float)w));
      }
   }
   void setDashedStroke(double w) {
      if (dashedStroke == null) {
         float[] dashes= {8, 4, 8, 4};
            dashedStroke = new BasicStroke(1.f, BasicStroke.CAP_BUTT,
                                                        BasicStroke.JOIN_BEVEL, 10.f,
                                                        dashes, 0.f);
      }
      g.setStroke(dashedStroke);
   }


   void setNormalStroke() {
      g.setStroke(normalStroke);
   }

   private void resetStroke() {
      g.setStroke(normalStroke);
   }


   public void setStroke(BasicStroke bs) {
      g.setStroke(bs);
   }



   public void drawPixelLine(int x0, int y0, int x1, int y1) {
      g.drawLine(x0, y0, x1, y1);
   }

   void drawPolyline(double[] xp, double[] yp) {
      drawPolyline(xp, yp, xp.length);
   }

   public void drawPolyline(double[] xp, double[] yp, int np) {
      g.drawPolyline(worldTransform.intDeviceX(xp), worldTransform.intDeviceY(yp), np);
   }

   public void drawPolyline(double[] xp, double[] yp, int np, Color col, double width,
         boolean widthIsPixels) {
      setBasicStroke(width);
      if (col != null) {
    	  setColor(col);
      }
      g.drawPolyline(worldTransform.intDeviceX(xp), worldTransform.intDeviceY(yp), np);
      resetStroke();
   }

   public void drawMarks(double[] xp, double[] yp) {
	   for (int i = 0; i < xp.length; i++) {
		   int x = powx(xp[i]);
		   int y = powy(yp[i]);
		   g.fillRect(x, y, 1, 1);
	   }
   }

   public void drawMarks(double[] xp, double[] yp, int n) {
	   for (int i = 0; i < n; i++) {
		   int x = powx(xp[i]);
		   int y = powy(yp[i]);
		   g.fillRect(x, y, 1, 1);
	   }
   }

   public void drawIntMarks(double[] xp, double[] yp, int n, int w, int h) {
	   for (int i = 0; i < n; i++) {
		   int x = powx(xp[i]);
		   int y = powy(yp[i]);
		   g.fillRect(x, y, w, h);
	   }
   }

   public void drawAreaMarks(double[] xp, double[] yp, double diam) {
	   int n = xp.length;
	   for (int i = 0; i < n; i++) {
		   int x = powx(xp[i]);
		   int y = powy(yp[i]);
		   g.fill(new Ellipse2D.Double(x, y, diam, diam));

	   }
   }


   public void drawPolygon(double[] xp, double[] yp) {
      drawPolygon(xp, yp, xp.length);
   }



   public void drawPolygon(double[] xp, double[] yp, int np) {
      g.drawPolygon(worldTransform.intDeviceX(xp), worldTransform.intDeviceY(yp), np);
   }

   public void drawPolygon(double[] xp, double[] yp, int np, Color col, double width,
         boolean widthIsPixels) {
      setBasicStroke(width);
      setColor(col);
      g.drawPolygon(worldTransform.intDeviceX(xp), worldTransform.intDeviceY(yp), np);
      resetStroke();
   }

    public void fillPolygon(double[] xp, double[] yp, int np) {
	      g.fillPolygon(worldTransform.intDeviceX(xp), worldTransform.intDeviceY(yp), np);
    }

   public void fillPolygon(double[] xp, double[] yp, int np, Color col) {
      setColor(col);
      g.fillPolygon(worldTransform.intDeviceX(xp), worldTransform.intDeviceY(yp), np);
   }


   public void fillPolygon(double[] xp, double[] yp) {
      g.fillPolygon(worldTransform.intDeviceX(xp), worldTransform.intDeviceY(yp), xp.length);
   }


public void fillPolygon(Polypoint pp, int cfill, int cline) {
   int[] ix = worldTransform.intDeviceX(pp.getXPts());
   int[] iy = worldTransform.intDeviceY(pp.getYPts());
   g.setColor(new Color(cfill));
   g.fillPolygon(ix, iy, ix.length);

   g.setColor(new Color(cline));
   g.drawPolygon(ix, iy, ix.length);
}


   public void setColor(SColor sc) {
      g.setColor(sc.getColor());
   }


   public void setColor(Color c) {
      g.setColor(c);
   }

   public void setColorWhite() {
      g.setColor(Color.white);
   }

   public void setColorRed() {
      g.setColor(Color.red);
   }

   public void setColorGreen() {
      g.setColor(Color.green);
   }

   public void setColorBlue() {
      g.setColor(Color.blue);
   }

   public void setColorBlack() {
      g.setColor(Color.black);
   }

   public void setColorGray() {
      g.setColor(Color.gray);
   }

   public void setColorCyan() {
      g.setColor(Color.cyan);
   }
   public void setColorMagenta() {
      g.setColor(Color.magenta);
   }

   public void setColorYellow() {
      g.setColor(Color.yellow);
   }
   public void setColorOrange() {
      g.setColor(Color.orange);
   }


   public void setColorDarkGray() {
      g.setColor(Color.darkGray);
   }

   public void drawWhiteLine(double width, double[] xp, double[] yp) {
      setColor(Color.white);
      setBasicStroke(3.);
      drawPolyline(xp, yp, xp.length);
      resetStroke();
   }


   public void fillRectangle(double x, double y, Color c, int size) {
      int ix = powx(x);
      int iy = powy(y);
      fillPixelRectangle(ix, iy, c, size);
   }

   public void fillRectangle(double x1, double y1, double x2, double y2, Color c) {
      int ix = powx(x1);
      int iy = powy(y2);
      int dx = powx(x2) - ix + 1;
      int dy = powy(y1) - iy + 1;
      g.setColor(c);
      g.fillRect(ix, iy, dx, dy);
   }

   public void fillPixelRectangle(int ix, int iy, Color c, int size) {
      g.setColor(c);
      int hs = size / 2;
      g.fillRect(ix - hs, iy - hs, size, size);
   }



   public void drawLine(double x0, double y0, double x1, double y1, Color col, double width,
         boolean widthIsPixels) {
      setBasicStroke(width);
      setColor(col);
      drawLine(x0, y0, x1, y1);
      resetStroke();
   }


   public void drawLine(Position p1, Position p2) {
      drawLine(p1.getX(), p1.getY(), p2.getX(), p2.getY());
   }


   public void drawLine(double x0, double y0, double x1, double y1) {
      int ix0 = powx(x0);
      int iy0 = powy(y0);
      int ix1 = powx(x1);
      int iy1 = powy(y1);

      g.drawLine(ix0, iy0, ix1, iy1);
   }


   public void drawCenteredBox(Position pos, int hx, int hy) {
      int ixc = powx(pos.getX());
      int iyc = powy(pos.getY());

      g.drawRect(ixc - hx, iyc - hy, 2 * hx, 2 * hy);
   }

   public void fillCenteredBox(Position pos, int hx, int hy) {
      int ixc = powx(pos.getX());
      int iyc = powy(pos.getY());

      g.fillRect(ixc - hx, iyc - hy, 2 * hx, 2 * hy);
   }


   public void drawFixedSizeLine(double x, double y, Color c, int idx, int idy, int wfac) {
      g.setColor(c);
      resetStroke();
      int ix = powx(x);
      int iy = powy(y);

      setBasicStroke(wfac);

      g.drawLine(ix, iy, ix + idx, iy + idy);
      resetStroke();

   }

   public void drawText(String s, double x, double y) {
      int ix = powx(x);
      int iy = powy(y);
      g.drawString(s, ix, iy);
   }


   public void drawLineOffsetText(String s, double x, double y, int dx, int dy) {
	      int ix = powx(x);
	      int iy = powy(y);
	      g.drawLine(ix, iy, ix + dx, iy + dy);
	      g.drawString(s, ix + dx, iy + dy);
	   }


   public void drawString(String s, int x, int y) {
      g.drawString(s, x, y);
   }

   public void drawCenteredString(String s, int x, int y) {
      int w = stringWidth(s);

      g.drawString(s, x-w/2, y);
   }

   public void drawLabel(String s, double x, double y, Color c) {
      g.setColor(c);
      drawLabel(s, x, y);
   }


   public void drawUpLabel(String s, double x, double y, Color c) {
      g.setColor(c);
      drawUpLabel(s, x, y);
   }


   public void drawUpLabel(String s, double x, double y) {
      g.setTransform(upTransform);
      int ix = powx(x);
      int iy = powy(y);
      g.drawString(s, ix + 6, iy);
      g.setTransform(normalTransform);
   }



   public void drawLabel(String s, double x, double y) {
      int ix = powx(x);
      int iy = powy(y);
      drawLabelAt(s, ix, iy - 6);
   }

   public void drawCenteredLabel(String s, double x, double y) {
      int ix = powx(x);
      int iy = powy(y);
      int hw = stringWidth(s) / 2;
      drawLabelAt(s, ix - hw, iy - 6);
   }

   public void drawXCenteredYTopAlignedLabel(String s, double x, double y) {
      int ix = powx(x);
      int iy = powy(y);
      int hw = stringWidth(s) / 2;
      g.drawString(s, ix-hw, iy + 9);
   }

   public void drawLeftAlignedLabel(String s, double x, double y) {
      int ix = powx(x);
      int iy = powy(y);
      drawLabelAt(s, ix + 6, iy + 6);
      }


   public void drawRightAlignedLabel(String s, double x, double y) {
      int ix = powx(x);
      int iy = powy(y);
      int w = stringWidth(s);
      drawLabelAt(s, ix - w - 4, iy + 6);
      }

      public void drawXCenteredYBottomAlignedLabel(String s, double x, double y) {
         int ix = powx(x);
         int iy = powy(y);
         int hw = stringWidth(s) / 2;
         drawLabelAt(s, ix - hw, iy  - 2);
      }






      public void fillCenteredRectangle(double x, double y, double rx, double ry) {
      E.missing();
   }


   public void drawCircle(double x, double y, double r) {
      int ix1 = powx(x - r);
      int iy1 = powy(y + r);

      int ix2 = powx(x + r);
      int iy2 = powy(y - r);

      g.drawOval(ix1, iy1, ix2 - ix1, iy2 - iy1);
   }



   public void fillCircle(double x, double y, double r) {
      int ix1 = powx(x - r);
      int iy1 = powy(y + r);

      int ix2 = powx(x + r);
      int iy2 = powy(y - r);

      g.fillOval(ix1, iy1, ix2 - ix1, iy2 - iy1);
   }


   public void fillIntCircle(double x, double y, int r) {
      int ix = powx(x);
      int iy = powy(y);
      g.fillOval(ix-r, iy-r, 2*r, 2*r);
   }

   public void drawIntCircle(double x, double y, int r) {
      int ix = powx(x);
      int iy = powy(y);
      g.drawOval(ix-r, iy-r, 2*r, 2*r);
   }



   public void fillCenteredOval(double cx, double cy, double rx, double ry, Color cfill) {

      int ix1 = powx(cx - rx);
      int iy1 = powy(cy + ry);

      int ix2 = powx(cx + rx);
      int iy2 = powy(cy - ry);

      setColor(cfill);
      g.fillOval(ix1, iy1, ix2 - ix1, iy2 - iy1);
   }

   public void fillCenteredOval(Position pos, int ir) {

      int ixc = powx(pos.getX());
      int iyc = powy(pos.getY());


      g.fillOval(ixc-ir, iyc-ir, 2*ir, 2*ir);
   }





   public void drawCenteredOval(double cx, double cy, double rx, double ry,
         Color clin, double width, boolean widthIsPixels) {

      int ix1 = powx(cx - rx);
      int iy1 = powy(cy + ry);

      int ix2 = powx(cx + rx);
      int iy2 = powy(cy - ry);
      setBasicStroke(width);
      setColor(clin);
      g.drawOval(ix1, iy1, ix2 - ix1, iy2 - iy1);
      resetStroke();
   }


   public void drawCenteredOval(double cx, double cy, int hx, int hy) {
      int ixc = powx(cx);
      int iyc = powy(cy);
      g.drawOval(ixc - hx, iyc - hy, 2 * hx, 2 * hy);
   }

   public void drawCenteredOval(Position pos, int hx, int hy) {
      int ixc = powx(pos.getX());
      int iyc = powy(pos.getY());

      g.drawOval(ixc - hx, iyc - hy, 2 * hx, 2 * hy);
   }

   
   public void drawExactCenteredOval(Position pos, int hx, int hy) {
	      float fxc = fpowx(pos.getX());
	      float fyc = fpowy(pos.getY());
	      
	      Ellipse2D.Float oval = new Ellipse2D.Float(fxc - hx, fyc - hy, 2 * hx, 2 * hy);
	      g.draw(oval);
	   }
   
   public void fillExactCenteredOval(Position pos, int hx, int hy, Color cfill, Color cborder, double width) {
	      float fxc = fpowx(pos.getX());
	      float fyc = fpowy(pos.getY());
	      
	      Ellipse2D.Float oval = new Ellipse2D.Float(fxc - hx, fyc - hy, 2 * hx, 2 * hy);
	      g.setColor(cfill);
	      g.fill(oval);
	      g.setColor(cborder);
	      g.draw(oval);
	   }

   

   public void drawFilledOval(double cx, double cy, double rx, double ry, Color cfill,
         Color cborder, double width, boolean widthIsPixels) {

      int ix1 = powx(cx - rx);
      int iy1 = powy(cy + ry);

      int ix2 = powx(cx + rx);
      int iy2 = powy(cy - ry);

      setColor(cfill);
      g.fillOval(ix1, iy1, ix2 - ix1, iy2 - iy1);

      if (width > 0.5) {
         setBasicStroke(width);
         setColor(cborder);
         g.drawOval(ix1, iy1, ix2 - ix1, iy2 - iy1);
         resetStroke();
      }
   }

 
   public void drawFilledRectangle(double cx, double cy, int w, int h, Color cfill) {

        int ix = powx(cx);
        int iy = powy(cy);


        setColor(cfill);
        g.fillRect(ix - w/2, iy - h/2, w, h);
     }

   public void drawFilledRectangle(double cx, double cy, double rx, double ry, Color cfill,
         Color cborder, double width, boolean widthIsPixels) {

      int ix1 = powx(cx - rx);
      int iy1 = powy(cy + ry);

      int ix2 = powx(cx + rx);
      int iy2 = powy(cy - ry);

      setColor(cfill);
      g.fillRect(ix1, iy1, ix2 - ix1, iy2 - iy1);

      if (width > 0.5) {
         setBasicStroke(width);
         setColor(cborder);
         g.drawRect(ix1, iy1, ix2 - ix1, iy2 - iy1);
         resetStroke();
      }
   }

 public void drawRectangle(double cx, double cy, double rx, double ry,
       Color cborder, double width, boolean widthIsPixels) {

    int ix1 = powx(cx - rx);
    int iy1 = powy(cy + ry);

    int ix2 = powx(cx + rx);
    int iy2 = powy(cy - ry);

    setColor(cborder);
    g.drawRect(ix1, iy1, ix2 - ix1, iy2 - iy1);
 }

 public void drawFilledRoundedRectangle(double cx, double cy, double rx, double ry, double cr, Color cfill,
         Color cborder, double width, boolean widthIsPixels) {

      int ix1 = powx(cx - rx);
      int iy1 = powy(cy + ry);

      int ix2 = powx(cx + rx);
      int iy2 = powy(cy - ry);
 
      int icx = worldTransform.xpix(cr);
      int icy = worldTransform.ypix(cr);
      
      setColor(cfill);
      g.fillRoundRect(ix1, iy1, ix2 - ix1, iy2 - iy1, icx, icy);

      if (width > 0.5) {
         setBasicStroke(width);
         setColor(cborder);
         g.drawRoundRect(ix1, iy1, ix2 - ix1, iy2 - iy1, icx, icy);
         resetStroke();
      }
   }

 public void drawRoundedRectangle(double cx, double cy, double rx, double ry, double cr,
       Color cborder, double width, boolean widthIsPixels) {

    int ix1 = powx(cx - rx);
    int iy1 = powy(cy + ry);

    int ix2 = powx(cx + rx);
    int iy2 = powy(cy - ry);
    
    int icx = worldTransform.xpix(cr);
    int icy = worldTransform.ypix(cr);
    
    setColor(cborder);
    g.drawRoundRect(ix1, iy1, ix2 - ix1, iy2 - iy1, icx, icy);
 }



  public void drawRectangle(int[] xyxy) {
      g.drawRect(xyxy[0], xyxy[1], xyxy[2] - xyxy[0], xyxy[3] - xyxy[1]);
   }





   public void drawDashedRectangle(double cx, double cy,
                                                       double rx, double ry) {
     int ix1 = powx(cx - rx);
      int iy1 = powy(cy + ry);

      int ix2 = powx(cx + rx);
      int iy2 = powy(cy - ry);


      setDashedStroke(1.);

      g.drawRect(ix1, iy1, ix2 - ix1, iy2 - iy1);

      setNormalStroke();
   }

   public void drawOval(double cx, double cy, double rx, double ry, Color cborder, double width,
         boolean widthIsPixels) {

      int ix1 = powx(cx - rx);
      int iy1 = powy(cy + ry);

      int ix2 = powx(cx + rx);
      int iy2 = powy(cy - ry);

      setColor(cborder);
      g.drawOval(ix1, iy1, ix2 - ix1, iy2 - iy1);
   }



   public void drawCarrotSides(double xa, double ya, double ra, double xb, double yb, double rb) {

      double vy = xb - xa;
      double vx = -(yb - ya);
      double vl = Math.sqrt(vx * vx + vy * vy);
      if (vl <= 0.0) {
         vl = 1.e-6; // ***
      }
      vx /= vl;
      vy /= vl;
      drawLine(xa - ra * vx, ya - ra * vy, xb - rb * vx, yb - rb * vy);
      drawLine(xa + ra * vx, ya + ra * vy, xb + rb * vx, yb + rb * vy);


   }



   public int getLabelPoint(double[] xpts, double[] ypts) {
      int n = xpts.length;
      int iret = 0;
      for (int i = 0; i < n; i++) {
         if (isShowing(xpts[i], ypts[i])) {
            iret = i;
            break;
         }
      }
      return iret;
   }


   public void drawOffsetCenteredLabel(String s, double x, double y) {
      int ix = powx(x);
      int iy = powy(y);
      iy += 8;
      ix += 16;
      int sw = stringWidth(s);
      if (ix + sw > worldTransform.getCanvasWidth()) {
         ix = worldTransform.getCanvasWidth() - sw - 20;
      }

      g.setColor(Color.orange);
      g.fillRect(ix, iy-16, sw+10, 16);
      g.setColor(Color.black);
      g.drawRect(ix, iy-16, sw+10, 16);
      g.drawString(s, ix+5, iy-3);

   }

   public void drawLabelAt(String s, int ix, int iy) {

      int sw = stringWidth(s);

      g.setColor(Color.white);
      g.fillRect(ix, iy-16, sw+10, 16);
      g.setColor(Color.gray);
      g.drawRect(ix, iy-16, sw+10, 16);
      g.setColor(Color.black);
      g.drawString(s, ix+5, iy-3);

   }

   public void drawLegendItem(String s, int ioff) {
	   int sw = stringWidth(s);
	   int wid = worldTransform.getCanvasWidth();
	   g.drawString(s, wid - sw - 10, 12 * ioff + 20);
   }



   public int stringWidth(String s) {
         return g.getFontMetrics().stringWidth(s);
   }


   public void drawFilledTriangle(double x0, double y0, double x1,
                                                 double y1, double x2, double y2,
                                                  Color fillColor, Color color, double width, boolean b) {
      double[] xpts = {x0, x1, x2};
      double[] ypts = {y0, y1, y2};
      int[] ipx = worldTransform.intDeviceX(xpts);
      int[] ipy = worldTransform.intDeviceY(ypts);

      setColor(fillColor);
      g.fillPolygon(ipx, ipy, 3);
      setColor(color);
      if (b) {

      } else {
    	  E.warning("cant do world width xLines");
      }

      if (width > 0.5) {
         setBasicStroke(width);
         g.drawPolygon(ipx, ipy, 3);
         resetStroke();
      }
   }


   public void drawCircle(Position position, double radius) {
      drawCircle(position.getX(), position.getY(), radius);
    }


   public void fillCircle(Position position, double radius) {
      fillCircle(position.getX(), position.getY(), radius);
   }


   public void paintTrash() {
      paintTrash(false);
   }


   public void paintTrash(boolean live) {
      int w = worldTransform.getCanvasWidth();
      int h = worldTransform.getCanvasHeight();
      int[][] xy = {{w-22, w-4, w-7, w-19},
                    {h-22, h-22, h-6, h-6}};
      g.setColor(new Color(60, 190, 40));
      g.fillPolygon(xy[0], xy[1], xy[0].length);
      g.fillOval(w-19, h-8, 12, 5);


      if (live) {
         g.setColor(Color.red);
      } else {
         g.setColor(Color.darkGray);
      }
      //     g.drawPolygon(xy[0], xy[1], xy[0].length);
      g.fillOval(w-22, h-25, 18, 6);

      g.setColor(new Color(100, 240, 100));
      g.drawOval(w-22, h-25, 18, 6);

   }




   public void paintLiveTrash() {
      paintTrash(true);
   }


   public void fillBackground(Color c) {
      g.setColor(c);
      g.fillRect(0, 0, getCanvasWidth(), getCanvasHeight());
   }


   public void fillIntRectangle(int x, int y, int cw, int ch, Color color) {
     g.setColor(color);
     g.fillRect(x, y, cw, ch);
   }


   public void drawCenteredPixelLine(double x, double y, int[] xpts, int[] ypts) {
      int cx = powx(x);
      int cy = powy(y);

      int[] xp = new int[xpts.length];
      int[] yp = new int[xpts.length];
      for (int i = 0; i < xpts.length; i++) {
         xp[i] = cx + xpts[i];
         yp[i] = cy + ypts[i];
      }
      g.drawPolyline(xp, yp, xp.length);
   }


   public void setIntColor(int icol) {
      setColor(new Color(icol));
   }
   
   public void drawCable(Position pa, Position pcenter, Position pb) {
	   drawCable(pa, pcenter, pb, null, 1, true);
   }
   
   public void drawCable(Position pa, Position pcenter, Position pb, Color c, double lw, boolean wip) {
      double ax = pa.getX();
      double ay = pa.getY();
      double bx = pb.getX();
      double by = pb.getY();
      double cx = pcenter.getX();
      double cy = pcenter.getY();

      drawHalfCable(cx, cy, bx-ax, by-ay, bx, by, 14, c, lw, wip);
      drawHalfCable(cx, cy, ax-bx, ay-by, ax, ay, 14, c, lw, wip);
   }



    public void drawHalfCable(double x0, double y0, double dx0, double dy0, double x1,
         double y1, int n, Color c, double lw, boolean wip) {

      double vx, vy, vl, dx, dy, dl, f, alp0, alp;

      double r = 0.03;
      double alpa = 0.1;
      double alpb = 0.4;

      double[] xp = new double[n];
      double[] yp = new double[n];

      xp[0] = x0;
      yp[0] = y0;
      dx = dx0;
      dy = dy0;
      for (int i = 1; i < n; i++) {
         alp0 = alpa + (alpb * i) / (n - 1);
         vx = x1 - xp[i - 1];
         vy = y1 - yp[i - 1];
         vl = Math.sqrt(vx * vx + vy * vy);
         alp = (i == 1 ? alp0 / 2 : (i == n - 1 ? 1. : alp0));

         dl = Math.sqrt(dx * dx + dy * dy);
         dx = (1. - alp) * dx / dl + alp * vx / vl;
         dy = (1. - alp) * dy / dl + alp * vy / vl;
         f = (vl - r) / (n - i);
         xp[i] = xp[i - 1] + f * dx;
         yp[i] = yp[i - 1] + f * dy;
      }
      drawPolyline(xp, yp, n, c, lw, wip);
   }

    
    
    
    
    
    
    
    
    
    


    public void setColorRange(double ca, double cb) {
       ctMin = ca;
       ctMax = cb;
    }

    public void setColorTable(Color[] ct) {
       colorTable = ct;
    }


    public void setDefaultColorTable() {
       colorTable = new Color[256];
       for (int i = 0; i < colorTable.length; i++) {
          colorTable[i] = new Color(i, i, i);
       }

    }



   public void drawColoredCells(double[][][] mesh, double[] dat) {
      int nel = dat.length;

      if (colorTable == null) {
          setDefaultColorTable();
      }

      double dc = ctMax - ctMin;
      if (dc <= 0.) {
         dc = 1.;
      }
      for (int i = 0; i < nel; i++) {
         double[] xb = mesh[i][0];
         double[] yb = mesh[i][1];


         double fc = (dat[i] - ctMin) / dc;
         if (fc < 0.) {
            fc = 0.;
         }
         int ic = (int)(255 * fc);
         if (ic > 255) {
            ic = 255;
         }
         if (ic < 0) {
            ic = 0;
         }
         fillPolygon(xb, yb, xb.length, colorTable[ic]);
      }
   }


   
   public void drawColoredCells(double[][][] mesh, double[] dat, boolean[] mask) {
	      int nel = dat.length;

	      if (colorTable == null) {
	          setDefaultColorTable();
	      }

	      double dc = ctMax - ctMin;
	      if (dc <= 0.) {
	         dc = 1.;
	      }
	      for (int i = 0; i < nel; i++) {
	    	  if (mask[i]) {
	         double[] xb = mesh[i][0];
	         double[] yb = mesh[i][1];


	         double fc = (dat[i] - ctMin) / dc;
	         if (fc < 0.) {
	            fc = 0.;
	         }
	         int ic = (int)(255 * fc);
	         if (ic > 255) {
	            ic = 255;
	         }
	         if (ic < 0) {
	            ic = 0;
	         }
	         fillPolygon(xb, yb, xb.length, colorTable[ic]);
	    	  }
	    	 }
	   }

   
   
   
   
   
   
   public void paintLegend(int ileg, String s) {
      int w = worldTransform.getCanvasWidth();
      g.drawString(s, w-80, 30 + 20 * ileg);
   }



   // 3D

   public double getXProj(double[] c) {
	   return xProj(c[0], c[1], c[2]);
   }

   public double getYProj(double[] c) {
	   return yProj(c[0], c[1], c[2]);
   }

   public double getZProj(double[] c) {
	   return zProj(c[0], c[1], c[2]);
   }

   public double getXProj(float[] c) {
	   return xProj(c[0], c[1], c[2]);
   }

   public double getYProj(float[] c) {
	   return yProj(c[0], c[1], c[2]);
   }

   public double getZProj(float[] c) {
	   return zProj(c[0], c[1], c[2]);
   }



   public double getZProj(double x, double y, double z) {
	   return zProj(x, y, z);
   }


   public int getXProjPixel(double x, double y, double z) {
	   double wx = xProj(x, y, z);
	   int ix = powx(wx);
	   return ix;
   }

   public int getYProjPixel(double x, double y, double z) {
	   double wy = yProj(x, y, z);
	   int iy = powy(wy);
	   return iy;
   }



   double xProj(double x, double y, double z) {
	   return worldTransform.xProj(x, y, z);
   }

   double yProj(double x, double y, double z) {
	   return worldTransform.yProj(x, y, z);
   }

   private double zProj(double x, double y, double z) {
	   return worldTransform.zProj(x, y, z);
   }



   public void draw3DPoint (double x, double y, double z) {
      g.drawOval (powx(xProj(x,y,z)) - 2,  powy(yProj(x,y,z)) - 2, 4, 4);
   }


   public void fill3DOval (double x, double y, double z,
			  int hw, int hh) {
	   g.fillOval(powx(xProj(x,y,z)) - hw,  powy(yProj(x,y,z)) - hh, 2*hw, 2 * hh);
		  //************** missing code - recopy!!!!!
   }

   public void draw3DCircle (double x, double y, double z,
				   double r) {
      drawCircle(xProj(x,y,z), yProj(x,y,z), r);

   }

   public void fill3DCircle (double x, double y, double z,
		   double r) {
	   fillCircle(xProj(x,y,z), yProj(x,y,z), r);

}


   public void draw3DMark(double x, double y, double z, int ityp, int isize) {
		   int ix = powx(xProj(x, y, z));
		   int iy = powy(yProj(x, y, z));
		   // TODO change mark accorting to ityp;
		   g.drawLine(ix-isize, iy, ix+isize, iy);
		   g.drawLine(ix, iy-isize, ix, iy+isize);
}
   /*
   public void draw3DHalfLine(double xa, double ya, double za,
				                    double xb, double yb, double zb) {
      drawHalfLine(xProj(xa,ya,za), yProj(xa,ya,za),
		       xProj(xb,yb,zb), yProj(xb,yb,zb));
   }
*/


   public void draw3DZOffsetLine(double xa, double ya, double za,
					                   double xb, double yb, double zb,
					                   double z0, double dpdz) {
      double zpa = zProj(xa, ya, za);
      double zpb = zProj(xb, yb, zb);
      drawLine(xProj(xa,ya,za) + dpdz * (zpa-z0), yProj(xa,ya,za),
		xProj(xb,yb,zb) + dpdz * (zpb - z0), yProj(xb,yb,zb));
   }



   void drawOutline(double xa, double ya, double ra,
			              double xb, double yb, double rb) {

     double vy = xb - xa;
     double vx = -(yb - ya);
     double vl = Math.sqrt (vx * vx + vy * vy);
     if (vl <= 0.0) {
    	 vl = 1.e-6; // ***
     }
     vx /= vl;
     vy /= vl;

     drawLine (xa - ra * vx, ya - ra * vy,  xb - rb * vx, yb - rb * vy);
     drawLine (xa + ra * vx, ya + ra * vy,  xb + rb * vx, yb + rb * vy);

     drawLine (xa - ra * vx, ya - ra * vy, xa + ra * vx, ya + ra * vy);
     drawLine (xb - rb * vx, yb - rb * vy, xb + rb * vx, yb + rb * vy);
  }



   void drawSides(double xa, double ya, double ra, double xb, double yb, double rb) {

		double vy = xb - xa;
		double vx = -(yb - ya);
		double vl = Math.sqrt(vx * vx + vy * vy);
		if (vl <= 0.0) {
			vl = 1.e-6; // ***
		}
		vx /= vl;
		vy /= vl;

		drawLine(xa - ra * vx, ya - ra * vy, xb - rb * vx, yb - rb * vy);
		drawLine(xa + ra * vx, ya + ra * vy, xb + rb * vx, yb + rb * vy);
	}




   void fillOutline(double xa, double ya, double ra,
			              double xb, double yb, double rb) {

     double vy = xb - xa;
     double vx = -(yb - ya);
     double vl = Math.sqrt (vx * vx + vy * vy);
     if (vl <= 0.0) {
    	 vl = 1.e-6; // ***
     }
     vx /= vl;
     vy /= vl;
     drawLine(xa - ra * vx, ya - ra * vy,  xb - rb * vx, yb - rb * vy);
     drawLine(xa + ra * vx, ya + ra * vy,  xb + rb * vx, yb + rb * vy);

     double[] xx = {xa-ra*vx, xb-rb*vx, xb+rb*vx, xa+ra*vx, xa-ra*vx};
     double[] yy = {ya-ra*vy, yb-rb*vy, yb+rb*vy, ya+ra*vy, ya-ra*vy};

     fillPolygon(xx, yy, 5);
  }


   public void draw3DLine(double xa, double ya, double za,
			   double xb, double yb, double zb) {
	   		drawLine(xProj(xa,ya,za), yProj(xa,ya,za), xProj(xb,yb,zb), yProj(xb,yb,zb));
   }


   public void draw3DOutline(double xa, double ya, double za, double ra,
			         			   double xb, double yb, double zb, double rb) {
      drawOutline(xProj(xa,ya,za), yProj(xa,ya,za), ra,
		          xProj(xb,yb,zb), yProj(xb,yb,zb), rb);
   }


   public void fill3DSegment(double xa, double ya, double za, double ra,
			                       double xb, double yb, double zb, double rb) {
      fillOutline(xProj(xa,ya,za), yProj(xa,ya,za), ra,
		      xProj(xb,yb,zb), yProj(xb,yb,zb), rb);
   }

   public void draw3DCarrot(double xa, double ya, double za, double ra,
			   double xb, double yb, double zb, double rb) {
	   double x2a = xProj(xa,ya,za);
	   double y2a = yProj(xa,ya,za);
	   double x2b = xProj(xb,yb,zb);
	   double y2b = yProj(xb,yb,zb);
	   drawSides(x2a, y2a, ra, x2b, y2b, rb);
	   drawCircle(x2a, y2a, ra);
	   drawCircle(x2b, y2b, rb);
   }


   public void draw3DSegment(double xa, double ya, double za, double ra,
		   double xb, double yb, double zb, double rb) {
   double x2a = xProj(xa,ya,za);
   double y2a = yProj(xa,ya,za);
   double x2b = xProj(xb,yb,zb);
   double y2b = yProj(xb,yb,zb);
   drawOutline(x2a, y2a, ra, x2b, y2b, rb);
   }


  public void drawString3D(double x, double y, double z, String lbl) {
	   double x2a = xProj(x, y, z);
	   double y2a = yProj(x, y, z);
	   drawString(lbl, powx(x2a), powy(y2a));
  }

  public void drawString3DOffset(double x, double y, double z, String lbl, int idx, int idy) {
	   double x2a = xProj(x, y, z);
	   double y2a = yProj(x, y, z);
	   drawString(lbl, powx(x2a)+idx, powy(y2a)+idy);
 }

   public boolean visible3D (double x, double y, double z) {
      return worldTransform.visible3D(x, y, z);

   }

   public void draw3DMarks(float[][] ca, int n) {
	   for (int i = 0; i < n; i++) {
		   int x = powx(xProj(ca[i][0], ca[i][1], ca[i][2]));
		   int y =  powy(yProj(ca[i][0], ca[i][1], ca[i][2]));
		   g.fillRect(x, y, 1, 1);
	   }
   }


   public void drawSome3DMarks(float[][] ca, int n, double pas) {
	   for (double d = 0; d < n; d+= pas) {
		   int i = (int)d;
		   int x = powx(xProj(ca[i][0], ca[i][1], ca[i][2]));
		   int y =  powy(yProj(ca[i][0], ca[i][1], ca[i][2]));
		   g.fillRect(x, y, 1, 1);
	   }
   }


   public void draw3DIntMarks(float[][] ca, int n, int w, int h) {
	   for (int i = 0; i < n; i++) {
		   int x = powx(xProj(ca[i][0], ca[i][1], ca[i][2]));
		   int y =  powy(yProj(ca[i][0], ca[i][1], ca[i][2]));
		   g.fillRect(x, y, w, h);
	   }
   }


   public void draw3DAreaMarks(float[][] ca, int n, double diam) {
	   for (int i = 0; i < n; i++) {
		   int x = powx(xProj(ca[i][0], ca[i][1], ca[i][2]));
		   int y =  powy(yProj(ca[i][0], ca[i][1], ca[i][2]));
		   g.fill(new Ellipse2D.Double(x, y, diam, diam));

	   }
   }




   public void drawUpperSome3DMarks(float[][] ca, int n, double pas, double zp, double zd) {
	   for (double d = 0; d < n; d+= pas) {
		   int i = (int)d;
		   double z = zProj(ca[i][0], ca[i][1], ca[i][2]);
		   double f = ca[i][4];
		   if (z < (f * zd + (1.-f) * zp)) {
			   int x = powx(xProj(ca[i][0], ca[i][1], ca[i][2]));
			   int y =  powy(yProj(ca[i][0], ca[i][1], ca[i][2]));
			   g.fillRect(x, y, 1, 1);
		   }
	   }
   }


   public void drawUpper3DIntMarks(float[][] ca, int n, int w, int h, double zp, double zd) {
	   for (int i = 0; i < n; i++) {
		   double z = zProj(ca[i][0], ca[i][1], ca[i][2]);
		   double f = ca[i][4];
		   if (z < (f * zd + (1.-f) * zp)) {

		   int x = powx(xProj(ca[i][0], ca[i][1], ca[i][2]));
		   int y =  powy(yProj(ca[i][0], ca[i][1], ca[i][2]));
		   g.fillRect(x, y, w, h);
		   }
	   }
   }


   public void drawUpper3DAreaMarks(float[][] ca, int n, double diam, double zp, double zd) {
	   for (int i = 0; i < n; i++) {
		   double z = zProj(ca[i][0], ca[i][1], ca[i][2]);
		   double f = ca[i][4];
		   if (z < (f * zd + (1.-f) * zp)) {
		   int x = powx(xProj(ca[i][0], ca[i][1], ca[i][2]));
		   int y =  powy(yProj(ca[i][0], ca[i][1], ca[i][2]));
		   g.fill(new Ellipse2D.Double(x, y, diam, diam));
		   }

	   }
   }



   public void draw3DPolygon(double[][] da) {
	   int n = da.length;
	   int[] xp = new int[n];
	   int[] yp = new int[n];
	   for (int i = 0; i < n; i++) {
		   xp[i] = powx(xProj(da[i][0], da[i][1], da[i][2]));
		   yp[i] =  powy(yProj(da[i][0], da[i][1], da[i][2]));
	   }
	   g.drawPolygon(xp, yp, n);
   }





public void startBox() {
	wkBox = new Box();
}

public Box getBox() {
	return wkBox;
}

public void push3D(double x, double y, double z) {
	wkBox.push(xProj(x, y, z), yProj(x, y, z));
}
	public void push(double x, double y) {
		wkBox.push(x, y);
	}


	public void drawAxes() {
		// nothing to do
	}


	public int screenDistance2(double x, double y, double z, int x2, int y2) {
		int xs = powx(xProj(x, y, z));
		int ys =  powy(yProj(x, y, z));
		int dx = xs - x2;
		int dy = ys - y2;
		int d2 = dx * dx + dy * dy;
		return d2;
	}


}
