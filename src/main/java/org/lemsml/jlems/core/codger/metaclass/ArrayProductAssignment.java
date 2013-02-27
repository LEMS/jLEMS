package org.lemsml.jlems.core.codger.metaclass;

public class ArrayProductAssignment extends ArrayReduceAssignment {
 
	
	public ArrayProductAssignment(String vnm, String anm, String fnm) {
		super(vnm, anm, fnm);
	}

	@Override
	public String generateJava() {
		String ret = "" + varName + " =  1;\n";
		ret += "for (" + arrayName + "_base wk : " + "arr_" + arrayName +  ") {\n";
		ret += "    " + varName + " *= wk" + functionName + ";\n";
		ret += "}\n";
		return ret;
	}

 
}
