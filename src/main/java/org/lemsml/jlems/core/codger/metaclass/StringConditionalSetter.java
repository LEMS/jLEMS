package org.lemsml.jlems.core.codger.metaclass;

public class StringConditionalSetter extends AbstractOperation {
 
	String cs1;
	String cs2;
	
	String locName;
	String clsName;
	
	
	public StringConditionalSetter(String s1, String s2, String lvnm, String cvnm) {
		 cs1 = s1;
		 cs2 = s2;
		 locName = lvnm;
		 clsName = cvnm;
	}

	@Override
	public String generateJava() {
		String ret = "if (" + cs1 + ".equals(\"" + cs2 + "\")) {\n";
		ret += "    " + locName + " = " + clsName + ";\n";
		ret += "}";
		return ret;
	}

 
}
