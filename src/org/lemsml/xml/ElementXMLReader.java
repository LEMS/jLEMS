package org.lemsml.xml;

import org.lemsml.io.FormatException;
import org.lemsml.util.ContentError;

 
public class ElementXMLReader {



   public static Object read(String s) throws ParseException, BuildException, ContentError, FormatException, XMLException {
      return deserialize(s);
   }



   public static Object deserialize(String s) throws ParseException, BuildException, ContentError, FormatException, XMLException {

      ElementConstructor ein = new ElementConstructor();

      XMLReader reader = new XMLReader(ein);

      Object obj = reader.readObject(s);

      return obj;
   }

}
