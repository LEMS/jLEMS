package org.lemsml.jlems.core.serial;


public final class StringEncoder {

	
	private StringEncoder() {
		
	}

   static String xmlEscape(String sin) {
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
