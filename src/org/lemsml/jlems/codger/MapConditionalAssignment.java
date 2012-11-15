package org.lemsml.jlems.codger;

import java.util.HashMap;

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

}
