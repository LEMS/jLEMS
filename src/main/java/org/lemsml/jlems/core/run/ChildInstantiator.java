package org.lemsml.jlems.core.run;

import org.lemsml.jlems.core.sim.ContentError;


public interface ChildInstantiator {

	void childInstantiate(StateInstance si) throws ContentError, ConnectionError, RuntimeError;
	
}
