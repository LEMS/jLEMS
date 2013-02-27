package org.lemsml.jlems.core.codger.metaclass;

public class FloatAssignment extends AbstractOperation {

	String varName;
	String expr;
	
	public FloatAssignment(String vnm, String ex) {
		super();
		varName = vnm;
		expr = ex;
	}

	@Override
	public String generateJava() {
		String ret = "" + varName + " = " + toJavaFuncs(expr) + ";";
		return ret;
	}

	
	private String toJavaFuncs(String expr) {
		String ret = " " + expr;
		String[] fns = {"exp", "pow"};
		String[] jfns = {"Math.exp", "Math.pow"};
		
		
		for (int i = 0; i < fns.length; i++) {
			String df = fns[i];
			String jf = jfns[i];
			ret= ret.replaceAll(" " + df + "\\(", " " + jf + "\\(");
		}
		ret = ret.trim();
		return ret;
	}
}
