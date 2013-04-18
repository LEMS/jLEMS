package org.lemsml.jlems.core.api;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.lemsml.jlems.core.api.interfaces.ILEMSDocument;
import org.lemsml.jlems.core.api.interfaces.ILEMSDocumentReader;
import org.lemsml.jlems.core.reader.LemsFactory;
import org.lemsml.jlems.core.sim.ContentError;
import org.lemsml.jlems.core.type.Lems;
import org.lemsml.jlems.core.xml.XMLElement;
import org.lemsml.jlems.core.xml.XMLElementReader;
import org.lemsml.jlems.io.reader.FileInclusionReader;

public class LEMSDocumentReader implements ILEMSDocumentReader
{

	@Override
	public ILEMSDocument readModel(URL modelURL) throws IOException, ContentError
	{
		File f = new File(modelURL.getFile());
		FileInclusionReader fir = new FileInclusionReader(f);
		//FIXME: If the included files will be passed as URL then we'll need something like a URLInclusionReader
		String modelString = fir.read();

		// TODO tmp - make reader cope without extra spaces
		XMLElementReader exmlr = new XMLElementReader(modelString + "    ");
		XMLElement xel = exmlr.getRootElement();

		LemsFactory lf = new LemsFactory();
		Lems lems = lf.buildLemsFromXMLElement(xel);
		return lems;
	}

}
