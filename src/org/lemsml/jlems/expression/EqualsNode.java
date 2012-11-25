package org.lemsml.jlems.expression;

import java.util.HashMap;

import org.lemsml.jlems.eval.AbstractBComp;
import org.lemsml.jlems.eval.EQComp;
import org.lemsml.jlems.run.RuntimeError;
import org.lemsml.jlems.sim.ContentError;

public class EqualsNode extends AbstractComparisonNode {


	
	public EqualsNode() {
		super("equal_to");
	}

   
	
	public EqualsNode copy() {
		return new EqualsNode();
	}
	
	public int getPrecedence() {
		return 20;
	}
	 
	public boolean compare(double x, double y) throws RuntimeError {
		throw new RuntimeError("Called equals comparison of doubles");	
 	}
 

	public AbstractBComp makeEvaluable(HashMap<String, Double> fixedHM) throws ContentError {
		checkLeftRight();
		return new EQComp(leftEvaluable.makeEvaluable(fixedHM), rightEvaluable.makeEvaluable(fixedHM));
	}

	@Override
	public boolean compareInts(long ix, long iy) {
		return (ix == iy);
	}
}
