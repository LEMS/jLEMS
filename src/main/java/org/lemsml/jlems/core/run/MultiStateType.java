package org.lemsml.jlems.core.run;

import java.util.ArrayList;

import org.lemsml.jlems.core.sim.ContentError;
import org.lemsml.jlems.core.sim.StateTypeVisitor;

public class MultiStateType {

	ArrayList<StateType> cba;
	
	
	public MultiStateType() {
		cba = new ArrayList<StateType>();
	}
	
	
	 

	
	public void add(StateType cb) {
		cba.add(cb);
	}
	
	public ArrayList<StateType> getCBs() {
		return cba;
	}
	
	
	// TODO - set knownAs (name of this array within parent) here instead of "multi"? 
	public MultiInstance newInstance() throws ContentError, ConnectionError, RuntimeError {
		MultiInstance mi =  new MultiInstance("", "multi");
		for (StateType cb : cba) {
			mi.add(cb.newInstance());
		}
		return mi;
	}

	
	public void visitAll(StateTypeVisitor v) {
		for (StateType cb : cba) {
			cb.visitAll(v);
		}
		
	}
	
}
