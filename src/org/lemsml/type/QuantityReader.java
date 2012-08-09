package org.lemsml.type;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.lemsml.expression.ParseError;
import org.lemsml.util.ContentError;
import org.lemsml.util.E;

public class QuantityReader {

	// static String unitsrex = "([:alpha: _]*)$";
	// static Pattern exppat = Pattern.compile("\\((.*)\\)(.*?)$");

	static Pattern pat = Pattern.compile("(^[\\d\\.\\+-]*(?:[Ee][\\+-]?[\\d]+)?)(.*?)$");

	public static DimensionalQuantity parseValue(String arg, LemsCollection<Unit> units) throws ParseError, ContentError {

		DimensionalQuantity ret = new DimensionalQuantity();
		if (arg.trim().length() == 0) {
			ret.setNoValue();

		} else {

			Matcher matcher = pat.matcher(arg);

			if (matcher.find()) {
				String snum = matcher.group(1); // + matcher.group(2);
				String su = matcher.group(2);

				double d = Double.parseDouble(snum);
				// E.info("Val is " +d+", unit is "+su);

				if (su == null || su.length() <= 0) {
					ret.setValue(d, units.getByPseudoName("none"));

				} else {
					su = su.trim();
					if (units.hasPseudoName(su)) {
						ret.setValue(d, units.getByPseudoName(su));

					} else {
						E.info("Unrecognized units: " + su + "\nknown units:\n" + units);
						throw new ParseError("Unrecognized units " + su);
					}
				}

				ret.setOriginalText(arg);

			} else {
				E.error("Can't parse " + arg);
			}
		}
		return ret;
	}

}
