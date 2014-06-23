package org.lemsml.jlems.core.type;

import java.util.HashSet;

import org.lemsml.jlems.core.expression.ParseError;
import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.core.sim.ContentError;

public final class QuantityReader {

    // static Pattern pat = Pattern.compile("(^[\\d\\.\\+-]*(?:[Ee][\\+-]?[\\d]+)?)(.*?)$");
    static HashSet<String> numHS;

    static QuantityReader instance;

    static {
        numHS = new HashSet<String>();
        for (int i = 0; i <= 9; i++) {
            numHS.add("" + i);
        }
        numHS.add(".");
        numHS.add("+");
        numHS.add("-");
    }

    private QuantityReader() {

    }

    public static DimensionalQuantity parseValue(String aarg, LemsCollection<Unit> units) throws ParseError, ContentError {
        return getInstance().parseVal(aarg, units);
    }

    public static QuantityReader getInstance() {
        if (instance == null) {
            instance = new QuantityReader();
        }
        return instance;
    }

    public DimensionalQuantity parseVal(String aarg, LemsCollection<Unit> units) throws ParseError, ContentError {
        String arg = aarg.trim();
        
        DimensionalQuantity ret = new DimensionalQuantity();
        try {
            double d = Double.parseDouble(aarg);
            ret.setOriginalText(aarg);
            Unit noUnit = units.getByPseudoName(Unit.NO_UNIT_SYMBOL);
            if (noUnit==null)
                noUnit = Unit.getNoUnit();
            ret.setValue(d, noUnit);
            return ret;
        } catch (NumberFormatException nfe) {
            // continue...
        }

        if (arg.trim().length() == 0) {
            ret.setNoValue();

        } else {

            String[] snv = splitToMagnitudeAndUnit(arg);

            String snum = snv[0];
            String su = snv[1];

            double d = Double.parseDouble(snum);
            // E.info("Val is " +d+", unit is "+su);

            if (su == null || su.length() <= 0) {
                ret.setValue(d, units.getByPseudoName("none"));

            } else {
                su = su.trim();
                if (units.hasPseudoName(su)) {
                    ret.setValue(d, units.getByPseudoName(su));

                } else {
                    String msg = "Unrecognized units: " + su + " when parsing " + aarg;
                    E.info(msg + "\n" + "known units: " + units);
                    throw new ParseError("Unrecognized units " + su);
                }
            }

            ret.setOriginalText(arg);

        }
        return ret;
    }

    public static String[] splitToMagnitudeAndUnit(String arg) {
        
        String[] ret = new String[2];
        
        try {
            double d = Double.parseDouble(arg);
            ret[0] = d+"";
            ret[1] = "";
            return ret;
        } catch (NumberFormatException nfe) {
            // continue...
        }
        
        int ild = 0;
        if (arg.indexOf(" ") > 0) {
            ild = arg.indexOf(" ");
        } else {
            ild = lastNumIndex(arg);
        }

        String snum = arg;
        String su = "";
        if (ild > 0) {
            snum = arg.substring(0, ild);
            su = arg.substring(ild, arg.length());
        }
        ret[0] = snum;
        ret[1] = su.trim();
        return ret;
    }

    private static int lastNumIndex(String str) {
        int ret = 0;
        int sl = str.length();
        boolean doneE = false;

        while (true) {
            if (ret >= sl) {
                break;
            }
            String sn = str.substring(ret, ret + 1);
            if (ret < sl && numHS.contains(sn)) {
                // continue
                ret += 1;
            } else if (!doneE && (sn.equals("e") || sn.equals("E")) && ret < sl - 2 && numHS.contains(str.substring(ret + 1, ret + 2))) {
                doneE = true;
                ret += 1;

            } else {
                break;
            }
        }
        return ret;
    }

    public static void main(String[] argv) {

        QuantityReader qr = QuantityReader.getInstance();
        qr.runChecks();
    }

    public void runChecks() {
        E.info("numhs is " + numHS);
        String[] qs = {"1 mV", "1.234mV", "1.2e-4 mV", "1.23e-5A", "1.23e4A", "1.45E-8 m", "1.23E-8m2", "60", "6000", "123000", "-1.00000008E8"};
        for (String s : qs) {
            splitOne(s);
            
        }
    }

    private void splitOne(String s) {
        String[] snv = splitToMagnitudeAndUnit(s);
        E.info("Split " + s + " into: [" + snv[0] + "] and [" + snv[1]+"]");
    }

}
