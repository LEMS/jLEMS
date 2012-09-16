package org.lemsml.type.structure;

import org.lemsml.expression.ParseError;
import org.lemsml.run.Builder;
import org.lemsml.type.Component;
import org.lemsml.type.ComponentType;
import org.lemsml.type.Lems;
import org.lemsml.type.LemsCollection;
import org.lemsml.type.dynamics.BuildElement;
import org.lemsml.util.ContentError;

public class Structure {

	public LemsCollection<BuildElement> buildElements = new LemsCollection<BuildElement>();

	
	public void resolve(Lems lems, ComponentType ct) throws ContentError, ParseError {
		for (BuildElement be : buildElements) {
			be.resolveLocal(lems, ct);
			be.resolve(lems, ct);
		}
	}
  
	 
 
	public Builder makeBuilder(Component cpt) throws ContentError, ParseError {
		Builder b = new Builder();
		
		for (BuildElement be : buildElements) {
			b.add(be.makeBuilder(cpt));
		}
	
		return b;
	}

 
	

	
	
}
