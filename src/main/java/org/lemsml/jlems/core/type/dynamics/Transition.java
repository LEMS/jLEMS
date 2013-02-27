package org.lemsml.jlems.core.type.dynamics;

import org.lemsml.jlems.core.sim.ContentError;

public class Transition {

	public String regime;

	Regime r_regime;
	
	public void resolve(Dynamics bhv) throws ContentError {
		r_regime = bhv.getRegime(regime);
	}
	
	public String getRegime() {
		return regime;
	}

	public Transition makeCopy() {
		Transition ret = new Transition();
		ret.regime = regime;
		return ret;
	}
	
}
