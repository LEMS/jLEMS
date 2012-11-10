package org.lemsml.jlems.expression;

import java.util.HashMap;

import org.lemsml.jlems.eval.DVal;
import org.lemsml.jlems.eval.Minus;
import org.lemsml.jlems.sim.ContentError;
 

public class UnaryMinusNode extends FloatResultNode {

	public UnaryMinusNode() {
		super("-");
		left = new ConstantNode("0");
	}
 
	public UnaryMinusNode copy() {
		return new UnaryMinusNode();
	}
	
	public int getPrecedence() {
		 return 0;
 	}
	
	public double op(double x, double y) {
		return - y;
	}
	
	@Override
	public void claim() throws ParseError {
		claimRight();
	}

	
	public DVal makeEvaluable(HashMap<String, Double> fixedHM) throws ContentError {
		checkLeftRight();
		return new Minus(leftEvaluable.makeEvaluable(fixedHM), rightEvaluable.makeEvaluable(fixedHM));
	}

	 
	public Dimensional dimop(Dimensional dl, Dimensional dr) throws ContentError {
		Dimensional ret = null;
		if (dl == null) {
			ret = dr;
		} else if (dl.matches(dr)) {
			ret = dl;
		} else if (dl.isAny()) {
			ret = dr;
		} else if (dr.isAny()) {
			ret = dl;
		} else {
			throw new ContentError("Dimension mismatch - can't subtract " + dr + " (rhs) from " + dl+" (lhs)");
		}
		return ret;
 	}
	
	

	public Dimensional evaluateDimensional(HashMap<String, Dimensional> dhm) throws ContentError {
		throw new ContentError("Can't apply function operations to dimensions");
	}
	
}
