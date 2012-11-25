package org.lemsml.jlemsviz.plot;


import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JSplitPane;
import javax.swing.border.EmptyBorder;


public class AboveBelowSplitPanel extends BasePanel implements ComponentListener {

   static final long serialVersionUID = 1001;

   AboveBelowSplitPanel follower;

   JSplitPane jSplitPane;

   boolean ignoreMoves = false;

   boolean drawDivider = false;

   BasePanel ctop;


   VerticalAxisGraphDivider dependentDivider;

   GraphColors gcols;

   public AboveBelowSplitPanel(BasePanel c1, BasePanel c2, GraphColors gc) {
      super();

      gcols = gc;
      ctop = c1;

      boolean CONTINUOUS_LAYOUT = true;

      setLayout(new GridLayout(1, 1, 0, 0));
      jSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, CONTINUOUS_LAYOUT, c1, c2);


      if (c2 instanceof CornerPanel) {
         jSplitPane.setUI(new AboveBelowSplitAxisPanelUI(gc));
      } else {
         jSplitPane.setUI(new AboveBelowSplitPanelUI(gc));
      }

      jSplitPane.setBorder(new EmptyBorder(0, 0, 0, 0));
      jSplitPane.setDividerSize(3);
      add(jSplitPane);

      if (DataDisplay.interactive) {
         c1.addComponentListener(this);
      }
   }


   public void setDependentDivider(VerticalAxisGraphDivider agd) {
      dependentDivider = agd;
   }



   public void setBg(Color c) {
      jSplitPane.setBackground(c);
   }


   public void setDividerSize(int n) {
      jSplitPane.setDividerSize(n);
   }


   public void setResizeWeight(double d) {
      jSplitPane.setResizeWeight(d);
   }


   public void componentHidden(ComponentEvent e) {
	   // ignore
   }


   public void componentMoved(ComponentEvent e) {
	   // ignore
   }


   public void componentResized(ComponentEvent e) {
      if (ignoreMoves) {

      } else {
         sliderMoved();
      }
   }


   public void componentShown(ComponentEvent e) {
	   // ignore
   }



   public void applyLAF() {
      // should be done after adding components
      // cspui.setOwnDivider();
   }



   public void setSplitPanelFollower(AboveBelowSplitPanel absp) {
      follower = absp;

      follower.follow(this);
   }


   public void sliderMoved() {
      if (follower != null) {
         follower.follow(this);
      }

      // revalidate();
      if (dependentDivider != null) {
         dependentDivider.repaint();
      }

   }



   public int getDividerLocation() {
      int idl = jSplitPane.getDividerLocation();
      if (idl < 0) {
         idl = getHeight() - 36; // ADHOC
      }

      return idl;
   }



   public void setDividerLocation(int dloc) {
      jSplitPane.setDividerLocation(dloc);
   }


   public void follow(AboveBelowSplitPanel absrc) {

      ignoreMoves = true;

      int srcloc = absrc.getDividerLocation();
      int iloc = jSplitPane.getDividerLocation();
      if (iloc != srcloc) {
         jSplitPane.setDividerLocation(srcloc);
      }

      ignoreMoves = false;
   }

}
