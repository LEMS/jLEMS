package org.lemsml.jlems.core.expression;

import java.util.HashMap;

import org.lemsml.jlems.core.sim.ContentError;

public abstract class AbstractBooleanOperatorNode extends AbstractOperatorNode implements BooleanParseTreeNode {

	BooleanParseTreeNode leftEvaluable;
	BooleanParseTreeNode rightEvaluable;
	
	public AbstractBooleanOperatorNode(String s) {
		super(s);
	}
 
	public abstract boolean bool(boolean x, boolean y);
 
 
	
	protected void checkLeftRight() throws ContentError {
		if (leftEvaluable == null) {
			if (left instanceof BooleanParseTreeNode) {
				leftEvaluable = (BooleanParseTreeNode)left;
			} else {
				throw new ContentError("Wrong node type in float operator: " + left);
			}
		}
		if (rightEvaluable == null) {
			if (right instanceof BooleanParseTreeNode) {
				rightEvaluable = (BooleanParseTreeNode)right;	
			} else {
				throw new ContentError("Wrong node type in float operator: " + right);
			}
		}
	}
	

	public Dimensional getDimensionality(HashMap<String, Dimensional> dimHM) throws ContentError {
		checkLeftRight();
		Dimensional dl = leftEvaluable.getDimensionality(dimHM);
		Dimensional dr = rightEvaluable.getDimensionality(dimHM);
		Dimensional ret = null;
		if (dl != null && dr != null && dl.isDimensionless() && dr.isDimensionless()) {
			ret = dl;
		} else {
			throw new ContentError("Null dimension in operator: " + dl + " " + dr + " operator: " + symbol);
		}
		return ret;
	}

	 
	 
 

	public Dimensional evaluateDimensional(HashMap<String, Dimensional> dhm) throws ContentError {
		throw new ContentError("Can't apply boolean operations to dimensions");
	}
	
	
	@Override
	public void substituteVariables(HashMap<String, String> varHM) throws ContentError {
		checkLeftRight();
		leftEvaluable.substituteVariables(varHM);
		rightEvaluable.substituteVariables(varHM);
	}
 
	
	public String toExpression() throws ContentError {
		checkLeftRight();
		return "(" + leftEvaluable.toExpression() + " " + symbol + " " + rightEvaluable.toExpression() + ")";
	}
	
}
 