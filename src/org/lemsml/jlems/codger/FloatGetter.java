package org.lemsml.jlems.codger;
 
public class FloatGetter extends AbstractGetter {

	
	String gnm;
	String valName;
	
	
	public FloatGetter(String s, String val) {
		super();
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
