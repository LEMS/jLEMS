package org.lemsml.jlems.type.structure;

import org.lemsml.jlems.expression.ParseError;
import org.lemsml.jlems.run.BuilderElement;
import org.lemsml.jlems.run.WhenBuilder;
import org.lemsml.jlems.sim.ContentError;
import org.lemsml.jlems.type.Component;
import org.lemsml.jlems.type.ComponentType;
import org.lemsml.jlems.type.Lems;

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
