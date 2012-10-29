package org.lemsml.jlemsviz.plot;

import java.awt.GridLayout;

import javax.swing.JSplitPane;
import javax.swing.border.EmptyBorder;

public class LeftRightSplitPanel extends BasePanel {
   static final long serialVersionUID = 1001;

   JSplitPane jsp;

   GraphColors gcols;

   public LeftRightSplitPanel(AboveBelowSplitPanel c1,
                    AboveBelowSplitPanel c2, GraphColors gc) {
      super();
      gcols = gc;

      boolean CONTINUOUS_LAYOUT = true;

      setLayout(new GridLayout(1, 1, 0, 0));
      jsp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
			   CONTINUOUS_LAYOUT, c1, c2);

      jsp.setUI(new LeftRightSplitPanelUI(c1, gcols));

      jsp.setBorder(new EmptyBorder(0, 0, 0, 0));
      jsp.setDividerSize(3);
      add(jsp);
   }


   public void setResizeWeight(double d) {
      jsp.setResizeWeight(d);
   }


   public void applyLAF() {
      // should be done after adding components - bug if you ask me.
      //       cspui.setOwnDivider();
   }


}


