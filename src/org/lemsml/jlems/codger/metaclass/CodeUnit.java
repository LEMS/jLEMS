package org.lemsml.jlems.codger.metaclass;

import java.util.ArrayList;
import java.util.HashSet;


// abstract superclass of MetaClass and MetaInterface
public abstract class CodeUnit {

	public String name;
	
	public MetaPackage metaPackage;

	public HashSet<String> pkgHS = new HashSet<String>();
	
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




	public abstract String generateJava();
	 
	
}
