package org.lemsml.jlems.io.xmlio;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.HashSet;

import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.core.serial.WrapperElement;
import org.lemsml.jlems.core.sim.ContentError;
import org.lemsml.jlems.core.type.Attribute;
import org.lemsml.jlems.core.type.BodyValued;
import org.lemsml.jlems.core.type.Component;
import org.lemsml.jlems.core.type.Inheritor;
import org.lemsml.jlems.core.type.LemsCollection;
import org.lemsml.jlems.core.xml.XMLElement;

public class XMLSerializer {

	static HashMap<String, String> defaultAttributeMap = new HashMap<String, String>();

	static {
		defaultAttributeMap.put("eXtends", "extends");
	}

	HashMap<String, String> attributeMap = defaultAttributeMap;

	boolean conciseTags;
	boolean quoteStrings;

	public XMLSerializer() {
		conciseTags = true;
		quoteStrings = true;
	}

	public void setConciseTags(boolean b) {
		conciseTags = b;
	}

	public void setQuoteStrings(boolean b) {
		quoteStrings = b;
	}

	public static XMLSerializer newInstance() {
		return new XMLSerializer();
	}

	public static String serialize(Object ob) throws ContentError {
		return getSerialization(ob);
	}

	 

	public static String getSerialization(Object ob) throws ContentError {
		return newInstance().writeObject(ob);
	}

	public String writeObject(Object obj) throws ContentError {
		XMLElement xe = makeXMLElement(null, null, null, obj);
		return xe.serialize();
	}

	public XMLElement makeXMLElement(XMLElement dest, Object parent, String knownAs, Object ob) throws ContentError {
		XMLElement ret = null;

		if (ob instanceof String) {
			ret = new XMLElement("String");
			ret.setBody((String) ob);

		} else if (ob instanceof Component) {
			// TODO knownAs intead of null?
			ret = makeComponentElement(null, (Component) ob);

		} else if (ob instanceof LemsCollection) {
			LemsCollection<?> lc = (LemsCollection<?>) ob;
			if (lc.size() > 0) {
				ret = new WrapperElement("anon");
				for (Object child : lc) {
					if (parent instanceof Inheritor && ((Inheritor) parent).inherited(child)) {
						// we inherited it from the parent of the object that
						// contains the list
						// dont need to write it out again

					} else {
						XMLElement che = makeXMLElement(dest, null, null, child);
						if (che != null) {
							 
								ret.add(che);
						 
						}
					}
				}

			}

		} else {
			String tag = "error";
			if (knownAs != null) {
				tag = knownAs;
			} else {

				tag = ob.getClass().getName();

				if (conciseTags) {
					int ilast = tag.lastIndexOf(".");
					if (ilast >= 0) {
						tag = tag.substring(ilast + 1, tag.length());
					}
				}
			}

			String stag = tag;
			 
			ret = new XMLElement(stag);
 

			if (ob instanceof BodyValued) {
				String sb = ((BodyValued) ob).getBodyValue();
				if (sb != null) {
					ret.setBody(sb);
				}
			}

			Field[] flds = ob.getClass().getFields();

			for (int i = 0; i < flds.length; i++) {
				String fieldName = flds[i].getName();
				Object wk = null;
				try {
					wk = flds[i].get(ob);
				} catch (Exception e) {
					E.warning("failed to get field " + fieldName + " in " + ob);
				}
				if (Modifier.isFinal(flds[i].getModifiers())) {
					wk = null;
					// not settable
				} else if (Modifier.isPublic(flds[i].getModifiers())) {
					if (fieldName.startsWith("p_") || fieldName.startsWith("r_")) {
						// leave it out all the same - local private and
						// reference elements

					} else {
						 

						// export map may set it null if it shouldn't be
						// exported
						if (fieldName != null) {
							
							if (wk instanceof Double) {
								setAttribute(ret, fieldName, "" + ((Double) wk).doubleValue());

							} else if (wk instanceof Integer) {
								setAttribute(ret, fieldName, "" + ((Integer) wk).intValue());

							} else if (wk instanceof Boolean) {
								setAttribute(ret, fieldName, (((Boolean) wk).booleanValue() ? "true" : "false"));

							} else if (wk instanceof String) {
								setAttribute(ret, fieldName, (String) wk);

							} else if (wk instanceof double[]) {
								setAttribute(ret, fieldName, makeString((double[]) wk));

							} else if (wk instanceof int[]) {
								setAttribute(ret, fieldName, makeString((int[]) wk));

							} else if (wk instanceof boolean[]) {
								setAttribute(ret, fieldName, makeString((boolean[]) wk));

							} else if (wk instanceof String[]) {
								setAttribute(ret, fieldName, makeString((String[]) wk));

							} else if (wk != null) {

								// should child elements be known by the field
								// name in the parent, or their element type?
								// XMLElement xe = makeXMLElement(ob, fieldName,
								// wk);
								XMLElement xe = makeXMLElement(ret, ob, null, wk);

								if (xe == null) {
									// must have been something we don't want
								} else if (xe instanceof WrapperElement) {
									for (XMLElement sub : xe.getElements()) {
										ret.add(sub);
									}

								} else {
									ret.add(xe);
								}
							}
						}
					}
				}
			}
		}
		return ret;

	}

	private void setAttribute(XMLElement ret, String fieldName, String avalue) {
		String value = avalue;
		String anm = fieldName;
		 
		if (attributeMap.containsKey(anm)) {
			anm = attributeMap.get(anm);
		}
		ret.addAttribute(anm, value);
	}

	private XMLElement makeComponentElement(String tagName, Component cpt) {
		String typeName = cpt.getTypeName();
		XMLElement ret = null;

		if (tagName == null) {
			ret = new XMLElement(typeName);
		} else {
			ret = new XMLElement(tagName);
			ret.addAttribute("type", typeName);
		}

		String enm = cpt.getExtendsName();
		if (enm != null) {
			ret.addAttribute("extends", enm);
		}

		if (cpt.getID() != null) {
			ret.addAttribute("id", cpt.getID());
		}

		// ParamValues are the parsed ones - could put them back in with units
		// added?
		// for (ParamValue pv : cpt.getParamValues()) {
		// ret.addAttribute(pv.getName(), pv.parseValue(s));
		// }

		HashSet<String> atts = new HashSet<String>();
		HashMap<String, Component> refHM = cpt.getRefComponents();
		if (refHM != null) {
			for (String s : refHM.keySet()) {
				Component tgt = refHM.get(s);
				ret.addAttribute(s, tgt.getID());
				atts.add(s);
			}
		}

		for (Attribute att : cpt.getAttributes()) {
			String sa = att.getName();
			if (!atts.contains(sa)) {
				ret.addAttribute(sa, att.getValue().toString());
			}
		}

		for (Component cch : cpt.getStrictChildren()) {
			ret.add(makeComponentElement(null, cch));
		}

		HashMap<String, Component> chm = cpt.getChildHM();
		if (chm != null) {
			for (String s : chm.keySet()) {
				Component c = chm.get(s);
				XMLElement xe = makeComponentElement(s, c);
				ret.add(xe);
			}
		}

		return ret;
	}

	private String makeString(double[] wk) {
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (double d : wk) {
			if (!first) {
				sb.append(",");
			}
			sb.append("" + d);
			first = false;
		}
		return sb.toString();
	}

	private String makeString(int[] wk) {
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (int d : wk) {
			if (!first) {
				sb.append(",");
			}
			sb.append("" + d);
			first = false;
		}
		return sb.toString();
	}

	// lots to go wrong here... TODO
	private String makeString(String[] wk) {
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (String d : wk) {
			if (!first) {
				sb.append(",");
			}
			sb.append("" + d);
			first = false;
		}
		return sb.toString();
	}

	private String makeString(boolean[] wk) {
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (boolean b : wk) {
			if (!first) {
				sb.append(",");
			}
			sb.append("" + (b ? "true" : "false"));
			first = false;
		}
		return sb.toString();
	}

}