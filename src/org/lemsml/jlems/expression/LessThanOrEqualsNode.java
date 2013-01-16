package org.lemsml.jlems.expression;

import java.util.HashMap;

import org.lemsml.jlems.eval.AbstractBComp;
import org.lemsml.jlems.eval.LEQComp;
import org.lemsml.jlems.sim.ContentError;

public class LessThanOrEqualsNode extends AbstractComparisonNode {


	
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

	public AbstractBComp makeEvaluable(HashMap<String, Double> fixedHM) throws ContentError {
		checkLeftRight();
		return new LEQComp(leftEvaluable.makeEvaluable(fixedHM), rightEvaluable.makeEvaluable(fixedHM));
	}

	@Override
	public boolean compareInts(long ix, long iy) {
		return (ix <= iy);
	}





	@Override
	public void doVisit(ExpressionVisitor ev) throws ContentError {
			checkLeftRight();
			ev.visitLessThanOrEqualsNode(leftEvaluable, rightEvaluable);
		}
		
 
	
}
