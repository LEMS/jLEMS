package org.lemsml.procedure;

import org.lemsml.run.EquilibrateStatement;
import org.lemsml.run.ExecutableStatement;
import org.lemsml.type.Lems;

public class Equilibrate extends Statement {

	@Override
	public ExecutableStatement makeExecutableStatement(Lems lems) {
		EquilibrateStatement es = new EquilibrateStatement();
		return es;
	}

}
