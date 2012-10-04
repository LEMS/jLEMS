package org.lemsml.jlemsio.xmlio;

import org.lemsml.jlems.io.FormatException;
import org.lemsml.jlems.type.Attribute;
import org.lemsml.jlems.type.Parameterized;
import org.lemsml.jlems.util.ContentError;
import org.lemsml.jlems.xml.BuildException;


public interface Constructor {

   Object newInstance(String cnm, boolean allowComponent);

   Object getChildObject(Object parent, String name, Attribute[] atta) throws BuildException, ContentError;
 
   void applyAttributes(Object obj, Attribute[] atta, Parameterized ptzd) throws ContentError, FormatException;

   boolean setAttributeField(Object parent, String fieldName, String child, Parameterized ptzd) throws ContentError, FormatException;

   boolean setField(Object parent, String fieldName, Object child, Parameterized ptzd) throws ContentError, FormatException;

   Object getField(Object parent, String fieldName) throws ContentError;

   void appendContent(Object child, String content) throws ContentError;

   void setIntFromStatic(Object ret, String id, String sv) throws ContentError, FormatException;
 

}
