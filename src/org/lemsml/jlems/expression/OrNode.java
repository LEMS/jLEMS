package org.lemsml.jlems.expression;

import java.util.HashMap;

import org.lemsml.jlems.eval.AbstractBVal;
import org.lemsml.jlems.eval.Or;
import org.lemsml.jlems.sim.ContentError;

public class OrNode extends AbstractBooleanOperatorNode {

        public static final String SYMBOL = ".or.";

	public OrNode() {
		super(SYMBOL);
	}

    

	
	public OrNode copy() {
		return new OrNode();
	}
	
	public int getPrecedence() {
		return 20;// TODO: check..
	}
	 
	public boolean bool(boolean x, boolean y) {
		return x || y;
	}

	
	public AbstractBVal makeEvaluable(HashMap<String, Double> fixedHM) throws ContentError {
		checkLeftRight();
		return new Or(leftEvaluable.makeEvaluable(fixedHM), rightEvaluable.makeEvaluable(fixedHM));
	}

	  

	public void checkDimensions(HashMap<String, Dimensional> dimHM) throws ContentError {
		getDimensionality(dimHM);
	}




	@Override
	public void doVisit(ExpressionVisitor ev) {
		ev.visitOrNode(this);
	}




}
