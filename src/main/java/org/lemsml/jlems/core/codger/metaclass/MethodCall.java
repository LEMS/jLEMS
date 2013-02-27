package org.lemsml.jlems.core.codger.metaclass;

import java.util.ArrayList;

public class MethodCall extends AbstractOperation {

	String methodName;

	ArrayList<AbstractMethodArgument> arguments = new ArrayList<AbstractMethodArgument>();
	
	public MethodCall(String str) {
		super();
		methodName = str;
	}

	@Override
	public String generateJava() {
		String ret = "" + methodName + "(";
		boolean first = true;
		for (AbstractMethodArgument ama : arguments) {
			if (!first) {
				ret = ret + ", ";
			}
 			ret = ret + ama.generateCallJava();
		}	
		ret = ret + ");";
		return ret;
	}

	 
	public void addFloatArgument(String s) {
		arguments.add(new FloatMethodArgument(s));
		
	}
 
}
