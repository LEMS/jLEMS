package org.lemsml.jlems.core.run;

public class RuntimeDisplay {

	String id;
	String title;
	
	double[] box;
 

    @Override
	public String toString() {
		return "RuntimeDisplay, id=" + id + " title=" + title;
	}
	

	public String getID() {
		return id;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String ttl) {
		title = ttl;
	}
	
	public void setID(String sid) {
		id = sid;
	}
	
	public void setBox(double[] b) {
		box = b;
	}

	public double[] getBox() {
		 return box;
	}

}
