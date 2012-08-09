package org.lemsml.xml;

import java.util.ArrayList;
import java.util.HashMap;

public class XMLElementTmp {

	XMLElementTmp parent;
	
	String type;
	String body;
	ArrayList<XMLElementTmp> children = new ArrayList<XMLElementTmp>();
	ArrayList<XMLAttribute> attributes = new ArrayList<XMLAttribute>();
	
	public XMLElementTmp(String st) {
		type = st;
	}
	
	public void setBody(String txt) {
		body = txt;
	}
	
	public void addAttribute(String n, String v) {
		attributes.add(new XMLAttribute(n, v));
	}
	
	public XMLElementTmp addElement(String ct) {
		XMLElementTmp ret = new XMLElementTmp(ct);
		add(ret);
		return ret;
	}

	public void add(XMLElementTmp xe) {
		xe.parent = this;
		children.add(xe);
	}

	public XMLElementTmp getParent() {
		return parent;
	}
	
	public ArrayList<XMLElementTmp> getChildren() {
		return children;
	}
	
	public String toXMLString(String indent) {
		StringBuilder sb = new StringBuilder();
		boolean sameLine = true;
		sb.append(indent + "<" + type);
		for (XMLAttribute att : attributes) {
			sb.append(" ");
			sb.append(att.name + "=\"" + att.value +"\"");
		}
		
		if ((body == null || body.length() == 0) && children.size() == 0) {
			sb.append("/>\n");
			
		} else {
			sb.append(">");
			if (children.size() > 0 || attributes.size() > 0) {
				sb.append("\n");
				sameLine = false;
			}
			
			if (body != null) {
				sb.append(body);
				if (children.size() > 0) {
					sb.append("\n");
				}   
			}
			for (XMLElementTmp xe : children) {
				sb.append(xe.toXMLString(indent + "    "));
				sameLine = false;
			}
			sb.append((sameLine ? "" : indent) + "</" + type + ">\n");
		}
		
		return sb.toString();
	}

	
	
	public void addBodiedElement(String nm, String info) {
		XMLElementTmp xe = addElement(nm);
		xe.setBody(info);
	}

	public ArrayList<XMLElementTmp> getElements() {
		return children;
	}

	public void addAttributes(HashMap<String, String> hm) {
		 for (String s: hm.keySet()) {
			 addAttribute(s, hm.get(s));
		 }
	}

	public boolean hasElement(String rpd) {
		boolean ret = false;
		if (getElement(rpd) != null) {
			ret = true;
		}
		return ret;
	}
	
	
	public XMLElementTmp xGetElement(String rpd) {
		XMLElementTmp ret = null;
		for (XMLElementTmp xe : children) {
			if (xe.type.equals(rpd)) {
				ret = xe;
			}
		}
		return ret;
	}

	public XMLElementTmp getElement(String rpd) {
		return xGetElement(rpd);
	}
	
}
