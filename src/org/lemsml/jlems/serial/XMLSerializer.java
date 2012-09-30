package org.lemsml.jlems.serial;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.HashSet;

import org.lemsml.jlems.io.ExportNameMapper;
import org.lemsml.jlems.type.Attribute;
import org.lemsml.jlems.type.BodyValued;
import org.lemsml.jlems.type.Component;
import org.lemsml.jlems.type.Inheritor;
import org.lemsml.jlems.type.LemsCollection;
import org.lemsml.jlems.util.ContentError;
import org.lemsml.jlems.util.E;
import org.lemsml.jlems.xml.XMLElement;

public class XMLSerializer {

	
	ExportNameMapper exportMap;
	
	
	static HashMap<String, String> defaultAttributeMap = new HashMap<String, String>();

	{
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
	
	public static String serialize(Object obj, ExportNameMapper map) throws ContentError {
		 XMLSerializer xs = newInstance();
		 xs.setExportMap(map);
		 String ret = xs.writeObject(obj);
		 return ret;
	}
	
	private void setExportMap(ExportNameMapper map) {
		exportMap = map;
	}

	public static String getSerialization(Object ob) throws ContentError {
		return newInstance().writeObject(ob);
	}

	public String writeObject(Object obj) throws ContentError {
		XMLElement xe = makeXMLElement(null, null, null, obj);
		return xe.toXMLString("");
	}

	 
	public XMLElement makeXMLElement(XMLElement dest, Object parent, String knownAs, Object ob) throws ContentError {
		XMLElement ret = null;

		if (ob instanceof String) {
			ret = new XMLElement("String");
			ret.setBody((String) ob);

			
		} else if (ob instanceof Component) {
			// TODO knownAs intead of null? 
			ret = makeComponentElement(null, (Component)ob);
			
			
		} else if (ob instanceof LemsCollection) {
 			LemsCollection<?> lc = (LemsCollection<?>) ob;
			if (lc.size() > 0) {
				ret = new WrapperElement("anon");
				for (Object child : lc) {
					if (parent instanceof Inheritor && ((Inheritor)parent).inherited(child)) {
						// we inherited it from the parent of the object that contains the list
						// dont need to write it out again 
						
					} else {					
						XMLElement che = makeXMLElement(dest, null, null, child);
						if (che != null) {
							if (exportMap != null && exportMap.pushesDown(child.getClass())) {
								String rpd = exportMap.getPushDown(child.getClass());
								if (ret.hasElement(rpd)) {
									ret.getElement(rpd).add(che);
									
								} else if (dest != null && dest.hasElement(rpd)) {
									dest.getElement(rpd).add(che);
	 								
								} else {
									XMLElement xe = new XMLElement(rpd);
									xe.add(che);
									ret.add(xe);
								}
								
								
							} else {
								ret.add(che);
							}
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
			if (exportMap != null) {
				stag = exportMap.getExportElementName(ob.getClass());
				if (stag == null) {
					stag = tag;
				}
			}
			ret = new XMLElement(stag);

			if (exportMap != null && exportMap.hasAutoAttributes(ob.getClass())) {
				ret.addAttributes(exportMap.getAutoAttributes(ob.getClass()));
				 
			}
			
			if (ob instanceof BodyValued) {
				String sb = ((BodyValued)ob).getBodyValue();
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
					E.warning("failed to get field " + fieldName + " in  " + ob);
				}
				if (Modifier.isFinal(flds[i].getModifiers())) {
					wk = null;
					// not settable
				} else if (Modifier.isPublic(flds[i].getModifiers())) {
					if (fieldName.startsWith("p_") || fieldName.startsWith("r_")) {
						// leave it out all the same - local private and
						// reference elements

					} else {
						if (exportMap != null) {
							if (exportMap.elementizesAttribute(ob.getClass(), fieldName)) {
								String eltName = exportMap.getAttributeToElementName(ob.getClass(), fieldName);
								fieldName = null;
								
								String sval = "" + wk;
								sval = sval.trim();
								
				 				
								// TODO this is a bit lazy - nesting two with a magic string
								if (eltName.indexOf("/") > 0) {
									int ps = eltName.indexOf("/");
									XMLElement xe1 = new XMLElement(eltName.substring(0, ps));
									XMLElement xe2 = new XMLElement(eltName.substring(ps+1, eltName.length()));
									ret.add(xe1);
									xe1.add(xe2);
									xe2.setBody(sval);
									 
								} else {
									XMLElement xe = new XMLElement(eltName);
									xe.setBody(sval);
									ret.add(xe);
								}
									 
								
							} else {
								fieldName = exportMap.getExportAttributeName(ob.getClass(), fieldName);
							}
							
						
						}
						
						// export map may set it null if it shouldn't be exported
						if (fieldName != null) {
						
							Class<?> cls = ob.getClass();
							
						if (wk instanceof Double) {
							setAttribute(cls, ret, fieldName, "" + ((Double) wk).doubleValue());

						} else if (wk instanceof Integer) {
							setAttribute(cls, ret, fieldName, "" + ((Integer) wk).intValue());

						} else if (wk instanceof Boolean) {
							setAttribute(cls, ret, fieldName, (((Boolean) wk).booleanValue() ? "true" : "false"));

						} else if (wk instanceof String) {
							setAttribute(cls, ret, fieldName, (String) wk);

						} else if (wk instanceof double[]) {
							setAttribute(cls, ret, fieldName, makeString((double[]) wk));

						} else if (wk instanceof int[]) {
							setAttribute(cls, ret, fieldName, makeString((int[]) wk));

						} else if (wk instanceof boolean[]) {
							setAttribute(cls, ret, fieldName, makeString((boolean[]) wk));

						} else if (wk instanceof String[]) {
							setAttribute(cls, ret, fieldName, makeString((String[]) wk));

						} else if (wk != null) {

							// should child elements be known by the field name in the parent, or their element type?
//							XMLElement xe = makeXMLElement(ob, fieldName, wk);
							XMLElement xe = makeXMLElement(ret,ob, null, wk);
							
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

	private void setAttribute(Class<?> cls, XMLElement ret, String fieldName, String avalue) {
		String value = avalue;
		String anm = fieldName;
		if (exportMap != null) {
			String sv = exportMap.getMappedAttributeValue(cls, anm, value);
 			if (sv != null) {
				value = sv;
			}
		}
		
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
		
		// ParamValues are the parsed ones - could put them back in with units added?
		//for (ParamValue pv : cpt.getParamValues()) {
		//	ret.addAttribute(pv.getName(), pv.parseValue(s));
		//}
		
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
