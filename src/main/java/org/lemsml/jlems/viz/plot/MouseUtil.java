package org.lemsml.jlems.viz.plot;

import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;

import org.lemsml.jlems.core.logging.E;


public final class MouseUtil {
   
   public final static int LEFT = 1;
   public final static int MIDDLE = 2;
   public final static int RIGHT = 3;
    
   
   private MouseUtil() {
	   
   }
   
   public static int getButton(MouseEvent e) {   
      int modif = e.getModifiers();
      int rbutton = 0;

      if (modif == 0 || (modif & InputEvent.BUTTON1_MASK) != 0) {
         rbutton = LEFT;

      } else if ((modif & InputEvent.BUTTON2_MASK) != 0) {
         rbutton = MIDDLE;

      } else if ((modif & InputEvent.BUTTON3_MASK) != 0) {
         rbutton = RIGHT;

      } else if ((modif & InputEvent.SHIFT_MASK) != 0) {
         rbutton = MIDDLE;

      } else if ((modif & InputEvent.CTRL_MASK) != 0) {
         rbutton = RIGHT;

      } else {
         E.info("unknown button mask: " + modif + " "
               + "knowns mask (b1, b2 b3, shift, ctrl, alt ,meta): " + InputEvent.BUTTON1_MASK
               + " " + InputEvent.BUTTON2_MASK + " " + InputEvent.BUTTON3_MASK + " "
               + InputEvent.SHIFT_MASK + " " + InputEvent.CTRL_MASK + " " + InputEvent.ALT_MASK
               + " " + InputEvent.META_MASK);
         rbutton = LEFT;
      }
      return rbutton;
   }

}
