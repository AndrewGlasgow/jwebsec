package jwebsec;

import java.beans.Transient;
import java.io.Serializable;

/**
 * <code>Token</code> encapsulates a token ID and its expiration time.
 * <p>
 *   &copy;2025 jWebSec. All rights reserved.
 * </p>
 * 
 * @version 0.2.0
 * @author <a href="mailto:andrew_glasgow.dev@outlook.com">Andrew Glasgow</a>
 */
public final class Token implements Comparable<Token>, Serializable {
    
    private static final long serialVersionUID = 202502271934L;
    
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
    @Transient
    public boolean isExpired() {
        return expiryTime <= System.currentTimeMillis();
    }
    
    /**
     * Returns true if this token is valid, e.g., not expired.
     * @return true if this token is valid
     */
    @Transient
    public boolean isValid() {
        return expiryTime > System.currentTimeMillis();
    }
    
    /**
     * Returns the number of seconds remaining before this token expires.
     * 
     * @return seconds until token expiration
     */
    @Transient
    public int getSecondsUntilExpiration() {
        return (int)((expiryTime - System.currentTimeMillis()) / 1000);
    }
    
    @Override
    public int compareTo(Token t) {
        int result;
        if (this == t) {
            result = 0;
        } else if (t == null) {
            result = -1;
        } else if ((result = id.compareTo(t.id)) == 0) {
            long diff = expiryTime - t.expiryTime;
            if (diff == 0) {
                result = 0;
            } else {
                result = diff < 0 ? -1 : 1;
            }
        }
        return result;
    }
}
