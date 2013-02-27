package org.lemsml.jlems.core.codger.metaclass;

public class ObjectToArrayInstantiation extends Instantiation {

	String pack;
	
	public ObjectToArrayInstantiation(String pkg, String nm, String typ) {
		super(nm, typ);
		pack = pkg;
	}
	
	@Override
	public String generateJava() {
		String ret = "" + name + ".add(new " + type + "());\n";
		return ret;
	}

}
