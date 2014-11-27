package org.lemsml.jlems.schema;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;


import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.core.type.LemsCollection;
import org.lemsml.jlems.io.util.FileUtil;

// NB this is not used - it could be used to create a set of plain java classes for instantiating the data model 
// and operating on them as a preliminary stage before mapping to the core/type classes.
// or maybe as an API


public class ModelGenerator {


	private void generateModelSource(File destdir) throws IOException {
		LemsClasses lclasses = LemsClasses.getInstance();
		
		ArrayList<LemsClass> alc = lclasses.getClasses();
		
		for (LemsClass lc : alc) {
			String fnm = lc.getName() + ".java";
			File fcl = new File(destdir, fnm);
					
			String ssrc = generateSingleModelSource(lc);
			FileUtil.writeStringToFile(ssrc, fcl);
		
			E.info("Written " + fcl.getAbsolutePath());
		}
	}
	

	private String generateSingleModelSource(LemsClass lc) {
		StringBuilder sb = new StringBuilder();
		
		StringBuilder msb = new StringBuilder();
		
		sb.append("package org.lemsml.jlems.model;\n\n");
		Class c = lc.jclass;

		if (hasListFields(c)) {
			sb.append("import java.util.ArrayList;\n");
			sb.append("import java.util.List;\n");
		}
			
		sb.append("public class " + lc.getName() + " {\n\n");
		
		
		
		
		
		
		for (Field f : c.getFields()) {
			if (java.lang.reflect.Modifier.isPublic(f.getModifiers())) {
				
				if (java.lang.reflect.Modifier.isStatic(f.getModifiers())) {
					// not set from xml  
					
				} else {
					String dec = null;
				
				if (f.getType() == String.class) {
					dec = "String";
				} else if (f.getType() == double.class) {
					dec = "double";
				} else if (f.getType() == int.class) {
					dec = "int";
				} else if (f.getType() == boolean.class) {
					dec = "boolean";
				}
				
				if (dec != null) {
					
					String fname = f.getName();
					String capname = capitalize(f.getName());
					
					sb.append("    private " + dec + " " + f.getName() + ";\n");
				
					
					msb.append("    public void set" + capname + "(" + dec + " v) {\n");
					msb.append("        " + fname + " = v;\n");
					msb.append("    }\n\n");
					
					msb.append("    public " + dec + " get" + capname + "() {\n");
					msb.append("        return " + fname + ";\n");
					msb.append("    }\n\n");
					
				}
			}
		}
		}
		
		sb.append("\n");
		
		if (hasListFields(c)) {
			for (Field f : c.getFields()) {
				if (java.lang.reflect.Modifier.isPublic(f.getModifiers())) {
					if (f.getType() == LemsCollection.class) {
						String ccnm = getListClassName(f.getName());
			
						String lccnm = lowerCase(ccnm) + "s";
						
						sb.append("     ArrayList<" + ccnm + "> " + lccnm + " = new ArrayList<" + ccnm +">();\n");
					
						msb.append("    public List<" + ccnm + "> get" + capitalize(lccnm) + "() {\n");
						msb.append("      return " + lccnm +";\n");
						msb.append("    }\n\n");
					
						
						msb.append("    public void add" + ccnm + "(" + ccnm + " v){\n");
						msb.append("      " + lccnm + ".add(v);\n");
						msb.append("    }\n\n");
					}
				}
			}
		}
		
		sb.append(msb.toString());
		
		
		sb.append("}\n\n");
		
		String ret = sb.toString();
		return ret;
	}
	
	
		
	private String capitalize(String s) {
		String ret = s.substring(0, 1).toUpperCase() + s.substring(1, s.length());
		return ret;
	}
	
	
	private String lowerCase(String s) {
		String ret = s.substring(0, 1).toLowerCase() + s.substring(1, s.length());
		return ret;
	}
	 
	
	

	protected String getListClassName(String s) {
		String ret = s;
		
		// TODO no special cases
		if (s.equals("dynamicses")) {
			ret = "Dynamics";
			
		} else if (s.equals("attachmentses")) {
				ret = "Attachments";
		} else {
		if (ret.endsWith("s")) {
			ret = ret.substring(0, ret.length() - 1);
		}
		ret = ret.substring(0, 1).toUpperCase() + ret.substring(1, ret.length());
		}
		return ret;
	}
	
	
	private boolean hasListFields(Class<?> c) {
		boolean ret = false;
		for (Field f : c.getFields()) {
			if (java.lang.reflect.Modifier.isPublic(f.getModifiers())) {
				if (f.getType() == LemsCollection.class) {
					ret = true;
					break;
				}
			}
		}
		return ret;
	}
	
	
	private void appendListItemInstantiation(StringBuilder sb, Class<?> c) {
	sb.append("        for (XMLElement cel : xel.getXMLElements()) {\n");
	sb.append("            String xn = cel.getTag();\n\n");
	sb.append("            Object obj = instantiateFromXMLElement(cel);\n");
	
	sb.append("            if (xn.equals(\"UNUSED\")) {\n");
	
	for (Field f : c.getFields()) {
		if (java.lang.reflect.Modifier.isPublic(f.getModifiers())) {
			if (f.getType() == LemsCollection.class) {
				
				String ccnm = getListClassName(f.getName());
				
				sb.append("            } else if (obj instanceof " + ccnm + ") {\n");
				sb.append("                ret." + f.getName() + ".add((" + ccnm + ")obj);\n");
				
			}
		}
	}	
 
	sb.append("            } else {\n");
	sb.append("                E.warning(\"unrecognized element \" + cel);\n");
	sb.append("            }\n");
	sb.append("        }\n\n\n");
	}
	
	
	
	
	public static void main(String[] argv) throws IOException {
		ModelGenerator lmg = new ModelGenerator();
		
		File fmod = new File("src/org/lemsml/jlems/model");
		
		lmg.generateModelSource(fmod);
		
		 
	}

	
	
}
