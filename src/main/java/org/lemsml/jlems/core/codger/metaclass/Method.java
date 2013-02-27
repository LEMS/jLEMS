package org.lemsml.jlems.core.codger.metaclass;

import java.util.ArrayList;

import org.lemsml.jlems.core.logging.E;
 

public class Method {

	String name;
	
	VarType returnType;
	String returnName;
	
	
	ArrayList<AbstractMethodArgument> arguments = new ArrayList<AbstractMethodArgument>();
	
	ArrayList<AbstractOperation> ops = new ArrayList<AbstractOperation>();
	
 
	
	public Method(String nm) {
		returnType = VarType.VOID;
		name = nm;
	}
	 

	public void setReturnType(VarType typ) {
		returnType = typ;
	}
	
	public void setReturnName(String rnm) {
		returnName = rnm;
	}
	
	
	public String getName() {
		return name;
	}
	
	public VarType getReturnType() {
		return returnType;
	}

	public void addFloatArgument(String s) {
		arguments.add(new FloatMethodArgument(s));	
	}
	
	public void addStringArgument(String s) {
		arguments.add(new StringMethodArgument(s));
	}
	
	public void addMapArgument(String mnm, VarType keyType, VarType valType) {
		arguments.add(new MapMethodArgument(mnm, keyType, valType));
	}

	public MethodCall newMethodCall(String str) {
		 MethodCall mc = new MethodCall(str);
		 ops.add(mc);
		 return mc;
	}


	public void addIncrement(String vnm, AbstractExpression expr) {
		Increment inc = new Increment(vnm, expr);
		ops.add(inc);
	}

 
		
	 
	public String generateJava() {
 		StringBuilder sb = new StringBuilder();
		sb.append("public " + VarTypes.javaVarType(returnType) + " " + name + "(");
		boolean first = true;
	
		
		for (AbstractMethodArgument ma : arguments) {
			if (!first) {
				sb.append(", ");
			}
			first = false;
			sb.append(ma.generateJava());
		}
		sb.append(") {\n");
		
		
		if (returnType != null && returnType != VarType.VOID) {
			sb.append("    " + VarTypes.javaVarType(returnType) + " " + 
						returnName + " = " + VarTypes.javaVarDefault(returnType) + ";\n");
		}
		
		String opindent = "    ";
		for (AbstractOperation op : ops) {
			String str = op.generateJava();
			String[] bits = str.split("\n");
			for (String s : bits) {
				sb.append(opindent + s + "\n");
			}
 		}
	
		if (returnType != null && returnType != VarType.VOID) {
			sb.append("    return " + returnName + ";\n");
		}
		
		
		sb.append("}\n");
	
		return sb.toString();
	}

	 
		public String generateInterfaceJava() {
	 		StringBuilder sb = new StringBuilder();
			sb.append("public " + VarTypes.javaVarType(returnType) + " " + name + "(");
			boolean first = true;
			for (AbstractMethodArgument ma : arguments) {
				if (!first) {
					sb.append(", ");
				}
				first = false;
				sb.append(ma.generateJava());
			}
			sb.append(");\n");
			return sb.toString();
		}
		
		
	
 
	public String generateCallJava() {
		String ret = "" + name + "(";
		boolean first = true;
		for (AbstractMethodArgument ama : arguments) {
			if (!first) {
				ret = ret + ", ";
			}
			first = false;
 			ret = ret + ama.generateCallJava();
		}	
		ret = ret + ");";
		return ret;
	}

	
	
	
	public void addFloatAssignment(String varName, String expr) {
		FloatAssignment fa = new FloatAssignment(varName, expr);
		ops.add(fa);
	}
	
	public void addFloatMethodAssignment(String varName, String mnm) {
		FloatMethodAssignment fa = new FloatMethodAssignment(varName, mnm);
		ops.add(fa);
	}


	public void addArrayProduct(String vnm, String anm, String fn) {
		ArrayProductAssignment apa = new ArrayProductAssignment(vnm, anm, fn);
		ops.add(apa);
	}
	
	public void addArraySum(String vnm, String anm, String fn) {
		ArraySumAssignment asa = new ArraySumAssignment(vnm, anm, fn);
		ops.add(asa);
	}

	public boolean sameAs(Method cmm) {
		boolean ret=  false;
		if (name.equals(cmm.name) && returnType == cmm.returnType) {
			// TODO check args...
			ret = true;
		}
		return ret;
	}

	public void addRefCall(String scnm, MetaClass rmc, String mnm) {
		MethodCallOnChild rc = new MethodCallOnChild(scnm, rmc.getMethod(mnm));
		ops.add(rc);
	}

	public void addMultiRefCall(String anm, MetaInterface cmi, String mnm) {
		E.info("add mrc " + cmi + " ");
		MethodCallOnChildren mrc = new MethodCallOnChildren(anm, cmi.getClassName(), cmi.getMethod(mnm));
		ops.add(mrc);
	}

	public void addCall(Method method) {
		 LocalMethodCall mc = new LocalMethodCall(method);
		 ops.add(mc);
	}

	public void addMapDoubleExtraction(String var, String map, String val) {
		MapDoubleExtraction mde = new MapDoubleExtraction(var, map, val);
		ops.add(mde);
	}


	public void addStringConditionalSetter(String s1, String s2, String lnm, String cnm) {
		StringConditionalSetter scs = new StringConditionalSetter(s1, s2, lnm, cnm);
		ops.add(scs);
	}
	
}
