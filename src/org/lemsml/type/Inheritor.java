package org.lemsml.type;

import org.lemsml.util.ContentError;

public interface Inheritor {

	public boolean inherited(Object obj) throws ContentError;
	
}
