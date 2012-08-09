package org.lemsml.sim;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.lemsml.util.ContentError;
import org.lemsml.util.E;
import org.lemsml.util.FileUtil;


public class StringInclusionReader extends InclusionReader {

	String origText;

    String lastSuccessfulInclude = null;

    static ArrayList<File> searchDirs = new ArrayList<File>();
    //File prefDir;
    
    
	StringInclusionReader(String txt) {
		origText  = txt;
		
		searchDirs.add(new File(System.getProperty("user.dir")));
	}

    public static void addSearchPath(File f)
    {
        searchDirs.add(f);
    }

 
	public String getRelativeContent(String s) throws ContentError {

                //E.info("Getting rel path for: "+s+", rootDir: "+ rootDir.getAbsolutePath()+", searchDirs: "+ searchDirs);

		File f = new File(".", s);
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
			throw new ContentError("Can't find file at path: " + s+"\nAll search dirs: "+searchDirs);
		}
        try{
            E.info("Reading in content from: " + f.getCanonicalPath());
            String ret = FileUtil.readStringFromFile(f);

            return ret;
        }
        catch (IOException ex){
            throw new ContentError("Problem reading from file: "+f, ex);
        }
	}
 
	public String getRootContent() {
		return origText;
	}
	
	
	
}
