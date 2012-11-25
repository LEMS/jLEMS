package org.lemsml.jlems.expression;

import java.util.HashMap;

import org.lemsml.jlems.eval.AbstractBComp;
import org.lemsml.jlems.eval.GEQComp;
import org.lemsml.jlems.sim.ContentError;

public class GreaterThanOrEqualsNode extends AbstractComparisonNode {


	
	public GreaterThanOrEqualsNode() {
		super("greater_than_or_equal_to");
	}

 
	public GreaterThanOrEqualsNode copy() {
		return new GreaterThanOrEqualsNode();
	}
	
	public int getPrecedence() {
		return 10;
	}
	 
	public boolean compare(double x, double y) {
		return (x >= y);
	}

	public AbstractBComp makeEvaluable(HashMap<String, Double> fixedHM) throws ContentError {
		checkLeftRight();
		return new GEQComp(leftEvaluable.makeEvaluable(fixedHM), rightEvaluable.makeEvaluable(fixedHM));
	}
	
	@Override
	public boolean compareInts(long ix, long iy) {
		return (ix >= iy);
	}
	
	
}
