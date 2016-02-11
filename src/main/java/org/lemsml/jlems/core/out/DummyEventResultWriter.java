package org.lemsml.jlems.core.out;

import org.lemsml.jlems.core.run.RuntimeOutput;

public class DummyEventResultWriter implements EventResultWriter {


	public String getID()
	{
		return "";
	}
	
	public DummyEventResultWriter(RuntimeOutput ro) {
		 
	}

	@Override
    public void recordEvent(String id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void advance(double t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addedRecorder() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}
	
	
}
