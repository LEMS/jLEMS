package org.lemsml.jlems.viz.plot;

import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;


final class LeftRightSplitPanelUI extends BasicSplitPaneUI {

   AboveBelowSplitPanel absp;

   GraphColors gcols;


   LeftRightSplitPanelUI(AboveBelowSplitPanel ap, GraphColors gc) {
      super();

      gcols = gc;

      absp = ap;
   }



   public static ComponentUI createUI(JComponent jcomponent) {
         return new LeftRightSplitPanelUI(null, null);
   }

   public BasicSplitPaneDivider createDefaultDivider() {
      return new VerticalAxisGraphDivider(this, absp, gcols);
   }


   public void paint(Graphics graphics, JComponent jcomponent) {
	   // nothing to do
   }


   protected void uninstallDefaults() {
	   // nothing to do
   }

}

