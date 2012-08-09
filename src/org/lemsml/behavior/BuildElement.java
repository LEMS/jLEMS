package org.lemsml.behavior;

import org.lemsml.expression.ParseError;
import org.lemsml.run.BuilderElement;
import org.lemsml.type.Component;
import org.lemsml.type.ComponentType;
import org.lemsml.type.Lems;
import org.lemsml.type.LemsCollection;
import org.lemsml.util.ContentError;

public abstract class BuildElement {
	
	public LemsCollection<BuildElement> buildElements = new LemsCollection<BuildElement>();

	
	public void resolve(Lems lems, ComponentType ct) throws ContentError, ParseError {
		for (BuildElement be : buildElements) {
			be.resolveLocal(lems, ct);
			be.resolve(lems, ct);
		}
	}
	
	public void resolveLocal(Lems lems, ComponentType ct) throws ContentError, ParseError {
	}
	
	
	public abstract BuilderElement makeBuilder(Component cpt) throws ContentError, ParseError;
	
	public void makeChildBuilders(Component cpt, BuilderElement bre) throws ContentError, ParseError {
		for (BuildElement be : buildElements) {
			bre.add(be.makeBuilder(cpt));
		}
	}
 
	
	
}
