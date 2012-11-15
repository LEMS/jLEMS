package org.lemsml.jlems.codger;

import java.util.ArrayList;


public class MetaClass {

	public String name;
	
	public ArrayList<MetaField> metaFields = new ArrayList<MetaField>();
	
	public ArrayList<MetaMethod> metaMethods = new ArrayList<MetaMethod>();
	
	
	public MetaClass(String s) {
		name = s;
	}


	public void addConstant(String nm, double val) {
	
		FixedMetaField mf = new FixedMetaField(nm, val);
		metaFields.add(mf);
	}


	public void addVariable(String s) {
		VariableMetaField vmf = new VariableMetaField(s);
		metaFields.add(vmf);
	}


	public MetaMethod newMetaMethod(VarType typ, String nm, String rv) {
		MetaMethod mm = new MetaMethod(typ, nm, rv);
		metaMethods.add(mm);
		return mm;
	}


	public MetaMethod newMetaMethod(String nm) {
		MetaMethod mm = new MetaMethod(VarType.VOID, nm, null);
		metaMethods.add(mm);
		return mm;
	}

 
	
	
	
}
