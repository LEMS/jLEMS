package org.lemsml.jlems.core.codger.metaclass;

import org.lemsml.jlems.core.logging.E;

public class VarTypes {
	
	public static String javaVarType(VarType vt) {
		String ret = "";
		if (vt.equals(VarType.DOUBLE)) {
			ret = "double";
		} else if (vt.equals(VarType.STRING)) {
			ret = "String";
		} else if (vt.equals(VarType.INTEGER)) {
			ret = "int";
		} else if (vt.equals(VarType.VOID)) {
			ret = "void";
		} else if (vt.equals(VarType.DOUBLEPOINTER)) {
			ret = "DoublePointer";
		} else {
			E.error("Unrecognized var type " + vt);
		}
		return ret;
	}
	
	
	public static String javaVarDefault(VarType vt) {
		String ret = "";
		if (vt.equals(VarType.DOUBLE)) {
			ret = "0.";
		} else if (vt.equals(VarType.STRING)) {
			ret = "\"\"";
		} else if (vt.equals(VarType.INTEGER)) {
			ret = "0";
		} else if (vt.equals(VarType.VOID)) {
			ret = "";
		} else {
			E.error("Unrecognized var type " + vt);
		}
		return ret;
	}

}
