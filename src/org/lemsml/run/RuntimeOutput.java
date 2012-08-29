package org.lemsml.run;

public class RuntimeOutput {

	String id;
	String title;
	
	
	public RuntimeOutput(String aid, String ttl) {
		 id = aid;
		 title = ttl;
	}


	public String getID() {
		return id;
	}
	
	public String getTitle() {
		return title;
	}

}
