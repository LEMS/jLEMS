package org.lemsml.jlems.canonical;

import org.lemsml.jlems.type.Component;
import org.lemsml.jlems.type.ComponentType;
import org.lemsml.jlems.type.Dimension;
import org.lemsml.jlems.type.Lems;
import org.lemsml.jlems.util.DimensionsExport;

public class CanonicalWriter {

	Lems lems;
	
	
	public CanonicalWriter(Lems l) {
		lems = l;
	}

	public String writeText() {
		
		CanonicalElement root = new CanonicalElement("Lems");
		
		for (Dimension d : lems.dimensions) {
			root.add(DimensionsExport.makeCanonical(d));
		}
		for (ComponentType t : lems.componentTypes) {
			root.add(t.makeCanonical());
		}
		for (Component c : lems.components) {
			root.add(c.makeCanonical());
		}
	
		return root.toXMLString();
	}

}
