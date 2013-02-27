package org.lemsml.jlems.core.type.structure;

import org.lemsml.jlems.core.expression.ParseError;
import org.lemsml.jlems.core.run.Builder;
import org.lemsml.jlems.core.sim.ContentError;
import org.lemsml.jlems.core.type.Component;
import org.lemsml.jlems.core.type.ComponentType;
import org.lemsml.jlems.core.type.Lems;
import org.lemsml.jlems.core.type.LemsCollection;

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
