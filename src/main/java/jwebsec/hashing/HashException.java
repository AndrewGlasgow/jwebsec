package jwebsec.hashing;

import java.security.GeneralSecurityException;

/**
 * A <code>HashException</code> should be thrown by a
 * <code>HashAlgorithmProvider</code> implementation when a hashing operation
 * fails or there is no such algorithm available.
 * <p>
 *   &copy;2025 jWebSec. All rights reserved.
 * </p>
 * 
 * @version 0.1.0
 * @author <a href="mailto:andrew_glasgow.dev@outlook.com">Andrew Glasgow</a>
 */
public class HashException extends Exception {
    
    public HashException(String msg) {
        super(msg);
    }
    
    public HashException(GeneralSecurityException e) {
        super(e);
    }
}
