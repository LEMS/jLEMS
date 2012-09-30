package org.lemsml.jlems.run;

public class BuildContext {

	InstancePairSet<StateInstance> wkPairs;
	
	public BuildContext() {
		
	}
	
	public void setWorkPairs(InstancePairSet<StateInstance> ips) {
		wkPairs = ips;
	}
	
	public InstancePairSet<StateInstance> getWorkPairs() {
		return wkPairs;
	}
}
