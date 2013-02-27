package org.lemsml.jlems.viz.plot;

import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;


final class AboveBelowSplitPanelUI extends BasicSplitPaneUI {

GraphColors gcols;

   AboveBelowSplitPanelUI(GraphColors gc) {
      super();
      gcols = gc;
   }

   public static ComponentUI createUI(JComponent jcomponent) {
         return new AboveBelowSplitPanelUI(null);
   }

   
   public BasicSplitPaneDivider createDefaultDivider() {
      return new HorizontalAxisGraphDivider(this, gcols);
   }



/*
   public void paint(Graphics graphics, JComponent jcomponent) {
	   // nothing to do
   }


   protected void uninstallDefaults() {
	   // ignore
   }
*/
}

