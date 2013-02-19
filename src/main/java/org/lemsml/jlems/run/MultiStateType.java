package org.lemsml.jlems.run;

import java.util.ArrayList;

import org.lemsml.jlems.sim.StateTypeVisitor;
import org.lemsml.jlems.sim.ContentError;

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
