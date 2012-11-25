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
   
	 
	
	public void resolve(Lems lems, Parser p) throws ContentError, ParseError {
		// nothing to do
	}


	public String getName() {
		return name;
	}

 
	public ComponentTypeReference makeCopy() {
		 ComponentTypeReference ret = new ComponentTypeReference();
		 ret.name = name;
		 return ret;
	}

  
}
