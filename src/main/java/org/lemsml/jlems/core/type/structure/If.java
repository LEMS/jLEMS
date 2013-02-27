package org.lemsml.jlems.core.type.structure;

import org.lemsml.jlems.core.expression.ParseError;
import org.lemsml.jlems.core.run.BuilderElement;
import org.lemsml.jlems.core.run.IfBuilder;
import org.lemsml.jlems.core.sim.ContentError;
import org.lemsml.jlems.core.type.Component;
import org.lemsml.jlems.core.type.ComponentType;
import org.lemsml.jlems.core.type.Lems;
 

public class If extends BuildElement {

	public String test;
 
	public BuilderElement makeBuilder(Component cpt) throws ContentError {
		return new IfBuilder(test);
	}

	@Override
	public void resolveLocal(Lems lems, ComponentType ct) throws ContentError, ParseError {
		// nothing to do
	}

	
	
	
}
