package org.lemsml.jlems.core.codger.metaclass;

import java.util.ArrayList;
import java.util.HashSet;


// abstract superclass of MetaClass and MetaInterface
public abstract class CodeUnit {

	public String name;
	
	public MetaPackage metaPackage;

	public HashSet<String> pkgHS = new HashSet<String>();
	public HashSet<String> clsHS = new HashSet<String>();
	
	
	public CodeUnit(MetaPackage gp, String s) {
		metaPackage = gp;
		name = s;
	}

	public String getClassName() {
		return name;
	}
	
	
	public void addIncludePackage(String p) {
		pkgHS.add(p);
	}
	

	
	public ArrayList<String> getSubPackages() {
		return metaPackage.getSubPackages();
	}


	public String getFQClassName() {
		String ret = metaPackage.getPackageName() + "." + name;
		return ret;
	}
	


	public abstract String generateJava();
	 
	
}
