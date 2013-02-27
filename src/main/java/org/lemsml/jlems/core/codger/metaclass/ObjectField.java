package org.lemsml.jlems.core.codger.metaclass;


public class ObjectField extends AbstractField {

	String type;
	
	String pack;
	
	public ObjectField(String p, String nm, String typ) {
		super(nm);
		pack = p;
		type = typ;
	}
	
	
	@Override
	public String generateJava() {
		
//		String ret = "private " + type + " " + name + ";";
		
		String ret = "private " + type + " " + name + ";";
		return ret;
	}

}
