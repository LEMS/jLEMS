package org.lemsml.jlems.viz.plot;

public class DisplayLine {

	int npts = 0;
	
	double[] xpts = new double[100];
	double[] ypts = new double[100];
	
	String name;
	
	SColor scolor;
	
	
	public DisplayLine(String sl) {
		name = sl;
		scolor = new SColor("#ffffff");
	}

	public DisplayLine(String sl, String sc) {
		name = sl;
		if (sc == null) {
			scolor = new SColor("#ffffff");
		} else {
			scolor = new SColor(sc);
		}
	}
	
	
	public void setPoints(double[] a, double[] b) {
		xpts = a;
		ypts = b;
		npts = xpts.length;
	}
	
	
	public SColor getColor() {
		 return scolor;
	}

	public double[] getXpts() {
		return xpts;
	}
	
	public double[] getYpts() {
		return ypts;
	}

	public int getNpts() {
		return npts;
	}

    public String getName() {
        return name;
    }

    
	
	public void addPoint(double x, double y) {
		if (npts == xpts.length) {
			int nnew = (int)Math.round(1.5 * npts);
			double[] xpn = new double[nnew];
			double[] ypn = new double[nnew];
			for (int i = 0; i < npts; i++) {
				xpn[i] = xpts[i];
				ypn[i] = ypts[i];
			}
			xpts = xpn;
			ypts = ypn;
		}
		xpts[npts] = x;
		ypts[npts] = y;
		npts += 1;
	}
	
	
}
