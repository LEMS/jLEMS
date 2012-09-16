package org.lemsml.nineml;

import org.lemsml.io.FormatException;
import org.lemsml.type.LemsCollection;
import org.lemsml.type.dynamics.OnCondition;

public class NineML_OnCondition  {

	public NineML_Trigger trigger;
	
	public LemsCollection<NineML_StateAssignment> cM_StateAssignments = new LemsCollection<NineML_StateAssignment>();
	
	public LemsCollection<NineML_EventOut> cM_EventOuts = new LemsCollection<NineML_EventOut>();

	 
	
	public OnCondition getOnCondition() throws FormatException {	
		OnCondition oc = new OnCondition();
		oc.test = trigger.getExpression();
		 
		for (NineML_StateAssignment cmsa : cM_StateAssignments) {
			oc.addStateAssignment(cmsa.getStateAssignment());
		}
		
		for (NineML_EventOut eo : cM_EventOuts) {
			oc.addEventOut(eo.getEventOut());
		}
		
		return oc;
	}
	
	
	
	
	
}
