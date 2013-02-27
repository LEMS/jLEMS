package org.lemsml.jlems.core.codger.metaclass;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import org.lemsml.jlems.core.logging.E;


public class MetaClass extends CodeUnit {


	public HashSet<String> dependencies = new HashSet<String>();
	
	public ArrayList<MetaInterface> interfaces = new ArrayList<MetaInterface>();

	public ArrayList<AbstractField> metaFields = new ArrayList<AbstractField>();
	
	public ArrayList<Constructor> constructors = new ArrayList<Constructor>();
	
	public ArrayList<Method> methods = new ArrayList<Method>();
	
 
	public ArrayList<AbstractGetter> getters = new ArrayList<AbstractGetter>();
	
	
	public MetaClass(MetaPackage gp, String s) {
		super(gp, s);
	}

	

	public void addConstant(String nm, double val) {
	
		FixedField mf = new FixedField(nm, val);
		metaFields.add(mf);
	}


	public void addVariable(String s) {
		VariableField vmf = new VariableField(s);
		metaFields.add(vmf);
	}


	public Method newMetaMethod(VarType typ, String nm, String rv) {
		Method mm = new Method(nm);
		mm.setReturnType(typ);
		mm.setReturnName(rv);
		methods.add(mm);
		return mm;
	}


	public Method newMetaMethod(String nm) {
		Method mm = new Method(nm);
		mm.setReturnType(VarType.VOID);
		methods.add(mm);
		return mm;
	}


	public String generateJava() {
		StringBuilder sb = new StringBuilder();
		
		String rootPkg = metaPackage.getPackageName();
		
		sb.append("package " + rootPkg + ";\n");
		
		for (String s : pkgHS) {
			sb.append("import " + rootPkg + "." + s + ".*;\n");
		}
		
		for (String s : clsHS) {
			sb.append("import " + s + ";\n");
		}
		
		
		for (String s : dependencies) {
			sb.append("import " + getJavaDep(s) + ";\n");
		}
		
		
		sb.append("\n");
		sb.append("public class " + name);
		
		if (!interfaces.isEmpty()) {
			boolean first = true;
			sb.append(" implements ");
			for (MetaInterface mi : interfaces) {
				if (!first) {
					sb.append(", ");
				}
				sb.append(mi.getClassName());
				first = false;
			}
		}
		
		sb.append(" {\n\n");
	
		for (AbstractField mf : metaFields) {
			appendIndented(mf.generateJava(), sb, 1);
			sb.append("\n");
		}
		sb.append("\n");

		
		for (Constructor mc : constructors) {
			appendIndented(mc.generateJava(name), sb, 1);
			sb.append("\n");
		}
		
		// empty constructor
	//	sb.append("    public " + name + "() {\n    }\n\n");
		

	
		for (AbstractGetter g : getters) {
			appendIndented(g.generateJava(), sb, 1);
			sb.append("\n");
		}
		
		for (Method mm : methods) {
			appendIndented(mm.generateJava(), sb, 1);
			sb.append("\n");
		}
		
		sb.append("}\n");
		return sb.toString();
	}


	private String getJavaDep(String s) {
		String ret = "";
		if (s.equals("LIST")) {
			ret = "java.util.ArrayList";
		} else if (s.equals("MAP")) {
			ret = "java.util.HashMap";
			
		} else if (s.equals("DOUBLEPOINTER")) {
			ret = "org.lemsml.jlems.run.DoublePointer";
			
		} else {
			E.missing("unrecognized dependency " + s);
		}
		return ret;
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

	
	

	public void addObjectField(String pkg, String nm, String typ) {
		addIncludePackage(pkg);
		ObjectField omf = new ObjectField(pkg, nm, typ);
		metaFields.add(omf);
	}


	public Constructor addMetaConstructor() {
		Constructor mc = new Constructor();
		constructors.add(mc);
		return mc;
	}


	public void addObjectArrayField(String pkg, String nm, String typ) {
		addIncludePackage(pkg);
		ArrayField oaf = new ArrayField(pkg, nm, typ);
		metaFields.add(oaf);
	}



	public void addImplements(MetaInterface mi) {
		interfaces.add(mi);
		String s = mi.getFQClassName();
		clsHS.add(s);
	}



	public void addDependency(String s) {
		dependencies.add(s);
	}



	public Collection<? extends Method> getMetaMethods() {
		return methods;
	}



	public boolean hasMethod(Method cmm) {
		boolean ret = false;
		for (Method mm : methods) {
			 if (mm.sameAs(cmm)) {
				 ret = true;
				 break;
			 }  
		}
		return ret;
	}
	
	public boolean hasGetter(AbstractGetter cmm) {
		boolean ret = false;
		for (AbstractGetter mm : getters) {
			 if (mm.sameAs(cmm)) {
				 ret = true;
				 break;
			 }  
		}
		return ret;
	}


	public Collection<? extends AbstractGetter> getGetters() {
		return getters;
	}



	public Method getMethod(String mnm) {
		Method ret = null;
		for (Method mm : methods) {
			if (mm.getName().equals(mnm)) {
				ret = mm;
				break;
			}
		}
		return ret;
	}


	
	
}
