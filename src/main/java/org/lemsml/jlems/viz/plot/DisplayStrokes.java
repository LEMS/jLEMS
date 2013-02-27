package org.lemsml.jlems.viz.plot;
 

import java.awt.BasicStroke;

import org.lemsml.jlems.core.logging.E;
 
public class DisplayStrokes {


   String[] styleNames;
   int nsn;

   double[] widths;
   int nw;


   BasicStroke[][] strokes;

   public DisplayStrokes(String[] sn, double[] w) {
      styleNames = sn;
      widths = w;
      nsn = styleNames.length;
      nw = widths.length;
      strokes = new BasicStroke[nsn][nw];
   }


   public BasicStroke getStroke(int ilin, int iwin) {
	   int il = ilin;
	   int iw = iwin;
      if (il >= nsn) {
         il = nsn-1;
      }
      if (iw >= nw) {
         iw = nw-1;
      }

      BasicStroke ret = strokes[il][iw];
      if (ret == null) {
         ret = makeStroke(styleNames[il], widths[iw]);
         strokes[il][iw] = ret;
      }
      return ret;
   }



   private BasicStroke makeStroke(String snm, double w) {
     float  fw = (float)w;
     BasicStroke ret = null;

     if (snm.equals("solid")) {
        ret = new BasicStroke(fw);

     } else if (snm.equals("dotted")) {
        float[] dashes= {2, 2, 2, 2};
        ret = new BasicStroke(fw, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL,
                                        10.f,  dashes, 0.f);

     } else if (snm.equals("short dash")) {
        float[] dashes= {6, 6, 6, 6};
        ret = new BasicStroke(fw, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL,
                                        10.f,  dashes, 0.f);

   } else if (snm.equals("long dash")) {
      float[] dashes= {8, 4, 8, 4};
      ret = new BasicStroke(fw, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL,
                                      10.f,  dashes, 0.f);


   } else if (snm.equals("dot-dash")) {
      float[] dashes= {2, 2, 8, 2};
      ret = new BasicStroke(fw, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL,
                                      10.f,  dashes, 0.f);


   } else {
      E.warning("unrecognized line stype " + snm);
      ret = new BasicStroke(fw);
   }

     return ret;
   }


}
