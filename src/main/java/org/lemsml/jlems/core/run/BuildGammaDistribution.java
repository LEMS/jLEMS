package org.lemsml.jlems.core.run;

import org.lemsml.jlems.core.logging.E;

public class BuildGammaDistribution extends BuildDistribution {

	double mean;
	double shape;
	
	
	public BuildGammaDistribution(double m, double s) {
		mean = m;
		shape = s;
	}

	public double getSample() {
		return RunRandom.getInstance().nextGamma(mean, shape);
	}

	
	
	
	public static void main(String[] argv) {
	 
	
		for (int i = 1; i < 10; i+= 2) {
			double m = 1 + 20 * Math.random();
			double s = 1. * i;
			BuildGammaDistribution bgd = new BuildGammaDistribution(m, s);
			bgd.checkMean();
		}
	}

	
	private void checkMean() {
		 
		int[] ns = {10, 100, 1000, 10000};
		for (int k = 0; k < ns.length; k++) {
			int n = ns[k];
			
			for (int l = 0; l < 1; l++) {
				
				double tot = 0;
				for (int i = 0; i < n; i++) {
					tot += getSample();
				}
				double avg = tot/n;
				System.out.println("n=" + n + ", shape=" + shape + ", err=" + (avg - mean)/mean);
			}
		}
	}
	
}
