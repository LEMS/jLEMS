package org.lemsml.run;

import org.lemsml.util.ContentError;
import org.lemsml.util.RuntimeError;

public interface ChildInstantiator {

	public void childInstantiate(StateInstance si) throws ContentError, ConnectionError, RuntimeError;
	
}
