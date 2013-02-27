package org.lemsml.jlems.core.codger.metaclass;

public abstract class ArrayReduceAssignment extends AbstractOperation {

	String varName;
	String arrayName;
	String functionName;
	
	public ArrayReduceAssignment(String vnm, String anm, String fnm) {
		super();
		varName = vnm;
		arrayName = anm;
		functionName = fnm;
	}

	 

 
}
