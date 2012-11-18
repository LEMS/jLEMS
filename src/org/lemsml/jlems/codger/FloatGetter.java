package org.lemsml.jlems.codger;

import org.lemsml.jlems.util.StringUtil;

public class FloatGetter extends Getter {

	
	String gnm;
	String valName;
	
	
	public FloatGetter(String s, String val) {
		gnm = s;
		valName = val;
 	}


	@Override
	public String generateJava() {
 		String ret = "public double get_" + gnm + "() {\n";
		ret += "    return " + valName + ";\n";
		ret += "}\n";
		return ret;
	}

}
