

package org.lemsml.xml;

import org.lemsml.type.Attribute;
 
 
public class XMLToken {
   
   final static int NONE = 0;
   final static int OPEN = 1;
   final static int CLOSE = 2;
   
   final static int NUMBER = 3;
   final static int STRING = 4;

   final static int OPENCLOSE = 5;

   final static int INTRO = 6;
   final static int COMMENT = 7;


   String[] types = {"NONE", "OPEN", "CLOSE", "NUMBER", "STRING", 
		     "OPENCLOSE", "INTRO", "COMMENT"};


   int type;

   String svalue;
   double dvalue;

   int natt;
   String[] attNV;

   

   public XMLToken() {
      type = NONE;
   }

   /*
   public String toString() {
      String sr = ("XMLToken type=" + type + " sv=" + svalue + " dv=" + dvalue +
		   " natt=" + natt);
      for (int i = 0; i < natt; i++) {
	 sr += "   att[" + i + "]:" + attNV[i] + "\n";
      }
      return sr;
   }
   */
   
   public String getXMLText() {
	   StringBuffer sb = new StringBuffer();
	   if (type == OPEN) {
		   sb.append("<" + svalue);
		   sb.append(makeAttributesText());
		   sb.append(">\n");
		   
	   } else if (type == OPENCLOSE) {
		   sb.append("<" + svalue);
		   sb.append(makeAttributesText());
		   sb.append("/>\n");
	   
	   } else if (type == CLOSE) {
		   sb.append("</" + svalue + ">\n");
	   
	   } else if (type == NUMBER) {
		   sb.append("" + dvalue); 
	   
	   } else if (type == STRING) {
		   sb.append(svalue);
	   
	   } else {
		   // skip the rest;
	   }
	   return sb.toString();
   }
   
   

   public boolean isOpen() { 
      return (type == OPEN || type == OPENCLOSE); 
   }

   public boolean isClose() { 
      return (type == OPENCLOSE || type == CLOSE); 
   }

   public boolean isNumber() { 
      return (type == NUMBER); 
   }

   public boolean isString() { 
      return (type == STRING); 
   }

   public boolean isNone() { 
      return (type == NONE); 
   }

   public boolean isIntro() { 
      return (type == INTRO); 
   }

   public boolean isComment() { 
      return (type == COMMENT); 
   }



    @Override
   public String toString() {
      String s = types[type] + " " ;
      if (type == OPEN ||
	  type == STRING || 
	  type == INTRO || 
	  type == COMMENT || 
	  type == CLOSE || 
	  type == OPENCLOSE) {
	 s += svalue;
	 
	 if (type == OPEN || type==OPENCLOSE) {
		 s += makeAttributesText();
	 }
      } else if (type == NUMBER) {
	 s += " " + dvalue;
      }
      return s;
   }

   
   private String makeAttributesText() {
	   StringBuffer sb = new StringBuffer();
	   if (natt > 0) {
		   for (int i = 0; i < natt; i++) {
			   sb.append(" " + attNV[2*i] + "=\"" + attNV[2*i+1] + "\"");
		   }
	   }
	   return sb.toString();
    }
   
   
   public void setType(int itype) { 
      type = itype; 
    }
   
   
   public void setStringValue(String s) { 
      svalue = s; 
      }

   
   public void setDValue(double d) { 
      dvalue = d; 
      }
   
   
   public void setAttributes(String[] sa) {
      attNV = sa;
      natt = sa.length / 2;
   }



   public boolean hasAttribute(String sat) {
      boolean bret = false;
      for (int i = 0; i < natt; i++) {
	 if (attNV[2*i].equals(sat)) bret = true;
      }
      return bret;
   }

   public Attribute[] getAttributes() {
      XMLAttribute[] nvpa = new XMLAttribute[natt];
      for (int i = 0; i < natt; i++) {
	      nvpa[i] = new XMLAttribute(attNV[2*i], attNV[2*i+1]);
      }
      return nvpa;
   }


   
   public String getAttribute(String sat) {
      String sret = null;
      for (int i = 0; i < natt; i++) {
	 if (attNV[2*i].equals(sat)) sret = attNV[2*i+1];
      }
      return sret;
   }


   public String getName() {
      return svalue;
   }

   public String getOpenTagString() {
      return ("<" + svalue + ">");
   }

   public String getCloseTagString() {
      return ("</" + svalue + ">");
   }


   
   public boolean closes(XMLToken start) {
      return (svalue.equals(start.getName()) && isClose());
   }
   

   public int getNumAttributes() { 
      return natt; 
   }


   public String getAttributeName(int i) { 
      return attNV[2*i]; 
   }

   public String getAttributeValue(int i) { 
      return attNV[2*i+1]; 
   }

}
