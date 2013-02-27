package org.lemsml.jlems.core.codger.metaclass;

 
public class FloatGetter extends AbstractGetter {

	
	
	public FloatGetter(String s, String val) {
		super(s, val);
	 
 	}


	@Override
	public String generateJava() {
 		String ret = "public double get_" + name + "() {\n";
		ret += "    return " + valName + ";\n";
		ret += "}\n";
		return ret;
	}
	
	
	public String generateInterfaceJava() {
		String ret = "public double get_" + name + "();\n";
		return ret;
	}
	
	
	
	
}
