package org.lemsml.jlems.core.serial;

import org.lemsml.jlems.core.xml.XMLElement;


// a wrapper element is created for things that occur in the java hierarchy, but shouldn't be exported in the xml tree
// eg the "listOfX" type constructs, where the "listOfX" element itself is redundant and we just want the bare X elements
// in the parent

public class WrapperElement extends XMLElement {

	public WrapperElement(String s) {
		super(s);
	}
	
	

}
