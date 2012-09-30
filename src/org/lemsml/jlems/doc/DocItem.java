package org.lemsml.jlems.doc;

import java.util.ArrayList;

import org.lemsml.jlems.xml.XMLElement;


public class DocItem {

	
	String info;
	
	Class<? extends Object> cls;
	
	ArrayList<Class<? extends Object>> containers = new ArrayList<Class<? extends Object>>();
	ArrayList<Class<? extends Object>> contents = new ArrayList<Class<? extends Object>>();
	
	ArrayList<AttItem> attItems = new ArrayList<AttItem>();
	
	
	public DocItem(Class<? extends Object> cl) {
		cls = cl;

	}		 
	
	
	
	public void addContainer(Class<? extends Object> cl) {
		if (!containers.contains(cl)) {
			containers.add(cl);
 
		}
	}

	public void addContent(Class<? extends Object> cl) {
		if (!contents.contains(cl)) {
			contents.add(cl);
 
		}
	}

	

	public void setInfo(String s) {
		info = s;
	 
	}



	public void addAttribute(String name, Class<?> type, String info) {
		attItems.add(new AttItem(name, type, info));
	}


	private String shortName(Class<?> cls) {
		String ret = cls.getName();
		ret = ret.substring(ret.lastIndexOf(".") + 1, ret.length());
		return ret;
	}

	
	public XMLElement makeXMLElement() {
		XMLElement ret = new XMLElement("ElementType");
		ret.addAttribute("name", shortName(cls));
		
		if (info != null) {
			ret.addBodiedElement("Info", info);
		}
		for (AttItem ai : attItems) {
			ret.add(ai.makeXMLElement());
		}
		
		
		for (Class<?> c : containers) {
			XMLElement x = new XMLElement("OccursInside");
			x.setBody(shortName(c));
			ret.add(x);
		}
		

		for (Class<?> c : contents) {
			XMLElement x = new XMLElement("CanContain");
			x.setBody(shortName(c));
			ret.add(x);
		}
		
		return ret;
	}

}
