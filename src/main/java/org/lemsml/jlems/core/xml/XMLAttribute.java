package org.lemsml.jlems.core.xml;

import org.lemsml.jlems.core.type.Attribute;
 
public class XMLAttribute implements Attribute {

   String name;
   String value;

   boolean flag = false;

   public XMLAttribute(String s, String val) {
      name = s;
      value = val;
   }

   public String getName() { return name; }

   public String getValue() { return value; }

   
   public String toString() {
	   return "XMLAttribute(" + name + ", " + value + ") ";
   }
   
   public void setFlag() {
	   flag = true;
   }
   
   public void clearFlag() {
	   flag = false;
   }
   
   public boolean flagged() {
	   return flag;
   }

   public Attribute makeCopy() {
	   return new XMLAttribute(name, value);
   }

   
}
