package org.lemsml.behavior;

import java.util.HashMap;

import org.lemsml.expression.ParseError;
import org.lemsml.expression.Parser;
import org.lemsml.expression.Valued;
import org.lemsml.type.LemsCollection;
import org.lemsml.util.ContentError;

public class OnEntry extends PointResponse {
 
	public void resolve(Behavior bhv, LemsCollection<StateVariable> stateVariables, HashMap<String, Valued> valHM, Parser parser) throws ContentError, ParseError {
		supResolve(bhv, stateVariables, valHM, parser);
	}



}
