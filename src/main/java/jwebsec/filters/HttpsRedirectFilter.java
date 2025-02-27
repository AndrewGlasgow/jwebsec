package jwebsec.filters;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <code>HttpsRedirectFilter</code> redirects insecure HTTP requests to HTTPS.
 * <p>
 *   It is recommended that this web filter be declared and mapped in the
 *   application's deployment descriptor file (web.xml) before any other
 *   filters.
 * </p>
 * <div>
 *   <table>
 *     <thead>
 *       <tr>
 *         <th>Parameter Name</th>
 *         <th>Type</th>
 *         <th>Default Value</th>
 *         <th>Description</th>
 *         <th>Req/Opt</th>
 *       </tr>
 *     </thead>
 *     <tbody>
 *       <tr>
 *         <td>STATUS_CODE</td>
 *         <td>int</td>
 *         <td>301</td>
 *         <td>The HTTP redirection status code sent to clients.</td>
 *         <td>Optional</td>
 *       </tr>
 *       <tr>
 *         <td>HTTPS_PORT</td>
 *         <td>int</td>
 *         <td>443</td>
 *         <td>Include the query string for redirection of GET requests.</td>
 *         <td>Optional</td>
 *       </tr>
 *       <tr>
 *         <td>INCLUDE_QUERY_STRING</td>
 *         <td>boolean</td>
 *         <td>false</td>
 *         <td>Include the query string for redirection of GET requests.</td>
 *         <td>Optional</td>
 *       </tr>
 *     </tbody>
 *   </table>
 * </div>
 * <p>&copy;2025 jWebSec. All rights reserved.</p>
 * 
 * @version 0.1.0
 * @author <a href="mailto:andrew_glasgow.dev@outlook.com">Andrew Glasgow</a>
 */
public final class HttpsRedirectFilter implements Filter {
    
    /* Default HTTP status code for redirection (301). */
    public static final int DEFAULT_STATUS_CODE = 301;
    
    /* Default HTTPS port (443). */
    public static final int DEFAULT_HTTPS_PORT = 443;
    
    /* HTTP redirection status code. */
    private int statusCode = DEFAULT_STATUS_CODE;
    
    /* HTTPS port. */
    private int httpsPort = DEFAULT_HTTPS_PORT;
    
    /* Include query string in redirection of GET requests. */
    private boolean includeQueryString = false;
    
    /**
     * Default constructor.
     */
    public HttpsRedirectFilter() {
        super();
    }
    
    @Override
    public void init(final FilterConfig config) throws ServletException {
        String param = config.getInitParameter("STATUS_CODE");
        if (param != null && !param.isEmpty()) {
            try {
                statusCode = Integer.parseInt(param);
            } catch (NumberFormatException e) {}
        }
        param = config.getInitParameter("HTTPS_PORT");
        if (param != null && !param.isEmpty()) {
            try {
                httpsPort = Integer.parseInt(param);
            } catch (NumberFormatException e) {}
        }
        includeQueryString = Boolean.parseBoolean(
                config.getInitParameter("INCLUDE_QUERY_STRING"));
    }

    @Override
    public void doFilter(
            final ServletRequest req,
            final ServletResponse resp,
            final FilterChain chain) throws IOException, ServletException {
        if (req.isSecure() && req.getScheme().equals("https")) {
            chain.doFilter(req, resp);
        } else {
            final HttpServletRequest request = (HttpServletRequest)req;
            final HttpServletResponse response = (HttpServletResponse)resp;
            StringBuilder url = new StringBuilder(512);
            url.append("https://");
            url.append(request.getLocalName());
            if (httpsPort != DEFAULT_HTTPS_PORT) {
                url.append(":");
                url.append(httpsPort);
            }
            url.append(request.getRequestURI());
            if (includeQueryString && request.getMethod().equals("GET")) {
                String query = request.getQueryString();
                if (query != null && !query.isEmpty()) {
                    url.append("?");
                    url.append(query);
                }
            }
            response.setStatus(statusCode);
            response.sendRedirect(url.toString());
        }
    }
}
