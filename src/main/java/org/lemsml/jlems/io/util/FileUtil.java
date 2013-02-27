package org.lemsml.jlems.io.util;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.core.sim.ContentError;
 


public final class FileUtil {


	private FileUtil() {
		
	}

   public static byte[] readHeader(File f, int n) throws IOException {
      byte[] ret = null;
      
         FileInputStream ins = new FileInputStream(f);
         ret = new byte[n];
         int nread = ins.read(ret);
         if (nread != n) {
            E.error("readNBytes wanted " + n + " but got " + nread);
         }
         ins.close();
      
      return ret;
   }



   public static byte[] readBytes(File f) throws IOException {
      byte[] ret = null;
       
         FileInputStream fis = new FileInputStream(f);
         BufferedInputStream bis = new BufferedInputStream(fis);
         ByteArrayOutputStream baos = new ByteArrayOutputStream();

         byte[] bb = new byte[4096];
         int nread = bis.read(bb);
         while (nread > 0) {
            baos.write(bb, 0, nread);
            nread = bis.read(bb);
         }
         ret = baos.toByteArray();
         bis.close();
      return ret;
   }



   public static String readStringFromFile(File f) throws IOException {
      String sdat = "null";
      if (f != null) {
      
            boolean dogz = f.getName().endsWith(".gz");
            InputStream ins = new FileInputStream(f);
            if (dogz) {
               ins = new GZIPInputStream(ins);
            }
            InputStreamReader insr = new InputStreamReader(ins);
            BufferedReader fr = new BufferedReader(insr);

            StringBuilder sb = new StringBuilder();
            while (fr.ready()) {
               sb.append(fr.readLine());
               sb.append("\n");
            }
            fr.close();
            sdat = sb.toString();

      }
      return sdat;
   }



   public static boolean writeStringToFile(String sdat, File f) throws IOException {
      String fnm = f.getName();
      boolean ok = false;
      if (f != null) {
         boolean dogz = fnm.endsWith(".gz");
       
            OutputStream fos = new FileOutputStream(f);
            if (dogz) {
               fos = new GZIPOutputStream(fos);
            }
            OutputStreamWriter osw = new OutputStreamWriter(fos);

            osw.write(sdat, 0, sdat.length());
            osw.close();
            ok = true;

         
      }
      return ok;
   }

   public static boolean appendStringToFile(String sdat, File f) throws IOException {
	      String fnm = f.getName();
	      boolean ok = false;
	      if (f != null) {
	    	  OutputStream fos = new FileOutputStream(f, true);
	          
	            OutputStreamWriter osw = new OutputStreamWriter(fos);

	            osw.write(sdat, 0, sdat.length());
	            osw.close();
	            ok = true;

	         
	      }
	      return ok;
	   }
   
   

   public static String getRootName(File f) {
      String fnm = f.getName();
      return getRootName(fnm);
   }


   public static String getRootName(String fnm) {
      int ild = fnm.lastIndexOf(".");

      String root = fnm;
      if (ild > 0) {
    	  root = fnm.substring(0, fnm.lastIndexOf("."));
      }
      return root;
   }



   public static void writeBytes(byte[] ba, File f) {
      writeByteArrayToFile(ba, f);
   }


   public static void writeByteArrayToFile(byte[] ba, File f) {
      if (f == null) {
         return;
      }
      try {
         OutputStream os = new BufferedOutputStream(new FileOutputStream(f));
         os.write(ba);
         os.flush();
         os.close();
      } catch (Exception e) {
         E.error("cant write byte array " + ba + " to " + f);
      }  
   }



   public static void copyFile(File fsrc, File fdestin) throws IOException {
	   File fdest = fdestin;
	   if (fdest.isDirectory()) {
		   fdest = new File(fdest, fsrc.getName());
	   }

      if (fsrc.exists()) {
         
            InputStream in = new FileInputStream(fsrc);
            OutputStream out = new FileOutputStream(fdest);

            // Transfer bytes from in to out
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
               out.write(buf, 0, len);
            }
            in.close();
            out.close();
         

      } else {
         E.warning("copy - missing file " + fsrc);
      }
   }



   public static String findPath(File f, String name) {
      String ret = null;

      for (File fs : f.listFiles()) {
         if (fs.getName().equals(name)) {
            ret = "";
            break;
         }
      }

      if (ret == null) {
         for (File fd : f.listFiles()) {
            if (fd.isDirectory()) {
               String s = findPath(fd, name);
               if (s != null) {
                  if (s.equals("")) {
                     ret = fd.getName();
                  } else {
                     ret = fd.getName() + "/" + s;
                  }
                  break;
               }
            }
         }
      }
      return ret;
   }



   public static String readFirstLine(File f) throws IOException {

      String ret = null;
      if (f != null) {
        
            InputStream ins = new FileInputStream(f);
            InputStreamReader insr = new InputStreamReader(ins);
            BufferedReader fr = new BufferedReader(insr);
            while (ret == null || ret.trim().length() == 0) {
            	ret = fr.readLine().trim();
            }
            fr.close();

         
      }
      return ret;
   }

   public static String readNLinesFromFile(File f, int n) throws IOException {
	   StringBuffer sb = new StringBuffer();

	      if (f != null) {
	        
	            InputStream ins = new FileInputStream(f);
	            InputStreamReader insr = new InputStreamReader(ins);
	            BufferedReader fr = new BufferedReader(insr);
	            int nread = 0;
	            while (fr.ready() && nread < n) {
	            	sb.append(fr.readLine());
	            	sb.append("\n");
	            	nread++;
	            }
	            fr.close();

	          
	      }
	      return sb.toString();
	   }


   public static String getRelativeDirectory(File ftgt, File rtFolder) throws ContentError {
      File fpar = ftgt.getParentFile();
      int ns = 0;

      String sret = null;

      while (fpar != null && !(fpar.equals(rtFolder))) {
         if (sret == null) {
            sret = fpar.getName();
         } else {
            sret = fpar.getName() + "/" + sret;
         }
         fpar = fpar.getParentFile();

         ns += 1;
         if (ns > 8) {
            throw new ContentError("too many steps trying to get relative files ? " + ftgt.getAbsolutePath() + " "
                  + rtFolder.getAbsolutePath());
          
         }
      }

      return sret;
   }


   // MAYDO make this smarter (or use GlobFileFilter from jakarta ORO ?)
   public static ArrayList<File> matchingFiles(String srcPattern) {
      ArrayList<File> ret = new ArrayList<File>();
      if (srcPattern.indexOf("*") < 0) {
         File fd = new File(srcPattern);
         if (fd.exists() && fd.isDirectory()) {
            for (File f : fd.listFiles()) {
               ret.add(f);
            }
         }

      } else {
         int istar = srcPattern.indexOf("*");
         String sa = srcPattern.substring(0, istar);
         String sb = srcPattern.substring(istar + 1, srcPattern.length());
         File ftop = new File(sa);
         for (File fg : ftop.listFiles()) {
            File fp = new File(fg, sb);
            if (fp.exists()) {
               ret.add(fp);
            }
         }
      }
      return ret;
   }


   public static File[] routeToAncestor(File dtgt, File dtop) {
	      int nup = 0;
	      File[] dh = new File[10];
	      File dwk = dtgt;
	      dh[nup++] = dwk;

	      while (true) {
	         if (dwk == null || dwk.equals(dtop) || nup == 10) {
	            break;
	         }

	         dwk = dwk.getParentFile();
	         dh[nup++] = dwk;
	      }
	      File[] fr = new File[nup];
	      for (int i = 0; i < nup; i++) {
	         fr[i] = dh[i];
	      }
	      return fr;
	   }



	   public static String pathFromAncestor(File ftop, File ftgt) {
	      File[] af = routeToAncestor(ftgt, ftop);
	      String sr = "";
	      for (int i = 0; i < af.length - 1; i++) {
	         sr += af[af.length - 2 - i].getName();
	         sr += "/";
	      }
	      return sr;
	   }


	   public static String relpath(int nl) {
	      String arel = "../";
	      String srel = "";
	      for (int k = 0; k < nl; k++) {
	         srel += arel;
	      }
	      if (nl == 0) {
	         srel = "./"; // POSERR
	      }
	      return srel;
	   }



	public static String[] getResourceList(File fdir, String extn) {
		ArrayList<String> als = new ArrayList<String>();
		for (File f : fdir.listFiles()) {
			String fnm = f.getName();
			if (fnm.endsWith(extn)) {
				als.add(fnm.substring(0, fnm.length())); //  - extn.length()));
			}
		}
		return als.toArray(new String[als.size()]);
	}



	public static File extensionSibling(File rootFile, String sext) {
		 return new File(rootFile.getParentFile(), getRootName(rootFile) + sext);
	}



	public static String absoluteRoot(File rootFile) {
		 return new File(rootFile.getParentFile(), getRootName(rootFile)).getAbsolutePath();
	}



	public static void copyFiles(File srcdir, File destdir) throws IOException {
		 for (File f : srcdir.listFiles()) {
			 copyFile(f, new File(destdir, f.getName()));
		 }
	}


 



	public static void writeJarFile(ArrayList<File> tojar, File fout, HashMap<String, String> mats) throws IOException {
	     
	    	  Manifest m = new Manifest();
	    	  if (mats != null) {
	    		  for (String s : mats.keySet()) {
	    			  m.getMainAttributes().putValue(s,  mats.get(s));
	    		  }
	    	  }

	          ByteArrayOutputStream baos = new ByteArrayOutputStream();
	          JarOutputStream zos = new JarOutputStream(baos, m);
	          for (File f : tojar) {
	            zos.putNextEntry(new JarEntry(f.getName()));
	            FileInputStream fis  = new FileInputStream(f);
	                byte[] buf = new byte[4096];
	                int nread = 0;
	                while((nread = fis.read(buf)) > 0) {
	                   zos.write(buf, 0, nread);
	                }
	                fis.close();

	             zos.closeEntry();
	          }
	          zos.flush();
	          zos.close();

	          byte[] ba= baos.toByteArray();
 	          writeByteArrayToFile(ba, fout);
	      
	   }




	 public static File getSiblingFile(File fme, String ext) {
        String fnm = fme.getName();
        int ild = fnm.lastIndexOf(".");
        if (ild > 1) {
           fnm = fnm.substring(0, ild);
        }
        File fret = new File(fme.getParentFile(), fnm + ext);
        return fret;

  }



	public static void clearNew(File flog) throws IOException {
		if (flog.exists()) {
			flog.delete();
		}
		writeStringToFile("", flog);
	}


	public static void appendLine(File flog, String txt) throws IOException {
		 
		        BufferedWriter out = new BufferedWriter(new FileWriter(flog, true));
		        out.write(txt + "\n");
		        out.close();
		   

	}



}
