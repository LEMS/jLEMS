package org.lemsml.jlems.core.type.structure;

import org.lemsml.jlems.core.expression.ParseError;
 
import org.lemsml.jlems.core.run.Builder;
import org.lemsml.jlems.core.run.BuilderElement;
import org.lemsml.jlems.core.sim.ContentError;
import org.lemsml.jlems.core.type.Component;
import org.lemsml.jlems.core.type.ComponentType;
import org.lemsml.jlems.core.type.Lems;
import org.lemsml.jlems.core.type.LemsCollection;
import org.lemsml.jlems.core.annotation.ModelElement;

@ModelElement(info = "By default, each Component in a model gives rise to a single instance of its state variables when the model is executed. " +
"The state variables are then governed by the dynamics definition in the associated ComponentType. Elements in the Structure declaration " +
" can be used to change this behavior, for example to make multiple instances of the state variables, or to instantiate a different component. " +
"A typical application for the latter would be a Component that defines a population of cells. The population Component might define the number " +
"of cells it contains but would refer to a Component defined elsewhere for the actual cell model to use.")
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
			BuilderElement bde = be.makeBuilder(cpt);
			if (bde != null) {
				b.add(bde);
			} else {
				throw new ContentError("null builder element from " + be);
		    }
		}
	
		return b;
	}

 
	

	
	
}
