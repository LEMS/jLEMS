package org.lemsml.expression;

import java.util.HashMap;

import org.lemsml.eval.DVal;
import org.lemsml.eval.Times;

public class TimesNode extends FloatResultNode {

	
	public TimesNode() {
		super("*");
	}

        @Override
        protected String getMathMLElementName() {
                return "times";
        }

	
	public TimesNode copy() {
		return new TimesNode();
	}
	
	public int getPrecedence() {
		return 3;
	}

	public double op(double x, double y) {
		return x * y;
	}
	
	public DVal makeFixed(HashMap<String, Double> fixedHM) {
		return new Times(leftEvaluable.makeFixed(fixedHM), rightEvaluable.makeFixed(fixedHM));
	}
	
	public Dimensional dimop(Dimensional dl, Dimensional dr) {
		Dimensional ret = dl.getTimes(dr);
		return ret;
	}
	
}
