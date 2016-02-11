package org.lemsml.jlems.io.out;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.core.out.EventResultWriter;
import org.lemsml.jlems.core.run.RuntimeError;
import org.lemsml.jlems.core.run.RuntimeEventOutput;
import org.lemsml.jlems.core.type.simulation.EventWriter;
import org.lemsml.jlems.io.util.FileUtil;

public class FileEventResultWriter implements EventResultWriter {

    String id;
    String path;
    String fileName;
    String format;

    ArrayList<String> currentEvents = new ArrayList<String>();
    StringBuilder lines = new StringBuilder();
    boolean newFile = true;
    boolean verbose = false;

    public FileEventResultWriter(RuntimeEventOutput ro) {
         id = ro.getID();
         path = ro.getPath();
         fileName = ro.getFileName();
         format = ro.getFormat();
    }

    @Override
    public String getID()
    {
        return id;
    }

    @Override
    public void recordEvent(String id) {

        if (verbose) System.out.println("FileEventResultWriter "+id+" recordEvent: "+id+"...");
        currentEvents.add(id);

    }

    @Override
    public void advance(double t) throws RuntimeError {

        for (String ev: currentEvents) {
            if (format.equals(EventWriter.FORMAT_TIME_ID)) {
                lines.append((float)t+"\t"+ev+"\n");
            } else if (format.equals(EventWriter.FORMAT_ID_TIME)) {
                lines.append(ev+"\t"+(float)t+"\n");
            }
        }
        currentEvents.clear();
    }

    @Override
    public void addedRecorder() {
        // ...
    }

    public void flush() throws RuntimeError {

        File fdest = getFile();

        try {
            if (newFile) {
                FileUtil.writeStringToFile(lines.toString(), fdest);
            } else {
                FileUtil.appendStringToFile(lines.toString(), fdest);
            }
        } catch (IOException ex) {
            throw new RuntimeError("Can't write to file: " + fileName, ex);
        }

        newFile = false;
    }



    private File getFile() {
        File fdir;
        if (path != null) {
            fdir = new File(path);
        } else if (fileName.startsWith("/")) {
            fdir = null;
        } else {
            fdir = new File(".");
        }

        File fdest = new File(fdir, fileName);

        if (!fdest.getParentFile().exists()) {

            try
            {
                E.informativeError("This LEMS file is requesting to write to a file: "+fileName+", resolved as: "+fdest.getCanonicalPath()
                        +"\nHowever, parent directory: "+fdest.getParentFile().getCanonicalPath()+" does not exist!");
            } catch (IOException ex)
            {
                E.informativeError("This LEMS file is requesting to write to a file: "+fileName+", resolved as: "+fdest.getAbsolutePath()
                        +"\nHowever, parent directory: "+fdest.getParentFile().getAbsolutePath()+" does not exist!");
            }
            System.exit(1);
        }
        return fdest;
    }


    @Override
    public void close() throws RuntimeError {
        if (verbose) System.out.println("close()...");
        flush();

        File f = getFile();
        E.info("Written to the event file " + f.getAbsolutePath() + " " + f.length());
    }



}
