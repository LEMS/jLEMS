package org.lemsml.expression;

import java.util.HashMap;

import org.lemsml.eval.BComp;
import org.lemsml.eval.GEQComp;
import org.lemsml.util.ContentError;

public class GreaterThanOrEqualsNode extends ComparisonNode {


	
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

	public BComp makeFixed(HashMap<String, Double> fixedHM) throws ContentError {
		return new GEQComp(leftEvaluable.makeFixed(fixedHM), rightEvaluable.makeFixed(fixedHM));
	}
	
	@Override
	public boolean compareInts(long ix, long iy) {
		return (ix >= iy);
	}
	
	
}
