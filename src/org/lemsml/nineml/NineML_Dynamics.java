package org.lemsml.nineml;

import org.lemsml.behavior.Behavior;
import org.lemsml.io.FormatException;
import org.lemsml.io.IOFace;
import org.lemsml.type.LemsCollection;
import org.lemsml.util.E;

public class NineML_Dynamics implements IOFace {

	public LemsCollection<NineML_StateVariable> cM_StateVariables = new LemsCollection<NineML_StateVariable>();
	 
	
	public LemsCollection<NineML_Alias> cM_Aliases = new LemsCollection<NineML_Alias>();
	
	 
	public LemsCollection<NineML_Regime> cM_Regimes = new LemsCollection<NineML_Regime>();

	
	
 
	public Object getInternal() throws FormatException {
		return getBehavior();
	}
	
	
		
	public Behavior getBehavior() throws FormatException {	
		Behavior b = new Behavior();
		
		E.info("getting behavior - nregimes = " + cM_Regimes.size());
		
		for (NineML_StateVariable cmsv : cM_StateVariables) {
			b.addStateVariable(cmsv.getStateVariable());
		}
	
		
		for (NineML_Alias ali : cM_Aliases) {
			b.addDerivedVariable(ali.getDerivedVariable());
		}
		E.info("stripping regimes and putting in main");
		if (cM_Regimes.size() == 1) {
			NineML_Regime cmr = cM_Regimes.first();
			for (NineML_TimeDerivative td : cmr.cM_TimeDerivatives) {
				b.addTimeDerivative(td.getTimeDerivative());
			}
			for (NineML_OnCondition oc : cmr.cM_OnConditions) {
				b.addOnCondition(oc.getOnCondition());
			}

			
		} else {
			E.info("Propagating regimes as is...");
			for (NineML_Regime cmr : cM_Regimes) {
				b.addRegime(cmr.getRegime());				
			}
		}
		E.info("made behavior " + b.regimes.size());
		return b;
	}
	
	
	
	
	
}
