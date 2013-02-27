package org.lemsml.jlems.core.reader;

import org.lemsml.jlems.core.sim.ContentError;
import org.lemsml.jlems.core.type.ComponentType;
import org.lemsml.jlems.core.type.Lems;
import org.lemsml.jlems.core.type.dynamics.Dynamics;
import org.lemsml.jlems.core.xml.XMLAttribute;
import org.lemsml.jlems.core.xml.XMLElement;

public class SampleLemsFactory extends AbstractLemsFactory {

	@Override
	public Lems buildLemsFromXMLElement(XMLElement root) throws ContentError {
		Lems ret = null;
		if (root.isTag("Lems")) {
			ret = (Lems)(instantiateFromXMLElement(root));
			
		} else {
			throw new ContentError("Root tag must be Lems, not " + root.getTag());
		}
		return ret;
	}

	
	public Object instantiateFromXMLElement(XMLElement xel) {
		Object ret = null;
		String tag = xel.getName();
		if (tag.equals("ComponentType")) {
			ret = buildComponentType(xel);
			
		} else if (tag.equals("Structure")) {
			
		} else {
			
		}
		return ret;
	}
	
	private ComponentType buildComponentType(XMLElement xel) {
		ComponentType ret = new ComponentType();
	
		for (XMLAttribute xa : xel.getAttributes()) {
			String xn = xa.getName();
			if (xn.equals("name")) {
				ret.name = xa.getValue();
			} else if (xn.equals("n")) {
			 //	ret.n = Integer.parseInt(xa.getValue());
			} else {
				
			}		
		}
		
		for (XMLElement xe : xel.getElements()) {
			Object och = instantiateFromXMLElement(xe);
			if (och instanceof Dynamics) {
				ret.dynamicses.add((Dynamics)och);
			} 
		}
		
		
		return ret;
	}
	
}
