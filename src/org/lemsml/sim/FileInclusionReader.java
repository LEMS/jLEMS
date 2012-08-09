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
            throw new ContentError("Can't find file at path: " + s + "\nAll search directories for LEMS files: " + searchDirs+
                    "\n\nIt may be useful to add the LEMS_HOME environment variable pointing to the directory containing the compiled LEMS jar file.\n"
                    + "If you are running NeuroML 2 models, also add the NML2_HOME"
                    + " environment variable for the directory containing NeuroML 2 files (including NeuroML2CoreTypes).\n");
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

    public String oldGetRelativeContent(String s) throws ContentError {
        File f = new File(rootDir, s);

        E.info("Getting rel path for: " + s + ", rootDir: " + rootDir.getAbsolutePath()
                + ", resolved to: " + f.getAbsolutePath() + ", lastSuccessfulInclude: " + lastSuccessfulInclude);


        if (f.exists()) {
            lastSuccessfulInclude = f.getParentFile().getAbsolutePath();
        } else {
            // Needed in case a file includes a file which includes another file with a different path
            //TODO: This needs overhaul, but should work for one level of nesting...

            f = new File(lastSuccessfulInclude, s);
            //E.info("Trying in folder: "+ lastSuccessfulInclude+", f: "+ f.getAbsolutePath());

            if (!f.exists()) {
                File cwd = new File(System.getProperty("user.dir"));
                //E.info("Trying in folder: "+ System.getProperty("user.dir"));

                f = new File(cwd, s); // try in current working dir...
                if (f.exists()) {
                    lastSuccessfulInclude = f.getParentFile().getAbsolutePath();
                }
            } else {
                lastSuccessfulInclude = f.getParentFile().getAbsolutePath();
            }
        }

        try{
            E.info("Including file: " + f.getCanonicalPath());
            String ret = FileUtil.readStringFromFile(f);

            return ret;
        }
        catch (IOException ex){
            throw new ContentError("Problem reading from file: "+f.getAbsolutePath(), ex);
        }
    }

    public String getRootContent() throws ContentError {
        try{
            return FileUtil.readStringFromFile(rootFile);
        }
        catch (IOException ex){
            throw new ContentError("Problem reading from file: "+rootFile.getAbsolutePath(), ex);
        }
    }

	
	
}
