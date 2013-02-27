package org.lemsml.jlems.core.type;

import org.lemsml.jlems.core.annotation.ModelElement;
import org.lemsml.jlems.core.annotation.ModelProperty;


@ModelElement(info="Fixes the value of a parameter in the parent class, so that it does not have to be supplied separately " +
		"in component definitions.")
public class Fixed implements PseudoNamed  {

	@ModelProperty(info="")
	public String parameter;

	@ModelProperty(info="")
	public String value;

   
	
	public String getValue() {
		return value;
	}
	
	public String getPseudoName() {
		return parameter;
	}
	
}
