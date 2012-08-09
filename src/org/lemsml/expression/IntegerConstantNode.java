package org.lemsml.expression;
 

public class IntegerConstantNode extends Node {
	
	String sval = null;

	int ival;
	
	public IntegerConstantNode(String s) {
		super();
		sval = s;
		ival = Integer.parseInt(s);
	}
	
        @Override
	public String toString() {
		return sval;
	}
 

        public int getValue() {
        	return ival;
        }
	

	
	
	
}
