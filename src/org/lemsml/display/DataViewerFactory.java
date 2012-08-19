package org.lemsml.display;

public class DataViewerFactory {

	public DataViewerFactory delegatedFactory = null;
	
	
	static DataViewerFactory instance;

	
	public static DataViewerFactory getFactory() {
		if (instance == null) {
			instance = new DataViewerFactory();
		}
		return instance;
	}
	
	
	public void setDelegate(DataViewerFactory dvf) {
		delegatedFactory = dvf;
	}
	
	
	public DataViewer newDataViewer() {
		DataViewer ret = null;
		
		if (delegatedFactory != null) {
			ret = delegatedFactory.newDataViewer();
		} else {
			ret = new PrintDataViewer();
		}
		
		return ret;
	}
	
}
