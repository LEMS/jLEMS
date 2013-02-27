package org.lemsml.jlems.core.display;

public class DataViewerFactory {

	public DataViewerFactory delegatedFactory = null;
	
	
	static DataViewerFactory instance;

	
	public static DataViewerFactory getFactory() {
		synchronized(DataViewerFactory.class) {
 		if (instance == null) {
			instance = new DataViewerFactory();
		}
		}
		return instance;
	}
	
	
	public void setDelegate(DataViewerFactory dvf) {
		delegatedFactory = dvf;
	}
	
	
	public DataViewer newDataViewer(String title) {
		DataViewer ret = null;
		
		if (delegatedFactory != null) {
			ret = delegatedFactory.newDataViewer(title);
		} else {
			ret = new PrintDataViewer(title);
		}
		
		return ret;
	}
	
}
