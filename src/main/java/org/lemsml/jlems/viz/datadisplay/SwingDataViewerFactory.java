package org.lemsml.jlems.viz.datadisplay;

import java.awt.HeadlessException;
import org.lemsml.jlems.core.display.DataViewer;
import org.lemsml.jlems.core.display.DataViewerFactory;
import org.lemsml.jlems.core.logging.E;

public final class SwingDataViewerFactory extends DataViewerFactory {

	static SwingDataViewerFactory instance;
	
	// inject this into the jLEMS DataViewerFactory:
	public static void initialize() {
 	if (instance == null) {
			instance = new SwingDataViewerFactory();
		}
	 
	} 
	
	private SwingDataViewerFactory() {
		super();
		DataViewerFactory.getFactory().setDelegate(this);
	}
	
	
	@Override
	public DataViewer newDataViewer(String title) {
        DataViewer ret = null;
        try {
            ret = new StandaloneViewer(title);
        } catch (HeadlessException he) {
            E.informativeError("Problem starting the GUI!\n"
                    + "Are you running LEMS in headless mode, e.g. logged in to a remote machine?\n"
                    + "Try running jLEMS with the -nogui option");
            System.exit(1);
        }
		return ret;
	}

 
}
