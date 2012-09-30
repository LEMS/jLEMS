package org.lemsml.jlems.nineml;

import org.lemsml.jlems.io.FormatException;
import org.lemsml.jlems.io.IOFace;
import org.lemsml.jlems.type.dynamics.StateVariable;

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
