package org.lemsml.jlems.api;
 
import org.lemsml.jlems.logging.E;
import org.lemsml.jlems.type.Dimension;
import org.lemsml.jlems.type.Lems;
 


// NB this should be the only way to access lems components from outside - 
// no direct access to Component or ComponentType objects so we can refactor 
// them and not worry about breaking the API.
public class LemsBuilder {

	Lems lems;
	

	public LemsBuilder() {
		lems = new Lems();
	}
	
	
	public void addDimension(String name, DimensionValue ds) {
		 Dimension d = ds.buildDimension(name);
		 lems.addDimension(d);
	}
	
	public void addUnit(String s) {
		E.missing();
	}
	
	
}
