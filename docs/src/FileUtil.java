 


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public abstract class FileUtil {



   public static byte[] readHeader(File f, int n) {
      byte[] ret = null;
      try {
         FileInputStream ins = new FileInputStream(f);
         ret = new byte[n];
         int nread = ins.read(ret);
         if (nread != n) {
            E.error("readNBytes wanted " + n + " but got " + nread);
         }
         ins.close();
      } catch (Exception ex) {
         E.error("readNBytes problem " + ex);
      }
      return ret;
   }



   public static byte[] readBytes(File f) {
      byte[] ret = null;
      try {
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

      } catch (Exception ex) {
         E.error("readNBytes problem " + ex);
      }
      return ret;
   }



   public static String readStringFromFile(File f) {
      String sdat = "null";
      if (f != null) {
         try {
            boolean dogz = (f.getName().endsWith(".gz"));
            InputStream ins = new FileInputStream(f);
            if (dogz) {
               ins = new GZIPInputStream(ins);
            }
            InputStreamReader insr = new InputStreamReader(ins);
            BufferedReader fr = new BufferedReader(insr);

            StringBuffer sb = new StringBuffer();
            while (fr.ready()) {
               sb.append(fr.readLine());
               sb.append("\n");
            }
            fr.close();
            sdat = sb.toString();

         } catch (IOException ex) {
            E.error("file read error ");
            ex.printStackTrace();
         }
      }
      return sdat;
   }



   public static boolean writeStringToFile(String sdat, File f) {
      String fnm = f.getName();
      boolean ok = false;
      if (f != null) {
         boolean dogz = (fnm.endsWith(".gz"));
         try {
            OutputStream fos = new FileOutputStream(f);
            if (dogz) {
               fos = new GZIPOutputStream(fos);
            }
            OutputStreamWriter osw = new OutputStreamWriter(fos);

            osw.write(sdat, 0, sdat.length());
            osw.close();
            ok = true;

         } catch (IOException ex) {
            E.error("file writing error, trying to write file " + f + " " + f.getParentFile().exists());
            ex.printStackTrace();
         }
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
      } catch (Exception e) {
         E.error("cant write byte array " + ba + " to " + f);
      }
   }



   public static void copyFile(File fsrc, File fdestin) {
	   File fdest = fdestin;
	   if (fdest.isDirectory()) {
		   fdest = new File(fdest, fsrc.getName());
	   }

      if (fsrc.exists()) {
         try {
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
         } catch (Exception ex) {
            E.error("file copy exception");
         }

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



   public static String readFirstLine(File f) {

      String ret = null;
      if (f != null) {
         try {
            InputStream ins = new FileInputStream(f);
            InputStreamReader insr = new InputStreamReader(ins);
            BufferedReader fr = new BufferedReader(insr);
            while (ret == null || ret.trim().length() == 0) {
            	ret = fr.readLine().trim();
            }
            fr.close();

         } catch (IOException ex) {
            E.error("file read error ");
            ex.printStackTrace();
         }
      }
      return ret;
   }

   public static String readNLinesFromFile(File f, int n) {
	   StringBuffer sb = new StringBuffer();

	      if (f != null) {
	         try {
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

	         } catch (IOException ex) {
	            E.error("file read error ");
	            ex.printStackTrace();
	         }
	      }
	      return sb.toString();
	   }


   public static String getRelativeDirectory(File ftgt, File rtFolder) {
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
            E.error("too many steps trying to get relative files ? " + ftgt.getAbsolutePath() + " "
                  + rtFolder.getAbsolutePath());
            break;
         }
      }

      return sret;
   }


   // TODO make this smarter (or use GlobFileFilter from jakarta ORO ?)
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


   public final static File[] routeToAncestor(File dtgt, File dtop) {
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



	   public final static String pathFromAncestor(File ftop, File ftgt) {
	      File[] af = routeToAncestor(ftgt, ftop);
	      String sr = "";
	      for (int i = 0; i < af.length - 1; i++) {
	         sr += af[af.length - 2 - i].getName();
	         sr += "/";
	      }
	      return sr;
	   }


	   public final static String relpath(int nl) {
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
		 return (new File(rootFile.getParentFile(), getRootName(rootFile)).getAbsolutePath());
	}



	public static void copyFiles(File srcdir, File destdir) {
		 for (File f : srcdir.listFiles()) {
			 copyFile(f, new File(destdir, f.getName()));
		 }
	}



	 public static File makePackageFolder(File fbase, Class<?> cls) {
		 File fwk = fbase; // new File("psics-out");
		 Package p = cls.getPackage();
		 StringTokenizer st = new StringTokenizer(p.getName(), ".");
		 while (st.hasMoreTokens()) {
			 fwk = new File(fwk, st.nextToken());
		 }
		 if (!fwk.exists()) {
			 fwk.mkdirs();
		 }
		 return fwk;
	 }



	public static void writeJarFile(ArrayList<File> tojar, File fout, HashMap<String, String> mats) {
	      try {
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
	       } catch (Exception ex) {
	          E.error("custom jar writing error " + ex);
	          ex.printStackTrace();
	       }
	   }


}
