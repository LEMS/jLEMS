package org.lemsml.jlems.core.type;

import org.lemsml.jlems.core.annotation.ModelElement;
import org.lemsml.jlems.core.annotation.ModelProperty;


@ModelElement(info="Defines a named collection of paris of instances, similar to the Collection element.")
public class PairCollection implements Named {

	@ModelProperty(info="")
	public String name;
	
	public String getName() {
		return name;
	}
	

	public PairCollection makeCopy() {
		PairCollection ret = new PairCollection();
		ret.name = name;
		return ret;
	}
	
}
