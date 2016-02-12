package org.lemsml.jlems.io.out;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.core.out.ResultWriter;
import org.lemsml.jlems.core.run.RuntimeError;
import org.lemsml.jlems.core.run.RuntimeOutput;
import org.lemsml.jlems.io.util.FileUtil;

public class FileResultWriter implements ResultWriter {

    String id;
    String path;
    String fileName;
    int colCount;

    ArrayList<double[]> allData;

    int columnCounter;
    double[] currentRow = null;
    boolean newFile = true;

    boolean verbose = false;

    public FileResultWriter(RuntimeOutput ro) {
         id = ro.getID();
         path = ro.getPath();
         fileName = ro.getFileName();
         colCount = 1;
         allData = new ArrayList<double[]>();
    }

    @Override
    public String getID()
    {
        return id;
    }

    @Override
    public void addPoint(String id, double x, double y) {

        if (verbose) System.out.println("addPoint: "+id+", "+columnCounter+" ("+(float)x+", "+(float)y+", ...)");
        currentRow[columnCounter] = y;
        columnCounter += 1;
        
    }


    @Override
    public void advance(double t) throws RuntimeError {

        if (currentRow != null) {
            if (verbose) System.out.println(".. advance: "+(float)t+", "+ currentRow.length+" points  ("+(float)currentRow[0]+", "+ (currentRow.length>1 ? (float)currentRow[1]: "--")+", ...)");
            allData.add(currentRow);
            if (verbose) System.out.println("a Last dat of "+allData.size()+" ("+(float)allData.get(allData.size()-1)[0]+", "+(float)allData.get(allData.size()-1)[1]+", ...)");
        } else {
            if (verbose) System.out.println(".. no data advance...");
        }

        if (allData.size() > 1000) {
            flush();
        }

        currentRow = new double[colCount];
        currentRow[0] = t;
        columnCounter = 1;
    }



    @Override
    public void addedRecorder() {
        colCount += 1;
    }

    public void flush() throws RuntimeError {

        if (verbose) System.out.println("--------------\nf Last dat ("+(float)allData.get(allData.size()-1)[0]+", "+(float)allData.get(allData.size()-1)[1]+", ...)");
        StringBuilder sb = new StringBuilder();
        for (double[] d : allData) {
            for (int i = 0; i < d.length; i++) {
                sb.append((float)d[i]);
                sb.append("\t");
            }
            sb.append("\n");
        }

        if (verbose) {
            System.out.println("Flushed "+allData.size()+" sets of points...");
            System.out.println("("+(float)allData.get(0)[0]+", "+(float)allData.get(0)[1]+", ...)");
            System.out.println("...");
            System.out.println("("+(float)allData.get(allData.size()-1)[0]+", "+(float)allData.get(allData.size()-1)[1]+", ...)");
        }

        allData = new ArrayList<double[]>();

        File fdest = getFile();

        try {
            if (newFile) {
                FileUtil.writeStringToFile(sb.toString(), fdest);
            } else {
                FileUtil.appendStringToFile(sb.toString(), fdest);
            }
        } catch (IOException ex) {
            throw new RuntimeError("Can't write to file: " + fileName, ex);
        }

        newFile = false;
    }



    protected File getFile() {
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
        E.info("Written to the file " + f.getAbsolutePath() + " " + f.length());
    }



}
