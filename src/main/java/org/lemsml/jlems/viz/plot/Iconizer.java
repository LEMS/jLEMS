package org.lemsml.jlems.viz.plot;

 

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;
 
public final class Iconizer {


   // args: poly file, dim, r, g, b, outfile
   public static void main(String[] argv) throws IOException {
      File fin = new File(argv[0]);
      int dim = Integer.parseInt(argv[1]);
      int r = Integer.parseInt(argv[2]);
      int g = Integer.parseInt(argv[3]);
      int b = Integer.parseInt(argv[4]);
      File fout = new File(argv[5]);

      double[][] xy = readPerimiter(readStringFromFile(fin));

    
      Color cgray = new Color(140, 140, 140);

      BufferedImage bim = filledPolygonImage(xy[0], xy[1], dim, cgray,
					     0.02, 0.04, 2.5);


      BufferedImage bimc = filledPolygonImage(xy[0], xy[1], dim, 
					     new Color(r, g, b), 0., 0., 1.5);
      
      bim.getGraphics().drawImage(bimc, 0, 0, null);


     
	 ImageIO.write(bim, "png", fout);
     
      
   }


   private Iconizer() {
	   
   }


   public static BufferedImage filledPolygonImage(double[] xp, double[] yp, 
						  int dim, Color col,
						  double osx, double osy,
						  double elf) {
      
      
      
      BufferedImage transim = new BufferedImage(dim, dim, 
						BufferedImage.TYPE_INT_ARGB);
      
      WritableRaster alpha = transim.getAlphaRaster();
      WritableRaster raster = transim.getRaster();


      double[][] d = alphaPixelize(xp, yp, dim, osx, osy, elf);


      int w = dim;
      int h = dim;

      int r = col.getRed();
      int g = col.getGreen();
      int b = col.getBlue();


      // transparency array;
      int[] itrans = new int[1];
      itrans[0] = 0;

      int[] ipxin = new int[4];

      for (int i = 0; i < w; i++) {
	 for (int j = 0; j < h; j++) {
	    
	    if (d[i][j] > 0.) {
	       
	       ipxin[0] = r;
	       ipxin[1] = g;
	       ipxin[2] = b;
	       
	       raster.setPixel(i, j, ipxin);
	    }
	    
	    
	    
	    itrans[0] = (int)(255 * d[i][j]);
	    alpha.setPixel(i, j, itrans);
	 }
      }

      return transim;
   }

















   // create a dim x dim array with the internal pixels of the polygon
   // defined by perimeter xp,yp filled with fillColor. Boundary regins
   // have transparency set according to overlap. 
   // result ints are R,G,B,alpha
   
   public static double[][] alphaPixelize(double[] xp0, double[] yp0, int dim,
					  double osx, double osy, 
					  double elf) {
      
      double[][] ret = new double[dim][dim];
      
      double[] xp = rerange(xp0);
      double[] yp = rerange(yp0);
      
      
      double dx = 1. / dim;
      double dy = 1. / dim;
      
      int nsamp = 10;


      for (int i = 0; i < dim; i++) {
	 for (int j = 0; j < dim; j++) {
	    double x0 = (i + 0.5) * dx;
	    double y0 = (j + 0.5) * dy;
	    
	    ret[i][j] = coverage(xp, yp, x0-osx, y0-osy, 
				 elf * dx, elf * dy, nsamp);

	 }
      }
      return ret;
   }




      

   private static double coverage(double[] xp, double[] yp, 
			   double xc0, double yc0, double dx0, double dy0, 
			   int nsamp) {

      int nin = 0;
      
      double x0 = xc0 - 0.5 * dx0;
      double y0 = yc0 - 0.5 * dy0;


      double dx = dx0 / nsamp;
      double dy = dy0 / nsamp;

      for (int i = 0; i < nsamp; i++) {
	 for (int j = 0; j < nsamp; j++) {
	    double x = x0 + (i + 0.5) * dx;
	    double y = y0 + (i + 0.5) * dy;
	    
	    if (pointIsInside(x, y, xp, yp)) {
	       nin++;
	    }
	 }
      }
      
      return (1. * nin) / (nsamp * nsamp);

   }







   private static double[] rerange(double[] ap0) {
      double amin = ap0[0];
      double amax = ap0[0];
      for (int i = 0; i < ap0.length; i++) {
	 if (ap0[i] < amin) {
	    amin = ap0[i];
	 }
	 if (ap0[i] > amax) {
	    amax = ap0[i];
	 }
      }

      amin -= 0.1 * (amax - amin);
      amax += 0.1 * (amax - amin);

      
      double[] ap = new double[ap0.length];
      for (int i = 0; i < ap0.length; i++) {
	 ap[i] = (ap0[i] - amin) / (amax - amin);
      }
      return ap;
   }







                           
   private static boolean pointIsInside(double x, double y, 
					double[] xb, double[] yb){ 
     int n = xb.length;
     int iwn = 0;
     for (int i = 0; i < n; i++) {
	int idir = 0;
	int p = (i+1)%n;
	if (yb[i] <= y && yb[p] > y) {
      idir = 1; 
   }
	if (yb[i] > y && yb[p] <= y) {
      idir = -1;
   }
	if (idir != 0) {
	   double f = (y - yb[i]) / (yb[p] - yb[i]);
	   double xc = f * xb[p] + (1.-f) * xb[i];
	   int isid = (xc > x ? 1 : -1);
	   iwn += isid * idir;
	}
	
     }
     return (iwn != 0);
  }








  private static double[][] readPerimiter(String s) {
     StringTokenizer st = new StringTokenizer(s, "\n\t ,");
     
     int n = st.countTokens() / 2;

     double[][] sy = new double[2][n];
     
     for (int i = 0; i < n; i++) {
	sy[0][i] = new Double(st.nextToken()).doubleValue();
	sy[1][i] = new Double(st.nextToken()).doubleValue();
     }
     return sy;
  }






   public static String readStringFromFile (File f) throws IOException {
      String sdat = "";
      
	 InputStream ins = new FileInputStream(f);
	 InputStreamReader insr = new InputStreamReader(ins);
	 BufferedReader fr = new BufferedReader (insr);
	 StringBuffer sb = new StringBuffer();
	 while (fr.ready()) {
	    sb.append (fr.readLine());
	    sb.append ("\n");
	    }
	 fr.close();
	 sdat = sb.toString();
       
      return sdat;
   }
   

}

