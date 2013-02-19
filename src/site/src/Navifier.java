package src;

import java.io.File;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.StringTokenizer;


public class Navifier {


   public Navifier( ) {

   }



   public static void main(String[] argv) {
      (new Navifier()).navifyAll(argv[0]);
   }



   public void navifyAll(String sftop) {
      navifyAll(new File(sftop));
   }


   public void navify(File ftop) {
      navifyAll(ftop);
   }

   public void navifyAll(File ftop) {
      navifyAll(ftop, ftop, 0, "");
   }



   // all other methods are private;


   private void navifyAll(File ftgt, File ftop, int depth, String toroot) {
      if (ftgt.getName().equals("CVS")) {
	 return;
      }

      navify(ftgt, ftop, depth, toroot);

      File[] af = ftgt.listFiles();
      for (int i = 0; i < af.length; i++) {
	 if (af[i].isDirectory()) {
	    String dnm = af[i].getName();
	    if (dnm.endsWith(".thumb")) {
	       // ignore these - they are catalog thumbnail stores;

	    } else {
	       navifyAll(af[i], ftop, depth+1, toroot + "../");
	    }
	 }
      }
   }



   private void navify(File ftgt, File ftop, int depth, String toroot) {
      if (ftgt.getName().equals("CVS")) {
	 return;
      }


      if (ftgt.exists() && ftgt.isDirectory()) {

	 String path = FileUtil.pathFromAncestor(ftop, ftgt);

	 File[] af = ftgt.listFiles();

	 af = applyOrdering(af, ftgt);

	 StringBuffer sb = new StringBuffer();

	 sb.append("<index dir=\"" + path +  "\" depth=\"" +
		   depth + "\" toroot=\"" + toroot + "\">\n");


	 for (int i = 0; i < af.length; i++) {
	    File wf = af[i];
	    long lastmod = wf.lastModified();
	    String date = DateFormat.getDateTimeInstance().format(new Date(lastmod));

	    String fnm = wf.getName();

	    if (fnm.startsWith(".") || fnm.startsWith("_") || fnm.equals("CVS")) {
	       // ignore;

	    } else {
	       if (wf.isDirectory()) {

		  sb.append("   <directory name=\"" + wf.getName() + "\"" +
			    " date=\"" + date + "\"/>\n");
	       } else {
		  appendFileIfMatches(sb, wf, ".xml", "xmlfile");
	       }
	    }
	 }



	 File[] frp = FileUtil.routeToAncestor(ftgt, ftop);
	 int nlev = frp.length;

	 String rdown = "";
	 for (int ilev = 0; ilev < nlev-1; ilev++) {
	    String rp = FileUtil.relpath(nlev - 1 - ilev);
	    int ih = nlev - 1 - ilev;
	    File dpar = frp[ih];

	    String direct = frp[ih-1].getName();
	    sb.append("   <xparent level=\"" + ilev + "\"" +
		      " pathto=\"" + rp + "\"" +
		      " path=\"" + rdown + "\"" +
		      " direct=\"" +  direct  + "\"/>\n");


	    File[] afp = dpar.listFiles();

	    for (int i = 0; i < afp.length; i++) {
	       if (afp[i].isDirectory()) {

		  if (ih > 0 && frp[ih-1].equals(afp[i])) {
		     rdown += afp[i].getName() + "/";
		  }

	       }
	    }
	 }

	 sb.append("</index>\n");

	 File fdx = new File(ftgt, "_index.xml");
	 FileUtil.writeStringToFile(sb.toString(), fdx);
      }
   }



   private File[] applyOrdering(File[] af, File parent) {
      File[] afr = new File[af.length];
      File orderFile = new File(parent, "order.txt");
      if (orderFile.exists()) {
	 String s = FileUtil.readStringFromFile(orderFile);
	 StringTokenizer stok = new StringTokenizer(s, ",; \n\r\t");

	 HashMap<String, File> hm = new HashMap<String, File>();
	 for (int i = 0; i < af.length; i++) {
	    hm.put(af[i].getName(), af[i]);
	 }

	 int nout = 0;
	 while (stok.hasMoreTokens()) {
	    String fnm = stok.nextToken();
	    if (hm.containsKey(fnm)) {
	       afr[nout++] = (hm.get(fnm));
	       hm.remove(fnm);
	    }
	 }
	 for (File f : hm.values()) {
	    afr[nout++] = f;
	 }

      } else {
	 afr = af;
      }
      return afr;

   }







   private void appendFileIfMatches(StringBuffer sb, File f, String ext, String tag) {
      String fnm = f.getName();
      if (fnm.startsWith(".") || fnm.startsWith("_")) {

      } else if (fnm.endsWith(ext)) {
	 String fnr = fnm.substring(0, fnm.length()-ext.length());

	 String root = fnr;
	 int ild = root.lastIndexOf(".");
	 if (ild > 0) {
	    root = root.substring(0, ild);
	 }

	 sb.append("   <" + tag +
		   " name=\"" + fnr + "\"" +
		   " root=\"" + root + "\"/>\n");
      }
   }






}
