package org.lemsml.jlemsio.xmlio;

import org.lemsml.jlems.type.Attribute;
import org.lemsml.jlems.type.Parameterized;
import org.lemsml.jlems.util.ContentError;
import org.lemsml.jlems.util.E;
import org.lemsml.jlems.xml.XMLElement;

 


public class ElementConstructor implements Constructor {
   
   
   public Object newInstance(String s, boolean allowCpt) {
	   E.info("econ making a " + s);
      return new XMLElement(s);
   }

   
   public boolean setAttributeField(Object parent, String fieldName, String val) {
      if (parent instanceof XMLElement) {
	 XMLElement XMLp = (XMLElement)parent;
	 XMLp.addAttribute(fieldName, val);
      }
      return true;
   }


   public void appendContent(Object obj, String s) {
	   E.info("ap content ... " + s);
      ((XMLElement)obj).addToBody(s);
   }


   public Object getChildObject(Object parent, String name, Attribute[] atta) {
      XMLElement elt = new XMLElement(name);
      return elt;
   }


   public void applyAttributes(Object obj, Attribute[] atta) {
      ((XMLElement)obj).copyAttributes(atta);
   }


   
   public boolean setField(Object parent, String fieldName, Object child) {
      boolean ok = false;

      if (parent instanceof XMLElement && child instanceof String) {

	 XMLElement XMLp = (XMLElement)parent;
	 XMLElement ec = new XMLElement(fieldName);

	 ec.setBody((String)child);
	 XMLp.addXMLElement(ec);


      } else if (parent instanceof XMLElement && child instanceof XMLElement) {
	 XMLElement XMLp = (XMLElement)parent;
	 XMLElement XMLc = (XMLElement)child;
	 
	 if (XMLc.getName().equals(fieldName)) {
	    XMLp.addXMLElement(XMLc);
	    ok = true;

	 } else {
	    E.error(" - element instantiator set field hs fieldname " + 
			       fieldName + "  but element " + XMLc.getName());
	 }
	 
      } else {
	 E.error(" - ElementInstantiator set field : fieldname=" + fieldName + 
			    " parent=" + parent + 
			   "    child=" + child + " " + child.getClass().getName() + 
			    "  but need elements only");
	 (new Exception()).printStackTrace();
      }
      return ok;
   }

   
   public Object getField(Object parent, String fieldName) {
      return new XMLElement(fieldName);
   }


   public void setIntFromStatic(Object ret, String id, String sv) {
      E.missing();
   }

 


@Override
public void applyAttributes(Object obj, Attribute[] atta, Parameterized ptzd)
		throws ContentError {
	 if (obj instanceof XMLElement) {
		 XMLElement xe = (XMLElement)obj;
		 for (Attribute att : atta) {
			 xe.addAttribute(att.getName(), att.getValue());
		 }
	 } else {
		 E.missing();
	 }
}


@Override
public boolean setAttributeField(Object parent, String fieldName, String child,
		Parameterized ptzd) throws ContentError {
	E.missing();
	return false;
}


@Override
public boolean setField(Object parent, String fieldName, Object child,
		Parameterized ptzd) throws ContentError {
	 	   if (parent == null) {
	    	  // OK
	      } else if (parent instanceof XMLElement && child instanceof XMLElement) {
	    	  ((XMLElement)parent).add((XMLElement)child);
	      } else {
	    	  E.missing();
	      }
	
	return false;
}
   

}
