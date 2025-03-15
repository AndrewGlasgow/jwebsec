package jwebsec.crypto;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import jwebsec.JavaUtils;

/**
 * <code>PBKDF2HashProvider</code> provides support for the PBKDF2WithHmacSHA512
 * hash algorithm which should be included with the JRE.
 * <p>
 *   &copy;2025 jWebSec. All rights reserved.
 * </p>
 * 
 * @version 0.2.0
 * @author <a href="mailto:andrew_glasgow.dev@outlook.com">Andrew Glasgow</a>
 */
public final class PBKDF2HashProvider implements HashAlgorithmProvider {
    
    /* PBKDF2WithHmacSHA512 */
    public static final String ALGORITHM_NAME = "PBKDF2WithHmacSHA512";
    
    /* Parameter name: &quot;ITERATION_COUNT&quot; */
    public static final String PARAMETER_NAME_ITERATION_COUNT =
            "ITERATION_COUNT";
    
    /* Default iteration count (65,536). */
    public static final int DEFAULT_ITERATION_COUNT = 65536;
    
    /* Key length in bits (512). */
    public static final int KEY_LENGTH = 512;
    
    private int iterationCount;
    
    /**
     * Default <code>PBKDF2HashProvider</code> constructor.
     */
    public PBKDF2HashProvider() {
        this(DEFAULT_ITERATION_COUNT);
    }
    
    /**
     * <code>PBKDF2HashProvider</code> constructor with <code>PBEKeySpec</code>
     * iteration count.
     * 
     * @param iterationCount the iteration count
     */
    public PBKDF2HashProvider(int iterationCount) {
        this.iterationCount = iterationCount;
    }
    
    /**
     * Return the <code>PBEKeySpec</code> iteration count.
     * 
     * @return the iteration count
     */
    public int getIterationCount() {
        return iterationCount;
    }
    
    /**
     * Set the <code>PBEKeySpec</code> iteration count.
     * 
     * @param i the iteration count
     */
    public void setIterationCount(int i) {
        iterationCount = i;
    }
    
    @Override
    public String getAlgorithmName() {
        return ALGORITHM_NAME;
    }
    
    @Override
    public Object getParameter(String name) {
        Object value;
        if (PARAMETER_NAME_ITERATION_COUNT.equals(name)) {
            value = iterationCount;
        } else {
            value = null;
        }
        return value;
    }

    @Override
    public void setParameter(String name, Object value) {
        if (PARAMETER_NAME_ITERATION_COUNT.equals(name)
                && value instanceof Integer) {
            iterationCount = (Integer)value;
        }
    }
    
    @Override
    public Object removeParameter(String name) {
        return null;
    }
    
    @Override
    public void reset() {
        iterationCount = DEFAULT_ITERATION_COUNT;
    }
    
    @Override
    public byte[] hash(char[] password, byte[] salt) throws HashException {
        try {
            SecretKeyFactory factory =
                    SecretKeyFactory.getInstance(ALGORITHM_NAME);
            KeySpec spec = new PBEKeySpec(
                    password, salt, iterationCount, KEY_LENGTH);
            byte[] hashValue = factory.generateSecret(spec).getEncoded();
            for (int i = 0; i < password.length; i++) {
                password[i] = '*';
            }
            return hashValue;
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new HashException(e);
        }
    }
    
    @Override
    public byte[] hash(
            char[] password, byte[] salt, byte[] pepper) throws HashException {
        return hash(password, JavaUtils.combine(salt, pepper));
    }
}
