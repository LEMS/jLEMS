package org.lemsml.jlems.core.type.structure;

import org.lemsml.jlems.core.expression.ParseError;
import org.lemsml.jlems.core.run.BuilderElement;
import org.lemsml.jlems.core.sim.ContentError;
import org.lemsml.jlems.core.type.Component;
import org.lemsml.jlems.core.type.ComponentType;
import org.lemsml.jlems.core.type.Lems;
import org.lemsml.jlems.core.type.LemsCollection;

public abstract class BuildElement {
	
	public LemsCollection<BuildElement> buildElements = new LemsCollection<BuildElement>();

	
	public void resolve(Lems lems, ComponentType ct) throws ContentError, ParseError {
		for (BuildElement be : buildElements) {
			be.resolveLocal(lems, ct);
			be.resolve(lems, ct);
		}
	}
	
	public abstract void resolveLocal(Lems lems, ComponentType ct) throws ContentError, ParseError;
	
	
	public abstract BuilderElement makeBuilder(Component cpt) throws ContentError, ParseError;
	
	public void makeChildBuilders(Component cpt, BuilderElement bre) throws ContentError, ParseError {
		for (BuildElement be : buildElements) {
			bre.add(be.makeBuilder(cpt));
		}
	}
 
	
	
}
