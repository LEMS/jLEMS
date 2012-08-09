package org.lemsml.procedure;

import org.lemsml.run.ExecutablePrint;
import org.lemsml.run.ExecutableStatement;
import org.lemsml.type.Lems;
import org.lemsml.util.ContentError;

public class Print extends Statement {

	
	public String template;

	@Override
	public ExecutableStatement makeExecutableStatement(Lems lems) throws ContentError {
		ExecutablePrint ep = new ExecutablePrint(template, lems);
		return ep;
	}
	
	
}
