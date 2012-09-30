package org.lemsml.jlems.type.procedure;

import org.lemsml.jlems.run.ExecutableStatement;
import org.lemsml.jlems.type.Lems;
import org.lemsml.jlems.util.ContentError;

public abstract class Statement {

	public abstract ExecutableStatement makeExecutableStatement(Lems lems) throws ContentError;
	 
}
