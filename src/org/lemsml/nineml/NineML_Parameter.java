package org.lemsml.nineml;

import org.lemsml.io.FormatException;
import org.lemsml.io.IOFace;
import org.lemsml.type.Parameter;

public class NineML_Parameter implements IOFace {

	public String name;
	public String dimension;
	
	
	 
	public Parameter getParameter() throws FormatException {
		Parameter p = new Parameter(name, dimension);
		return p;
	}



	@Override
	public Object getInternal() throws FormatException {
		return getParameter();
	}
	
	
	
	
	
	
}
