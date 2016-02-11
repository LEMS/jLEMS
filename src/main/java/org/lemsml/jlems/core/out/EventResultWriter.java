package org.lemsml.jlems.core.out;

import org.lemsml.jlems.core.run.RuntimeError;

public interface EventResultWriter {

	//void addPoint(String id, double x, double y);
    
    void recordEvent(String id);
	
	String getID();

	void advance(double t) throws RuntimeError;

	void addedRecorder();

	void close() throws RuntimeError;

}
