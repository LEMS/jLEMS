package org.lemsml.jlemsio.xmlio;

import org.lemsml.jlems.io.FormatException;
import org.lemsml.jlems.util.ContentError;
import org.lemsml.jlems.xml.BuildException;
import org.lemsml.jlems.xml.ParseException;
import org.lemsml.jlems.xml.XMLElement;
import org.lemsml.jlems.xml.XMLException;

 
public class ElementXMLReader {



   public static Object read(String s) throws ParseException, BuildException, ContentError, FormatException, XMLException {
      return deserialize(s);
   }

   public XMLElement readElement(String s) throws ParseException, BuildException, ContentError, FormatException, XMLException {
	   Object o = deserialize(s);
	   XMLElement ret = (XMLElement)o;
	   return ret;
   }

   public static Object deserialize(String s) throws ParseException, BuildException, ContentError, FormatException, XMLException {

      ElementConstructor ein = new ElementConstructor();

      XMLReader reader = new XMLReader(ein);

      Object obj = reader.readObject(s);

      return obj;
   }

}
