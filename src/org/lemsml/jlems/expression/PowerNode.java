package org.lemsml.jlems.expression;

import java.util.HashMap;

import org.lemsml.jlems.eval.DVal;
import org.lemsml.jlems.eval.Power;
import org.lemsml.jlems.logging.E;
import org.lemsml.jlems.sim.ContentError;

public class PowerNode extends FloatResultNode {

	public PowerNode() {
		super("^");
	}
	
	public PowerNode copy() {
		return new PowerNode();
	}
	
	public int getPrecedence() {
		return 1;
	}

    
	
	
	public double op(double x, double y) {
		return Math.pow(x, y);
	}
	
	public DVal makeFixed(HashMap<String, Double> fixedHM) throws ContentError {
		checkLeftRight();
		return new Power(leftEvaluable.makeFixed(fixedHM), rightEvaluable.makeFixed(fixedHM));
	}

	 
	public Dimensional dimop(Dimensional dl, Dimensional dr) throws ContentError {
		Dimensional ret = null;
		if (dl.isDimensionless()) {
			ret = new ExprDimensional();
			
		} else if (dr.isDimensionless()) {
			double dpow = dr.getDoubleValue();
			if (Double.isNaN(dpow)) {
				E.repeatableWarning("Can't check dimensionality in power operation (power not known to be constant) " + dl + " " + dr);
				E.info("class of power node is " + dr.getClass() + " left=" + leftEvaluable + " right=" + rightEvaluable);
			} else {
				ret = dl.power(dpow);				
			}
			
		} else {
			throw new ContentError("powers must be dimensionless");
		}
		return ret;
	}
	
	

	public Dimensional evaluateDimensional(HashMap<String, Dimensional> dhm) throws ContentError {
		throw new ContentError("Can't (yet) apply power operations to dimensions");
	}
	
}
