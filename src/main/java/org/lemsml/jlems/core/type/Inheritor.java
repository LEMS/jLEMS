package org.lemsml.jlems.core.type;

import org.lemsml.jlems.core.sim.ContentError;

public interface Inheritor {

	boolean inherited(Object obj) throws ContentError;
	
}
