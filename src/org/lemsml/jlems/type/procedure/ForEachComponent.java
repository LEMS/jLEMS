package org.lemsml.jlems.type.procedure;

import org.lemsml.jlems.run.ExecutableForEach;
import org.lemsml.jlems.run.ExecutableStatement;
import org.lemsml.jlems.type.Lems;
import org.lemsml.jlems.type.LemsCollection;
import org.lemsml.jlems.util.ContentError;

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
