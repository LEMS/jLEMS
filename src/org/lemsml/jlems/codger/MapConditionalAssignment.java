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
		StringBuilder sb = new StringBuilder();
		sb.append(MetaField.defaultDeclare(varType, varname));
		sb.append("\n");
		sb.append("if (" + fname + ".equals(\"NONE\") {\n" + "}");
		for (String s : map.keySet()) {
			sb.append(" else if (" + fname + ".equals(" + s + ")) {\n" + "   " + varname + " = " + map.get(s) + ";\n");
			sb.append("}");
		}
		 
		return sb.toString();
	}

}
