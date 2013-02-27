package org.lemsml.jlems.core.type.procedure;

import org.lemsml.jlems.core.run.ExecutableForEach;
import org.lemsml.jlems.core.run.ExecutableStatement;
import org.lemsml.jlems.core.sim.ContentError;
import org.lemsml.jlems.core.type.Lems;
import org.lemsml.jlems.core.type.LemsCollection;

public class ForEachComponent extends Statement {

	public String select;
	
	public String as;
	
	
	public LemsCollection<Statement> statements = new LemsCollection<Statement>();


	@Override
	public ExecutableStatement makeExecutableStatement(Lems lems) throws ContentError {
		ExecutableForEach ret = new ExecutableForEach(select, as);
		
		for (Statement s : statements) {
			ret.add(s.makeExecutableStatement(lems));
		}
		
		return ret;
	}
	
}
