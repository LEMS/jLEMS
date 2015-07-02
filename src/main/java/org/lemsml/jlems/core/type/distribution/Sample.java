package org.lemsml.jlems.core.type.distribution;

import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.core.type.LemsCollection;

public class Sample {
	
	
	public String parameter;
	
	public LemsCollection<Distribution> distributions = new LemsCollection<Distribution>();
	
	
	
	public String getParameterName() {
		return parameter;
	}
	
	public Distribution getDistribution() {
		return distributions.first();
	}
	

}
