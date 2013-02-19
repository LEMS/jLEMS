package org.lemsml.jlems.type.procedure;

import org.lemsml.jlems.run.ExecutablePrint;
import org.lemsml.jlems.run.ExecutableStatement;
import org.lemsml.jlems.sim.ContentError;
import org.lemsml.jlems.type.Lems;

public class Print extends Statement {

	
	public String template;

	@Override
	public ExecutableStatement makeExecutableStatement(Lems lems) throws ContentError {
		ExecutablePrint ep = new ExecutablePrint(template, lems);
		return ep;
	}
	
	
}
