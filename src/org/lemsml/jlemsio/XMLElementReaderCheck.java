package org.lemsml.jlemsio;

import java.io.File;

import org.lemsml.jlems.util.E;
import org.lemsml.jlems.xml.XMLElement;
import org.lemsml.jlems.xml.XMLElementReader;
 

public class XMLElementReaderCheck {

	
	
	public static void main(String[] argv) {
		
		File fdir = new File("examples");
		File fex = new File(fdir, "example1.xml");
		
		try {
		FileInclusionReader fir = new FileInclusionReader(fex);
		
		String testString = fir.read();
	 
		E.info("Reading from string " + testString.hashCode());
		
	//	E.info("read : " + testString.substring(0, 2000));
		
		XMLElementReader xer = new XMLElementReader(testString);
		
		XMLElement xel = xer.getRootElement();
		
	//	E.info("Read " + xel.toXMLString(""));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
	}
	
	
	
}
