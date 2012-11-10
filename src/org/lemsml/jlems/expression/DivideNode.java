package org.lemsml.jlems.expression;

import java.util.HashMap;

import org.lemsml.jlems.eval.DVal;
import org.lemsml.jlems.eval.Divide;
import org.lemsml.jlems.sim.ContentError;

public class DivideNode extends FloatResultNode {

	
	
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
	
	public DVal makeEvaluable(HashMap<String, Double> fixedHM) throws ContentError {
		checkLeftRight();
		return new Divide(leftEvaluable.makeEvaluable(fixedHM), rightEvaluable.makeEvaluable(fixedHM));
	}

 
	public Dimensional dimop(Dimensional dl, Dimensional dr) {
		return dl.getDivideBy(dr);
	}


}
