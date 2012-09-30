package org.lemsml.jlems.xml;


public final class StringEncoder {



   public static String xmlEscape(String sin) {
	   String s = sin;
      s = s.replaceAll("&", "&amp;");
      s = s.replaceAll("\"", "\\\\\"");
      s = s.replaceAll("\n", "\\\\n\\\\\n");
      return s;
   }


   static String xmlUnescape(String sin) {
	   String s = sin;
      s = s.replaceAll("&amp;", "&");
      s = s.replaceAll("\\\\\"", "\"");
      s = s.replaceAll("\n\n", "\n");
      return s;
   }

}
