package jwebsec;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

/**
 * <code>RNGUtil</code> provides some basic random number generation utilities.
 * This class will attempt to use a strong
 * <code>java.security.SecureRandom</code> instance first, falling back to
 * <code>java.util.Random</code> in the event that no such algorithm is
 * available.
 * <p>
 *   &copy;2025 jWebSec. All rights reserved.
 * </p>
 * 
 * @version 0.1.1
 * @author <a href="andrew_glasgow.dev@outlook.com">Andrew Glasgow</a>
 */
public final class RNGUtil {
    
    /* Private <code>Random</code> instance for internal use only. */
    private static final Random RNG =
            createRandomInstance(generateInitialSeed());
    
    /**
     * Generates an initial seed for internal use only.
     * 
     * @return initial seed
     */
    private static long generateInitialSeed() {
        long p = 7691;
        long t = 31 * System.currentTimeMillis();
        for (int i = 0; i < 8; i++) {
            int n = 127 - (int)(0xFF & t >>> 8 * i);
            if (n == 0) {
                n = 128;
            }
            p *= n;
        }
        return p ^ t;
    }
    
    /**
     * Generate a random seed value.
     * 
     * @return random seed
     */
    public static long generateRandomSeed() {
        return RNG.nextLong() ^ RNG.nextLong();
    }
    
    /**
     * Creates a new <code>Random</code> instance using a random seed.
     * 
     * @return a new <code>Random</code> instance
     */
    public static Random createRandomInstance() {
        return createRandomInstance(RNG.nextLong());
    }
    
    /**
     * Creates a new <code>Random</code> instance using the specified seed.
     * 
     * @param seed the seed
     * @return a new <code>Random</code> instance
     */
    public static Random createRandomInstance(long seed) {
        Random rng;
        try {
            rng = SecureRandom.getInstanceStrong();
            rng.setSeed(seed);
        } catch (NoSuchAlgorithmException e) {
            rng = new Random(seed);
        }
        return rng;
    }
    
    /**
     * Convenience method for generating an array of random bytes.
     * 
     * @param length the number of bytes
     * @return an array of random bytes
     */
    public static byte[] nextBytes(int length) {
        byte[] bytes = new byte[length];
        RNG.nextBytes(bytes);
        return bytes;
    }
    
    /**
     * Convenience method for getting the next random int value.
     * 
     * @return the next random int value
     */
    public static int nextInt() {
        return RNG.nextInt();
    }
    
    /**
     * Convenience method for getting the next random int value up to the bound
     * (exclusive).
     * 
     * @param bound the exclusive bound
     * @return the next random int value
     */
    public static int nextInt(int bound) {
        return RNG.nextInt(bound);
    }
    
    /**
     * Convenience method for getting the next random long value.
     * 
     * @return the next random long value
     */
    public static long nextLong() {
        return RNG.nextLong();
    }
    
    /**
     * Convenience method for getting the next random long value up to the bound
     * (exclusive).
     * 
     * @param bound the exclusive bound
     * @return the next random long value
     */
    public static long nextLong(long bound) {
        return RNG.nextLong(bound);
    }
}
