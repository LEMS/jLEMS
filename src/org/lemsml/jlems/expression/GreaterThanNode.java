package org.lemsml.jlems.expression;

import java.util.HashMap;

import org.lemsml.jlems.eval.BComp;
import org.lemsml.jlems.eval.GTComp;
import org.lemsml.jlems.sim.ContentError;

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

	public BComp makeEvaluable(HashMap<String, Double> fixedHM) throws ContentError {
		checkLeftRight();
		return new GTComp(leftEvaluable.makeEvaluable(fixedHM), rightEvaluable.makeEvaluable(fixedHM));
	}

	@Override
	public boolean compareInts(long ix, long iy) {
		return (ix > iy);
	}
	
	 
	
}
