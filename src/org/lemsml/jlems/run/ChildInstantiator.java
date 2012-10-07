package org.lemsml.jlems.run;

import org.lemsml.jlems.sim.ContentError;


public interface ChildInstantiator {

	public void childInstantiate(StateInstance si) throws ContentError, ConnectionError, RuntimeError;
	
}
