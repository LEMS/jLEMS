package org.lemsml.jlems.core.run;

import java.util.ArrayList;

import org.lemsml.jlems.core.sim.ContentError;

public abstract class BuilderElement {

	
	ArrayList<BuilderElement> elts = new ArrayList<BuilderElement>();
	
	
	public void add(BuilderElement be) {
		elts.add(be);
	}

	public abstract void consolidateStateTypes() throws ContentError;
  
}
