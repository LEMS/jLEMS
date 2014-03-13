package org.lemsml.jlems.io.out;

import org.lemsml.jlems.core.display.DataViewer;
import org.lemsml.jlems.core.display.DataViewerFactory;
import org.lemsml.jlems.core.out.ResultWriter;
import org.lemsml.jlems.core.out.ResultWriterFactory;
import org.lemsml.jlems.core.run.RuntimeOutput;
import org.lemsml.jlems.core.type.LemsCollection;

public final class StringBuilderResultWriterFactory extends ResultWriterFactory {

	static StringBuilderResultWriterFactory instance;
	static LemsCollection<StringBuilderResultWriter> resultWriters = new LemsCollection<StringBuilderResultWriter>();
	// inject this into the jLEMS DataViewerFactory:
	public static void initialize() {
		if (instance == null) {
			instance = new StringBuilderResultWriterFactory();
		}
	 
	} 
	
	private StringBuilderResultWriterFactory() {
		super();
		ResultWriterFactory.getFactory().setDelegate(this);
	}
	
	
	@Override
	public ResultWriter newResultWriter(RuntimeOutput ro) {
		ResultWriter ret = new StringBuilderResultWriter(ro);	
		resultWriters.add((StringBuilderResultWriter)ret);
		return ret;
	}

	public static LemsCollection<StringBuilderResultWriter> getStringBuilderResultWriters() {
		return resultWriters;
	} 
 
}
