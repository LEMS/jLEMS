package org.lemsml.jlems.core.xml;


// read XMLElements from compact text

public class TextElementReader {

	String srcString;
	int totalLength;
	
	String[] lines;  
	
	XMLElement rootElement;
	
	int[] lineOffsets;
	int iwk;
	
	
	public TextElementReader(String src) {
		srcString = src;
		lines = srcString.split("\n");
		totalLength = srcString.length();
	}
	
	
	public XMLElement getRootElement() throws XMLException {
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
	
	
	private void parseSrc() throws XMLException {
		readLineOffsets();
		XMLElement container = new XMLElement("Lems");
		
		iwk = 0;
	 
			readChildren(0, container);
		 
		rootElement = container;
	}


	private void readChildren(int indent, XMLElement parent) throws XMLException {
		while (true) {
			String line = nextNonEmptyLine();
 			if (line == null) {
				// reached end of file
				break;
			}
			
			int ind = getIndent(line);
 			
			if (ind < indent) {
				// finished this element - current line belongs to a parent
				iwk -= 1;
				break;
				
			} else if (ind > indent) {
				throw new XMLException("Found mismatched indentation " + indent + " " + ind);
			
			} else {	
				XMLElement xelt = readElementAndAttributes();
				parent.add(xelt);
				int nextInd = nextIndent();
				if (nextInd > indent) {
					readChildren(nextInd, xelt);
				}
			}
		}
	}
	
	
	private String nextNonEmptyLine() {
		String ret=  null;
		iwk += 1;
		
		while (true) {
			if (iwk >= lines.length) {
				break;
			}
			if (lines[iwk].trim().length() > 0) {
				ret = lines[iwk];
				break;
			}
			iwk += 1;
		}
		return ret;
	}
	
	
	private int getIndent(String line) {
		int ret = line.indexOf(line.trim());
		return ret;
	}
	
	
	private int nextIndent() {
		int ret = -1;
		String lin = nextNonEmptyLine();
		if (lin != null) {
			ret = getIndent(lin);
		}
		if (ret >= 0) {
			iwk -= 1;
		}
		return ret;
	}
	
	 
	private XMLElement readElementAndAttributes() throws XMLException {
		String line = lines[iwk];
		line = line.trim();
		String[] parts = line.split(",");
		String p0 = parts[0].trim();
		String[] bits = p0.split(" ");
		
		XMLElement ret = new XMLElement(bits[0]);
		if (bits.length > 1) {
			ret.addAttribute("name", bits[1]);
			ret.addAttribute("id", bits[1]);
		}
		
		for (int inv = 1; inv < parts.length; inv++) {
			String[] abits = parts[inv].trim().split("=");
			if (abits.length == 2) {
				ret.addAttribute(abits[0].trim(), abits[1].trim());
			} else {
				throw new XMLException("Can't parse line " + line);
			}
		}
		return ret;
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
