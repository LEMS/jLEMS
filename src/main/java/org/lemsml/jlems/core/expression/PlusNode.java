package org.lemsml.jlems.core.expression;

import java.util.HashMap;

import org.lemsml.jlems.core.eval.AbstractDVal;
import org.lemsml.jlems.core.eval.Plus;
import org.lemsml.jlems.core.sim.ContentError;

public class PlusNode extends AbstractFloatResultNode {

    public static final String SYMBOL = "+";

	public PlusNode() {
		super(SYMBOL);
	}

	
    @Override
	public PlusNode copy() {
		return new PlusNode();
	}
	
    @Override
	public int getPrecedence() {
		return 5;
	}
	 
    @Override
	public double op(double x, double y) {
		return (Double.isNaN(x) ? 0 : x) + y;
	}

	
    @Override
	public AbstractDVal makeEvaluable(HashMap<String, Double> fixedHM) throws ContentError {
		checkLeftRight();
		
		return new Plus(leftEvaluable.makeEvaluable(fixedHM), rightEvaluable.makeEvaluable(fixedHM));
	}

    @Override
	public Dimensional dimop(Dimensional dl, Dimensional dr) throws ContentError {
		Dimensional ret = null;
		if (dl.matches(dr)) {
			ret = dl;
		} else {
			throw new ContentError("Dimensions do not match in plus: " + dl + " " + dr);
		}
		return ret;
	}
	

    @Override
	public Dimensional evaluateDimensional(HashMap<String, Dimensional> dhm) throws ContentError {
		throw new ContentError("Can't apply addition operations to dimensions");
	}
	 


	@Override
	public void doVisit(ExpressionVisitor ev) throws ContentError {
			checkLeftRight();
			ev.visitPlusNode(leftEvaluable, rightEvaluable);
		}
		
 
	
}
