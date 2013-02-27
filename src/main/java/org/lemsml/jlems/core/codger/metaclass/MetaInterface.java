package org.lemsml.jlems.core.codger.metaclass;

import java.util.ArrayList;
import java.util.HashSet;

import org.lemsml.jlems.core.logging.E;

public class MetaInterface extends CodeUnit {

	
	public ArrayList<Method> methods = new ArrayList<Method>();

	public ArrayList<MetaClass> implementers = new ArrayList<MetaClass>();
	
	public ArrayList<AbstractGetter> getters = new ArrayList<AbstractGetter>();
	
	
	public MetaInterface(MetaPackage gp, String s) {
		super(gp, s);
	}

	 
	public Method newMetaMethod(String nm) {
		Method mm = new Method(nm);
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
	
		sb.append("\n");
		sb.append("public interface " + name + " {\n\n");


	
		for (Method mm : methods) {
			appendIndented(mm.generateInterfaceJava(), sb, 1);
			sb.append("\n");
		}
		
		for (AbstractGetter g : getters) {
			appendIndented(g.generateInterfaceJava(), sb, 1);
			sb.append("\n");
		}
	
		sb.append("}\n");
		return sb.toString();
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

	public void addImplementer(MetaClass mc) {
		implementers.add(mc);
	}

	public void pullUp() {
		if (implementers.isEmpty()) {
			// nothign to do
		} else {
			
			pullUpMethods();
			pullUpGetters();
		}
	}
	
	
	private void pullUpMethods() {
			ArrayList<Method> candidates = new ArrayList<Method>();
			candidates.addAll(implementers.get(0).getMetaMethods());
			
			// E.info("Pulling up - implementers: " + implementers.size() + " considerin " + candidates.size());
			
			HashSet<Method> kept = new HashSet<Method>();
			kept.addAll(candidates);
			
			for (MetaClass mc : implementers) {
				for (Method mm : candidates) {
					if (kept.contains(mm)) {
						if (mc.hasMethod(mm)) {
							// OK leave it 
						} else {
							kept.remove(mm);
						}
					}
				}
			}
			E.info("kept " + kept.size());
			
			for (Method mm : kept) {
				methods.add(mm);
			}
	}

	
	
	
	private void pullUpGetters() {
		ArrayList<AbstractGetter> candidates = new ArrayList<AbstractGetter>();
		candidates.addAll(implementers.get(0).getGetters());
		E.info("Pulling up - implementers: " + implementers.size() + " considerin " + candidates.size());
	
		HashSet<AbstractGetter> kept = new HashSet<AbstractGetter>();
		kept.addAll(candidates);
	
		for (MetaClass mc : implementers) {
			for (AbstractGetter mm : candidates) {
				if (kept.contains(mm)) {
				if (mc.hasGetter(mm)) {
					// OK leave it 
				} else {
					kept.remove(mm);
				}
			}
		}
		}
		E.info("kept " + kept.size());
	
		for (AbstractGetter mm : kept) {
			getters.add(mm);
		}
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

	 
	
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

}
