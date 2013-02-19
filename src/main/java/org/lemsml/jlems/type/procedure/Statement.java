package org.lemsml.jlems.type.procedure;

import org.lemsml.jlems.run.ExecutableStatement;
import org.lemsml.jlems.sim.ContentError;
import org.lemsml.jlems.type.Lems;

public abstract class Statement {

	public abstract ExecutableStatement makeExecutableStatement(Lems lems) throws ContentError;
	 
}
