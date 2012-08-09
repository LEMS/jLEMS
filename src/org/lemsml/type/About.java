package org.lemsml.type;
 
import org.lemsml.xml.XMLContainer;

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
