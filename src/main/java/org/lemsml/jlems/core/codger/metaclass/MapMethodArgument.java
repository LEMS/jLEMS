package org.lemsml.jlems.core.codger.metaclass;

public class MapMethodArgument extends AbstractMethodArgument {

	VarType keyType;
	VarType valType;
	
	public MapMethodArgument(String s, VarType kt, VarType vt) {
		super(s);
		keyType = kt;
		valType = vt;
	}

 
	public String generateJava() {
		return "HashMap<" + VarTypes.javaVarType(keyType) + "," + VarTypes.javaVarType(valType) + "> " + name;
	}
	
	public String generateCallJava() {
		return name;
	}
	
}
