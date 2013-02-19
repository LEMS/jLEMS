
package org.lemsml.jlems.viz.plot;

import java.awt.Graphics;

import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

class HorizontalAxisGraphDivider extends BasicSplitPaneDivider {
   static final long serialVersionUID = 1001;

   GraphColors gcols;

   HorizontalAxisGraphDivider(BasicSplitPaneUI bspui, GraphColors gc) {
      super(bspui);
      gcols = gc;
      setBorder(new EmptyBorder(0, 0, 0, 0));
   }


   public void paint(Graphics g) {
      int w = getWidth();
      int h = getHeight();

      g.setColor(gcols.getBorderBg());
      g.fillRect(0, 0, w, h);

      
      /*
      g.setColor(gcols.getGraphBg());
      g.fillRect(0, 0, w, h);

      g.setColor(gcols.getBorderFg());
      g.drawLine(0, h-1, w, h-1);
      */
   }


}

