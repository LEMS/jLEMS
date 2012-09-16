package org.lemsml.nineml;

import org.lemsml.io.FormatException;
import org.lemsml.io.IOFace;
import org.lemsml.type.dynamics.StateVariable;

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
