package org.lemsml.jlems.core.expression;

import java.util.HashMap;

import org.lemsml.jlems.core.eval.AbstractBComp;
import org.lemsml.jlems.core.eval.GTComp;
import org.lemsml.jlems.core.sim.ContentError;

public class GreaterThanNode extends AbstractComparisonNode {


	
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

	public AbstractBComp makeEvaluable(HashMap<String, Double> fixedHM) throws ContentError {
		checkLeftRight();
		return new GTComp(leftEvaluable.makeEvaluable(fixedHM), rightEvaluable.makeEvaluable(fixedHM));
	}

	@Override
	public boolean compareInts(long ix, long iy) {
		return (ix > iy);
	}
	


	@Override
	public void doVisit(ExpressionVisitor ev) throws ContentError {
			checkLeftRight();
			ev.visitGreaterThanNode(leftEvaluable, rightEvaluable);
		}
		
 
	
}
