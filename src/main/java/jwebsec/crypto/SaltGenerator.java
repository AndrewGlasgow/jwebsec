package jwebsec.crypto;

import java.util.Random;
import jwebsec.RNGUtil;

/**
 * <code>SaltGenerator</code> will generate a random array of bytes for a
 * hashing salt value. The salt value should be stored in its own field with the
 * associated user/password record in the database.
 * <p>
 *   &copy;2025 jWebSec. All rights reserved.
 * </p>
 * 
 * @version 0.1.0
 * @author <a href="mailto:andrew_glasgow.dev@outlook.com">Andrew Glasgow</a>
 */
public final class SaltGenerator {
    
    /* Minimum salt length in bytes (16). */
    public static final int MIN_SALT_LENGTH = 16;
    
    /* Maximum salt length in bytes (64). */
    public static final int MAX_SALT_LENGTH = 64;
    
    /* private <code>Random</code> instance. */
    private static final Random RNG = RNGUtil.createRandomInstance();
    
    /**
     * Get random salt of n bytes.
     * 
     * @param n the number of bytes
     * @return the salt
     */
    public static byte[] getSalt(int n) {
        if (n < MIN_SALT_LENGTH) {
            n = MIN_SALT_LENGTH;
        } else if (n > MAX_SALT_LENGTH) {
            n = MAX_SALT_LENGTH;
        }
        byte[] salt = new byte[n];
        RNG.nextBytes(salt);
        return salt;
    }
}
