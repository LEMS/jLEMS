package org.lemsml.jlems;

public final class ResourceRoot {

	private static ResourceRoot instance;
	
	 
	private ResourceRoot() {
		
	}
	
	
	public static ResourceRoot getRoot() {
		synchronized(ResourceRoot.class) {
		if (instance == null) {
			instance = new ResourceRoot();
		}
		}
		return instance;
	}
}
