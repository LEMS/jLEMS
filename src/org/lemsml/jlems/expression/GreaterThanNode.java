package org.lemsml.jlems.expression;

import java.util.HashMap;

import org.lemsml.jlems.eval.BComp;
import org.lemsml.jlems.eval.GTComp;
import org.lemsml.jlems.util.ContentError;

public class GreaterThanNode extends ComparisonNode {


	
	public GreaterThanNode() {
		super("greater_than");
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

	public BComp makeFixed(HashMap<String, Double> fixedHM) throws ContentError {
		return new GTComp(leftEvaluable.makeFixed(fixedHM), rightEvaluable.makeFixed(fixedHM));
	}

	@Override
	public boolean compareInts(long ix, long iy) {
		return (ix > iy);
	}
	
	 
	
}
