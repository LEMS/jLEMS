package org.lemsml.jlems.expression;

import java.util.HashMap;

import org.lemsml.jlems.eval.And;
import org.lemsml.jlems.eval.AbstractBVal;
import org.lemsml.jlems.sim.ContentError;

public class AndNode extends AbstractBooleanOperatorNode {

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

	
	public AbstractBVal makeEvaluable(HashMap<String, Double> fixedHM) throws ContentError {
		checkLeftRight();
		return new And(leftEvaluable.makeEvaluable(fixedHM), rightEvaluable.makeEvaluable(fixedHM));
	}

	
	  
	public void checkDimensions(HashMap<String, Dimensional> dimHM) throws ContentError {
		getDimensionality(dimHM);

	}
}
