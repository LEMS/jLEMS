package org.lemsml.procedure;

import org.lemsml.run.ExecutableProcedure;
import org.lemsml.type.Lems;
import org.lemsml.type.LemsCollection;
import org.lemsml.util.ContentError;

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
