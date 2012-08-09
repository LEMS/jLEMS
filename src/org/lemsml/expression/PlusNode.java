package org.lemsml.expression;

import java.util.HashMap;

import org.lemsml.eval.DVal;
import org.lemsml.eval.Plus;
import org.lemsml.util.ContentError;

public class PlusNode extends FloatResultNode {


	public PlusNode() {
		super("+");
	}

        @Override
        protected String getMathMLElementName() {
                return "plus";
        }


	
	public PlusNode copy() {
		return new PlusNode();
	}
	
	public int getPrecedence() {
		return 5;
	}
	 
	public double op(double x, double y) {
		return (Double.isNaN(x) ? 0 : x) + y;
	}

	
	public DVal makeFixed(HashMap<String, Double> fixedHM) {
		return new Plus(leftEvaluable.makeFixed(fixedHM), rightEvaluable.makeFixed(fixedHM));
	}

	 
	public Dimensional dimop(Dimensional dl, Dimensional dr) throws ContentError {
		Dimensional ret = null;
		if (dl.matches(dr)) {
			ret = dl;
		} else {
			throw(new ContentError("Dimensions do not match in plus: " + dl + " " + dr));
		}
		return ret;
	}
	

	public Dimensional evaluateDimensional(HashMap<String, Dimensional> dhm) throws ContentError {
		throw new ContentError("Can't apply addition operations to dimensions");
	}
	
	
}
