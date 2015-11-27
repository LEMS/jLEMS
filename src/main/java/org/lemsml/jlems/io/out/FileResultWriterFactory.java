package org.lemsml.jlems.io.out;

import org.lemsml.jlems.core.out.EventResultWriter;
import org.lemsml.jlems.core.out.ResultWriter;
import org.lemsml.jlems.core.out.ResultWriterFactory;
import org.lemsml.jlems.core.run.RuntimeEventOutput;
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
    
	@Override
	public EventResultWriter newEventResultWriter(RuntimeEventOutput ro) {
		EventResultWriter ret = new FileEventResultWriter(ro);	
		return ret;
	}

 
}
