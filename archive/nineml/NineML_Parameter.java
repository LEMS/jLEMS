package org.lemsml.jlems.nineml;

import org.lemsml.jlems.io.FormatException;
import org.lemsml.jlems.io.IOFace;
import org.lemsml.jlems.type.Parameter;

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
