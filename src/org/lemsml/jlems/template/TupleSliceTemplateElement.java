package org.lemsml.jlems.template;

import java.util.HashMap;

import org.lemsml.jlems.run.StateInstance;
import org.lemsml.jlems.util.E;
 

public class TupleSliceTemplateElement extends TemplateElement {

	
	String listName;
	
	String tupleName;
	String itemName;
	 
	
	public TupleSliceTemplateElement(String lnm, String tnm, String itnm) {
		listName = lnm;
	 
		tupleName = tnm;
		itemName = itnm;
	}

	
	
	public String toString() {
		return "Tuple Slice Item, " + tupleName + "." + itemName + ", from " + listName;
	}
	
	 
	@Override
	public String eval(HashMap<String, String> vmap) {
		String ret = "";
	 
		return ret;
	}



	@Override
	public String eval(StateInstance so, HashMap<String, StateInstance> context) {
		E.missing();
		return "";
	}
	
}
