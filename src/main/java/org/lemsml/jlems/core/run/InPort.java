package org.lemsml.jlems.core.run;

import org.lemsml.jlems.core.logging.E;

 
public class InPort implements InPortReceiver {

	StateInstance uin;
	String name;
	ActionBlock actionBlock;
	
	
	public InPort(StateInstance inst, String s, ActionBlock ab) {
		uin = inst;
		name = s;
		actionBlock = ab;
	}

	
    @Override
	public void receive() throws RuntimeError {
        //E.info("Receiving event on port "+name+" on "+uin.getInfo());
		if (actionBlock != null) {
			actionBlock.run(uin);
		}
		if (uin.hasRegimes) {
			uin.receiveRegimeEvent(name);
		}
	}
    
    @Override
	public String getName() {
        return name;
    }
	
}
