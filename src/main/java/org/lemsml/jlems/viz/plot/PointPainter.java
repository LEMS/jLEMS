package org.lemsml.jlems.viz.plot;


import java.awt.Graphics2D;


class PointPainter {

   Graphics2D g;



   
   void setGraphics(Graphics2D g2d) {
      g= g2d;
   }



   /*
   void highlight(BenchPoint bp) {

      int xc = bp.getX();
      int yc = bp.getY();
      int hw = bp.getRX();
      int hh = bp.getRY();

      g.setColor(Color.green);

      g.drawRect(xc - hw, yc - hw, 2 * hw, 2* hh);
   }
   */

}
