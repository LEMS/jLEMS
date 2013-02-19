package org.lemsml.jlems.viz.plot;

import java.awt.Color;

import javax.swing.JPanel;

public class BasePanel extends JPanel implements DComponent {
   static final long serialVersionUID = 1001;
   
     

   
   public void setBg(Color c) {
      setBackground(c);
   }
   
   public void setTooltip(String s) {
      setToolTipText(s);
   }
   
}
