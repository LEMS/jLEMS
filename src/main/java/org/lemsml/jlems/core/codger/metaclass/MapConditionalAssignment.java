package org.lemsml.jlems.core.codger.metaclass;

import java.util.HashMap;


 

public class MapConditionalAssignment extends AbstractOperation {

	VarType varType;
	String varname;
	String fname;
	HashMap<String, String> map;
	
	
	public MapConditionalAssignment(VarType d, String vnm, String fnm, HashMap<String, String> exposureMap) {
		super();
		varType = d;
		varname = vnm;
		fname = fnm;
		map = exposureMap;
	}


	@Override
	public String generateJava() {
		StringBuilder sb = new StringBuilder();
		sb.append(AbstractField.defaultDeclare(varType, varname));
		sb.append("\n");
		sb.append("if (" + fname + ".equals(\"NONE\") {\n" + "}");
		for (String s : map.keySet()) {
			sb.append(" else if (" + fname + ".equals(" + s + ")) {\n" + "   " + varname + " = " + map.get(s) + ";\n");
			sb.append("}");
		}
		 
		return sb.toString();
	}

}
