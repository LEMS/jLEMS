package org.lemsml.jlems.run;

import org.lemsml.jlems.util.ContentError;
import org.lemsml.jlems.util.RuntimeError;

public interface ChildInstantiator {

	public void childInstantiate(StateInstance si) throws ContentError, ConnectionError, RuntimeError;
	
}
