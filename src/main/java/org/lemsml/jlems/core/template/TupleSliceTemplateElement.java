package org.lemsml.jlems.core.template;

import java.util.HashMap;

import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.core.run.StateInstance;
import org.lemsml.jlems.core.run.StateRunnable;
 

public class TupleSliceTemplateElement extends AbstractTemplateElement {

	
	String listName;
	
	String tupleName;
	String itemName;
	 
	
	public TupleSliceTemplateElement(String lnm, String tnm, String itnm) {
		super();
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
	public String eval(StateRunnable so, HashMap<String, StateRunnable> context) {
		E.missing();
		return "";
	}
	
}
