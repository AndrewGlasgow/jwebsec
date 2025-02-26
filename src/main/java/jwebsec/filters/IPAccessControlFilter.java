package jwebsec.filters;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <code>IPAccessControlFilter</code> is a simple IP-based access control and
 * security filter which can be used to protect M2M (machine-to-machine) and
 * S2S (service-to-service) applications, web services and micro-services.
 * <table>
 *   <thead>
 *     <tr>
 *       <th>Parameter Name</th>
 *       <th>Type</th>
 *       <th>Default Value</th>
 *       <th>Description</th>
 *       <th>Req/Opt</th>
 *     </tr>
 *   </thead>
 *   <tbody>
 *     <tr>
 *       <td>ALLOWED_IP_ADDRESSES</td>
 *       <td>CSV</td>
 *       <td></td>
 *       <td>Comma-separated values list of allowed IP addresses.</td>
 *       <td>Optional</td>
 *     </tr>
 *     <tr>
 *       <td>ALWAYS_ALLOW_LOCALHOST</td>
 *       <td>boolean</td>
 *       <td>true</td>
 *       <td>Flag indicating whether or not localhost is always allowed.</td>
 *       <td>Optional</td>
 *     </tr>
 *     <tr>
 *       <td>LOG_BLOCKED_REQUESTS</td>
 *       <td>boolean</td>
 *       <td>true</td>
 *       <td>Flag indicating whether or not to log blocked requests.</td>
 *       <td>Optional</td>
 *     </tr>
 *   </tbody>
 * </table>
 * <p>
 *   &copy;2025 jWebSec. All rights reserved.
 * </p>
 *
 * @version 0.1.0
 * @author <a href="mailto:andrew_glasgow.dev@outlook.com">Andrew Glasgow</a>
 */
public final class IPAccessControlFilter implements Filter {
    
    /* Logger */
    private static final Logger LOGGER =
            Logger.getLogger(IPAccessControlFilter.class.getName());
    
    private final Set<String> ALLOWED_IP_ADDRESSES = new LinkedHashSet<>();
    private boolean alwaysAllowLocalhost = true;
    private boolean logBlockedRequests = true;
    
    /**
     * Default constructor.
     */
    public IPAccessControlFilter() {
        super();
    }
    
    @Override
    public void init(final FilterConfig config) throws ServletException {
        String param = config.getInitParameter("ALWAYS_ALLOW_LOCALHOST");
        if (param != null && !(param = param.trim()).isEmpty()) {
            alwaysAllowLocalhost = Boolean.parseBoolean(param);
        }
        param = config.getInitParameter("LOG_BLOCKED_REQUESTS");
        if (param != null && !(param = param.trim()).isEmpty()) {
            logBlockedRequests = Boolean.parseBoolean(param);
        }
        String csv = config.getInitParameter("ALLOWED_IP_ADDRESSES");
        if (csv != null && !(csv = csv.trim()).isEmpty()) {
            try (BufferedReader in =
                    new BufferedReader(new StringReader(csv))) {
                String line;
                String[] tokens;
                while ((line = in.readLine()) != null) {
                    tokens = line.trim().split("\\s*,\\s*");
                    ALLOWED_IP_ADDRESSES.addAll(Arrays.asList(tokens));
                }
            } catch (IOException e) {
                LOGGER.log(
                        Level.SEVERE,
                        "error reading CSV list of allowed IP addresses",
                        e);
            }
        }
    }
    
    @Override
    public void doFilter(
            final ServletRequest req,
            final ServletResponse resp,
            final FilterChain chain) throws IOException, ServletException {
        final String ipAddress = req.getRemoteAddr();
        final String localAddress = req.getLocalAddr();
        if ((alwaysAllowLocalhost && ipAddress.equals(localAddress))
                    || ALLOWED_IP_ADDRESSES.contains(ipAddress)) {
            chain.doFilter(req, resp);
        } else {
            final HttpServletRequest request = (HttpServletRequest)resp;
            final HttpServletResponse response = (HttpServletResponse)resp;
            response.setHeader(
                    "Cache-Control", "private, max-age=1800, must-revalidate");
            response.sendError(403);
            if (logBlockedRequests) {
                StringBuilder msg = new StringBuilder(128);
                msg.append("HTTP request blocked: ");
                msg.append(ipAddress);
                msg.append(" -> ");
                msg.append(request.getMethod());
                msg.append(" ");
                msg.append(request.getRequestURL().toString());
                LOGGER.log(Level.INFO, msg.toString());
            }
        }
    }
    
    @Override
    public void destroy() {
        synchronized (ALLOWED_IP_ADDRESSES) {
            ALLOWED_IP_ADDRESSES.clear();
        }
    }
}
