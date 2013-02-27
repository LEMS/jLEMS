package org.lemsml.jlems.core.expression;

import java.util.HashMap;

import org.lemsml.jlems.core.eval.AbstractDVal;
import org.lemsml.jlems.core.eval.Divide;
import org.lemsml.jlems.core.sim.ContentError;

public class DivideNode extends AbstractFloatResultNode {

	
	
	public DivideNode() {
		super("/");
	}

   
	
	public DivideNode copy() {
		return new DivideNode();
	}
	
	public int getPrecedence() {
		return 2;
	}

 
	public double op(double x, double y) {
		return (Double.isNaN(x) ? 1 : x) / y;
	}
	
	public AbstractDVal makeEvaluable(HashMap<String, Double> fixedHM) throws ContentError {
		checkLeftRight();
		return new Divide(leftEvaluable.makeEvaluable(fixedHM), rightEvaluable.makeEvaluable(fixedHM));
	}

 
	public Dimensional dimop(Dimensional dl, Dimensional dr) {
		return dl.getDivideBy(dr);
	}



	@Override
	public void doVisit(ExpressionVisitor ev) throws ContentError {
			checkLeftRight();
			ev.visitDivideNode(leftEvaluable, rightEvaluable);
		}
		
 
	
}
