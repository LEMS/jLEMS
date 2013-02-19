
package org.lemsml.jlems.viz.plot;

import java.awt.Graphics;

import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

class HorizontalAxisAxisDivider extends BasicSplitPaneDivider {
   static final long serialVersionUID = 1001;

   GraphColors gcols;

   HorizontalAxisAxisDivider(BasicSplitPaneUI bspui, GraphColors gc) {
      super(bspui);
      gcols = gc;
      setBorder(new EmptyBorder(0, 0, 0, 0));
   }


   public void paint(Graphics g) {
      int w = getWidth();
      int h = getHeight();

      g.setColor(gcols.getBorderBg());
      g.fillRect(0, 0, w, h);
   }


}

