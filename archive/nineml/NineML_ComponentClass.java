package org.lemsml.jlems.nineml;

import java.util.ArrayList;

import org.lemsml.jlems.io.FormatException;
import org.lemsml.jlems.io.IOFaceMixed;
import org.lemsml.jlems.type.ComponentType;
import org.lemsml.jlems.type.LemsCollection;
import org.lemsml.jlems.util.ContentError;

public class NineML_ComponentClass implements IOFaceMixed {

	public String name;
	
	public LemsCollection<NineML_Parameter> cM_Parameters = new LemsCollection<NineML_Parameter>();
	
	public LemsCollection<NineML_AnalogPort> cM_AnalogPorts = new LemsCollection<NineML_AnalogPort>();
	
	public LemsCollection<NineML_EventPort> cM_EventPorts = new LemsCollection<NineML_EventPort>();
	
	public LemsCollection<NineML_Dynamics> cM_Dynamicses = new LemsCollection<NineML_Dynamics>();

	// pending is to hold native elements that get loaded while we're loading 
	// the face element
	public ArrayList<Object> pending = new ArrayList<Object>();
	
 
	public Object getInternal() throws FormatException, ContentError {
		return getComponentType();	
	}
	
	public void addPending(Object ob) {
		pending.add(ob);
	}
	
	public ArrayList<Object> getPending() {
		return pending;
	}
	
	
	public ComponentType getComponentType() throws FormatException, ContentError {
		ComponentType ret = new ComponentType(name);
		
		for (NineML_Parameter p : cM_Parameters) {
			ret.addParameter(p.getParameter());
		}
		
		for (NineML_EventPort ep : cM_EventPorts) {
			ret.addEventPort(ep.getEventPort());
		}
		
		for (NineML_Dynamics d : cM_Dynamicses) {
			ret.addBehavior(d.getDynamics());
		}
		
		// these come last as they may need to add some stuff to the behavior
		for (NineML_AnalogPort ap : cM_AnalogPorts) {
			ap.applyToType(ret);
		}
		
		return ret;
		
	}
	
	
	
}
