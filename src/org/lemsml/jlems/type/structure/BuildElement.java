package org.lemsml.jlems.type.structure;

import org.lemsml.jlems.expression.ParseError;
import org.lemsml.jlems.run.BuilderElement;
import org.lemsml.jlems.type.Component;
import org.lemsml.jlems.type.ComponentType;
import org.lemsml.jlems.type.Lems;
import org.lemsml.jlems.type.LemsCollection;
import org.lemsml.jlems.util.ContentError;

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
