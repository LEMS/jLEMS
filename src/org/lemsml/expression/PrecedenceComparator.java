package org.lemsml.expression;

import java.util.Comparator;

public class PrecedenceComparator implements Comparator<OperatorNode> {

	 
	public int compare(OperatorNode a, OperatorNode b) {
		int pa = a.getPrecedence();
		int pb = b.getPrecedence();
		
		int ret = 0;
		if (pa < pb) {
			ret = -1;
		} else if (pa > pb) {
			ret = 1;
		}
		return ret;
	}

}
