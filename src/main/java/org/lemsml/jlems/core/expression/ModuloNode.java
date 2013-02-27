package org.lemsml.jlems.core.expression;

import java.util.HashMap;

import org.lemsml.jlems.core.eval.AbstractDVal;
import org.lemsml.jlems.core.eval.Mod;
import org.lemsml.jlems.core.sim.ContentError;

public class ModuloNode extends AbstractFloatResultNode {


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

	
	public AbstractDVal makeEvaluable(HashMap<String, Double> fixedHM) throws ContentError {
		checkLeftRight();
		return new Mod(leftEvaluable.makeEvaluable(fixedHM), rightEvaluable.makeEvaluable(fixedHM));
	}

	 
	public Dimensional dimop(Dimensional dl, Dimensional dr) throws ContentError {
		Dimensional ret = null;
		if (dl.matches(dr)) {
			ret = dl;
		} else {
			throw new ContentError("Dimensions do not match in plus: " + dl + " " + dr);
		}
		return ret;
	}

	public Dimensional evaluateDimensional(HashMap<String, Dimensional> dhm) throws ContentError {
		throw new ContentError("Can't apply modulo operations to dimensions");
	}
	


	@Override
	public void doVisit(ExpressionVisitor ev) throws ContentError {
			checkLeftRight();
			ev.visitModuloNode(leftEvaluable, rightEvaluable);
		}
		
 
	
}
