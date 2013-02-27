
package org.lemsml.jlems.viz.plot;

import java.awt.Graphics;

import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

class VerticalAxisGraphDivider extends BasicSplitPaneDivider {
   static final long serialVersionUID = 1001;

   boolean drawLine;
   AboveBelowSplitPanel absp;

      GraphColors gcols;

   VerticalAxisGraphDivider(BasicSplitPaneUI bspui, AboveBelowSplitPanel ap,
            GraphColors gc) {
      super(bspui);
      absp = ap;
      gcols = gc;
      setBorder(new EmptyBorder(0, 0, 0, 0));
      ap.setDependentDivider(this);
   }


   public void paint(Graphics g) {
      int w = getWidth();
      int h = getHeight();

//      g.setColor(gcols.getGraphBg());
 //     g.fillRect(0, 0, w, h);

      g.setColor(gcols.getBorderBg());
      g.fillRect(0, 0, w, h);

      
      /*
      g.setColor(gcols.getBorderFg());
      int iloc = absp.getDividerLocation();
      g.drawLine(0, 0, 0, iloc + 2);
      g.drawLine(0, iloc+2, getWidth(), iloc + 2);

      g.setColor(gcols.getBorderBg());
      g.fillRect(0, iloc+3, w, h - (iloc+3));
      */
   }


}

