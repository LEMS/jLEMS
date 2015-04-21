package org.lemsml.jlems.core.out;

import org.lemsml.jlems.core.run.RuntimeError;

public interface ResultWriter {

	void addPoint(String id, double x, double y);
	
	String getID();

	void advance(double t) throws RuntimeError;

	void addedRecorder();

	void close() throws RuntimeError;

}
