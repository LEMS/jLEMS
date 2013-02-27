package org.lemsml.jlems.io.out;

import org.lemsml.jlems.core.display.DataViewer;
import org.lemsml.jlems.core.display.DataViewerFactory;
import org.lemsml.jlems.core.out.ResultWriter;
import org.lemsml.jlems.core.out.ResultWriterFactory;
import org.lemsml.jlems.core.run.RuntimeOutput;

public final class FileResultWriterFactory extends ResultWriterFactory {

	static FileResultWriterFactory instance;
	
	// inject this into the jLEMS DataViewerFactory:
	public static void initialize() {
		if (instance == null) {
			instance = new FileResultWriterFactory();
		}
	 
	} 
	
	private FileResultWriterFactory() {
		super();
		ResultWriterFactory.getFactory().setDelegate(this);
	}
	
	
	@Override
	public ResultWriter newResultWriter(RuntimeOutput ro) {
		ResultWriter ret = new FileResultWriter(ro);	
		return ret;
	}

 
}
