package org.lemsml.jruntime;

import org.lemsml.jlems.run.GeneratedInstance;
import org.lemsml.jlems.run.RuntimeType;
import org.lemsml.jlems.run.StateRunnable;

public class NativeType implements RuntimeType {

	String cptID;
	
	Class cptClass;
	
	
	long timeCounter;
	boolean trackTime = false;
	
	
	public NativeType(String cid, Class cls) {
		cptID = cid;
		cptClass = cls;
	}

	public String getID() {
		return cptID;
	}
	
	public void enableTiming() {
		trackTime = true;
	}
	
	public long getTotalTime() {
		return timeCounter;
	}
	
	public void addTime(long dt) {
		timeCounter += dt;
	}
	
	
	public StateRunnable newStateRunnable() {
		StateRunnable ret = null;
		try {
			GeneratedInstance geninst = (GeneratedInstance)cptClass.newInstance();
		
			NativeWrapper nr = new NativeWrapper(geninst, this);
			ret = nr;
		} catch (Exception ex) {
			throw new RuntimeException("Can't get genrated instance ", ex);
		}
		return ret;
	}
	
}
