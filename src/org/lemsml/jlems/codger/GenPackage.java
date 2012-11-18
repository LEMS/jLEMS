package org.lemsml.jlems.codger;

public class GenPackage {

	
	String pckg;
	
	GenPackage parent;
	
	public GenPackage(String p) {
		pckg = p;
	}

	 
	public GenPackage(String p, GenPackage gp) {
		this(p);
		parent = gp;
	}

	
	

	String getPackageName() {
		String ret = "";
		if (parent != null) {
			ret = parent.getPackageName();
		}
		if (ret.length() > 0) {
			ret += ".";
		}
		ret += pckg;
		return ret;
	}
	

	public String getFQClassName(String cid) {
		 String ret = getPackageName() + "." + cid;
		 return ret;
	}

}
