package org.lemsml.jlems.core.type;

import org.lemsml.jlems.core.sim.ContentError;

public interface Parented {

	void setParent(Object ob) throws ContentError;
	
}
