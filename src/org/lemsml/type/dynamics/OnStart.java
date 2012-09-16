package org.lemsml.type.dynamics;

import java.util.HashMap;

import org.lemsml.expression.ParseError;
import org.lemsml.expression.Parser;
import org.lemsml.expression.Valued;
import org.lemsml.type.LemsCollection;
import org.lemsml.util.ContentError;

public class OnStart extends PointResponse   {


    
	public void resolve(Dynamics bhv, LemsCollection<StateVariable> stateVariables, HashMap<String, Valued> valHM, Parser parser) throws ContentError, ParseError {
		supResolve(bhv, stateVariables, valHM, parser);
	}



}
