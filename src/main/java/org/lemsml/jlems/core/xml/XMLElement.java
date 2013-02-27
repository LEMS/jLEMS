package org.lemsml.jlems.core.xml;

import java.util.ArrayList;
import java.util.HashMap;

import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.core.type.Attribute;

public class XMLElement {

	String type;
	String body;
	ArrayList<XMLElement> children = new ArrayList<XMLElement>();
	ArrayList<XMLAttribute> attributes = new ArrayList<XMLAttribute>();
	
	HashMap<String, ArrayList<XMLElement>> childTypeHM = new HashMap<String, ArrayList<XMLElement>>();
	HashMap<String, XMLAttribute> attHM = new HashMap<String, XMLAttribute>();
	
	public XMLElement(String st) {
		type = st;
	}
	
	public void setBody(String txt) {
		body = txt;
	}
	
	public void addAttribute(String n, String v) {
		XMLAttribute att = new XMLAttribute(n, v);
		attributes.add(att);
		attHM.put(n, att);
	}
	
	public ArrayList<XMLAttribute> getAttributes() {
		return attributes;
	}
	
	public XMLElement addElement(String ct) {
		XMLElement ret = new XMLElement(ct);
		add(ret);
		return ret;
	}

	public void addXMLElement(XMLElement xe) {
		add(xe);
	}
	
	public void add(XMLElement xe) {
		children.add(xe);
	
		if (childTypeHM.containsKey(xe.type)) {
			childTypeHM.get(xe.type).add(xe);
		} else {
			ArrayList<XMLElement> al = new ArrayList<XMLElement>();
			al.add(xe);
			childTypeHM.put(xe.type, al); 
		}
	}
	
	
	public String toString() {
		return "XMLElt: " + getTag() + " " + attributes.size() + ", " + children.size();
	}
	
	
	public String serialize() {
		return toXMLString("");
	}
	
	
	private String toXMLString(String indent) {
		StringBuilder sb = new StringBuilder();
		boolean sameLine = true;
		sb.append(indent + "<" + type);
		for (XMLAttribute att : attributes) {
			sb.append(" ");
			sb.append(att.name + "=\"" + att.value +"\"");
		}
		
		if ((body == null || body.length() == 0) && children.isEmpty()) {
			sb.append("/>\n");
			
		} else {
			sb.append(">");
			if (!children.isEmpty() || !attributes.isEmpty()) {  
				sb.append("\n");
				sameLine = false;
			}
			
			if (body != null) {
				sb.append(body);
				if (!children.isEmpty()) {
					sb.append("\n");
				}   
			}
			for (XMLElement xe : children) {
				sb.append(xe.toXMLString(indent + "    "));
				sameLine = false;
			}
			sb.append((sameLine ? "" : indent) + "</" + type + ">\n");
		}
		
		return sb.toString();
	}


	 

	
	
	public void addBodiedElement(String nm, String info) {
		XMLElement xe = addElement(nm);
		xe.setBody(info);
	}

	public void addToBody(String s) {
		 if (body == null) {
			 body = s;
		 } else {
			 body += " " + s;
		 }
		
	}

	public void copyAttributes(Attribute[] atta) {
		   if (atta != null) {
				 for (int i = 0; i < atta.length; i++) {
				    Attribute att = atta[i];
				    addAttribute(att.getName(), att.getValue());
				 }
			      }
	}

	public String getName() {
		return type;
	}

	public ArrayList<XMLElement> getElements() {
		return children;
	}

	public String getAttribute(String str) {
		String ret = "";
		if (attHM.containsKey(str)) {
			ret = attHM.get(str).getValue();
		}
		return ret;
	}

	 
	
	public boolean hasAttribute(String n) {
		boolean ret = false;
		if (attHM.containsKey(n)) {
			ret = true;
		}
		return ret;
	}

	public boolean hasElement(String str) {
		boolean ret = false;
		if (childTypeHM.containsKey(str)) {
			ret = true;
		}
		return ret;
	}

	public XMLElement getElement(String str) {
		ArrayList<XMLElement> al = childTypeHM.get(str);
		XMLElement ret = null;
		if (al.size() == 1) {
			ret = al.get(0);
		} else {
			E.error("getting single child but have " + al.size());
		}
		return ret;
	}

	public double getDouble(String str) {
		return Double.parseDouble(getAttribute(str));
	}

	public ArrayList<XMLElement> getElements(String str) {
		 return childTypeHM.get(str);
	}
	
	public ArrayList<XMLElement> getXMLElements() {
		 return children;
	}
	
	
	public boolean getBoolean(String str, boolean b) {
		 boolean ret = b;
		 if (attHM.containsKey(str)) {
			 String sv = attHM.get(str).getValue();
			 sv = sv.trim();
			 if (sv.equals("") || sv.equals("0") || sv.equals("false")) {
				 ret = false;
			 } else if (sv.equals("1") || sv.equals("true")) {
				 ret = true;
			 } else {
				 E.warning("unrecognized boolean attribute " + sv);
			 }
		 }
		 return ret;
	}

	public void addAttributes(HashMap<String, String> hm) {
		for (String s : hm.keySet()) {
			addAttribute(s, hm.get(s));
		}
		
	}

	public boolean isTag(String string) {
		 boolean ret = false;
		 if (type.equals(string)) {
			 ret = true;
		 }
		 return ret;
	}

	public String getTag() {
		return type;
	}

	public void appendBodyText(String stxt) {
		addToBody(stxt);
	}
	
}
