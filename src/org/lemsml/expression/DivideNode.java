package org.lemsml.expression;

import java.util.HashMap;

import org.lemsml.eval.DVal;
import org.lemsml.eval.Divide;

public class DivideNode extends FloatResultNode {

	
	
	public DivideNode() {
		super("/");
	}

        @Override
        protected String getMathMLElementName() {
                return "divide";
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
	
	public DVal makeFixed(HashMap<String, Double> fixedHM) {
		return new Divide(leftEvaluable.makeFixed(fixedHM), rightEvaluable.makeFixed(fixedHM));
	}

 
	public Dimensional dimop(Dimensional dl, Dimensional dr) {
		return dl.getDivideBy(dr);
	}


}
