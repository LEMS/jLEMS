package org.lemsml.behavior;

import org.lemsml.run.BuilderElement;
import org.lemsml.run.ChooseBuilder;
import org.lemsml.type.Component;
import org.lemsml.util.ContentError;

public class Choose extends BuildElement {

 
	public BuilderElement makeBuilder(Component cpt) throws ContentError {
		return new ChooseBuilder();
	}

	
	
	
}
