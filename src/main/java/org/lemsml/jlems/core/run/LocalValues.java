package org.lemsml.jlems.core.run;

import java.util.HashMap; 

import java.util.Set;

public class LocalValues {
	
	
	HashMap<String, DoublePointer> valueMap = new HashMap<String, DoublePointer>();
	
	 

	public Set<String> keySet() {
		return valueMap.keySet();
	}
	
	public double getValue(String s) {
		return valueMap.get(s).getValue();
	}


	public void addValue(String name, double value) {
		 valueMap.put(name, new DoublePointer(value));
	}

}
