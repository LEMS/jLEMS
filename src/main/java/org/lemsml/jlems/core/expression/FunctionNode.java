package org.lemsml.jlems.core.expression;

import java.util.HashMap;

import org.lemsml.jlems.core.eval.AbstractDVal;
import org.lemsml.jlems.core.eval.DFunc;
import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.core.sim.ContentError;
import org.lemsml.jlems.core.type.Lems;


public class FunctionNode extends AbstractUnaryNode implements DoubleParseTreeNode {

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
		return evaluate(arg, fname);
	}
 

	public static double evaluate(double arg, String fname) {
		double ret = Double.NaN;
		
		if (fname.equals(Parser.SIN)) {
			ret = Math.sin(arg);
			
		} else if (fname.equals(Parser.COS)) {
			ret = Math.cos(arg);
		
		} else if (fname.equals(Parser.TAN)) {
			ret = Math.tan(arg);

		} else if (fname.equals(Parser.SINH)) {
			ret = Math.sinh(arg);

		} else if (fname.equals(Parser.COSH)) {
			ret = Math.cosh(arg);

		} else if (fname.equals(Parser.TANH)) {
			ret = Math.tanh(arg);
		
		} else if (fname.equals(Parser.LN) || fname.equals(Parser.LOG)) {
			if (arg<=0)
			{
				E.error("Log/ln of zero/negative number!!: " + arg);
				return Double.NaN;
			}
			ret = Math.log(arg);
		
		} else if (fname.equals(Parser.EXP)) {
			ret = Math.exp(arg);
			
		} else if (fname.equals(Parser.ABS)) {   
			ret = Math.abs(arg);

		} else if (fname.equals(Parser.SQRT)) {
			if (arg<0)
			{
				E.error("Square root of negative number!!: " + arg);
				return Double.NaN;
			}
			ret = Math.sqrt(arg);
			
		} else if (fname.equals(Parser.CEIL)) {
			ret = Math.ceil(arg);

		} else if (fname.equals(Parser.RANDOM)) {
			ret = arg * Lems.getRandomGenerator().nextDouble();

		} else if (fname.equals(Parser.FACTORIAL)) {
			if (arg!=(int)arg)
			{
				E.error("Factorial of non integer!!: " + arg);
				return Double.NaN;
			}
			if (arg<0)
			{
				E.error("Factorial of negative number: " + arg);
				return Double.NaN;
			}
			int intVal = (int)arg;
			int fact = 1; 
	        for (int i = 1; i <= intVal; i++) {
	            fact *= i;
	        }
			ret = fact;

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
	
	
	public AbstractDVal makeEvaluable(HashMap<String, Double> fixedHM) throws ContentError {
		checkArg();
		return new DFunc(fname, argEvaluable.makeEvaluable(fixedHM));
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
	 


	@Override
	public void doVisit(ExpressionVisitor ev) throws ContentError {
		checkArg();
		ev.visitFunctionNode(fname, argEvaluable);	
	}
	
}
