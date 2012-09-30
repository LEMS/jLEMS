package org.lemsml.jlems.type.procedure;

import org.lemsml.jlems.run.ExecutableProcedure;
import org.lemsml.jlems.type.Lems;
import org.lemsml.jlems.type.LemsCollection;
import org.lemsml.jlems.util.ContentError;

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
