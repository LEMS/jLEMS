package org.lemsml.jlems.core.xml;

import org.lemsml.jlems.core.logging.E;

public class XMLElementReader {

	String srcString;
	int totalLength;
	
	String[] lines;  
	
	XMLElement rootElement;
	
	int[] lineOffsets;
	int iwk;
	
	
	public XMLElementReader(String src) {
		srcString = src;
		lines = srcString.split("\n");
		totalLength = srcString.length();
	}
	
	
	public XMLElement getRootElement() {
		if (rootElement == null) {
			parseSrc();
		}
		return rootElement;
	}
	
	
	private void readLineOffsets() {
		String[] bits = srcString.split("\n");
		lineOffsets = new int[bits.length];
		int co = 0;
		for (int i = 0; i < bits.length; i++) {
			lineOffsets[i] = co;
			co += bits[i].length() + 1;
		}
	}
	
	
	private void parseSrc() {
		readLineOffsets();
		
 		
		XMLElement container = new XMLElement("container");
		
		iwk = 0;
		try {
			readChildren(container);
		} catch (Exception ex) {
			E.info("Failed to read " + ex);
		}
			
			if (container.getXMLElements().size() == 1) {
				rootElement = container.getXMLElements().get(0);
			
			} else {
				E.error("read failed " + container.getXMLElements().size());
			}
		
	}


	private void readChildren(XMLElement parent) throws XMLException {
		while (iwk < totalLength) {
		int inx = inext("<");
		if (inx < 0) {
			// no element to read - just return
	 		break;
			
		} else {
			if (inx > iwk) {
				String stxt = srcString.substring(iwk, inx).trim();
				if (stxt.length() > 0) {
					parent.appendBodyText(stxt);
 				}
			}
			iwk = inx;
						
			if (nextString(2).equals("</")) {
				iwk += 2;
				String tg = parent.getTag();
				String sctag = nextString(tg.length() + 1);
				if (sctag.equals(tg + ">")) {
					// OK 
					iwk += tg.length() + 2; 
					break;
					
				} else {
					throw new XMLException("Non matching close tag at " + printLine() + "\n" +
							"tag is " + tg + " closer is " + sctag);
				}
				
			} else if (nextString(4).equals("<!--")) {
				int icc = inext("-->");
				if (icc > 0) {
			
					// E.info("extracted comment " + srcString.substring(iwk, icc + 3));
					iwk = icc + 3;
					
				} else {
					throw new XMLException("No close to comment? " + printLine());
				}
				
				
			} else {
			
			
			int isp = inextHigh(" ");
			int icb = inextHigh(">");
			int ice = inextHigh("/>");
			
			
			if (ice < isp && ice < icb) {
				// No children, no attributes
				String enm = srcString.substring(inx + 1, ice);			 
				XMLElement child = new XMLElement(enm);
	 			iwk = ice + 2;
				parent.add(child);
				
				
			} else if (icb < isp && icb < ice) {
				// has children, but no attributes
				String enm = srcString.substring(inx + 1, icb);		
				XMLElement child = new XMLElement(enm);
				iwk = icb + 1;
				parent.add(child);
				readChildren(child);
				
			} else if (isp < icb && isp < ice) {
				// 
				String enm = srcString.substring(inx + 1, isp);			 
 				XMLElement child = new XMLElement(enm);
				parent.add(child);
				iwk = isp + 1;
				
				int jca = inextHigh("/>");
				int jce = inextHigh(">");
				if (jca < jce) {
					// closed without any children or body text
					String attstring = srcString.substring(iwk, jca);
					iwk = jca + 2;
					addAttributes(child, attstring);
 					
				} else {
					String attstring = srcString.substring(iwk, jce);
					iwk = jce + 1;
					addAttributes(child, attstring);
					readChildren(child);
				}
			}
			}
		}
		}
	}
	
	
	
	private void addAttributes(XMLElement elt, String attstring) {
		try {
			parseAttributes(elt, attstring);
		} catch (XMLException ex) {
			E.warning("can't parse: " + attstring + " " + ex);
		}
	}
	
	
	private void parseAttributes(XMLElement elt, String attstring) throws XMLException {
	    String rest = attstring;
	    rest = rest.trim();
	    
	    while (rest.length() > 0) {
	    	int isp = inextHighIn(" ", rest);
	    	int ise = inextHighIn("=", rest);
	    	String nm = "";
	    	if (isp < ise) {
	    		nm = rest.substring(0, isp);
	    		rest = rest.substring(isp, rest.length());
	    		rest = rest.trim();
	    		
	    	} else if (ise < isp) {
	    		nm = rest.substring(0, ise);
	    		rest = rest.substring(ise, rest.length());
	 	    		
	    	} else {
	    		// neither present?
	    		throw new XMLException("expect space or =?");
	      	}
	    	
	    	rest = rest.substring(1, rest.length()).trim();

 	    	
	    	if (rest.indexOf("\"") == 0) {
	    		// OK
	    		int icb = rest.indexOf("\"", 1);
	    		if (icb > 0) {
	    			String v = rest.substring(1, icb);
	    			rest = rest.substring(icb + 1, rest.length()).trim();
	    			elt.addAttribute(nm, v);
	     		} else {
	    			throw new XMLException("no matching quotes");
	    		}
	    		
	    	} else {
	    		throw new XMLException("expecting opening quote at " + rest);
	    	}
	    }
	}
	
	
	
	private String printLine() {
		int ilin = bisectFind(lineOffsets, iwk);
		String ret=  lines[ilin];
		return ret;
	}
	
	
	private int bisectFind(int[] ia, int tgt) {
		int a = 0;
		int b = ia.length - 1;
		while (b - a > 1) {
			int m = (a + b) /2;
			if (ia[m] < tgt) {
				a = m;
			} else {
				b = m;
			}
		}
		return a;
	}
	
	
	private String nextString(int n) {
		String ret = "";
		if (iwk + n < totalLength) {
			ret = srcString.substring(iwk, iwk + n);
		} else {
			ret = "";
		}
		return ret;
	}
	
			
	private int inext(String str) {
		int ret = srcString.indexOf(str, iwk);
		return ret;
	}
			
	private int inextHigh(String str) {
		int ret = srcString.indexOf(str, iwk);
		if (ret < 0) {
			ret = Integer.MAX_VALUE;
		}
		return ret;
	}
	
	private int inextHighIn(String s, String src) {
		int ret = src.indexOf(s);
		if (ret < 0) {
			ret = Integer.MAX_VALUE;
		}
		return ret;
	}


	public static String deComment(String stxt) throws XMLException {
		 StringBuilder sb= new StringBuilder();
		 int ipos = 0;
		 while (true) {			 
			 int inc = stxt.indexOf("<!--", ipos);
			 if (inc > 0) {
				 sb.append(stxt.substring(ipos, inc));
				 int ie = stxt.indexOf("-->", inc + 3);
				 if (ie > 0) {
					 ipos = ie + 3;
				 } else {
					 throw new XMLException("Non matching comment around " + inc + " " + stxt);
				 }
			 } else {
				 break;
			 }
		 }
		 sb.append(stxt.substring(ipos, stxt.length()));
		 return sb.toString();
	}


	public static String deSpace(String stxt) {
		String ret = stxt.replaceAll("\\r|\\n|\\t| ", "");
		return ret;
	}
	
}
