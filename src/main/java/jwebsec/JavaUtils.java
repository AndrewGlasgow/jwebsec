package jwebsec;

/**
 * <code>JavaUtils</code> contains general helper methods for the Java language.
 * <p>
 *   &copy;2025 jWebSec. All rights reserved.
 * </p>
 * 
 * @version 0.2.0
 * @author <a href="mailto:andrew_glasgow.dev@outlook.com">Andrew Glasgow</a>
 */
public final class JavaUtils {
    
    /**
     * This method performs a null safe comparison of two
     * <code>Comparable</code> object references, fulfilling the general
     * contract of the <code>Comparable.compareTo()</code> method as if invoking
     * <code>ref1.compareTo(ref2)</code>, without the possibility of throwing a
     * <code>NullPointerException</code>.
     * <p>
     *   This method returns:
     * </p>
     * <ul>
     *   <li>-1 if (ref1 != null &amp;&amp; ref2 == null) ||
     *     (ref1.compareTo(ref2) &lt; 0)</li>
     *   <li>0 if ref1 == ref2 || ref1.equals(ref2)</li>
     *   <li>1 if (ref1 == null &amp;&amp; ref != null) ||
     *     (ref1.compareTo(ref2) &gt; 0)</li>
     * </ul>
     * 
     * @param ref1 first comparable object reference for comparison
     * @param ref2 seconds comparable object reference for comparison
     * @return comparison result
     */
    public static int nullSafeCompare(Comparable ref1, Comparable ref2) {
        int result;
        if (ref1 == ref2) {
            result = 0;
        } else if (ref1 == null) {
            result = 1;
        } else {
            result = ref1.compareTo(ref2);
        }
        return result;
    }
    
    /**
     * Returns true if two object references are equal, as defined by the
     * general contract of the <code>equals()</code> method. This method is
     * null-safe.
     * 
     * @param ref1
     * @param ref2
     * @return true if two object references are equal, otherwise false
     */
    public static boolean nullSafeEquals(Object ref1, Object ref2) {
        boolean eq = ref1 == ref2;
        if (!eq && ref1 != null) {
            eq = ref1.equals(ref2);
        }
        return eq;
    }
    
    private JavaUtils() {
        // reserved
    }
}
