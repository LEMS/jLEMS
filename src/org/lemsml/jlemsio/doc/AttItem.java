package org.lemsml.jlemsio.doc;

import org.lemsml.jlems.xml.XMLElement;

public class AttItem {

	String name;
	Class<?> type;
 	String info;
	
	public AttItem(String nm, Class<?> ct, String si) {
		name = nm;
		type = ct;
		info = si;
 
	}

	public XMLElement makeXMLElement() {
		 XMLElement ret= new XMLElement("Property");
		 ret.addAttribute("name", name);
		 ret.addAttribute("class", shortName(type));
		 ret.setBody(info);
		 return ret;
	}

	private String shortName(Class<?> cls) {
		String ret = cls.getName();
		ret = ret.substring(ret.lastIndexOf(".") + 1, ret.length());
		return ret;
	}

}
