package org.lemsml.canonical;

import org.lemsml.type.Component;
import org.lemsml.type.ComponentType;
import org.lemsml.type.Dimension;
import org.lemsml.type.Lems;
import org.lemsml.util.DimensionsExport;

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
