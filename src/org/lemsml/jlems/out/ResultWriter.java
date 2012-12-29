package org.lemsml.jlems.out;

import org.lemsml.jlems.run.RuntimeError;

public interface ResultWriter {

	void addPoint(String id, double x, double y);

	void advance(double t) throws RuntimeError;

	void addedRecorder();

	void close() throws RuntimeError;

}
