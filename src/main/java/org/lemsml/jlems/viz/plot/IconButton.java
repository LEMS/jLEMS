package org.lemsml.jlems.viz.plot;
 

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.JRadioButton;



public final class IconButton extends JRadioButton implements ActionListener {
   static final long serialVersionUID = 1001;
   IntActor intActor;
   int action;

   Color cbg;
   Color csel;

   public IconButton(String iconName, int iact, IntActor actor) {
      super();

      cbg = new Color(200, 200, 200);
      setBackground(cbg);

      intActor = actor;
      action = iact;

      Icon icon = IconLoader.createImageIcon(iconName);
      setIcon(icon);


      setToolTipText(iconName);

      addActionListener(this);

      attachRollover();
   }


   public void setBg(Color c) {
      cbg = c;
      setBackground(c);
   }

   public void attachRollover() {


      RolloverEffect rollover = new RolloverEffect(this,
						   RolloverEffect.ETCHED_DOWN,
						   RolloverEffect.ETCHED_UP);
      addMouseListener(rollover);
   }




   public void actionPerformed(ActionEvent aev) {
      intActor.intAction(action);
   }





}
