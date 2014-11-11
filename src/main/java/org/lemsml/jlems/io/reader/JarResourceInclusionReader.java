package org.lemsml.jlems.io.reader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.lemsml.jlems.core.sim.AbstractInclusionReader;
import org.lemsml.jlems.core.sim.ContentError;
import org.lemsml.jlems.io.util.FileUtil;
import org.lemsml.jlems.io.util.JUtil;

/*
 * Will be replaced by features in api branch!
 */
public class JarResourceInclusionReader extends AbstractInclusionReader {

    private File rootFile;
    
    private static ArrayList<File> searchDirs = new ArrayList<File>();
    private static ArrayList<String> searchPathsInJar = new ArrayList<String>();

    private File prefDir;
    
    private String content;

    
    public JarResourceInclusionReader(String content) {
    	this.content = content;
    	rootFile = null;
    	prefDir = null;

        searchDirs.add(new File(System.getProperty("user.dir")));
    }
        
    public JarResourceInclusionReader(File f) {
    	super();
    	rootFile = f;
        File rootDir = rootFile.getParentFile();
        
        if (rootDir != null) {
            searchDirs.add(rootDir);
            //searchDirs.add(rootDir.getParentFile());
        }
        prefDir = rootDir;

        searchDirs.add(new File(System.getProperty("user.dir")));
    }

    public static void addSearchPathInJar(String path) {
        if (!searchPathsInJar.contains(path)) 
    	searchPathsInJar.add(path);
    }

    public static void addSearchPath(File f) {
        if (!searchDirs.contains(f)) 
        	searchDirs.add(f);
    }

    public void addSearchPath(String s) {
    	 addSearchPath(new File(s));
    }
    

	public void addSearchPaths(String sp) {
		String[] bits = sp.split(":");
		for (String s : bits) {
			addSearchPath(s);
		}
	}

    
    @Override
    public String getRelativeContent(String attribute, String s) throws ContentError {
    	if(attribute.equals(URL))
    	{
    		throw new IllegalArgumentException("URL is not supported when using the FileInclusionReader!");
    	}
    	String ret = "";
    	
        //E.info("Getting rel path for: "+s+", searchPathsInJar: "+ searchPathsInJar);

        if (searchPathsInJar.isEmpty())
            searchPathsInJar.add("");
        
        for (String path: searchPathsInJar) {
        	String toTry = path+"/"+s;
            //System.out.println("Trying: "+toTry);
            if (toTry.indexOf("..")>=0) {
                ArrayList<String> elements = new ArrayList<String>();
                for (String el: toTry.split("/")) {
                    if (!el.equals(".."))
                        elements.add(el);
                    else
                        elements.remove(elements.size()-1);
                }
                toTry = "";
                for (String el:elements)
                    toTry += "/"+el;
                toTry = toTry.substring(1);
            }
            
        	try {
        		ret = JUtil.getRelativeResource(toTry);
        		//System.out.println("Resource found in jar: "+toTry);
                return ret;
        	} catch (ContentError ce) {
        		//System.out.println("Resource not found in jar: "+toTry);
        	}
        }
        
        File f = new File(prefDir, s);

        ///E.info("Trying for: "+f.getAbsolutePath()+", searchDirs: "+ searchDirs);
        if (f.exists()) {
            // we're OK
        } else {
            f = new File(s);

            if (!f.exists()) {

                for (File fd : searchDirs) {
                    File ftry = new File(fd, s);
                    if (ftry.exists()) {
                        f = ftry;
                        break;
                    }
                }
            }
        }
        
        if (f.exists()) {
            // NB its possible that fpar is different from one of the search dirs because s could be a
            // path rather than a single file name. So, if s is "dir1/dir2/filename" and we resolve it
            // relative to one of the current serach paths, we need to add dir2 to the search list because its
            // contents may include files with only the last part of the path.
            File fpar = f.getParentFile();
            if (searchDirs.contains(fpar)) {
                // already there.
            } else {
                searchDirs.add(0, fpar);
            }
        } else {
        	final StringBuilder sb = new StringBuilder();
        	sb.append("Can't find file at path: " + s + "\n");
        	sb.append("Search directories are: " + searchDirs + "\n");
        	sb.append("Search paths in jar are: " + searchPathsInJar + "\n");
         	throw new ContentError(sb.toString());
        }
        
        boolean readOK;
        try {
           // E.info("Reading " + f.getCanonicalPath());
            ret = FileUtil.readStringFromFile(f);
            readOK = true;
        } catch (IOException ex) {
        	readOK = false;
        	// not readable - readOK remains false and will be reported later
        }
        
        if (!readOK) {
        	 throw new ContentError("Error reading fole " + f.getAbsolutePath());
        }
        
        return ret;
    }

    
    @Override
    public String getRootContent() throws ContentError {
        try {
        	if (content!=null) {
        		return content;
        	}
        		
            return FileUtil.readStringFromFile(rootFile);
        }
        catch (IOException ex) {
            throw new ContentError("Problem reading from file: " + rootFile.getAbsolutePath(), ex);
        }
    }

	
	public static void main(String[] argv) throws IOException, ContentError
	{
        JarResourceInclusionReader jrir = new JarResourceInclusionReader("target/jlems-0.9.5.3.jar");
                
        System.out.println(">> "+jrir.getRelativeContent(JAR.toString(),"META-INF/../META-INF/MANIFEST.MF"));

	}
	
}
