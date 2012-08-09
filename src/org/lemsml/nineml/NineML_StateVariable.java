package org.lemsml.nineml;

import org.lemsml.behavior.StateVariable;
import org.lemsml.io.FormatException;
import org.lemsml.io.IOFace;

public class NineML_StateVariable implements IOFace {

	public String name;
	public String dimension;
	
	 
		
	
	public StateVariable getStateVariable() {	
		StateVariable sv = new StateVariable(name);
		sv.dimension = dimension;
		
		return sv;
	}




	@Override
	public Object getInternal() throws FormatException {
		return getStateVariable();
	}
	
	
	
	
}
