package org.lemsml.jlems.core.run;

import java.util.Random;

import org.lemsml.jlems.core.logging.E;

public class RunRandom {

	static RunRandom instance = null;
	

	final double Ee = 2.718281828459;
	 	 
	Random random;
	
	
	public static RunRandom getInstance() {
		if (instance == null) {
			instance = new RunRandom();
		}
		return instance;
	}
	
	public RunRandom() {
		random = new Random();
	}

	public double nextGaussian() {
		return random.nextGaussian();
	}
	
	
	public double nextUniform() {
		return random.nextDouble();
	}
	
	

	
	
    /**
     * 
     * RCC copied from Apache Commons Math: if we need more than just this method, may be worth adding the dependency.
     * 
     * <p>This implementation uses the following algorithms: </p>
     *
     * <p>For 0 < shape < 1: <br/>
     * Ahrens, J. H. and Dieter, U., <i>Computer methods for
     * sampling from gamma, beta, Poisson and binomial distributions.</i>
     * Computing, 12, 223-246, 1974.</p>
     *
     * <p>For shape >= 1: <br/>
     * Marsaglia and Tsang, <i>A Simple Method for Generating
     * Gamma Variables.</i> ACM Transactions on Mathematical Software,
     * Volume 26 Issue 3, September, 2000.</p>
     *
     * @return random value sampled from the Gamma(shape, scale) distribution
     */
    
    public double nextGamma(double mean, double shape)  {
    	double scale = mean / shape;
    	
        if (shape < 1) {
            // [1]: p. 228, Algorithm GS

            while (true) {
                // Step 1:
                final double u = random.nextDouble();
                final double bGS = 1 + shape / Ee;
                final double p = bGS * u;

                if (p <= 1) {
                    // Step 2:

                    final double x = Math.pow(p, 1 / shape);
                    final double u2 = random.nextDouble();

                    if (u2 > Math.exp(-x)) {
                        // Reject
                        continue;
                    } else {
                        return scale * x;
                    }
                } else {
                    // Step 3:

                    final double x = -1 * Math.log((bGS - p) / shape);
                    final double u2 = random.nextDouble();

                    if (u2 > Math.pow(x, shape - 1)) {
                        // Reject
                        continue;
                    } else {
                        return scale * x;
                    }
                }
            }
        }

        // Now shape >= 1

        final double d = shape - 0.333333333333333333;
        final double c = 1 / (3 * Math.sqrt(d));

        while (true) {
            final double x = random.nextGaussian();
            final double v = (1 + c * x) * (1 + c * x) * (1 + c * x);

            if (v <= 0) {
                continue;
            }

            final double x2 = x * x;
            final double u = random.nextDouble();

            // Squeeze
            if (u < 1 - 0.0331 * x2 * x2) {
                return scale * d * v;
            }

            if (Math.log(u) < 0.5 * x2 + d * (1 - v + Math.log(v))) {
                return scale * d * v;
            }
        }
    }

	
	
	
	
	
}
