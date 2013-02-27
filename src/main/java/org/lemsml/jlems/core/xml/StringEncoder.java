package org.lemsml.jlems.core.xml;


public final class StringEncoder {


	private StringEncoder() {
		
	}

	
   public static String xmlEscape(String sin) {
	   String s = sin;
      s = s.replaceAll("&", "&amp;");
      s = s.replaceAll("\"", "\\\\\"");
      s = s.replaceAll("\n", "\\\\n\\\\\n");
      return s;
   }


   public static String xmlUnescape(String sin) {
	   String s = sin;
      s = s.replaceAll("&amp;", "&");
      s = s.replaceAll("\\\\\"", "\"");
      s = s.replaceAll("\n\n", "\n");
      return s;
   }

}
