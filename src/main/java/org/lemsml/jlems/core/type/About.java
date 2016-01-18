package org.lemsml.jlems.core.type;
 
import org.lemsml.jlems.core.xml.XMLContainer;

public class About implements XMLContainer {

	String text = "";

    public About()
    {
    }

    public About(String text)
    {
        this.text = text;
    }
    
	
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
