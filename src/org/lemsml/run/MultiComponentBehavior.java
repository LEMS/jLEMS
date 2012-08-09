package org.lemsml.run;

import java.util.ArrayList;

import org.lemsml.util.ContentError;

public class MultiComponentBehavior {

	ArrayList<ComponentBehavior> cba;
	
	
	public MultiComponentBehavior(ArrayList<ComponentBehavior> a) {
		cba = a;
	}

	public ArrayList<ComponentBehavior> getCBs() {
		return cba;
	}
	
	
	// TODO - set knownAs (name of this array within parent) here instead of "multi"? 
	public MultiInstance newInstance() throws ContentError, ConnectionError {
		MultiInstance mi =  new MultiInstance("", "multi");
		for (ComponentBehavior cb : cba) {
			mi.add(cb.newInstance());
		}
		return mi;
	}

	
	public MultiComponentBehavior makeCopy() {
		ArrayList<ComponentBehavior> ccba = new ArrayList<ComponentBehavior>();
		for (ComponentBehavior cb : cba) {
			ccba.add(cb.makeShallowCopy());
		}
		MultiComponentBehavior ret = new MultiComponentBehavior(ccba);
		return ret;
	}
	
}
