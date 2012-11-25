package org.lemsml.jlems.expression;

import java.util.HashMap;

import org.lemsml.jlems.eval.AbstractBComp;
import org.lemsml.jlems.eval.NEQComp;
import org.lemsml.jlems.run.RuntimeError;
import org.lemsml.jlems.sim.ContentError;

public class NotEqualsNode extends AbstractComparisonNode {

	public NotEqualsNode() {
		super("!=");
	}

  
	
	public NotEqualsNode copy() {
		return new NotEqualsNode();
	}
	
	public int getPrecedence() {
		return 10;
	}
	 
	public boolean compare(double x, double y) throws RuntimeError {
		throw new RuntimeError("Called not equals comparison of doubles");	
	 }
	 
	 
	public AbstractBComp makeEvaluable(HashMap<String, Double> fixedHM) throws ContentError {
		checkLeftRight();
		return new NEQComp(leftEvaluable.makeEvaluable(fixedHM), rightEvaluable.makeEvaluable(fixedHM));
	}
	
	@Override
	public boolean compareInts(long ix, long iy) {
		return (ix != iy);
	}
}
