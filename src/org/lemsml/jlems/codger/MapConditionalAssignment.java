package org.lemsml.jlems.codger;

import java.util.HashMap;

import org.lemsml.jlems.logging.E;

public class MapConditionalAssignment extends Operation {

	VarType varType;
	String varname;
	String fname;
	HashMap<String, String> map;
	
	
	public MapConditionalAssignment(VarType d, String vnm, String fnm, HashMap<String, String> exposureMap) {
		varType = d;
		varname = vnm;
		fname = fnm;
		map = exposureMap;
	}


	@Override
	public String generateJava() {
		E.missing();
		return "";
	}

}
