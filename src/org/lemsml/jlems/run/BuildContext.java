package org.lemsml.jlems.run;

public class BuildContext {

	InstancePairSet<StateRunnable> wkPairs;
	
 
	
	public void setWorkPairs(InstancePairSet<StateRunnable> ips) {
		wkPairs = ips;
	}
	
	public InstancePairSet<StateRunnable> getWorkPairs() {
		return wkPairs;
	}
}
