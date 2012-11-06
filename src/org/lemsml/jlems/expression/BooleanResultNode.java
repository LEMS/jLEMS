package org.lemsml.jlems.expression;


public abstract class BooleanResultNode extends BooleanOperatorNode implements BooleanParseTreeNode {


	public BooleanResultNode(String s) {
		super(s);
	}

/*
	public boolean evalD(HashMap<String, Double> valHS) throws ParseError {
 		 boolean x = (leftEvaluable != null ? leftEvaluable.evalB(valHS) : false);
		 boolean y = (rightEvaluable != null ? rightEvaluable.evalB(valHS) : false);
		 boolean ret = bool(x, y);
		 return ret;
	}
 */
	
	public abstract boolean bool(boolean x, boolean y);

	
	
	
}
