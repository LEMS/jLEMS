package org.lemsml.jlems.codger;

import java.util.ArrayList;

import org.lemsml.jlems.run.ComponentBehavior;

public class MetaConstructor {

	 
	ArrayList<Instantiation> instantiations = new ArrayList<Instantiation>();
	
	
	public MetaConstructor() {
	 
	}
	
	public String generateJava(String cnm) {
		StringBuilder sb = new StringBuilder();
		sb.append("public " + cnm + "() {\n");
		for (Instantiation ins : instantiations) {
			sb.append("    " + ins.generateJava());
			sb.append("\n");
		}
		sb.append("}\n");
		return sb.toString();
	}

	
	public void addInstantiation(String s, String type) {
		Instantiation ins = new Instantiation(s, type);
		instantiations.add(ins);
	}

	public void addObjectToArrayInstantiator(String pkg, String nm, String typ) {
		ObjectToArrayInstantiation ins = new ObjectToArrayInstantiation(pkg, nm, typ);
		instantiations.add(ins);
	}

	
}
