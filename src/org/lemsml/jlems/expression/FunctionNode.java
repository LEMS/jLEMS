package org.lemsml.jlems.expression;

import java.util.HashMap;

import org.lemsml.jlems.eval.DFunc;
import org.lemsml.jlems.eval.DVal;
import org.lemsml.jlems.type.Lems;
import org.lemsml.jlems.util.ContentError;
import org.lemsml.jlems.util.E;


public class FunctionNode extends UnaryNode implements DoubleEvaluable {

	String fname = null;

	GroupNode args;

	DoubleEvaluable argEvaluable;

	public FunctionNode(String sf) {
		super();
		fname = sf;
	}



	@Override
	public String toString() {
		return fname + "(" + right + ")";
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

	public double evalD(HashMap<String, Double> valHS) throws ParseError {
		return call(argEvaluable.evalD(valHS));
	}

	// TODO multiple args
	public void evaluablize() throws ParseError {
		if (right instanceof DoubleEvaluable) {
			argEvaluable = (DoubleEvaluable) right;
			argEvaluable.evaluablize();
		} else {
			throw new ParseError("non evaluable function arg " + right);
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

	public void setValues(HashMap<String, Valued> valHM) throws ContentError {
		if (argEvaluable != null) {
			argEvaluable.setValues(valHM);
		}
	}

	public DVal makeFixed(HashMap<String, Double> fixedHM) throws ContentError {
		return new DFunc(fname, argEvaluable.makeFixed(fixedHM));
	}

	public Dimensional getDimensionality(HashMap<String, Dimensional> dimHM) throws ContentError {
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
	
	
}
