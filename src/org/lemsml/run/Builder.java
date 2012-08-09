package org.lemsml.run;

import java.util.ArrayList;
import java.util.HashMap;

import org.lemsml.util.ContentError;

public class Builder {

	
	ArrayList<BuilderElement> builderElements = new ArrayList<BuilderElement>();
	
	 

 
	public void postBuild(StateInstance si) throws ConnectionError, ContentError {
		HashMap<String, StateInstance> siHM = new HashMap<String, StateInstance>();

		BuildContext bc = new BuildContext();
		
		for (BuilderElement be : builderElements) {
			if (be instanceof PostBuilder) {
				PostBuilder pb = (PostBuilder)be;
				pb.postBuild(si, siHM, bc);
				
			}
		}
	}
	
 

	public void add(BuilderElement be) {
		builderElements.add(be);
	}
 

	public boolean isPostBuilder() {
		boolean ret = false;
		for (BuilderElement be : builderElements) {
			if (be instanceof PostBuilder) {
				ret = true;
			}
		}
		return ret;
	}


	public boolean isChildInstantiator() {
		boolean ret = false;
		for (BuilderElement be : builderElements) {
			if (be instanceof ChildInstantiator) {
				ret = true;
			}
		}
		return ret;
	}
	
	public void childInstantiate(StateInstance si) throws ContentError, ConnectionError {
		for (BuilderElement be : builderElements) {
			if (be instanceof ChildInstantiator) {
				((ChildInstantiator)be).childInstantiate(si);
			}
		}
	}



	public void consolidateComponentBehaviors() {
		for (BuilderElement be : builderElements) {
			be.consolidateComponentBehaviors();
		}
	}
}
