package org.lemsml.jlems.type.dynamics;

import org.lemsml.jlems.annotation.ModelProperty;
import org.lemsml.jlems.annotation.ModelElement;

@ModelElement(info="The children attribute should point to a children declaration in the parent element. " +
		"The variable field specifies the variable in the state elements that is governed by the scheme.")
public class Nodes {
	
	@ModelProperty(info="")
	public String children;
	
	@ModelProperty(info="")
	public String variable;
	
}
