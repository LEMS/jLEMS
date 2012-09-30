package org.lemsml.jlems.type.procedure;

import org.lemsml.jlems.run.EquilibrateStatement;
import org.lemsml.jlems.run.ExecutableStatement;
import org.lemsml.jlems.type.Lems;

public class Equilibrate extends Statement {

	@Override
	public ExecutableStatement makeExecutableStatement(Lems lems) {
		EquilibrateStatement es = new EquilibrateStatement();
		return es;
	}

}
