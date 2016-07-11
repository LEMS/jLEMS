package org.lemsml.jlems.core.run;
 
public interface InPortReceiver {

	public String getName();
    
	public void receive() throws RuntimeError;
	
}
