package org.lemsml.jlems.codger;

import java.util.ArrayList;
import java.util.HashSet;

import org.lemsml.jlems.run.ComponentBehavior;


public class MetaClass {

	public String name;
	
	
	public GenPackage genPackage;
	
	
	public HashSet<String> pkgHS = new HashSet<String>();

	public ArrayList<MetaField> metaFields = new ArrayList<MetaField>();
	
	public ArrayList<MetaConstructor> constructors = new ArrayList<MetaConstructor>();
	
	public ArrayList<MetaMethod> metaMethods = new ArrayList<MetaMethod>();
	
 
	public ArrayList<Getter> getters = new ArrayList<Getter>();
	
	
	public MetaClass(GenPackage gp, String s) {
		genPackage = gp;
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


	public String generateJava() {
		StringBuilder sb = new StringBuilder();
		
		String rootPkg = genPackage.getPackageName();
		
		sb.append("package " + rootPkg + ";\n");
		
		for (String s : pkgHS) {
			sb.append("import " + rootPkg + "." + s + ";\n");
		}
		
		sb.append("\n");
		sb.append("public class " + name + " {\n\n");
	
		for (MetaField mf : metaFields) {
			appendIndented(mf.generateJava(), sb, 1);
			sb.append("\n");
		}
		sb.append("\n");

		
		for (MetaConstructor mc : constructors) {
			appendIndented(mc.generateJava(name), sb, 1);
			sb.append("\n");
		}
		
		// empty constructor
	//	sb.append("    public " + name + "() {\n    }\n\n");
		

	
		for (Getter g : getters) {
			appendIndented(g.generateJava(), sb, 1);
			sb.append("\n");
		}
		
		for (MetaMethod mm : metaMethods) {
			appendIndented(mm.generateJava(), sb, 1);
			sb.append("\n");
		}
		
		sb.append("}\n");
		return sb.toString();
	}


	public void addFloatGetter(String s, String val) {
		 FloatGetter fg = new FloatGetter(s, val);
		 getters.add(fg);
	}

 
	
	private void appendIndented(String stxt, StringBuilder sb, int ind) {
		String indent = "";
		for (int i = 0; i < ind; i++) {
			indent += "    ";
		}
		for (String s : stxt.split("\n")) {
			sb.append(indent + s);
			sb.append("\n");
		}
	}

	
	private void addIncludePackage(String p) {
		pkgHS.add(p);
	}
	
	

	public void addObjectField(String pkg, String nm, String typ) {
		addIncludePackage(pkg);
		ObjectMetaField omf = new ObjectMetaField(pkg, nm, typ);
		metaFields.add(omf);
	}


	public MetaConstructor addMetaConstructor() {
		MetaConstructor mc = new MetaConstructor();
		constructors.add(mc);
		return mc;
	}


	public void addObjectArrayField(String pkg, String nm, String typ) {
		addIncludePackage(pkg);
		ObjectArrayField oaf = new ObjectArrayField(pkg, nm, typ);
		metaFields.add(oaf);
	}
	
	
	
}
