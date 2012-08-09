package org.lemsml.expression;

import java.util.HashMap;

import org.lemsml.eval.BComp;
import org.lemsml.eval.GTComp;

public class GreaterThanNode extends ComparisonNode {


	
	public GreaterThanNode() {
		super("greater_than");
	}

	@Override
	protected String getMathMLElementName() {
		return "gt";
	}
	
	public GreaterThanNode copy() {
		return new GreaterThanNode();
	}
	
	public int getPrecedence() {
		return 10;
	}
	 
	public boolean compare(double x, double y) {
		return (x > y);
	}

	public BComp makeFixed(HashMap<String, Double> fixedHM) {
		return new GTComp(leftEvaluable.makeFixed(fixedHM), rightEvaluable.makeFixed(fixedHM));
	}

	@Override
	public boolean compareInts(long ix, long iy) {
		return (ix > iy);
	}
	
	 
	
}
