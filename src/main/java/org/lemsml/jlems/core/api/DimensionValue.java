package org.lemsml.jlems.core.api;

import java.util.HashMap;

import org.lemsml.jlems.core.type.Dimension;

public class DimensionValue {

	HashMap<BaseDimension, Integer> dims = new HashMap<BaseDimension, Integer>();
 
	
	public void add(BaseDimension bd, int i) {
		if (dims.containsKey(bd)) {
			dims.put(bd, dims.get(bd) + i);
		} else {
			dims.put(bd, i);
		}
	}
	
	
	

	public Dimension buildDimension(String name) {
		Dimension ret = new Dimension(name);
		for (BaseDimension bd : dims.keySet()) {
			bd.apply(ret, dims.get(bd));
		}
		return ret;
	}
	
	
}
