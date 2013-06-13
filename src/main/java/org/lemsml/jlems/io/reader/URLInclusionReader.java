package org.lemsml.jlems.io.reader;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.lemsml.jlems.core.sim.AbstractInclusionReader;
import org.lemsml.jlems.core.sim.ContentError;
import org.lemsml.jlems.io.util.FileUtil;

public class URLInclusionReader extends AbstractInclusionReader
{

	private final URL _rootURL;
	private String _libraryRoot = null;

	public URLInclusionReader(URL url)
	{
		super();
		_rootURL = url;
	}

	public String getRelativeContent(String type, String inclusion) throws ContentError
	{
		String ret = "";
		String urlString = null;
		inclusion = inclusion.replace("\"", "");
		try
		{
			if(type.equals(FILE))
			{
				if(_libraryRoot == null)
				{
					throw new ContentError("No potential library root found, it's not possible to process file inclusions!");
				}
				else
				{
					if (inclusion.charAt(0)!='/')
					{
						inclusion="/"+inclusion;
					}
					urlString = _libraryRoot + inclusion;
				}
			}
			else if(type.equals(URL))
			{
				urlString=inclusion;
				if(urlString.endsWith(".xml"))
				{
					//Assuming one inclusion for all component types used
					_libraryRoot = urlString.substring(0, urlString.lastIndexOf("/"));
				}
			}
			URL url = new URL(urlString);
			ret = FileUtil.readStringFromURL(url);

		}
		catch(MalformedURLException e)
		{
			throw new ContentError("Problem reading from URL: " + urlString, e);
		}
		catch(IOException e)
		{
			throw new ContentError("Problem reading from URL: " + urlString, e);
		}
		return ret;
	}

	public String getRootContent() throws ContentError
	{
		try
		{
			return FileUtil.readStringFromURL(_rootURL);
		}
		catch(IOException ex)
		{
			throw new ContentError("Problem reading from URL: " + _rootURL.getPath(), ex);
		}
	}

}
