package org.lemsml.jlems.core.type.procedure;

import org.lemsml.jlems.core.run.ExecutablePrint;
import org.lemsml.jlems.core.run.ExecutableStatement;
import org.lemsml.jlems.core.sim.ContentError;
import org.lemsml.jlems.core.type.Lems;

public class Print extends Statement {

	
	public String template;

	@Override
	public ExecutableStatement makeExecutableStatement(Lems lems) throws ContentError {
		ExecutablePrint ep = new ExecutablePrint(template, lems);
		return ep;
	}
	
	
}
