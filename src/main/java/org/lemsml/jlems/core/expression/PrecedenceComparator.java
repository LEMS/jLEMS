package org.lemsml.jlems.core.expression;

import java.util.Comparator;

public class PrecedenceComparator implements Comparator<AbstractOperatorNode> {

	 
	public int compare(AbstractOperatorNode a, AbstractOperatorNode b) {
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
