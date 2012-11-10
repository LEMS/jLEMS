package org.lemsml.jlems.expression;

import java.util.HashMap;

import org.lemsml.jlems.eval.DFunc;
import org.lemsml.jlems.eval.DVal;
import org.lemsml.jlems.logging.E;
import org.lemsml.jlems.sim.ContentError;
import org.lemsml.jlems.type.Lems;


public class FunctionNode extends UnaryNode implements DoubleParseTreeNode {

	String fname = null;

	GroupNode args;

	DoubleParseTreeNode argEvaluable;

	public FunctionNode(String sf) {
		super();
		fname = sf;
	}



	@Override
	public String toString() {
		return "Function: " + fname + "(" + right + ")";
	}
	
	
	@Override
	public String toExpression() throws ContentError {
		checkArg();
		return fname + "(" + argEvaluable.toExpression() + ")";
	}
	
	

	@Override
	public void claim() throws ParseError {
		super.claim();
	}

	@Override
	public void replaceChild(Node nold, Node nnew) throws ParseError {
		if (right == nold) {
			right = nnew;
		} else {
			throw new ParseError("can't replace - not present " + nold);
		}
	}

 
 

	public double call(double arg) {
		double ret = Double.NaN;
		// "cos", "tan", "exp", "sum", "product", "ln"};
		if (fname.equals("sin")) {
			ret = Math.sin(arg);
			
		} else if (fname.equals("cos")) {
			ret = Math.cos(arg);
		
		} else if (fname.equals("tan")) {
			ret = Math.tan(arg);
		
		} else if (fname.equals("ln") || fname.equals("log")) {
			ret = Math.log(arg);
		
		} else if (fname.equals("exp")) {
			ret = Math.exp(arg);
			
		} else if (fname.equals("abs")) {
			ret = Math.abs(arg);

		} else if (fname.equals("sqrt")) {
			ret = Math.sqrt(arg);

		} else if (fname.equals("random")) {
			ret = arg * Lems.getRandomGenerator().nextDouble();

		} else {
			E.error("unrecognized function: " + fname);
		}
		return ret;
	}
 
	private void checkArg() throws ContentError {
		if (argEvaluable == null) {
			if (right instanceof DoubleParseTreeNode) {
				argEvaluable = (DoubleParseTreeNode) right;		
			} else {
				throw new ContentError("Wrong node type in function " + right);
			}
		}
	}
	
	
	public DVal makeFixed(HashMap<String, Double> fixedHM) throws ContentError {
		checkArg();
		return new DFunc(fname, argEvaluable.makeFixed(fixedHM));
	}

	public Dimensional getDimensionality(HashMap<String, Dimensional> dimHM) throws ContentError {
		checkArg();
		Dimensional ret = null;
		Dimensional diml = argEvaluable.getDimensionality(dimHM);
		if (diml.isDimensionless()) {
			ret = new ExprDimensional();
		} else {
			throw new ContentError("Function argument for " + fname + " is not dimensionless: " + diml);
		}

		return ret;
	}

	 
	public Dimensional evaluateDimensional(HashMap<String, Dimensional> dhm) throws ContentError {
		throw new ContentError("Can't apply function operations to dimensions");
	}
	
	 
	public void substituteVariables(HashMap<String, String> varHM) throws ContentError {
		 checkArg();
		 argEvaluable.substituteVariables(varHM);
	}
	
}
