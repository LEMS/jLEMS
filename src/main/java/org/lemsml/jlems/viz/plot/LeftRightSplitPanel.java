package org.lemsml.jlems.viz.plot;

import java.awt.GridLayout;

import javax.swing.JSplitPane;
import javax.swing.border.EmptyBorder;

public class LeftRightSplitPanel extends JSplitPane {
   static final long serialVersionUID = 1001;

   JSplitPane jsp;

   GraphColors gcols;

   public LeftRightSplitPanel(AboveBelowSplitPanel c1,
                    AboveBelowSplitPanel c2, GraphColors gc) {
      super(JSplitPane.HORIZONTAL_SPLIT, true, c1, c2);
      gcols = gc;
  
      setUI(new LeftRightSplitPanelUI(c1, gcols));

      setBorder(new EmptyBorder(0, 0, 0, 0));
      setDividerSize(3);
   }

 
 


}


