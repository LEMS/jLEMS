package org.lemsml.jlems.core.run;

import org.lemsml.jlems.core.logging.E;

public class BuildNormalDistribution extends BuildDistribution {

	double mean;
	double sd;
	
	public BuildNormalDistribution(double m, double s) {
		mean = m;
		sd = s;
	}

	@Override
	public double getSample() {
		
		double ret = mean + sd * RunRandom.getInstance().nextGaussian();
		
		E.info("Sample from normal returning " + ret + " mean=" + mean + " sd=" + sd);
		
		return ret;
	}
	
	
	
	
	
}
