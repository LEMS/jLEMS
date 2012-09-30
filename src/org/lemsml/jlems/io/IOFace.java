package org.lemsml.jlems.io;

import org.lemsml.jlems.util.ContentError;


public interface IOFace {

	public Object getInternal() throws FormatException, ContentError;
 
	
}
