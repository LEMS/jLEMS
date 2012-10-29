package org.lemsml.jlemsviz.plot;



import java.net.URL;
import java.util.HashMap;

import org.lemsml.jlemsviz.plot.images.IconRoot;
  

public class IconLoader {

//   static IconRoot iconRoot = new IconRoot();

   static HashMap<String, DImageIcon> icons;

   static {
      icons = new HashMap<String, DImageIcon>();
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

      if (imgURL != null) {
         return new DImageIcon(imgURL);

      } else {
         E.error("Couldn't find file: " + name);
         return null;
      }
   }


}
