package org.lemsml.jlems.core.expression;

import org.lemsml.jlems.core.sim.ContentError;

public class CWriter extends CommonLangWriter {

	@Override
	public void visitPowerNode(DoubleParseTreeNode leftEvaluable, DoubleParseTreeNode rightEvaluable) throws ContentError {
			sb.append("pow");
			sb.append(getArgStart());
			if (leftEvaluable != null) {
				leftEvaluable.doVisit(this);
			}
			sb.append(", ");
			rightEvaluable.doVisit(this);
			sb.append(getArgEnd());
	}
	
	
}

