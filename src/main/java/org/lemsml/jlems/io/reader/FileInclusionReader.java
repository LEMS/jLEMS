package org.lemsml.jlems.io.reader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.lemsml.jlems.core.sim.AbstractInclusionReader;
import org.lemsml.jlems.core.sim.ContentError;
import org.lemsml.jlems.io.util.FileUtil;


public class FileInclusionReader extends AbstractInclusionReader {

    private final File rootFile;
    
    private static ArrayList<File> searchDirs = new ArrayList<File>();
    private final File prefDir;

    
    
    public FileInclusionReader(File f) {
    	super();
    	rootFile = f;
        File rootDir = rootFile.getParentFile();
        
        if (rootDir != null) {
            searchDirs.add(rootDir);
          //  searchDirs.add(rootDir.getParentFile());
        }
        String lemsDirSystem = System.getenv("LEMS_HOME");

        if (lemsDirSystem != null) {
            searchDirs.add(new File(lemsDirSystem));
        }
        prefDir = rootDir;

        searchDirs.add(new File(System.getProperty("user.dir")));
    }

    public static void addSearchPath(File f) {
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

    
    public String getRelativeContent(String s) throws ContentError {
    	String ret = "";
    	
        //E.info("Getting rel path for: "+s+", rootDir: "+ rootDir.getAbsolutePath()+", searchDirs: "+ searchDirs);

        File f = new File(prefDir, s);
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
         	throw new ContentError(sb.toString());
        }
        
        boolean readOK = false;
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

   
    
    public String getRootContent() throws ContentError {
        try {
            return FileUtil.readStringFromFile(rootFile);
        }
        catch (IOException ex) {
            throw new ContentError("Problem reading from file: " + rootFile.getAbsolutePath(), ex);
        }
    }

	
	
}
