package org.lemsml.jlems.viz.plot;

import java.util.Locale;


public enum ColorNames {
   BLACK("#000000"),
   WHITE("#ffffff"),
   RED      ("#ff0000"),
   GREEN    ("#00ff00"),
   BLUE     ("#0000ff"),
   YELLOW   ("#ffff00"),
   MAGENTA  ("#ff00ff"),
   CYAN     ("#00ffff"),
   GREY     ("#a3a3a3"),
   GRAY     ("#a3a3a3"),
   NEARGRAY ("#b9b9b9"),
   MEDIUMMAGENTA("#a000a0"),
   PURPLE   ("#983aba"),
   LIGHTBLUE("#3737df"),
   COCOMACBLUE("#7795c4"),
   VERYLIGHTBLUE("#9696ff"),
   PALEYELLOW("#ffffa0"),
   MIDRED   ("#d00000"),
   MIDGREEN ("#00d000"),
   MIDBLUE  ("#0000d0"),
   LIGHTGRAY("#dedede"),
   DARKGRAY ("#646464"),
   MIDGRAY  ("#969696"),
   VERYDARKGRAY("#1d1d1d"),
   FAIRLYDARKGRAY("#414141"),
   MIDYELLOW("#dddd00"),
   DARKYELLOW("#b8b800"),
   ORANGE   ("#f0a400"),
   DARKORANGE("#dc9600"),
   BRIGHTORANGE("#ffd200"),
   LIGHTGREEN("#50ff50"),
   DARKRED  ("#700a0a"),
   DARKBLUE ("#0a0a70"),
   DARKGREEN("#006400"),
   MEDIUMGREEN("#00b400"),
   MEDIUMRED("#b40000"),
   MEDIUMBLUE("#0000b4"),
   STEELBLUE("#a899c8"),
   PINK     ("#fa506d"),
   DARKPINK ("#782837"),
   PALEPINK ("#ff9292"),
   BROWN    ("#773b17"),
   DARKBROWN("#4f280f"),
   BUFF     ("#b8aa7f"),
   DARKBUFF ("#90825d"),
   CCMBGREEN("#719874"),
   DELTACOL ("#010101"),
   SPATCHBG ("#d9d9c5"),
   SPATCHCANVAS("#d4d4c1"),
   BACKGROUND("-1");

   
   private String hexval;
   
  private ColorNames(String s) {
	  hexval = s;
  }

   public static String getHexValue(String sin) {
	   String s = sin;
      String ret = null;
      s = s.toUpperCase(Locale.ENGLISH);
      for (ColorNames cn : values()) {
              if (cn.name().equals(s)) {
                      ret = cn.hexval;
              }
      }
      return ret;
}



}
