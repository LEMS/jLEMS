package org.lemsml.xml;

import org.lemsml.io.FormatException;
import org.lemsml.type.Attribute;
import org.lemsml.type.Parameterized;
import org.lemsml.util.ContentError;


public interface Constructor {

   Object newInstance(String cnm);

   Object getChildObject(Object parent, String name, Attribute[] atta) throws BuildException, ContentError;
 
   void applyAttributes(Object obj, Attribute[] atta, Parameterized ptzd) throws ContentError, FormatException;

   boolean setAttributeField(Object parent, String fieldName, String child, Parameterized ptzd) throws ContentError, FormatException;

   boolean setField(Object parent, String fieldName, Object child, Parameterized ptzd) throws ContentError, FormatException;

   Object getField(Object parent, String fieldName) throws ContentError;

   void appendContent(Object child, String content) throws ContentError;

   void setIntFromStatic(Object ret, String id, String sv) throws ContentError, FormatException;

   void addSearchPackage(Package pkg);

}
