package org.lemsml.procedure;

import org.lemsml.run.ExecutableStatement;
import org.lemsml.type.Lems;
import org.lemsml.util.ContentError;

public abstract class Statement {

	public abstract ExecutableStatement makeExecutableStatement(Lems lems) throws ContentError;
	 
}
