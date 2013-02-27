package org.lemsml.jlems.core.type.structure;

import org.lemsml.jlems.core.expression.ParseError;
import org.lemsml.jlems.core.run.BuilderElement;
import org.lemsml.jlems.core.run.WhenBuilder;
import org.lemsml.jlems.core.sim.ContentError;
import org.lemsml.jlems.core.type.Component;
import org.lemsml.jlems.core.type.ComponentType;
import org.lemsml.jlems.core.type.Lems;

public class When extends BuildElement {

	public String test;

	 
	public BuilderElement makeBuilder(Component cpt) throws ContentError {
		return new WhenBuilder(test);
	}


	@Override
	public void resolveLocal(Lems lems, ComponentType ct) throws ContentError, ParseError {
		// TODO Auto-generated method stub
		
	}
	
}
