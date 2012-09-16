package org.lemsml.type.dynamics;

import java.util.HashMap;

import org.lemsml.annotation.Mat;
import org.lemsml.annotation.Mel;
import org.lemsml.expression.ParseError;
import org.lemsml.expression.Parser;
import org.lemsml.expression.Valued;
import org.lemsml.type.EventPort;
import org.lemsml.type.LemsCollection;
import org.lemsml.util.ContentError;



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
