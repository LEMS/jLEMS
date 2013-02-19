package org.lemsml.jlems.viz.plot;


import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JSplitPane;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicSplitPaneUI;


public class AboveBelowSplitPanel extends JSplitPane implements ComponentListener {

   static final long serialVersionUID = 1001;

   boolean drawDivider = false;

   BasePanel ctop;


   VerticalAxisGraphDivider dependentDivider;

   GraphColors gcols;

   public AboveBelowSplitPanel(BasePanel c1, BasePanel c2, GraphColors gc) {
      super(JSplitPane.VERTICAL_SPLIT, true, c1, c2);

      gcols = gc;
      ctop = c1;
 


      if (c2 instanceof CornerPanel) {
        setUI(new AboveBelowSplitAxisPanelUI(gc));
      } else {
         addComponentListener(this);
         c2.addComponentListener(this);

         // setUI(new BasicSplitPaneUI());
         setUI(new AboveBelowSplitPanelUI(gc));
      }

      
      setBorder(new EmptyBorder(0, 0, 0, 0));
      setDividerSize(3);
    
   }


   public void setDependentDivider(VerticalAxisGraphDivider agd) {
      dependentDivider = agd;
   }



   public void setBg(Color c) {
      setBackground(c);
   }

  


   public void componentHidden(ComponentEvent e) {
	   // ignore
   }


   public void componentMoved(ComponentEvent e) {
	   // ignore
   }


   public void componentResized(ComponentEvent e) {
         sliderMoved();
   }


   public void componentShown(ComponentEvent e) {
	   // ignore
   }



   public void applyLAF() {
      // should be done after adding components
      // cspui.setOwnDivider();
   }


 


   public void sliderMoved() {
	 
      // revalidate();
      if (dependentDivider != null) {
         dependentDivider.repaint();
      }

   }

  


   public void follow(AboveBelowSplitPanel absrc) {

     // ignoreMoves = true;

      int srcloc = absrc.getDividerLocation();
      int iloc = getDividerLocation();
      if (iloc != srcloc) {
         setDividerLocation(srcloc);
      }

      // ignoreMoves = false;
   }

}
