package org.lemsml.jlems.util;

import org.lemsml.jlems.canonical.CanonicalElement;
import org.lemsml.jlems.type.Dimension;

public class DimensionsExport {
	 // TODO this should probably go somewhere else
    public static String getSIUnit(Dimension d) {
        StringBuilder sb = new StringBuilder();

        int m = d.getM();
        int l = d.getL();
        int t = d.getT();
        int n = d.getN();
        int k = d.getK();
        int i = d.getI();
        
       
        if (m == 1) {
            sb.append(" kg");
        } else if (m != 0) {
            sb.append(" kg^" + m);
        }

        if (l == 1) {
            sb.append(" m");
        } else if (l != 0) {
            sb.append(" m^" + l);
        }

        if (t == 1) {
            sb.append(" s");
        } else if (t != 0) {
            sb.append(" s^" + t);
        }

        if (i == 1) {
            sb.append(" A");
        } else if (i != 0) {
            sb.append(" A^" + i);
        }

        if (k == 1) {
            sb.append(" K");
        } else if (k != 0) {
            sb.append(" K^" + i);
        }

        if (n == 1) {
            sb.append(" mol");
        } else if (n != 0) {
            sb.append(" mol^" + i);
        }

        return sb.toString().trim();
    }
    
    
    
    
    
    
    public static CanonicalElement makeCanonical(Dimension d) {
        CanonicalElement ce = new CanonicalElement("Dimension");
        ce.add(new CanonicalElement("name", d.getName()));
        
        int m = d.getM();
        int l = d.getL();
        int t = d.getT();
        int n = d.getN();
        int k = d.getK();
        int i = d.getI();
        
        
        if (m != 0) {
            ce.add(new CanonicalElement("mass", "" + m));
        }
        if (l != 0) {
            ce.add(new CanonicalElement("length", "" + l));
        }
        if (t != 0) {
            ce.add(new CanonicalElement("time", "" + t));
        }
        if (i != 0) {
            ce.add(new CanonicalElement("current", "" + i));
        }
        if (k != 0) {
            ce.add(new CanonicalElement("temperature", "" + i));
        }
        if (n != 0) {
            ce.add(new CanonicalElement("amount", "" + i));
        }
        return ce;
    }

    
    
    
    
    
}
