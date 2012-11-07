package org.lemsml.jlems.expression;

import java.util.HashMap;

import org.lemsml.jlems.eval.BComp;
import org.lemsml.jlems.eval.EQComp;
import org.lemsml.jlems.logging.E;
import org.lemsml.jlems.sim.ContentError;

public class EqualsNode extends ComparisonNode {


	
	public EqualsNode() {
		super("equal_to");
	}

   
	
	public EqualsNode copy() {
		return new EqualsNode();
	}
	
	public int getPrecedence() {
		return 20;
	}
	 
	public boolean compare(double x, double y) {
		E.warning("Comparing doubles for exact equality - probably not what you want to do");
		return (x == y);
	}
 

	public BComp makeFixed(HashMap<String, Double> fixedHM) throws ContentError {
		checkLeftRight();
		return new EQComp(leftEvaluable.makeFixed(fixedHM), rightEvaluable.makeFixed(fixedHM));
	}

	@Override
	public boolean compareInts(long ix, long iy) {
		return (ix == iy);
	}
}
