package org.lemsml.jlemsviz.plot;


public interface RangeListener {

         int X = 1;
         int Y = 2;
         int BOTH = 3;

     void rangeChanged(int mode, double[] newLimits);

}
