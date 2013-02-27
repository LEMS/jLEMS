package org.lemsml.jlems.core.run;

import java.util.ArrayList;

import org.lemsml.jlems.core.logging.E;

public class DestinationMap {

	ArrayList<StateRunnable> targetA;
	 
	String path;
	
	String name;
	String[] bits;
	
	boolean isMulti = false;
	
	// NB this only works for paths like synapse[*]/g
	// - needs generalizing along with PathDerived
	public DestinationMap(String p, ArrayList<StateRunnable> pla) {
		path = p;
		targetA = pla;
		bits = path.split("/");
		String sn = bits[0];
		
		if (sn.endsWith("[*]")) {
			isMulti = true;
			name = sn.substring(0, sn.length() - 3);
		} else {
			name = sn;
		}
		
	}

	
	
	public void checkInsert(String s, StateInstance inst) {
		if (s.equals(name)) {
			if (isMulti && bits.length == 2) {
				// OK
				targetA.add(inst);
				// E.info("dm added item to xTarget array");
			} else {
				E.missing("can't handle destination " + path + 
						" for possible addition " + inst);
			}
		}
	}
	
}
