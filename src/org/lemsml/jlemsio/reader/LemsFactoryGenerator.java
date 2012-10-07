package org.lemsml.jlemsio.reader;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;

import org.lemsml.jlems.logging.E;
import org.lemsml.jlems.type.LemsCollection;
import org.lemsml.jlemsio.util.FileUtil;

public class LemsFactoryGenerator {

	
	
	public LemsFactoryGenerator() {
		
	}
	

	private String generateJava() {
		LemsClasses lclasses = LemsClasses.getInstance();
		
		ArrayList<LemsClass> alc = lclasses.getClasses();
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("package org.lemsml.jlems.reader;\n\n");
		sb.append("import org.lemsml.jlems.type.*;\n");
		sb.append("import org.lemsml.jlems.type.dynamics.*;\n");
		sb.append("import org.lemsml.jlems.type.structure.*;\n");
		sb.append("import org.lemsml.jlems.type.simulation.*;\n\n");
		sb.append("import org.lemsml.jlems.type.procedure.*;\n\n");
		
		sb.append("import org.lemsml.jlems.xml.XMLElement;\n");
		sb.append("import org.lemsml.jlems.xml.XMLAttribute;\n");
		sb.append("import org.lemsml.jlems.logging.E;\n");
		
		sb.append("// NB this is generated code. Don't edit it. If there is a problem, fix the superclass,\n");
		sb.append("// the generator - org.jlems.jlemsio.LemsFactoryGenerator, or the class being instantiated.\n\n");
		sb.append("public class LemsFactory extends AbstractLemsFactory {\n\n\n");
	
		
		sb.append("    public Object instantiateFromXMLElement(XMLElement xel) {\n");
		sb.append("        Object ret = null;\n");
		sb.append("       String tag = xel.getName();\n\n");
		sb.append("        if (tag.equals(\"UNUSED\")) {\n");
	 	
		for (LemsClass lc : alc) {
			Class c = lc.jclass;
			sb.append("        } else if (tag.equals(\"" + c.getSimpleName() + "\")) {\n");
			sb.append("            ret = build" + c.getSimpleName() + "(xel);\n");
		}
	
		sb.append("        } else {\n");
		sb.append("            E.error(\"Unrecognized name \" + tag);\n");
		sb.append("        }\n");
		sb.append("        return ret;\n");
		sb.append("    }\n\n\n");
		
		
		for (LemsClass lc : alc) {
			sb.append(generateClassBuilderMethod(lc.jclass));
 		}
		
		sb.append("}\n");
		
		return sb.toString();
		
	}
	
	
	private String generateClassBuilderMethod(Class<?> c) {
		StringBuilder sb = new StringBuilder();
		
		String cnm = c.getSimpleName();
		sb.append("    private " + cnm + " build" + cnm + "(XMLElement xel) {\n");
		sb.append("        " + cnm + " ret = new " + cnm + "();\n\n");
		
		sb.append("        for (XMLAttribute xa : xel.getAttributes()) {\n");
		sb.append("            String xn = internalFieldName(xa.getName());\n");
		sb.append("            String xv = xa.getValue();\n\n");
		sb.append("            if (xn.equals(\"UNUSED\")) {\n");
		
		
		for (Field f : c.getFields()) {
			if (java.lang.reflect.Modifier.isPublic(f.getModifiers())) {
				
				if (java.lang.reflect.Modifier.isStatic(f.getModifiers())) {
					// not set from xml  
					
				} else {
					String exfn = null;
				
				if (f.getType() == String.class) {
					exfn = "parseString";
				} else if (f.getType() == double.class) {
					exfn = "parseDouble";
				} else if (f.getType() == int.class) {
					exfn = "parseInt";
				} else if (f.getType() == boolean.class) {
					exfn = "parseBoolean";
				}
				
				if (exfn != null) {
					sb.append("            } else if (xn.equals(\"" + f.getName() + "\")) {\n");
					sb.append("                ret." + f.getName() + " = " + exfn + "(xv);\n");
				}
				}
			}
		}
		
		
		
		sb.append("            } else {\n");
		sb.append("                E.warning(\"unrecognized attribute \" + xa);\n");
		sb.append("            }\n");
		sb.append("        }\n\n\n");
		
		
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
		
		
		sb.append("        return ret;\n");
		sb.append("    }\n\n");
		return sb.toString();
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
	
	
	
	
	public static void main(String[] argv) {
		LemsFactoryGenerator lfg = new LemsFactoryGenerator();
		
		String txt = lfg.generateJava();
 		
		File f = new File("src/org/lemsml/jlems/reader/LemsFactory.java");
		try {
			FileUtil.writeStringToFile(txt, f);
			E.info("Written " + f.getAbsolutePath());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	
}
