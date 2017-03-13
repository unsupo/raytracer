package utilities;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;

import static java.math.RoundingMode.*;

/**
 * Created by jarndt on 3/7/17.
 */
public class Utility {

    public static Number addNumbers(Number... values) {
        Object o = null;
        for(Number n : values)
            if((o == null || o instanceof Integer) && n instanceof Integer)
                o = new Integer(0);
            else if((o == null || o instanceof Long) && n instanceof Long)
                o = new Long(0);
            else if((o == null || o instanceof Float) && n instanceof Float)
                o = new Float(0);
            else if((o == null || o instanceof Double) && n instanceof Double)
                o = new Double(0);
            else if((o == null || o instanceof BigInteger) && n instanceof BigInteger)
                o = BigInteger.ONE;
            else if((o == null || o instanceof BigDecimal) && n instanceof BigDecimal)
                o = BigDecimal.ONE;

        if(o instanceof BigDecimal)
            return Arrays.asList(values).stream().map(a->(BigDecimal)a).reduce(BigDecimal.ZERO,(a,b)->a.add(b));
        if(o instanceof BigInteger)
            return Arrays.asList(values).stream().map(a->(BigInteger)a).reduce(BigInteger.ZERO,(a,b)->a.add(b));
        if(o instanceof Double)
            return Arrays.asList(values).stream().map(a->(Double)a).reduce(0.,(a,b)->a+b);
        if(o instanceof Float)
            return Arrays.asList(values).stream().map(a->(Float)a).reduce(0F,(a,b)->a+b);
        if(o instanceof Long)
            return Arrays.asList(values).stream().map(a->(Long)a).reduce(0L,(a,b)->a+b);

        return Arrays.asList(values).stream().map(a->(Integer)a).reduce(0,(a,b)->a+b);

    }public static Number subtractNumbers(Number a, Number b) {
        if(a instanceof BigDecimal || b instanceof BigDecimal)
            return ((BigDecimal)a).subtract((BigDecimal)b);
        if(a instanceof BigInteger || b instanceof BigInteger)
            return ((BigInteger)a).subtract((BigInteger)b);
        if(a instanceof Double || b instanceof Double)
            return new Double(a.doubleValue() - b.doubleValue());
        if(a instanceof Float || b instanceof Float)
            return new Float(a.floatValue() - b.floatValue());
        if(a instanceof Long || b instanceof Long)
            return new Long(a.longValue() - b.longValue());

        return new Integer(a.intValue() - b.intValue());
    }public static Number multiplyNumbers(Number a, Number b) {
        if(a instanceof BigDecimal || b instanceof BigDecimal)
            return ((BigDecimal)a).multiply((BigDecimal)b);
        if(a instanceof BigInteger || b instanceof BigInteger)
            return ((BigInteger)a).multiply((BigInteger)b);
        if(a instanceof Double || b instanceof Double)
            return new Double(a.doubleValue() * b.doubleValue());
        if(a instanceof Float || b instanceof Float)
            return new Float(a.floatValue() * b.floatValue());
        if(a instanceof Long || b instanceof Long)
            return new Long(a.longValue() * b.longValue());

        return new Integer(a.intValue() * b.intValue());
    }public static Number divideNumbers(Number a, Number b) {
        if(a instanceof BigDecimal || b instanceof BigDecimal)
            return ((BigDecimal)a).divide((BigDecimal)b);
        if(a instanceof BigInteger || b instanceof BigInteger)
            return ((BigInteger)a).divide((BigInteger)b);
        if(a instanceof Double || b instanceof Double)
            return new Double(a.doubleValue() / b.doubleValue());
        if(a instanceof Float || b instanceof Float)
            return new Float(a.floatValue() / b.floatValue());
        if(a instanceof Long || b instanceof Long)
            return new Long(a.longValue() / b.longValue());

        return new Integer(a.intValue() / b.intValue());
    }

    public static Number sqrtNumber(Number a) {
        if(a instanceof BigDecimal)
            return bigSqrt((BigDecimal)a);
        if(a instanceof BigInteger)
            return bigSqrt(new BigDecimal((BigInteger)a)).toBigInteger();
        if(a instanceof Double)
            return Math.sqrt(new Double(a.doubleValue()));
        if(a instanceof Float)
            return Math.sqrt(new Float(a.floatValue()));
        if(a instanceof Long)
            return Math.sqrt(new Long(a.longValue()));

        return Math.sqrt(new Integer(a.intValue()));
    }
    private static final BigDecimal SQRT_DIG = new BigDecimal(150);
    private static final BigDecimal SQRT_PRE = new BigDecimal(10).pow(SQRT_DIG.intValue());

    /**
     * Private utility method used to compute the square root of a BigDecimal.
     *
     * @author Luciano Culacciatti
     * @url http://www.codeproject.com/Tips/257031/Implementing-SqrtRoot-in-BigDecimal
     */
    private static BigDecimal sqrtNewtonRaphson  (BigDecimal c, BigDecimal xn, BigDecimal precision){
        BigDecimal fx = xn.pow(2).add(c.negate());
        BigDecimal fpx = xn.multiply(new BigDecimal(2));
        BigDecimal xn1 = fx.divide(fpx,2*SQRT_DIG.intValue(), HALF_DOWN);
        xn1 = xn.add(xn1.negate());
        BigDecimal currentSquare = xn1.pow(2);
        BigDecimal currentPrecision = currentSquare.subtract(c);
        currentPrecision = currentPrecision.abs();
        if (currentPrecision.compareTo(precision) <= -1){
            return xn1;
        }
        return sqrtNewtonRaphson(c, xn1, precision);
    }

    /**
     * Uses Newton Raphson to compute the square root of a BigDecimal.
     *
     * @author Luciano Culacciatti
     * @url http://www.codeproject.com/Tips/257031/Implementing-SqrtRoot-in-BigDecimal
     */
    public static BigDecimal bigSqrt(BigDecimal c){
        return sqrtNewtonRaphson(c,new BigDecimal(1),new BigDecimal(1).divide(SQRT_PRE));
    }
}
