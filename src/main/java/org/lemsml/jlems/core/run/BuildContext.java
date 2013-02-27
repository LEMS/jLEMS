package org.lemsml.jlems.core.run;

public class BuildContext {

	InstancePairSet<StateRunnable> wkPairs;
	
 
	
	public void setWorkPairs(InstancePairSet<StateRunnable> ips) {
		wkPairs = ips;
	}
	
	public InstancePairSet<StateRunnable> getWorkPairs() {
		return wkPairs;
	}
}
