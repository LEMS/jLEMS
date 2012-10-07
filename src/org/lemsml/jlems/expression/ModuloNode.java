package org.lemsml.jlems.expression;

import java.util.HashMap;

import org.lemsml.jlems.eval.DVal;
import org.lemsml.jlems.eval.Mod;
import org.lemsml.jlems.sim.ContentError;

public class ModuloNode extends FloatResultNode {


	public ModuloNode() {
		super("mod");
	}


	
	public ModuloNode copy() {
		return new ModuloNode();
	}
	
	public int getPrecedence() {
		return 5;
	}
	 
	public double op(double x, double y) {
		// TODO - v. lazy - need int op nodes
		return (Math.round(x) % Math.round(y));
	}

	
	public DVal makeFixed(HashMap<String, Double> fixedHM) throws ContentError {
		return new Mod(leftEvaluable.makeFixed(fixedHM), rightEvaluable.makeFixed(fixedHM));
	}

	 
	public Dimensional dimop(Dimensional dl, Dimensional dr) throws ContentError {
		Dimensional ret = null;
		if (dl.matches(dr)) {
			ret = dl;
		} else {
			throw(new ContentError("Dimensions do not match in plus: " + dl + " " + dr));
		}
		return ret;
	}

	public Dimensional evaluateDimensional(HashMap<String, Dimensional> dhm) throws ContentError {
		throw new ContentError("Can't apply modulo operations to dimensions");
	}
	
	
}
