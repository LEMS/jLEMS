package org.lemsml.expression;

import java.util.HashMap;
 
public abstract class UnaryNode extends Node {

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
			throw new ParseError("No right node for operator? right: "+right+", prev: "+prev+", nxt: "+nxt+", par: "+par);
		}
	}

    @Override
	public void replaceSymbols(HashMap<String, String> map) {
	 
		if (right != null) {
			right.replaceSymbols(map);
		}
	 
	}
}
