package org.lemsml.io;

import java.util.ArrayList;


public interface IOFaceMixed extends IOFace {

	 public void addPending(Object obj);
	 
	 public ArrayList<Object> getPending();
 
	
}
