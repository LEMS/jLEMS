package org.lemsml.jlems.core.api;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

import org.lemsml.jlems.core.api.interfaces.ILEMSDocumentReader;
import org.lemsml.jlems.core.reader.LemsFactory;
import org.lemsml.jlems.core.sim.ContentError;
import org.lemsml.jlems.core.type.Lems;
import org.lemsml.jlems.core.xml.XMLElement;
import org.lemsml.jlems.core.xml.XMLElementReader;

public class LEMSDocumentReader implements ILEMSDocumentReader
{

	@Override
	public Lems readModel(URL modelURL) throws IOException, ContentError
	{
		String modelString = new Scanner(modelURL.openStream(), "UTF-8").useDelimiter("\\A").next();

		// TODO tmp - make reader cope without extra spaces
		XMLElementReader exmlr = new XMLElementReader(modelString + "    ");
		XMLElement xel = exmlr.getRootElement();

		LemsFactory lf = new LemsFactory();
		Lems lems = lf.buildLemsFromXMLElement(xel);
		return lems;
	}

}
