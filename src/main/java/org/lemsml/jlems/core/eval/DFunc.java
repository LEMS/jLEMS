package org.lemsml.jlems.core.eval;

import java.util.ArrayList;
import java.util.HashSet;

import org.lemsml.jlems.core.expression.FunctionNode;


public class DFunc extends AbstractDVal {
	
	String fname; 
	
	public AbstractDVal arg;
	
	public DFunc(String fnm, AbstractDVal dva) {
		super();
		fname = fnm;
		arg = dva;
	}
	
	public DFunc makeCopy() {
		return new DFunc(fname, arg.makeCopy());
	}

	@Override
	public AbstractDVal makePrefixedCopy(String pfx, HashSet<String> stetHS) {
		return new DFunc(fname, arg.makePrefixedCopy(pfx, stetHS));
	}
	

    @Override
    public String toExpression() {
            return fname + "(" + arg.toExpression() + ")";
    }


	public double eval() {
		return FunctionNode.evaluate(arg.eval(), fname);
	}
		
	 /*
	public double eval() {
		double ret = 0;
        Double aval = arg.eval();

		if (fname.equals(Parser.SIN)) {
			ret = Math.sin(aval);
		
		} else if (fname.equals(Parser.COS)) {
			ret = Math.cos(aval);
		
		} else if (fname.equals(Parser.TAN)) {
			ret = Math.tan(aval);
		
		} else if (fname.equals(Parser.LN) || fname.equals(Parser.LOG)) {
			ret = Math.log(aval);
		
		} else if (fname.equals(Parser.EXP)) {
			ret = Math.exp(aval);

		} else if (fname.equals(Parser.SQRT)) {
			ret = Math.sqrt(aval);

		} else if (fname.equals(Parser.CEIL)) {
			ret = Math.ceil(aval);
			
		} else if (fname.equals(Parser.ABS)) {
			ret = Math.abs(aval);

		} else if (fname.equals(Parser.FACTORIAL)) {
			int intVal = aval.intValue();
			int fact = 1; 
	        for (int i = 1; i <= intVal; i++) {
	            fact *= i;
	        }
			ret = fact;
		
		} else if (fname.equals(Parser.RANDOM)) {
			ret = aval * Lems.getRandomGenerator().nextDouble();
		} else {
			E.missing();
		}
		return ret;
	}*/


	 
	public void recAdd(ArrayList<DVar> val) {
		arg.recAdd(val);
	}
	 
	public void substituteVariableWith(String var, String sub) {
		arg.substituteVariableWith(var, sub);
	}

	@Override
	public boolean variablesIn(HashSet<String> known) {
		return arg.variablesIn(known);
	}
 
}
