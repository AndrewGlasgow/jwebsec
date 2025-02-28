package jwebsec;

/**
 * <code>JavaUtils</code> contains general helper methods for the Java language.
 * <p>
 *   &copy;2025 jWebSec. All rights reserved.
 * </p>
 * 
 * @version 0.1.0
 * @author <a href="mailto:andrew_glasgow.dev@outlook.com">Andrew Glasgow</a>
 */
public final class JavaUtils {
    
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
