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
		E.missing("Need to sample normal");
		return mean;
	}
	
	
}
