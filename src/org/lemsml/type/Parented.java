package org.lemsml.type;

import org.lemsml.util.ContentError;

public interface Parented {

	public void setParent(Object ob) throws ContentError;
	
}
