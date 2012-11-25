package org.lemsml.jlems.codger;

public class ObjectMetaField extends AbstractMetaField {

	String type;
	
	String pack;
	
	public ObjectMetaField(String p, String nm, String typ) {
		super(nm);
		pack = p;
		type = typ;
	}
	
	
	@Override
	public String generateJava() {
		String ret = "private " + type + " " + name + ";";
		return ret;
	}

}
