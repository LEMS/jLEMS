package org.lemsml.expression;

import java.util.HashMap;

import org.lemsml.eval.BComp;
import org.lemsml.eval.LEQComp;
import org.lemsml.util.ContentError;

public class LessThanOrEqualsNode extends ComparisonNode {


	
	public LessThanOrEqualsNode() {
		super("less_than_or_equal_to");
	}

    
	public LessThanOrEqualsNode copy() {
		return new LessThanOrEqualsNode();
	}
	
	public int getPrecedence() {
		return 10;
	}
	 
	public boolean compare(double x, double y) {
		return (x <= y);
	}

	public BComp makeFixed(HashMap<String, Double> fixedHM) throws ContentError {
		return new LEQComp(leftEvaluable.makeFixed(fixedHM), rightEvaluable.makeFixed(fixedHM));
	}

	@Override
	public boolean compareInts(long ix, long iy) {
		return (ix <= iy);
	}
}
