package jwebsec.hashing;

/**
 * The <code>HashAlgorithmProvider</code> interface provides a mechanism for
 * using additional hash algorithms beyond those which are included with the
 * Java runtime environment. Some of the currently recommended third-party
 * hashing algorithms include Argon2 and BCrypt.
 * <p>
 *   &copy;2025 jWebSec. All rights reserved.
 * </p>
 * 
 * @version 0.1.0
 * @author <a href="mailto:andrew_glasgow.dev@outlook.com">Andrew Glasgow</a>
 */
public interface HashAlgorithmProvider {
    
    /**
     * Return this hash algorithm's name.
     * 
     * @return this hash algorithm's name
     */
    public String getAlgorithmName();
    
    /**
     * Get a hashing parameter value, or null if not set.
     * 
     * @param name hashing parameter name
     * @return hashing parameter value
     */
    public Object getParameter(String name);
    
    /**
     * Set a hashing parameter value.
     * 
     * @param name hashing parameter name
     * @param value hashing parameter value
     */
    public void setParameter(String name, Object value);
    
    /**
     * Remove and return a hashing parameter value, or null if not set.
     * 
     * @param name hashing parameter name
     * @return hashing parameter value
     */
    public Object removeParameter(String name);
    
    /**
     * Resets this hash algorithm provider instance to its initial state.
     */
    public void reset();
    
    /**
     * Hash the password with the specified salt.
     * 
     * @param password the password to be hashed
     * @param salt the hashing salt value
     * @return resulting hash value
     */
    public byte[] hash(char[] password, byte[] salt);
}
