package org.lemsml.jlems.core.codger.metaclass;

public class MapDoubleExtraction extends AbstractOperation {
 
	String var;
	String map;
	String val;
	
	
	public MapDoubleExtraction(String vnm, String mnm, String fnm) {
		var = vnm;
		map = mnm;
		val = fnm;
	}

	@Override
	public String generateJava() {
		String ret = "double " + var + " = " + map + ".get(\"" + val + "\").getValue();\n";
		return ret;
	}

 
}
