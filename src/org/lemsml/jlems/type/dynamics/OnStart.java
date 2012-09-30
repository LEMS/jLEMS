package org.lemsml.jlems.type.dynamics;

import java.util.HashMap;

import org.lemsml.jlems.expression.ParseError;
import org.lemsml.jlems.expression.Parser;
import org.lemsml.jlems.expression.Valued;
import org.lemsml.jlems.type.LemsCollection;
import org.lemsml.jlems.util.ContentError;

public class OnStart extends PointResponse   {


    
	public void resolve(Dynamics bhv, LemsCollection<StateVariable> stateVariables, HashMap<String, Valued> valHM, Parser parser) throws ContentError, ParseError {
		supResolve(bhv, stateVariables, valHM, parser);
	}



}
