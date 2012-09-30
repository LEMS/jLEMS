package org.lemsml.jlems.type.dynamics;

import org.lemsml.jlems.annotation.Mat;
import org.lemsml.jlems.annotation.Mel;

@Mel(info="The children attribute should point to a children declaration in the parent element. " +
		"The variable field specifies the variable in the state elements that is governed by the scheme.")
public class Nodes {
	
	@Mat(info="")
	public String children;
	
	@Mat(info="")
	public String variable;
	
}
