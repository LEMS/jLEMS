package org.lemsml.jlems.xml;

import org.lemsml.jlems.logging.E;
 
 
public class XMLElementReaderCheck {

	
	
	public static void main(String[] argv) {
		String testString = "<Lems>\n" +
				 "<Dimension name=\"voltage\" m=\"1\" l=\"2\" t=\"-3\" i=\"-1\"/>\n" +
				 "<!-- and this is a comment\n" +
				 "on two lines....-->\n" +
		 		 "<Bare/>\n" +
				 "<Unit symbol=\"kV\" dimension=\"voltage\" powTen=\"-3\"/>\n" +
				 "<Unit symbol=\"mV\" dimension=\"voltage\" powTen=\"-3\"/>\n" +
				 "</Lems>\n";
		
		
		XMLElementReader xer = new XMLElementReader(testString);
		
		XMLElement xel = xer.getRootElement();
		
		E.info("Read " + xel.serialize());
		
		
	}
	
	
	
}
