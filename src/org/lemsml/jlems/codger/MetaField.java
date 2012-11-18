package org.lemsml.jlems.codger;

import org.lemsml.jlems.logging.E;

public abstract class MetaField {

	public enum Type {DOUBLE, INTEGER, STRING, VOID}
	String name;
	
	
	public MetaField(String nm) {
		name = nm;
	}
	
	public abstract String generateJava();

	public static String defaultDeclare(VarType vt, String varname) {
		String ret = "";
		if (vt.equals(VarType.DOUBLE)) {
			ret = "double " + varname + " = 0;";
		} else if (vt.equals(VarType.STRING)) {
			ret = "String " + varname + " = \"\";";
		} else if (vt.equals(VarType.INTEGER)) {
			ret = "int " + varname + " = 0;";
		} else {
			E.error("Unrecognized var type " + vt);
		}
		return ret;
	}
	
}

