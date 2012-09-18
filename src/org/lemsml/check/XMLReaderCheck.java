package org.lemsml.check;
  
import org.lemsml.io.FormatException;
import org.lemsml.type.Lems;
import org.lemsml.util.ContentError;
import org.lemsml.util.E;
import org.lemsml.xml.BuildException;
import org.lemsml.xml.ParseException;
import org.lemsml.xml.XMLException;
import org.lemsml.xml.XMLReader;
 
public class XMLReaderCheck {

	public XMLReaderCheck() {
	}

 
	public void testReadFromString() {
		try {
		String testString = "<Lems>"
				+ "<Dimension name=\"voltage\" m=\"1\" l=\"2\" t=\"-3\" i=\"-1\"/>"
				+ "<Unit symbol=\"mV\" dimension=\"voltage\" powTen=\"-3\"/>"
				+ "</Lems>";

		XMLReader xmlReader = new XMLReader();
	    xmlReader.addSearchPackage(Lems.class.getPackage());
          
		Object obj = xmlReader.readFromString(testString);

		E.info("Read:  " + obj);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void main(String[] args) {
		XMLReaderCheck ct = new XMLReaderCheck();
		ct.testReadFromString();

	}

}