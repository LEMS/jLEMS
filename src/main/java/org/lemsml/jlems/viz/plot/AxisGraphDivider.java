
package org.lemsml.jlems.viz.plot;

import java.awt.Color;

import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
 

class AxisGraphDivider extends BasicSplitPaneDivider {
   static final long serialVersionUID = 1001;

   ColorSet colorSet;

   AxisGraphDivider(BasicSplitPaneUI bspui) {
      super(bspui);
   }

   public void setColorSet(ColorSet cs) {
      colorSet = cs;
   }
   
   Color getBorderBackground() {
      Color ret = null;
      if (colorSet == null) {
         ret = Color.gray;
      } else {
      ret = colorSet.getBackground();
      }
      return ret;
   }

   
   Color getGraphBackground() {
      Color ret = Color.darkGray;
      if (colorSet != null) {
       ret =  colorSet.getDataBackground();
      }
      return ret;
   }


   Color getBorderForeground() {
      Color ret = Color.white;
      if (colorSet != null) {
       ret = colorSet.getForeground();
      }
      return ret;
   }
}

