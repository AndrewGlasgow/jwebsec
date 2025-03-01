package jwebsec.filters;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <code>HttpResponseHeadersFilter</code> provides a mechanism for setting HTTP
 * response headers for all web resource paths matching the filter mapping. This
 * can be especially useful for setting Cache-Control, CORS, HSTS, and other
 * content security policy (CSP) headers.
 * <div>
 *   <table>
 *     <caption style="font-weight: bold;">Filter Parameters</caption>
 *     <thead>
 *       <tr>
 *         <th>Parameter Name</th>
 *         <th>Type</th>
 *         <th>Default Value</th>
 *         <th>Description</th>
 *       </tr>
 *     </thead>
 *     <tbody>
 *       <tr>
 *         <td>HEADERS</td>
 *         <td>String</td>
 *         <td></td>
 *         <td>Line separated <code>Header-Name: Value</code> pairs.</td>
 *       </tr>
 *     </tbody>
 *   </table>
 * </div>
 * <p>
 *   &copy;2025 jWebSec. All rights reserved.
 * </p>
 * 
 * @version 0.1.0
 * @author <a href="mailto:andrew_glasgow.dev@outlook.com">Andrew Glasgow</a>
 */
public final class HttpResponseHeadersFilter implements Filter {
    
    /* Logger */
    private static final Logger LOGGER =
            Logger.getLogger(HttpResponseHeadersFilter.class.getName());
    
    /* HTTP response headers map of name-value pairs. */
    private final Map<String, String> HEADERS = new LinkedHashMap<>();
    
    public HttpResponseHeadersFilter() {
        super();
    }
    
    @Override
    public void init(final FilterConfig config) throws ServletException {
        String param = config.getInitParameter("HEADERS");
        String line, name, value;
        int i;
        try (BufferedReader in = new BufferedReader(new StringReader(param))) {
            while ((line = in.readLine()) != null) {
                if ((line = line.trim()).isEmpty()
                        || (i = line.indexOf(':')) < 0) {
                    continue;
                }
                name = line.substring(0, i).trim();
                value = line.substring(i + 1).trim();
                HEADERS.put(name, value);
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "filter initialization error", e);
        }
    }

    @Override
    public void doFilter(
            final ServletRequest req,
            final ServletResponse resp,
            final FilterChain chain) throws IOException, ServletException {
        final HttpServletResponse response = (HttpServletResponse)resp;
        for (Entry<String, String> e : HEADERS.entrySet()) {
            response.setHeader(e.getKey(), e.getValue());
        }
        chain.doFilter(req, resp);
    }

    @Override
    public void destroy() {
        HEADERS.clear();
    }
}
