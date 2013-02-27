package org.lemsml.jlems.core.codger.metaclass;

public abstract class AbstractGetter {

	String name;
	String valName;
	
	public AbstractGetter(String sn, String sv) {
		name = sn;
		valName = sv;
	}
	
	public abstract String generateJava();

	public abstract String generateInterfaceJava();
	
	public boolean sameAs(AbstractGetter cmm) {
		boolean ret = false;
		if (name.equals(cmm.name)) {
			ret = true;
		}
		return ret;
	}
	
	
}
