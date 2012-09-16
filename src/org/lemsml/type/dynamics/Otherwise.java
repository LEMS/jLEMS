package org.lemsml.type.dynamics;

import org.lemsml.expression.ParseError;
import org.lemsml.run.BuilderElement;
import org.lemsml.run.OtherwiseBuilder;
import org.lemsml.type.Component;
import org.lemsml.type.ComponentType;
import org.lemsml.type.Lems;
import org.lemsml.util.ContentError;

public class Otherwise extends BuildElement {

 
	public BuilderElement makeBuilder(Component cpt) throws ContentError {
		return new OtherwiseBuilder();
	}

	@Override
	public void resolveLocal(Lems lems, ComponentType ct) throws ContentError, ParseError {
		// TODO Auto-generated method stub
		
	}

}
