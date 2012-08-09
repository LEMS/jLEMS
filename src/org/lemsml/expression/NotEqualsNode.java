package org.lemsml.expression;

import java.util.HashMap;

import org.lemsml.eval.BComp;
import org.lemsml.eval.NEQComp;

public class NotEqualsNode extends ComparisonNode {

	public NotEqualsNode() {
		super("!=");
	}

    @Override
    protected String getMathMLElementName() {
            return "neq";
    }
	
	public NotEqualsNode copy() {
		return new NotEqualsNode();
	}
	
	public int getPrecedence() {
		return 10;
	}
	 
	public boolean compare(double x, double y) {
		return (x != y);
	}

	public BComp makeFixed(HashMap<String, Double> fixedHM) {
		return new NEQComp(leftEvaluable.makeFixed(fixedHM), rightEvaluable.makeFixed(fixedHM));
	}
	
	@Override
	public boolean compareInts(long ix, long iy) {
		return (ix != iy);
	}
}
