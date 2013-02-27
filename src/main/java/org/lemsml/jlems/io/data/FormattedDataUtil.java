package org.lemsml.jlems.io.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.lemsml.jlems.core.logging.E;
 


public class FormattedDataUtil {

	public static double[] readFlatArray(File file) {
		double[][] mda = readDataArray(file);
		return flatten(mda);
	}


	public static double[] flatten(double[][] da) {
		double[] ret = null;
		if (da[0].length == 0) {
			ret = new double[0];
		} else {
			int nl = da.length;
			int ncol = da[0].length;
			ret = new double[nl * ncol];
			for (int i = 0; i < nl; i++) {
				for (int j = 0; j < ncol; j++) {
					ret[ncol * i + j] = da[i][j];
				}
			}
		}
		return ret;
	}


	public static double[][] transpose(double[][] a) {
		int nr = a.length;
		int nc = a[0].length;
		double[][] ret = new double[nc][nr];
		for (int i = 0; i < nr; i++) {
			for (int j = 0; j < nc; j++) {
				ret[j][i] = a[i][j];
			}
		}
		return ret;
	}
	
	

	public static double[][] readResourceDataArray(Class<? extends Object> cls, String fnm) {
		double[][] ret = new double[0][0];
		try {
			InputStream fis = cls.getResourceAsStream(fnm);
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			ret = readDataArrayFromReader(br);
		} catch (Exception ex) {
			E.error("cant read resource " + fnm + " rel to " + cls);
		}
		return ret;
	}



	public static double[][] readDataArray(File f) {
		double[][] ret = new double[0][0];
		if (!f.exists()) {
			E.error("no such file: " + f);
			return new double[0][0];
		}
		 try {
			 BufferedReader br = new BufferedReader(new FileReader(f));
			 ret = readDataArrayFromReader(br);
		 } catch (Exception ex) {
			 E.error("cant read " + f);
		 }
		 return ret;
	}


	private static double[][] readDataArrayFromReader(BufferedReader br) {

		int ncol = 0;
		ArrayList<double[]> al = new ArrayList<double[]>();

		try {

        while (br.ready()) {
           String line = br.readLine();
           if (line.trim().length() > 0) {
              StringTokenizer st = new StringTokenizer(line, " ,;\t");
              if (ncol == 0) {
                 ncol = st.countTokens();
              }

              if (st.countTokens() < ncol) {
                 E.warning("too few elements in row - skipping " + line);
              } else {
                 if (st.countTokens() > ncol) {
                    E.warning("extra tokens in line beyond " + ncol + "? " + line);
                 }

                 double[] da = readRow(st, ncol);

                 if (da == null) {
                    // must be headers;
                    st = new StringTokenizer(line, " ,;\t");

                 } else {
                    al.add(da);
                 }


              }

           }
        }
     } catch (Exception ex) {
        E.warning("file read exception for " + br + " " + ex);
        ex.printStackTrace();
     }

     double[][] ret = new double[al.size()][ncol];
     for (int i = 0; i < ret.length; i++) {
    	 double[] r = al.get(i);
    	 for (int j = 0; j < ncol; j++) {
    		 ret[i][j] = r[j];
    	 }
     }
     return ret;

}




public static double[] readRow(StringTokenizer st, int ncol) {
  double[] ret = new double[ncol];
  try {
     for (int i = 0; i < ret.length; i++) {
        ret[i] = Double.parseDouble(st.nextToken());
     }
  } catch (Exception ex) {
     ret = null;
  }
  return ret;
}



public static String[] readStringRow(StringTokenizer st, int ncol) {
  String[] ret = new String[ncol];
  if (st.countTokens() < ncol) {
     E.error("need " + ncol + " but got only " + st.countTokens() + " tokens in " + st);
  } else {
     for (int i = 0; i < ncol; i++) {
        ret[i] = st.nextToken();
     }
  }
  return ret;
}




public static double[] readRow(String line) {
  StringTokenizer st = new StringTokenizer(line, " ,;\t[]");
  int nc = st.countTokens();
  return readRow(st, nc);
}

 

}