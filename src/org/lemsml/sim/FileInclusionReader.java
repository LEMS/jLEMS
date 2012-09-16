package org.lemsml.sim;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.lemsml.util.ContentError;
import org.lemsml.util.E;
import org.lemsml.util.FileUtil;


public class FileInclusionReader extends InclusionReader {

    File rootFile;
    File rootDir;
    String lastSuccessfulInclude = null;
    static ArrayList<File> searchDirs = new ArrayList<File>();
    File prefDir;

    FileInclusionReader(File f) {
        rootFile = f;
        rootDir = rootFile.getParentFile();
        
        if (rootDir != null) {
            searchDirs.add(rootDir);
            searchDirs.add(rootDir.getParentFile());
        }
        String lemsDirSystem = System.getenv("LEMS_HOME");

        if (lemsDirSystem!=null) {
            searchDirs.add(new File(lemsDirSystem));
        }
        String nml2DirSystem = System.getenv("NML2_HOME");

        if (nml2DirSystem!=null) {
            searchDirs.add(new File(nml2DirSystem));
        }
        prefDir = rootDir;

        searchDirs.add(new File(System.getProperty("user.dir")));
    }

    public static void addSearchPath(File f) {
        searchDirs.add(f);
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
            // contents may include files with only the lat part of the path.
            File fpar = f.getParentFile();
            if (searchDirs.contains(fpar)) {
                // already there.
            } else {
                searchDirs.add(0, fpar);
            }
        } else {
        	StringBuffer sb = new StringBuffer();
        	sb.append("Can't find file at path: " + s + "\n");
        	sb.append("Search directories are: " + searchDirs + "\n");
         	throw new ContentError(sb.toString());
        }
        
        boolean readOK = false;
        try {
            E.info("Reading " + f.getCanonicalPath());
            ret = FileUtil.readStringFromFile(f);
            readOK = true;
        } catch (IOException ex) {
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
