package org.lemsml.jlems.expression;

import java.util.HashMap;

import org.lemsml.jlems.eval.BVal;
import org.lemsml.jlems.eval.Or;
import org.lemsml.jlems.sim.ContentError;

public class OrNode extends BooleanOperatorNode {

        public static final String SYMBOL = ".or.";

	public OrNode() {
		super(SYMBOL);
	}

    

	
	public OrNode copy() {
		return new OrNode();
	}
	
	public int getPrecedence() {
		return 20;// TODO: check..
	}
	 
	public boolean bool(boolean x, boolean y) {
		return x || y;
	}

	
	public BVal makeFixed(HashMap<String, Double> fixedHM) throws ContentError {
		checkLeftRight();
		return new Or(leftEvaluable.makeFixed(fixedHM), rightEvaluable.makeFixed(fixedHM));
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
