package remvn.recard.utils;

import java.math.BigDecimal;

public class NumberUtils {

	public static double subtractUsingBigDecimalOperation(double a, double b) {
		BigDecimal c = BigDecimal.valueOf(a).subtract(BigDecimal.valueOf(b));
		return c.doubleValue();
	}

}
