package org.lemsml.jlems.nineml;

import org.lemsml.jlems.io.FormatException;
import org.lemsml.jlems.io.IOFace;
import org.lemsml.jlems.type.LemsCollection;
import org.lemsml.jlems.type.dynamics.Regime;

public class NineML_Regime implements IOFace {

	public String name;
	
	public LemsCollection<NineML_TimeDerivative> cM_TimeDerivatives = new LemsCollection<NineML_TimeDerivative>();
	
	public LemsCollection<NineML_OnCondition> cM_OnConditions = new LemsCollection<NineML_OnCondition>();

	@Override
	public Object getInternal() throws FormatException {
		return getRegime();
	}
	
	public Regime getRegime() throws FormatException {
		Regime ret = new Regime(name);
		for (NineML_TimeDerivative td : cM_TimeDerivatives) {
			ret.addTimeDerivative(td.getTimeDerivative());
		}
		for (NineML_OnCondition oc : cM_OnConditions) {
			ret.addOnCondition(oc.getOnCondition());
		}
		return ret;
	}
	
	 
}
