package org.lemsml.io;

import org.lemsml.util.ContentError;


public interface IOFace {

	public Object getInternal() throws FormatException, ContentError;
 
	
}
