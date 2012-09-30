package org.lemsml.jlems.type;

import org.lemsml.jlems.util.ContentError;

public interface Parented {

	public void setParent(Object ob) throws ContentError;
	
}
