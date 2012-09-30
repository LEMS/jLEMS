package org.lemsml.jlems.type;

import org.lemsml.jlems.annotation.Mat;
import org.lemsml.jlems.annotation.Mel;


@Mel(info="Fixes the value of a parameter in the parent class, so that it does not have to be supplied separately " +
		"in component definitions.")
public class Fixed implements PseudoNamed  {

	@Mat(info="")
	public String parameter;

	@Mat(info="")
	public String value;

   
	
	public String getValue() {
		return value;
	}
	
	public String getPseudoName() {
		return parameter;
	}
	
}
