package org.lemsml.jlems.core.run;

 
public class InPort {

	StateInstance uin;
	String name;
	ActionBlock actionBlock;
	
	
	public InPort(StateInstance inst, String s, ActionBlock ab) {
		uin = inst;
		name = s;
		actionBlock = ab;
	}

	
	public void receive() throws RuntimeError {
		if (actionBlock != null) {
			actionBlock.run(uin);
		}
		if (uin.hasRegimes) {
			uin.receiveRegimeEvent(name);
		}
	}
	
}
