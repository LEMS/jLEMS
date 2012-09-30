package org.lemsml.jlems.type;

import org.lemsml.jlems.util.ContentError;

public interface Inheritor {

	public boolean inherited(Object obj) throws ContentError;
	
}
