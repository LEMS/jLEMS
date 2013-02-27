package org.lemsml.jlems.viz.plot;
 

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.JRadioButton;



public class IconRadioButton extends JRadioButton implements ActionListener {
   static final long serialVersionUID = 1001;
   
   IntActor intActor;
   int action;
   
   static Color cbg;
   static Color csel;

   static {
      cbg = new Color(200, 200, 200);
      csel = new Color(250, 250, 180);
   }

   public IconRadioButton(String iconName, int iact, IntActor actor) {
      super();
      
      setBackground(cbg);

      intActor = actor;
      action = iact;
      
      Icon icon = IconLoader.createImageIcon(iconName);
      setIcon(icon);
      

      setToolTipText(iconName);

      addActionListener(this);

      attachRollover();
   }
   


   private final void attachRollover() {
      

      RolloverEffect rollover = new RolloverEffect(this, 
						   RolloverEffect.ETCHED_DOWN, 
						   RolloverEffect.ETCHED_UP);
      addMouseListener(rollover);
   }
   


   public void showState() {
      if (isSelected()) {
	 setBackground(csel);
      } else {
	 setBackground(cbg);
      }
   }

   

   public void actionPerformed(ActionEvent aev) {
      intActor.intAction(action);
   }





}
