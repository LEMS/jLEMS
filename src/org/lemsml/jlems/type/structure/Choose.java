package org.lemsml.jlems.type.structure;

import org.lemsml.jlems.expression.ParseError;
import org.lemsml.jlems.run.BuilderElement;
import org.lemsml.jlems.run.ChooseBuilder;
import org.lemsml.jlems.type.Component;
import org.lemsml.jlems.type.ComponentType;
import org.lemsml.jlems.type.Lems;
import org.lemsml.jlems.util.ContentError;

public class Choose extends BuildElement {

 
	public BuilderElement makeBuilder(Component cpt) throws ContentError {
		return new ChooseBuilder();
	}

	@Override
	public void resolveLocal(Lems lems, ComponentType ct) throws ContentError, ParseError {
 		
	}

	
	
	
}
