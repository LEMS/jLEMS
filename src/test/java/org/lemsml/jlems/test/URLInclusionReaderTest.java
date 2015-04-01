package org.lemsml.jlems.test;

import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Test;
import org.lemsml.jlems.core.sim.ContentError;
import org.lemsml.jlems.io.reader.URLInclusionReader;

public class URLInclusionReaderTest
{

	@Test
	public void testRead() throws MalformedURLException, ContentError
	{
		URL url = new URL("https://www.dropbox.com/s/qzqhdthror8435e/LEMS_NML2_Ex5_DetCell.xml?dl=1");
		URLInclusionReader urlInclusionReader = new URLInclusionReader(url);
		String lemsContent=urlInclusionReader.read();
		assertNotNull(lemsContent);
		assertFalse(lemsContent.isEmpty());
	}

}
