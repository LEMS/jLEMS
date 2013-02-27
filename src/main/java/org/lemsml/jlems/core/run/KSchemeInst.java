package org.lemsml.jlems.core.run;

import org.lemsml.jlems.core.sim.ContentError;


public class KSchemeInst {

	KScheme scheme;
	
	MultiInstance stateMI;
	MultiInstance rateMI;
	
	double[] occ;
	
	
	public KSchemeInst(KScheme ks, MultiInstance smi, MultiInstance rmi) {
		 scheme = ks;
		 stateMI = smi;
		 rateMI = rmi;
	
		 
		 int ns = scheme.nstate;
		 occ = new double[ns];
		 // TODO equilibrium initialization
		 for (int i = 0; i < ns; i++) {
			 occ[i] = 1. / ns;
		 }
	}


	public String getName() {
		 return scheme.getName();
	}

    public void initialize(StateRunnable parent) throws RuntimeError, ContentError {
        stateMI.initialize(parent);
        rateMI.initialize(parent);
    }

	public void advance(StateInstance stateInstance, double t, double dt) throws RuntimeError {	
		scheme.advance(stateInstance, this, dt);
	}


	public void evaluate(StateInstance stateInstance) throws RuntimeError {
		scheme.evaluate(stateInstance, this);
	}

	
	
}
