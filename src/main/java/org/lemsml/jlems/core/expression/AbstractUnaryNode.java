package org.lemsml.jlems.core.expression;

import java.util.HashMap;
 
public abstract class AbstractUnaryNode extends Node {

	public Node right;

	
	
	public void claim() throws ParseError {
		claimRight();
	}
	
	public void claimRight() throws ParseError {
		if (next() != null) {
			right = next();
			right.deparent();
			right.detachPrevious();
			right.setParent(this);
			if (right.next() != null) {
				right.next().replacePrevious(this);
			}
		 
		} else {
			throw new ParseError("No right node for unary operator? " + " previous=" + prev + " parent=" + par);
		}
	}

    @Override
	public void replaceSymbols(HashMap<String, String> map) {
	 
		if (right != null) {
			right.replaceSymbols(map);
		}
	 
	}
}
