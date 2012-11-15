package org.lemsml.jlems.codger;

import java.util.ArrayList;
import java.util.HashMap;

import org.lemsml.jlems.codger.MetaField.Type;

public class MetaMethod {

	String name;
	
	VarType returnType;
	String returnName;
	
	
	ArrayList<MethodArgument> arguments = new ArrayList<MethodArgument>();
	
	ArrayList<Operation> ops = new ArrayList<Operation>();
	
	
	public MetaMethod(VarType typ, String nm, String rnm) {
		returnType = typ;
		name = nm;
		returnName = rnm;
	}


	public void addFloatArgument(String s) {
		arguments.add(new FloatMethodArgument(s));
		
	}


	public MethodCall newMethodCall(String str) {
		 MethodCall mc = new MethodCall(str);
		 ops.add(mc);
		 return mc;
	}


	public void addIncrement(String vnm, Expression expr) {
		Increment inc = new Increment(vnm, expr);
		ops.add(inc);
	}


	public void addMapConditionalAssignment(VarType d, String vnm, String fnm,
			HashMap<String, String> exposureMap) {
		MapConditionalAssignment mca = new MapConditionalAssignment(d, vnm, fnm, exposureMap);
		ops.add(mca);
	}
		
	 

}
