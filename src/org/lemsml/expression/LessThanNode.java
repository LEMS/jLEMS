package org.lemsml.expression;

import java.util.HashMap;

import org.lemsml.eval.BComp;
import org.lemsml.eval.LTComp;
import org.lemsml.util.ContentError;

public class LessThanNode extends ComparisonNode {


	
	public LessThanNode() {
		super("less_than");
	}

  
	
	public LessThanNode copy() {
		return new LessThanNode();
	}
	
	public int getPrecedence() {
		return 10;
	}
	 
	public boolean compare(double x, double y) {
		return (x < y);
	}

	public BComp makeFixed(HashMap<String, Double> fixedHM) throws ContentError {
		return new LTComp(leftEvaluable.makeFixed(fixedHM), rightEvaluable.makeFixed(fixedHM));
	}
	
	@Override
	public boolean compareInts(long ix, long iy) {
		return (ix < iy);
	}
	
}
