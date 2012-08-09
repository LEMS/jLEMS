package org.lemsml.type;

import org.lemsml.annotation.Mat;
import org.lemsml.annotation.Mel;


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
