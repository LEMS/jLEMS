package org.lemsml.jlems.type;

import org.lemsml.jlems.annotation.ModelElement;
import org.lemsml.jlems.annotation.ModelProperty;
import org.lemsml.jlems.expression.ParseError;
import org.lemsml.jlems.expression.Parser;
import org.lemsml.jlems.sim.ContentError;

@ModelElement(info="This is used in conjunction with PathParameter elements to specify the target class of selections " +
		"defined within components operating over the instance tree.")
public class ComponentTypeReference implements Named {

	@ModelProperty(info="")
	public String name;
  	
	public ComponentTypeReference() {
		
	}
	
	
	public ComponentTypeReference(String sn) {
	 
	}
	
	
	public void resolve(Lems lems, Parser p) throws ContentError, ParseError {
	 
	}


	public String getName() {
		return name;
	}

 
	public ComponentTypeReference makeCopy() {
		 return new ComponentTypeReference(name);
	}

  
}
