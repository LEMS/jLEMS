package org.lemsml.procedure;

import org.lemsml.run.ExecutableForEach;
import org.lemsml.run.ExecutableStatement;
import org.lemsml.type.Lems;
import org.lemsml.type.LemsCollection;
import org.lemsml.util.ContentError;

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
