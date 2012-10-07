package org.lemsml.jlems.type;

import org.lemsml.jlems.sim.ContentError;

public interface Inheritor {

	public boolean inherited(Object obj) throws ContentError;
	
}
