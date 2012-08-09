
package org.lemsml.xml;

import org.lemsml.util.ContentError;
 


public class XMLChecker {



   public static void checkXML(String s, boolean bshow) throws ParseException, ContentError {
      long starttime = System.currentTimeMillis();
      XMLTokenizer tkz = new XMLTokenizer(s);
      int nerror = 0;
      int nread = 0;

      while (true) {
	 XMLToken xmlt = tkz.nextToken();
	 if (bshow) {
	    System.out.println("item " + nread + "  " + xmlt);
	 }
	 nread++;
	 if (xmlt.isNone()) {
	    break;
	 }
      }
      long endtime = System.currentTimeMillis();

      System.out.println("  Total tags: " + nread + "\n  total errors: " + nerror +
			 "\n  tokenizing took " + (int)(endtime - starttime) + " ms");
   }








   public static String deGarbage(String sin) throws ContentError {
	   String s = sin;
      if (s.startsWith("<")) {
	 // fine;

      } else {
	 int iob = s.indexOf("<");

	 if (iob > 0) {
	    String junk = s.substring(0, iob);
	    if (junk.trim().length() > 0) {

	       System.out.println("WARNING - garbage at start of xml file - first < is at " +
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


