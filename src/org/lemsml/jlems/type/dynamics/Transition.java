package org.lemsml.jlems.type.dynamics;

import org.lemsml.jlems.sim.ContentError;

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
