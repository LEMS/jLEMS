package org.lemsml.jlems.core.check;
   
import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.core.reader.LemsFactory;
import org.lemsml.jlems.core.type.Lems;
import org.lemsml.jlems.core.xml.XMLElement;
import org.lemsml.jlems.core.xml.XMLElementReader;
 
public class XMLReaderCheck {

 
	public void checkReadFromString() {
		String testString = "<Lems>"
				+ "<Dimension name=\"voltage\" m=\"1\" l=\"2\" t=\"-3\" i=\"-1\"/>"
				+ "<Unit symbol=\"mV\" dimension=\"voltage\" powTen=\"-3\"/>"
				+ "</Lems>";

		try {
		XMLElementReader elementReader = new XMLElementReader(testString);
	       
		XMLElement xel = elementReader.getRootElement();
		E.info("Read XML to " + xel); 
		
		LemsFactory lf = new LemsFactory();
		
		Lems lems = lf.buildLemsFromXMLElement(xel);
		
		
		E.info("Read:  " + lems);
		} catch (Exception ex) {
			E.report("can't read " + testString, ex);
		}
	}

	public static void main(String[] args) {
		XMLReaderCheck ct = new XMLReaderCheck();
		ct.checkReadFromString();

	}

}