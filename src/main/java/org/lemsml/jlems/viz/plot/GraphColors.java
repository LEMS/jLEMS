package org.lemsml.jlems.viz.plot;

import java.awt.Color;

public class GraphColors {

   Color borderBg;
   Color borderFg;
   Color graphBg;



   public GraphColors() {
      borderBg = new Color(60, 60, 60);
      graphBg = new Color(40, 40, 40);
      borderFg = new Color(255, 255, 255);

   }


   public void setBorderBg(Color c) {
      borderBg = c;
   }

   public Color getBorderBg() {
      return borderBg;
   }

   public void setBorderFg(Color c) {
      borderFg = c;
   }

   public Color getBorderFg() {
      return borderFg;
   }

   public void setGraphBg(Color c) {
      graphBg = c;
   }

   public Color getGraphBg() {
      return graphBg;
   }




}
