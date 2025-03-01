package jwebsec;

import java.io.Serializable;
import static jwebsec.JavaUtils.nullSafeCompare;
import static jwebsec.JavaUtils.nullSafeEquals;

/**
 * <code>HttpClient</code> identifies a remote HTTP client.
 * <p>
 *   &copy;2025 jWebSec. All rights reserved.
 * </p>
 * 
 * @version 0.1.1
 * @author <a href="mailto:andrew_glasgow.dev@outlook.com">Andrew Glasgow</a>
 */
public final class HttpClient implements Comparable<HttpClient>, Serializable {
    
    private static final long serialVersionUID = 202502221500L;
    
    private final String ipAddress;
    private final String userAgent;
    private final long initTime = System.currentTimeMillis();
    private volatile Token fingerprint = null;
    
    /**
     * Public constructor.
     * 
     * @param ipAddress the client's IP address
     * @param userAgent the client's user-agent header value
     */
    public HttpClient(String ipAddress, String userAgent) {
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
    }
    
    /**
     * Get this client's IP address.
     * 
     * @return remote IP address
     */
    public String getIpAddress() {
        return ipAddress;
    }
    
    /**
     * Get this client's <code>User-Agent</code> header value.
     * 
     * @return User-Agent header value
     */
    public String getUserAgent() {
        return userAgent;
    }
    
    /**
     * Get the UTC initialization time of this object.
     * 
     * @return the UTC initialization time
     */
    public long getInitTime() {
        return initTime;
    }
    
    /**
     * Get this client's fingerprint token, or null if it has not been set.
     * 
     * @return the fingerprint token or null
     */
    synchronized public Token getFingerprint() {
        return fingerprint;
    }
    
    /**
     * Set this client's fingerprint token.
     * 
     * @param t  the fingerprint token
     */
    synchronized public void setFingerprint(Token t) {
        fingerprint = t;
    }
    
    /**
     * Returns true if this client matches the specified IP address and
     * User-Agent header value. The User-Agent header may be null.
     * 
     * @param ipAddress remote IP address
     * @param userAgent User-Agent header value
     * @return true if this client matches the specified IP address and
     *         User-Agent header value
     */
    public boolean matches(String ipAddress, String userAgent) {
        return this.ipAddress.equals(ipAddress)
                && nullSafeEquals(this.userAgent, userAgent);
    }
    
    @Override
    public boolean equals(final Object ref) {
        boolean eq = this == ref;
        if (!eq && ref instanceof HttpClient client) {
            eq = ipAddress.equals(client.ipAddress)
                    && nullSafeEquals(userAgent, client.userAgent)
                    && nullSafeEquals(fingerprint, client.fingerprint);
        }
        return eq;
    }
    
    @Override
    public int hashCode() {
        return 31 * ipAddress.hashCode()
                + 29 * (userAgent == null ? -1 : userAgent.hashCode())
                + 17 * (fingerprint == null ? -1 : fingerprint.hashCode());
    }
    
    @Override
    public int compareTo(final HttpClient client) {
        int result;
        if (this == client) {
            result = 0;
        } else if (client == null) {
            result = -1;
        } else if ((result = ipAddress.compareTo(client.ipAddress)) == 0
                && (result =
                        nullSafeCompare(userAgent, client.userAgent)) == 0) {
            result = nullSafeCompare(fingerprint, client.fingerprint);
        }
        return result;
    }
}
