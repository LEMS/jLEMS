package org.lemsml.run;

import org.lemsml.util.ContentError;

public interface ChildInstantiator {

	public void childInstantiate(StateInstance si) throws ContentError, ConnectionError;
	
}
