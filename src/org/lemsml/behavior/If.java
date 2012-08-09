package org.lemsml.behavior;

import org.lemsml.run.BuilderElement;
import org.lemsml.run.IfBuilder;
import org.lemsml.type.Component;
import org.lemsml.util.ContentError;
 

public class If extends BuildElement {

	public String test;
 
	public BuilderElement makeBuilder(Component cpt) throws ContentError {
		return new IfBuilder(test);
	}

	
	
	
}
