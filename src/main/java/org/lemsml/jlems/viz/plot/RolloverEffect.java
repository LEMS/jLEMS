package org.lemsml.jlems.viz.plot;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.border.Border;



public final class RolloverEffect extends MouseAdapter {

   JComponent button;

   int inormal;
   int iactive;

   Border normalBorder;
   Border activeBorder;

   public final static int NONE = 0;
   public final static int ETCHED_DOWN = 1;
   public final static int ETCHED_UP = 2;
   public final static int RAISED = 2;
  
   Color bgColor;
   
   int pL;
   int pR;
   int pT;
   int pB;
   
   private boolean hasPadding;

   public RolloverEffect(JComponent buttonIn) {
      this(buttonIn, ETCHED_DOWN, ETCHED_UP);
   }



   public RolloverEffect(JComponent buttonIn, int norm, int active) {
	   super();
	  hasPadding = false;
      bgColor = buttonIn.getBackground();
      
      inormal = norm;
      iactive = active;

      button = buttonIn;


      makeBorders();


      if (button instanceof AbstractButton) {
         ((AbstractButton)button).setBorderPainted(true);
      
      } else if (button instanceof JMenu) {
         ((JMenu)button).setBorderPainted(true);

      } else if (button instanceof JCheckBox) {
         ((JCheckBox)button).setBorderPainted(true);

      } else if (button instanceof JPanel) {
         // ((JPanel)button).setBorderPainted(true);
      }

      mouseExited(null);

   }


   
   
   public void setPadding(int p) {
      setPadding(p, p, p, p);
   }
   
   
   public void setPadding(int pl, int pr, int pt, int pb) {
      pL = pl;
      pR = pr;
      pT  = pt;
      pB = pb;
      hasPadding = true;
      makeBorders();
      mouseExited(null);
    }


   public void setBg(Color c) {
      bgColor = c;
      makeBorders();
      mouseExited(null);
   }


 


   public void makeBorders() {
      normalBorder = makeBorder(inormal);
      activeBorder = makeBorder(iactive); 
   }



   public void mouseEntered(MouseEvent me) {
          button.setBorder(activeBorder);
 }


   public void mouseExited(MouseEvent me) {
          button.setBorder(normalBorder);
     }


   private Border makeBorder(int type) {
      Color c = bgColor;
      Color cbr = myBrighter(c);
      Color cdk = myDarker(c);


      Border ret = null;
      if (type == ETCHED_DOWN) {
         // ret = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
         ret = BorderFactory.createEtchedBorder(cbr, cdk);

      } else if (type == ETCHED_UP) {
         // ret = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
         ret = BorderFactory.createEtchedBorder(cdk, cbr);
      } else {
         ret = BorderFactory.createEmptyBorder(2, 2, 2, 2);
      }
      
      
      if (hasPadding) {
         Border bdr = BorderFactory.createEmptyBorder(pT, pL, pB, pR);
         ret = BorderFactory.createCompoundBorder(ret, bdr);
      }
        return ret;
   }


   public static Color myBrighter(Color c) {
      return linMod(c, 35);
   }


   public static Color myDarker(Color c) {
      return linMod(c, -35);
   }


   public static Color linMod(Color c, int d) {
      int r = c.getRed();
      int g = c.getGreen();
      int b = c.getBlue();

      r += d;
      g += d;
      b += d;
      r = (r > 0 ? (r < 255 ? r : 255) : 0);
      g = (g > 0 ? (g < 255 ? g : 255) : 0);
      b = (b > 0 ? (b < 255 ? b : 255) : 0);
      return new Color(r, g, b);
   }





}
