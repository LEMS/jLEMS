package org.lemsml.jlems.type.dynamics;

import java.util.HashMap;

import org.lemsml.jlems.annotation.Mat;
import org.lemsml.jlems.annotation.Mel;
import org.lemsml.jlems.expression.ParseError;
import org.lemsml.jlems.expression.Parser;
import org.lemsml.jlems.expression.Valued;
import org.lemsml.jlems.type.EventPort;
import org.lemsml.jlems.type.LemsCollection;
import org.lemsml.jlems.util.ContentError;



@Mel(info="Event handler block")
public class OnEvent extends PointResponse {
 
	@Mat(info="the port to lesten on")
	public String port;
	
	EventPort eventPort;
	  
	 
	public void resolve(Dynamics bhv, LemsCollection<StateVariable> stateVariables, HashMap<String, Valued> valHM, Parser parser) throws ContentError, ParseError {
	
		eventPort = bhv.getComponentType().getInEventPort(port);
	 
		supResolve(bhv, stateVariables, valHM, parser);
	}




	public String getPortName() {
		return eventPort.getName();
	}




	
}
