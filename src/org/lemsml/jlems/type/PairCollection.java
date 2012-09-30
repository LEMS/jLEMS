package org.lemsml.jlems.type;

import org.lemsml.jlems.annotation.Mat;
import org.lemsml.jlems.annotation.Mel;


@Mel(info="Defines a named collection of paris of instances, similar to the Collection element.")
public class PairCollection implements Named {

	@Mat(info="")
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
