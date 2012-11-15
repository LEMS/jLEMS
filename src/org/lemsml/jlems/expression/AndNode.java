package org.lemsml.jlems.expression;

import java.util.HashMap;

import org.lemsml.jlems.eval.And;
import org.lemsml.jlems.eval.BVal;
import org.lemsml.jlems.sim.ContentError;

public class AndNode extends BooleanOperatorNode {

        public static final String SYMBOL = ".and.";

	public AndNode() {
		super(SYMBOL);
	}

    

	
	public AndNode copy() {
		return new AndNode();
	}
	
	public int getPrecedence() {
		return 20;// TODO: check..
	}
	 
	public boolean bool(boolean x, boolean y) {
		return x && y;
	}

	
	public BVal makeEvaluable(HashMap<String, Double> fixedHM) throws ContentError {
		checkLeftRight();
		return new And(leftEvaluable.makeEvaluable(fixedHM), rightEvaluable.makeEvaluable(fixedHM));
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

	public void checkDimensions(HashMap<String, Dimensional> dimHM) throws ContentError {
		getDimensionality(dimHM);

	}
}
