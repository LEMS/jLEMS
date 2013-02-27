package org.lemsml.jlems.core.selection;

import java.util.Comparator;

public class SelectionPrecedenceComparator implements Comparator<AbstractSelectionOperatorNode> {

	 
	public int compare(AbstractSelectionOperatorNode a, AbstractSelectionOperatorNode b) {
		double pa = a.getPrecedence();
		double pb = b.getPrecedence();
		
		int ret = 0;
		if (pa < pb) {
			ret = -1;
		} else if (pa > pb) {
			ret = 1;
		}
		return ret;
	}

}
