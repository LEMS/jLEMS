package org.lemsml.jlems.core.type;
 
import org.lemsml.jlems.core.xml.XMLContainer;

public class About implements XMLContainer {

	String text = "";
	
	public void setBodyValue(String s) {
		text += s;
	}

	public String getText() {
			return text;
	}

	public void setXMLContent(String s) {
		 text += s;
	 
	}

}
