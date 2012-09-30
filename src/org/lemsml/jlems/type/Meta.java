package org.lemsml.jlems.type;

import java.util.ArrayList;

import org.lemsml.jlems.util.ContentError;
import org.lemsml.jlems.xml.XMLElement;
 

// RawValued indicates that XML found inside Meta elements should be treated 
// processed into an element tree, but not interpreted as lems elements.
// other tools can do thier own thing with the content
public class Meta implements AddableTo, RawValued {

	public String context;
	
	public XMLElement valueWrapper = new XMLElement("wrapper");
	
	
	public void addXMLElement(XMLElement xe) {
		valueWrapper.add(xe);
		// E.info("Set VW " + xe.toXMLString(""));
	}

	// TODO is the rest of this used?
	public ArrayList<MetaItem> items = new ArrayList<MetaItem>();

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
