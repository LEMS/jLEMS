package org.lemsml.jlems.viz.plot;



// REFAC this all needs cleaning up a bit;


public class PickStore {

	int npoint;
	Pickable[] points;
	int[][] pointBoxes;


	int nregion;
	PickableRegion[] regions;
	int[][] regionRefs;



	// sloppy buffering;
	int xref = 0;
	int yref = 0;


	public PickStore() {
		points = new Pickable[10];
		pointBoxes = new int[10][4];

		regions = new PickableRegion[10];
		regionRefs = new int[10][2];
	}


	public void clear() {
		npoint = 0;
		nregion = 0;
	}


	public void addPoint(PickablePoint pbl, int xc, int yc) {

		int hrng = (pbl.getRange()) / 2;

		int icx0 = xc - hrng;
		int icy0 = yc - hrng;

		int icx1 = xc + hrng;
		int icy1 = yc + hrng;
		addPoint(pbl, icx0, icy0, icx1, icy1);
	}


	public void addPoint(Pickable pbl, int xa, int ya, int xb, int yb) {
		if (npoint >= points.length) {
			int nnew = (int) (1.5 * points.length);

			Pickable[] npb = new Pickable[nnew];
			int[][] ab = new int[nnew][4];

			for (int i = 0; i < npoint; i++) {
				npb[i] = points[i];
				// more efficient? - keeps memory intact? TOOD
				int[] aab = ab[i];
				int[] apb = pointBoxes[i];
				aab[0] = apb[0];
				aab[1] = apb[1];
				aab[2] = apb[2];
				aab[3] = apb[3];
			}
			points = npb;
			pointBoxes = ab;
		}
		points[npoint] = pbl;
		int[] apb = pointBoxes[npoint];
		apb[0] = xa;
		apb[1] = ya;
		apb[2] = xb;
		apb[3] = yb;

		pbl.setCache(npoint);
		npoint += 1;
	}


	public void addPickableRegion(PickableRegion pbl, int xr, int yr) {
		if (nregion >= regions.length) {
			int nnew = (int) (1.5 * regions.length + 10);
			PickableRegion[] npb = new PickableRegion[nnew];
			int[][] nrc = new int[nnew][2];

			for (int i = 0; i < nregion; i++) {
				npb[i] = regions[i];
				nrc[i][0] = regionRefs[i][0];
				nrc[i][1] = regionRefs[i][1];
			}
			regions = npb;
			regionRefs = nrc;
		}
		regions[nregion] = pbl;
		regionRefs[nregion][0] = xr;
		regionRefs[nregion][1] = yr;
		nregion += 1;

	}


	private boolean within(int[] xyxy, int x, int y) {
		return ((x > xyxy[0]) && (x < xyxy[2]) && (y > xyxy[1]) && (y < xyxy[3]));
	}


	public Pickable getClaimant(int mx, int my, double dx, double dy) {
		Pickable ret = null;
		for (int j = npoint - 1; j >= 0; j--) {
			if (within(pointBoxes[j], mx, my)) {
				ret = points[j];

				int[] pbox = pointBoxes[j];
				xref = (pbox[0] + pbox[2]) / 2;
				yref = (pbox[1] + pbox[3]) / 2;

				break;
			}
		}

		if (ret == null) {
			for (int j = nregion - 1; j >= 0; j--) {
				PickableRegion pr = regions[j];
				if (pr.contains(dx, dy)) {
					ret = pr;
					xref = regionRefs[j][0];
					yref = regionRefs[j][1];

					break;
				}
			}
		}


		return ret;
	}


	public int getClaimantRefX() {
		return xref;
	}


	public int getClaimantRefY() {
		return yref;
	}


	public boolean itemClaims(Pickable pbl, int mx, int my, double dx, double dy) {
		boolean ret = false;
		int ind = pbl.getCache();


		if (pbl instanceof PickableRegion) {
			if (((PickableRegion) pbl).contains(dx, dy)) {
				ret = true;
			}
		} else {
			if (within(pointBoxes[ind], mx, my)) {
				ret = true;
			}
		}


		return ret;
	}


	public int[] getEchoBox(PickablePoint pp) {
		return pointBoxes[pp.getCache()];
	}



}
