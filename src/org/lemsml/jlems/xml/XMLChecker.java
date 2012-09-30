
package org.lemsml.jlems.xml;

import org.lemsml.jlems.util.ContentError;
 


public class XMLChecker {



   public static String checkXML(String s, boolean bshow) throws ParseException, ContentError, XMLException {
	   StringBuffer sb = new StringBuffer();
	   long starttime = System.currentTimeMillis();
      XMLTokenizer tkz = new XMLTokenizer(s);
      int nerror = 0;
      int nread = 0;

      while (true) {
	 XMLToken xmlt = tkz.nextToken();
	 if (bshow) {
	    sb.append("item " + nread + "  " + xmlt + "\n");
	 }
	 nread++;
	 if (xmlt.isNone()) {
	    break;
	 }
      }
      long endtime = System.currentTimeMillis();

      sb.append("  Total tags: " + nread + "\n  total errors: " + nerror +
			 "\n  tokenizing took " + (int)(endtime - starttime) + " ms");
      return sb.toString();
   }








   public static String deGarbage(String sin) throws ContentError, XMLException {
	   String s = sin;
      if (s.startsWith("<")) {
	 // fine;

      } else {
	 int iob = s.indexOf("<");

	 if (iob > 0) {
	    String junk = s.substring(0, iob);
	    if (junk.trim().length() > 0) {

	       throw new XMLException("Garbage at start of xml file - first < is at " +
				  iob + " preceded by ---" + junk + "---");
	    }
	    s = s.substring(iob, s.length());

	 } else {
	    throw new ContentError(" - xml file contains no xml " + s);
	   
	 }
      }

      return s;
   }




}


