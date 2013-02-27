package org.lemsml.jlems.core.codger.metaclass;

public class ArraySumAssignment extends ArrayReduceAssignment {
 
	
	public ArraySumAssignment(String vnm, String anm, String fnm) {
		super(vnm, anm, fnm);
	}

	@Override
	public String generateJava() {
		String ret = "" + varName + " =  0;\n";
		ret += "for (" + arrayName + "_base wk : " + arrayName + "_list) {\n";
		ret += varName + " += wk" + functionName + ";\n";
		ret += "}\n";
		return ret;
	}

 
}
