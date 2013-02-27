package org.lemsml.jlems.core.run;

import java.util.HashMap;

import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.core.sim.ContentError;

public abstract class AbstractPostBuilder extends BuilderElement {

	public abstract void postBuild(StateRunnable tgt, HashMap<String, StateRunnable> sihm, BuildContext bc) throws ConnectionError, ContentError, RuntimeError;

	 
	
	public void postChildren(StateRunnable tgt, HashMap<String, StateRunnable> sihm, BuildContext bc) throws ConnectionError, ContentError, RuntimeError {
		for (BuilderElement be : elts) {
			if (be instanceof AbstractPostBuilder) {
				((AbstractPostBuilder)be).postBuild(tgt, sihm, bc);
			} else {
				E.error("CHECK - is this OK? non post-builder child in post-builder?");
			}
		}
	}
	
}
