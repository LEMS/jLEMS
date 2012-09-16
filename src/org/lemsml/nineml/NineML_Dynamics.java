package org.lemsml.nineml;

import org.lemsml.io.FormatException;
import org.lemsml.io.IOFace;
import org.lemsml.type.LemsCollection;
import org.lemsml.type.dynamics.Dynamics;
import org.lemsml.util.E;

public class NineML_Dynamics implements IOFace {

	public LemsCollection<NineML_StateVariable> ninemlStateVariables = new LemsCollection<NineML_StateVariable>();
	 
	
	public LemsCollection<NineML_Alias> ninemlAliases = new LemsCollection<NineML_Alias>();
	
	 
	public LemsCollection<NineML_Regime> ninemlRegimes = new LemsCollection<NineML_Regime>();

	
	
 
	public Object getInternal() throws FormatException {
		return getDynamics();
	}
	
	
		
	public Dynamics getDynamics() throws FormatException {	
		Dynamics b = new Dynamics();
		
		E.info("getting behavior - nregimes = " + ninemlRegimes.size());
		
		for (NineML_StateVariable cmsv : ninemlStateVariables) {
			b.addStateVariable(cmsv.getStateVariable());
		}
	
		
		for (NineML_Alias ali : ninemlAliases) {
			b.addDerivedVariable(ali.getDerivedVariable());
		}
		E.info("stripping regimes and putting in main");
		if (ninemlRegimes.size() == 1) {
			NineML_Regime cmr = ninemlRegimes.first();
			for (NineML_TimeDerivative td : cmr.cM_TimeDerivatives) {
				b.addTimeDerivative(td.getTimeDerivative());
			}
			for (NineML_OnCondition oc : cmr.cM_OnConditions) {
				b.addOnCondition(oc.getOnCondition());
			}

			
		} else {
			E.info("Propagating regimes as is...");
			for (NineML_Regime cmr : ninemlRegimes) {
				b.addRegime(cmr.getRegime());				
			}
		}
		E.info("made behavior " + b.regimes.size());
		return b;
	}
	
	
	
	
	
}
