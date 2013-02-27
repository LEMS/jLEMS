package org.lemsml.jlems.core.type.dynamics;

import java.util.HashMap;

import org.lemsml.jlems.core.expression.ParseError;
import org.lemsml.jlems.core.expression.Parser;
import org.lemsml.jlems.core.expression.Valued;
import org.lemsml.jlems.core.sim.ContentError;
import org.lemsml.jlems.core.type.LemsCollection;

public class OnEntry extends PointResponse {
 
	public void resolve(Dynamics bhv, LemsCollection<StateVariable> stateVariables, HashMap<String, Valued> valHM, Parser parser) throws ContentError, ParseError {
		supResolve(bhv, stateVariables, valHM, parser);
	}



}
