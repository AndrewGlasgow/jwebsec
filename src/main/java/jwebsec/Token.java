package jwebsec;

import java.beans.Transient;
import java.io.Serializable;

/**
 * <code>Token</code> encapsulates a security token ID/name, value, and expiry
 * time.
 * <p>
 *   &copy;2025 jWebSec. All rights reserved.
 * </p>
 * 
 * @version 0.4.1
 * @author <a href="mailto:andrew_glasgow.dev@outlook.com">Andrew Glasgow</a>
 */
public final class Token implements Comparable<Token>, Serializable {
    
    private static final long serialVersionUID = 202502271934L;
    
    private final String id;
    private final String value;
    private final long issuedTime;
    private final long expiryTime;
    
    /**
     * Constructor.
     * 
     * @param id the token ID
     * @param value the token's value
     * @param issuedTime
     * @param expiryTime the token's UTC expiration time
     */
    public Token(String id, String value, long issuedTime, long expiryTime) {
        this.id = id;
        this.value = value;
        this.issuedTime = issuedTime;
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
     * Get this token's value.
     * 
     * @return value
     */
    public String getValue() {
        return value;
    }
    
    /**
     * Get the UTC time this token was issued.
     * 
     * @return issued time
     */
    public long getIssuedTime() {
        return issuedTime;
    }
    
    /**
     * Get the current age of this token in milliseconds.
     * 
     * @return current age in milliseconds
     */
    @Transient
    public long getCurrentAge() {
        return System.currentTimeMillis() - issuedTime;
    }
    
    /**
     * Get the current age of this token in seconds.
     * 
     * @return current age in seconds
     */
    @Transient
    public long getCurrentAgeInSeconds() {
        return getCurrentAge() / 1000;
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
    public boolean equals(Object ref) {
        boolean eq = this == ref;
        if (!eq && ref instanceof Token token) {
            eq = id.equals(token.id)
                    && value.equals(token.value)
                    && issuedTime == token.issuedTime
                    && expiryTime == token.expiryTime;
        }
        return eq;
    }
    
    @Override
    public int hashCode() {
        return JavaUtils.hashCode(id, value);
    }
    
    @Override
    public int compareTo(Token t) {
        int result;
        if (this == t) {
            result = 0;
        } else if (t == null) {
            result = -1;
        } else if ((result = id.compareTo(t.id)) == 0
                    && (result = value.compareTo(t.value)) == 0) {
            long diff = issuedTime - t.issuedTime;
            if (diff == 0) {
                diff = expiryTime - t.expiryTime;
            }
            if (diff == 0) {
                result = 0;
            } else {
                result = diff < 0 ? -1 : 1;
            }
        }
        return result;
    }
}
