package org.lemsml.jlems.type;

import org.lemsml.jlems.sim.ContentError;

public interface Parented {

	public void setParent(Object ob) throws ContentError;
	
}
