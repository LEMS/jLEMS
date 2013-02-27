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
	
	
	ArrayList<double[]> dat;
	
	int wkCount;
	double[] wk = null;
	boolean newFile = true;
	
	
	public FileResultWriter(RuntimeOutput ro) {
		 id = ro.getID();
		 path = ro.getPath();
		 fileName = ro.getFileName();
		 colCount = 1;
		 dat = new ArrayList<double[]>();
	}

	   
	
	@Override
	public void addPoint(String id, double x, double y) {
		wk[wkCount] = y;
		wkCount += 1;
	
	}
	
	
	public void advance(double t) throws RuntimeError {
		if (wk != null) {
			dat.add(wk);
		}
		
		if (dat.size() > 1000) {
			flush();
		}
		
		
		wk = new double[colCount];
		wk[0] = t;
		wkCount = 1;
	}



	@Override
	public void addedRecorder() {
		colCount += 1;
	}
	
	public void flush() throws RuntimeError {
		StringBuilder sb = new StringBuilder();
		for (double[] d : dat) {
			for (int i = 0; i < d.length; i++) {
				sb.append(d[i]);
				sb.append("\t");
			}
			sb.append("\n");
		}
		
		dat = new ArrayList<double[]>();
		
		File fdest = getFile();
	 
		try {
		if (newFile) {
			FileUtil.writeStringToFile(sb.toString(), fdest);
		} else {
			FileUtil.appendStringToFile(sb.toString(), fdest);
		}
		} catch (IOException ex) {
			throw new RuntimeError("cant write file " + fileName, ex);
		}
		
		newFile = false;
	}

	
	
	private File getFile() {
		File fdir = null;
		if (path != null) {
			fdir = new File(path);
		} else {
			fdir = new File(".");
		}

		File fdest = new File(fdir, fileName);
		return fdest;
	}


	@Override
	public void close() throws RuntimeError {
		flush();
	
		File f = getFile();
		E.info("Written file " + f.getAbsolutePath() + " " + f.length());
	}

	
	
}
