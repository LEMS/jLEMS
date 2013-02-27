package org.lemsml.jlems.core.codger.metaclass;


public class ArrayField extends AbstractField {

	String type;
	
	String pack;
	
	public ArrayField(String p, String nm, String typ) {
		super(nm);
		pack = p;
		type = typ;
	}
	
	
	@Override
	public String generateJava() {
		String ret = "private ArrayList<" + type + "> " + name + " = new ArrayList<" + type + ">();";
		return ret;
	}

}
