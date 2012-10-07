package org.lemsml.jlems.type.structure;

import org.lemsml.jlems.expression.ParseError;
import org.lemsml.jlems.run.Builder;
import org.lemsml.jlems.sim.ContentError;
import org.lemsml.jlems.type.Component;
import org.lemsml.jlems.type.ComponentType;
import org.lemsml.jlems.type.Lems;
import org.lemsml.jlems.type.LemsCollection;

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
