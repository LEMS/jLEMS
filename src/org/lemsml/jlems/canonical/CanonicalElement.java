package org.lemsml.jlems.canonical;

import java.util.ArrayList;

public class CanonicalElement {

	String name;
	String body = "";
	ArrayList<CanonicalAttribute> attributes;
	ArrayList<CanonicalElement> children;
	
	public CanonicalElement(String enm) {
		name = enm;
	}
	
	public CanonicalElement(String enm, String bt) {
		name = enm;
		body = bt;
	}
	
	public void setBody(String s) {
		body = s;
	}
	
	public void add(CanonicalElement ce) {
		if (children == null) {
			children = new ArrayList<CanonicalElement>();
		}
		children.add(ce);
	}

	public String toXMLString() {
		return toXMLString(0);
	}
	
	
	public String toXMLString(int indentLevel) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < indentLevel; i++) {
			sb.append("   ");
		}
		sb.append("<" + name);
		if (attributes != null) {
			for (CanonicalAttribute ca : attributes) {
				sb.append(" " + ca.getName() + "=\"" + ca.getValue() + "\"");
			}
		}		
		sb.append(">");
		sb.append(body);
		if (children != null && children.size() > 0) {
			sb.append("\n");
			for (CanonicalElement ce : children) {
				sb.append(ce.toXMLString(indentLevel + 1));
			}
			for (int i = 0; i < indentLevel; i++) {
				sb.append("   ");
			}
		}
		sb.append("</" + name + ">\n");	
		if (indentLevel == 1) {
			sb.append("\n");
		}
		return sb.toString();
	}

	public void addAttribute(String sn, String sv) {
		 if (attributes == null) {
			 attributes = new ArrayList<CanonicalAttribute>();
		 }
		 attributes.add(new CanonicalAttribute(sn, sv));
		
	}
	
}
