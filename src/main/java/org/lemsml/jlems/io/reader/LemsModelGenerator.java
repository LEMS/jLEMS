package org.lemsml.jlems.io.reader;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;

import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.core.type.LemsCollection;
import org.lemsml.jlems.io.util.FileUtil;

public class LemsModelGenerator
{

	private void generateModelSource(File destdir) throws IOException
	{
		LemsClasses lclasses = LemsClasses.getInstance();

		ArrayList<LemsClass> alc = lclasses.getClasses();

		for (LemsClass lc : alc)
		{
			String fnm = lc.getName() + "Doc.java";
			File fcl = new File(destdir, fnm);

			String ssrc = generateSingleModelSource(lc);
			FileUtil.writeStringToFile(ssrc, fcl);

			E.info("Written " + fcl.getAbsolutePath());
		}
	}

	private String generateSingleModelSource(LemsClass lc)
	{
		StringBuilder sb = new StringBuilder();

		StringBuilder msb = new StringBuilder();

		sb.append("package org.lemsml.jlems.model;\n\n");
		Class<?> c = lc.jclass;

		if (hasListFields(c))
		{
			sb.append("import java.util.ArrayList;\n");
			sb.append("import java.util.List;\n");
		}

		sb.append("public class " + lc.getName() + "Doc {\n\n\n");

		for (Field f : c.getFields())
		{
			if (java.lang.reflect.Modifier.isPublic(f.getModifiers()))
			{
				if (java.lang.reflect.Modifier.isStatic(f.getModifiers()))
				{
					// not set from xml
				}
				else
				{
					String dec = null;

					if (f.getType() == String.class)
					{
						dec = "String";
					}
					else if (f.getType() == double.class)
					{
						dec = "double";
					}
					else if (f.getType() == int.class)
					{
						dec = "int";
					}
					else if (f.getType() == boolean.class)
					{
						dec = "boolean";
					}

					if (dec != null)
					{

						String fname = "_"+f.getName();
						String capname = capitalize(f.getName());

						sb.append("\tprivate " + dec + " " + fname + ";\n\n");

						msb.append("\tpublic void set" + capname + "(" + dec + " v) {\n");
						msb.append("\t\t" + fname + " = v;\n");
						msb.append("\t}\n\n");

						msb.append("\tpublic " + dec + " get" + capname + "() {\n");
						msb.append("\t\treturn " + fname + ";\n");
						msb.append("\t}\n\n");
					}
				}
			}
		}

		if (hasListFields(c))
		{
			for (Field f : c.getFields())
			{
				if (java.lang.reflect.Modifier.isPublic(f.getModifiers()))
				{
					if (f.getType() == LemsCollection.class)
					{
						String ccnm = getListClassName(f.getName())+"Doc";
						String lccnm = "_"+lowerCase(ccnm) + "s";
						sb.append("\tprivate ArrayList<" + ccnm + "> " + lccnm + " = new ArrayList<" + ccnm + ">();\n\n");
						msb.append("\tprivate List<" + ccnm + "> get" + capitalize(lccnm.substring(1)) + "() {\n");
						msb.append("\t\treturn " + lccnm + ";\n");
						msb.append("\t}\n\n");
					}
				}
			}
		}

		sb.append(msb.toString());
		sb.append("}\n\n");
		String ret = sb.toString();
		return ret;
	}

	private String capitalize(String s)
	{
		String ret = s.substring(0, 1).toUpperCase() + s.substring(1, s.length());
		return ret;
	}

	private String lowerCase(String s)
	{
		String ret = s.substring(0, 1).toLowerCase() + s.substring(1, s.length());
		return ret;
	}

	protected String getListClassName(String s)
	{
		String ret = s;

		// TODO no special cases
		if (s.equals("dynamicses"))
		{
			ret = "Dynamics";
		}
		else if (s.equals("attachmentses"))
		{
			ret = "Attachments";
		}
		else
		{
			if (ret.endsWith("s"))
			{
				ret = ret.substring(0, ret.length() - 1);
			}
			ret = ret.substring(0, 1).toUpperCase() + ret.substring(1, ret.length());
		}
		return ret;
	}

	private boolean hasListFields(Class<?> c)
	{
		boolean ret = false;
		for (Field f : c.getFields())
		{
			if (java.lang.reflect.Modifier.isPublic(f.getModifiers()))
			{
				if (f.getType() == LemsCollection.class)
				{
					ret = true;
					break;
				}
			}
		}
		return ret;
	}

	private void appendListItemInstantiation(StringBuilder sb, Class<?> c)
	{
		sb.append("\t\tfor (XMLElement cel : xel.getXMLElements()) {\n");
		sb.append("\t\t\tString xn = cel.getTag();\n\n");
		sb.append("\t\t\tObject obj = instantiateFromXMLElement(cel);\n");

		sb.append("\t\t\tif (xn.equals(\"UNUSED\")) {\n");

		for (Field f : c.getFields())
		{
			if (java.lang.reflect.Modifier.isPublic(f.getModifiers()))
			{
				if (f.getType() == LemsCollection.class)
				{

					String ccnm = getListClassName(f.getName());

					sb.append("\t\t\t} else if (obj instanceof " + ccnm + ") {\n");
					sb.append("\t\tret." + f.getName() + ".add((" + ccnm + ")obj);\n");

				}
			}
		}

		sb.append("\t\t\t} else {\n");
		sb.append("\t\t\t\tE.warning(\"unrecognized element \" + cel);\n");
		sb.append("\t\t\t}\n");
		sb.append("\t\t}\n\n\n");
	}

	public static void main(String[] argv) throws IOException
	{
		LemsModelGenerator lmg = new LemsModelGenerator();

		File fmod = new File("./target/generated-sources/src/org/lemsml/jlems/model");
		File parent = fmod.getParentFile();
		if(!parent.exists() && !parent.mkdirs()){
		    throw new IllegalStateException("Couldn't create dir: " + parent);
		}
		fmod.mkdirs();
		lmg.generateModelSource(fmod);

	}

}
