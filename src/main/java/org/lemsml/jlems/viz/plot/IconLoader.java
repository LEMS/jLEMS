package org.lemsml.jlems.viz.plot;



import java.net.URL;
import java.util.HashMap;

import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.viz.plot.images.IconRoot;
  

public final class IconLoader {

//   static IconRoot iconRoot = new IconRoot();

   static HashMap<String, DImageIcon> icons;

   static {
      icons = new HashMap<String, DImageIcon>();
   }


   private IconLoader() {
	   
   }
   
   public static DImageIcon getImageIcon(String s) {
      DImageIcon ret = null;
      if (icons.containsKey(s)) {
         ret = icons.get(s);
         } else {
            ret = createImageIcon(s);
            icons.put(s, ret);
         }
      return ret;
    }


   public static DImageIcon createImageIcon(String namein) {
	   String name = namein;
	   if (name.endsWith(".gif") || name.endsWith(".png")) {
         // ok as is;
      } else {
         name = name + ".gif";
      }

      URL imgURL = IconRoot.class.getResource(name);

      
      DImageIcon ret = null;
      if (imgURL != null) {
         ret = new DImageIcon(imgURL);

      } else {
         E.error("Couldn't find file: " + name);
      }
      return ret;
   }


}
