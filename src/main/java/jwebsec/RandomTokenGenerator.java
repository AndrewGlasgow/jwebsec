package jwebsec;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;

/**
 * <code>RandomTokenGenerator</code> can be used to generate reasonably secure
 * random tokens for a variety of purposes. This class will attempt to
 * initialize a strong <code>java.security.SecureRandom</code> instance, and
 * fallback to <code>java.util.Random</code> if no strong algorithm is
 * available.
 * <p>
 *   It is generally recommended to use alphanumeric (Base-62) tokens, as that
 *   should provide ample security without requiring any special encoding, such
 *   as is generally the case for Base-64 strings which must be URL encoded
 *   before being passed as a request parameter in an URL.
 * </p>
 * <p>
 *   &copy;2025 jWebSec. All rights reserved.
 * </p>
 * 
 * @version 0.2.0
 * @author <a href="mailto:andrew_glasgow.dev@outlook.com">Andrew Glasgow</a>
 */
public final class RandomTokenGenerator {
    
    /* Minimum token length (32). */
    public static final int MIN_TOKEN_LENGTH = 32;
    
    /* Maximum token length (4,096). */
    public static final int MAX_TOKEN_LENGTH = 4096;
    
    /* Default token length (64). */
    public static final int DEFAULT_TOKEN_LENGTH = 64;
    
    /* Standard English alphanumeric characters. */
    private static final char[] ALPHANUMERIC_CHARS = new char[]{
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
        'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
        'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd',
        'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
        'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
        'y', 'z'
    };
    
    /**
     * Checks the token length to ensure that it is between the minimum and
     * maximum token value lengths.
     * 
     * @param length the length of the token in characters
     * @return the validated token length
     */
    private static int checkTokenLength(int length) {
        if (length < MIN_TOKEN_LENGTH) {
            length = MIN_TOKEN_LENGTH;
        } else if (length > MAX_TOKEN_LENGTH) {
            length = MAX_TOKEN_LENGTH;
        }
        return length;
    }
    
    /**
     * Generate a seed for RNG.
     * 
     * @return an RNG seed
     */
    private static long generateSeed() {
        long seed = 31 * System.currentTimeMillis();
        for (int i = 0; i < 8; i++) {
            seed *= 127 - (0xFF & (seed >> (8 * i)));
        }
        return seed;
    }
    
    private final Random RNG;
    
    /**
     * The default constructor will automatically generate a seed for RNG.
     */
    public RandomTokenGenerator() {
        this(generateSeed());
    }
    
    /**
     * Construct a new <code>TokenGenerator</code> with the specified seed.
     * 
     * @param seed RNG seed
     */
    public RandomTokenGenerator(long seed) {
        Random rng;
        try {
            rng = SecureRandom.getInstanceStrong();
            rng.setSeed(seed);
        } catch (NoSuchAlgorithmException e) {
            rng = new Random(seed);
        }
        RNG = rng;
    }
    
    /**
     * Generate a character token ID of the specified length and radix.
     * 
     * @param length the token ID length
     * @param radix the radix for the character set
     * @return character token ID
     */
    private String createCharTokenValue(int length, int radix) {
        StringBuilder value = new StringBuilder(checkTokenLength(length));
        for (int i = 0; i < length; i++) {
            value.append(ALPHANUMERIC_CHARS[RNG.nextInt(radix)]);
        }
        return value.toString();
    }
    
    /**
     * Create an alphanumeric [0-9A-Za-z] token with the specified ID, value
     * length and expiry time.
     * 
     * @param id the token ID/name
     * @param length the token value length
     * @param expiryTime the UTC expiry time
     * @return a new alphanumeric token
     */
    public Token createAlphanumericToken(
            String id, int length, long expiryTime) {
        return new Token(
                id,
                createCharTokenValue(length, ALPHANUMERIC_CHARS.length),
                System.currentTimeMillis(),
                expiryTime);
    }
    
    /**
     * Create an Base-64 [0-9A-Za-z+/=] token with the specified token ID/name,
     * value length, and expiry time.
     * 
     * @param id the token ID/name
     * @param length the token value length
     * @param expiryTime the UTC expiry time
     * @return a new Base-64 token
     */
    public Token createBase64Token(String id, int length, long expiryTime) {
        length = 3 * checkTokenLength(length) / 4;
        int r = length % 4;
        if (r > 0) {
            length += 4 - r;
        }
        byte[] bytes = new byte[length];
        RNG.nextBytes(bytes);
        return new Token(
                id,
                Base64.getEncoder().encodeToString(bytes),
                System.currentTimeMillis(),
                expiryTime);
    }
    
    /**
     * Create an hexadecimal [0-9A-F] token with the specified token ID length
     * and expiry time.
     * 
     * @param id the token ID/name
     * @param length the token value length
     * @param expiryTime the UTC expiry time
     * @return a new hexadecimal token
     */
    public Token createHexadecimalToken(
            String id, int length, long expiryTime) {
        return new Token(
                id,
                createCharTokenValue(length, 16),
                System.currentTimeMillis(),
                expiryTime);
    }
}
