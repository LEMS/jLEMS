package org.lemsml.jlems.codger;

public class ObjectArrayField extends AbstractMetaField {

	String type;
	
	String pack;
	
	public ObjectArrayField(String p, String nm, String typ) {
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
