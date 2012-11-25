package org.lemsml.jlems.codger;

public abstract class AbstractMethodArgument {
	public String name;
	
	protected AbstractMethodArgument(String nm) {
		name = nm;
	}

	public abstract String generateJava();
	 
	
}
