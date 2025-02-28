package jwebsec;

/**
 * <code>Token</code> encapsulates a token ID and its expiration time.
 * <p>
 *   &copy;2025 jWebSec. All rights reserved.
 * </p>
 * 
 * @version 0.1.1
 * @author <a href="mailto:andrew_glasgow.dev@outlook.com">Andrew Glasgow</a>
 */
public final class Token {
    
    private final String id;
    private final long expiryTime;
    
    /**
     * Constructor.
     * 
     * @param id the token ID
     * @param expiryTime the token's UTC expiration time
     */
    public Token(String id, long expiryTime) {
        this.id = id;
        this.expiryTime = expiryTime;
    }
    
    /**
     * Get this token's ID.
     * 
     * @return id
     */
    public String getId() {
        return id;
    }
    
    /**
     * Get this token's UTC expiry time.
     * 
     * @return expiry time
     */
    public long getExpiryTime() {
        return expiryTime;
    }
    
    /**
     * Returns true if this token is expired, e.g., not valid.
     * 
     * @return true if this token is expired
     */
    public boolean isExpired() {
        return expiryTime <= System.currentTimeMillis();
    }
    
    /**
     * Returns true if this token is valid, e.g., not expired.
     * @return true if this token is valid
     */
    public boolean isValid() {
        return expiryTime > System.currentTimeMillis();
    }
    
    /**
     * Returns the number of seconds remaining before this token expires.
     * 
     * @return seconds until token expiration
     */
    public int getSecondsUntilExpiration() {
        return (int)((expiryTime - System.currentTimeMillis()) / 1000);
    }
}
