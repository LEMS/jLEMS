package org.lemsml.jlems.core.type;

import java.util.ArrayList;
import java.util.HashMap;

import org.lemsml.jlems.core.sim.ContentError;
import org.lemsml.jlems.core.xml.XMLAttribute;
import org.lemsml.jlems.core.xml.XMLElement;
 

// RawValued indicates that XML found inside Meta elements should be treated 
// processed into an element tree, but not interpreted as lems elements.
// other tools can do their own thing with the content
public class Meta {

	public String context;
	
	public XMLElement valueWrapper = new XMLElement("wrapper");
	
	private XMLElement p_sourceXML;
	
	public ArrayList<MetaItem> items = new ArrayList<MetaItem>();

	
	
	public Meta() {
		
	}
	
	public void setSource(XMLElement xe) {
		p_sourceXML = xe;
	}
	
	public void addXMLElement(XMLElement xe) {
		valueWrapper.add(xe);
		// E.info("Set VW " + xe.toXMLString(""));
	}

	public HashMap<String, String> getAttributes() {
		HashMap<String, String> ret = new HashMap<String, String>();
		if (p_sourceXML != null) {
			for (XMLAttribute xa : p_sourceXML.getAttributes()) {
				ret.put(xa.getName(), xa.getValue());
			}
		}
		return ret;
	}
	 

	public void add(Object obj) throws ContentError {
		if (obj instanceof MetaItem) {
			items.add((MetaItem)obj);
		} else {
			throw new ContentError("cant add " + obj);
		}
	} 

	public MetaItem newItem(String nm) {
		 MetaItem ret = new MetaItem(nm);
		 items.add(ret);
		 return ret;
	}

	public XMLElement getXMLValue() {
		 return valueWrapper;
	}


}
