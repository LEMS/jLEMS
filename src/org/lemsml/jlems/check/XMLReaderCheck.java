package org.lemsml.jlems.check;
  
import org.lemsml.jlems.io.FormatException;
import org.lemsml.jlems.reader.LemsFactory;
import org.lemsml.jlems.type.Lems;
import org.lemsml.jlems.util.ContentError;
import org.lemsml.jlems.util.E;
import org.lemsml.jlems.xml.BuildException;
import org.lemsml.jlems.xml.ParseException;
import org.lemsml.jlems.xml.XMLElement;
import org.lemsml.jlems.xml.XMLException;
import org.lemsml.jlemsio.xmlio.ElementXMLReader;
import org.lemsml.jlemsio.xmlio.XMLReader;
 
public class XMLReaderCheck {

	public XMLReaderCheck() {
	}

 
	public void testReadFromString() {
		try {
		String testString = "<Lems>"
				+ "<Dimension name=\"voltage\" m=\"1\" l=\"2\" t=\"-3\" i=\"-1\"/>"
				+ "<Unit symbol=\"mV\" dimension=\"voltage\" powTen=\"-3\"/>"
				+ "</Lems>";

		ElementXMLReader elementReader = new ElementXMLReader();
	       
		Object obj = elementReader.read(testString);

		XMLElement xel = (XMLElement)obj;
		E.info("Read XML to " + xel); 
		
		LemsFactory lf = new LemsFactory();
		
		Lems lems = lf.buildLemsFromXMLElement(xel);
		
		
		E.info("Read:  " + lems);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void main(String[] args) {
		XMLReaderCheck ct = new XMLReaderCheck();
		ct.testReadFromString();

	}

}