package org.lemsml.run;

import java.util.HashMap;

import org.lemsml.util.ContentError;
import org.lemsml.util.E;
import org.lemsml.util.RuntimeError;

public abstract class PostBuilder extends BuilderElement {

	public abstract void postBuild(StateInstance tgt, HashMap<String, StateInstance> sihm, BuildContext bc) throws ConnectionError, ContentError, RuntimeError;

	 
	
	public void postChildren(StateInstance tgt, HashMap<String, StateInstance> sihm, BuildContext bc) throws ConnectionError, ContentError, RuntimeError {
		for (BuilderElement be : elts) {
			if (be instanceof PostBuilder) {
				((PostBuilder)be).postBuild(tgt, sihm, bc);
			} else {
				E.error("CHECK - is this OK? non post-builder child in post-builder?");
			}
		}
	}
	
}
