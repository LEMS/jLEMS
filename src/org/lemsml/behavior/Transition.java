package org.lemsml.behavior;

import org.lemsml.util.ContentError;

public class Transition {

	public String regime;

	Regime r_regime;
	
	public void resolve(Behavior bhv) throws ContentError {
		r_regime = bhv.getRegime(regime);
	}
	
	public String getRegime() {
		return regime;
	}
	
}
