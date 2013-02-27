package org.lemsml.jlems.core.type.procedure;

import org.lemsml.jlems.core.run.ExecutableProcedure;
import org.lemsml.jlems.core.sim.ContentError;
import org.lemsml.jlems.core.type.Lems;
import org.lemsml.jlems.core.type.LemsCollection;

public class Procedure {

	
	public LemsCollection<Statement> statements = new LemsCollection<Statement>();

	public ExecutableProcedure makeExecutableProcedure(Lems lems) throws ContentError {
		ExecutableProcedure ret = new ExecutableProcedure();
		
		for (Statement statement : statements) {
			ret.addStep(statement.makeExecutableStatement(lems));
		}
		
		return ret;
	}
	
	
}
