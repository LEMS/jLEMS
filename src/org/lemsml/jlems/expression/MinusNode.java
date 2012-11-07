package org.lemsml.jlems.expression;

import java.util.HashMap;

import org.lemsml.jlems.eval.DVal;
import org.lemsml.jlems.eval.Minus;
import org.lemsml.jlems.sim.ContentError;
 

public class MinusNode extends FloatResultNode {

	public MinusNode() {
		super("-");
	}

	public MinusNode copy() {
		return new MinusNode();
	}
	
	public int getPrecedence() {
		 return 4;
 	}
	
	public double op(double x, double y) {
		return (Double.isNaN(x) ? 0 : x) - y;
	}
	
	public DVal makeFixed(HashMap<String, Double> fixedHM) throws ContentError {
		checkLeftRight();
		return new Minus(leftEvaluable.makeFixed(fixedHM), rightEvaluable.makeFixed(fixedHM));
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
