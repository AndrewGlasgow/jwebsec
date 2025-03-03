package jwebsec.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 * <code>UserLogoutController</code> is a controller servlet for processing a
 * user logout by invalidating the client HTTP session and sending a redirect.
 * The redirect URL can be passed to this servlet's path as URL parameter
 * <code>redirectURL</code>.
 * <p>
 *   &copy;2025 jWebSec. All rights reserved.
 * </p>
 * 
 * @version 0.1.0
 * @author <a href="mailto:andrew_glasgow.dev@outlook.com">Andrew Glasgow</a>
 */
public final class UserLogoutController extends HttpServlet {
    
    public UserLogoutController() {
        super();
    }
    
    /**
     * Logout the user by invalidating their session and clearing site data.
     * 
     * @param request
     * @param response
     * @throws IOException 
     */
    private void logout(
            final HttpServletRequest request,
            final HttpServletResponse response) throws IOException {
        try {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
        } catch (Throwable error) {
            // ignore any servlet or illegal state exceptions
        }
        String uri = request.getParameter("redirectURL");
        if (uri == null || uri.isEmpty()) {
            uri = request.getContextPath();
        }
        response.setHeader(
                "Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Clear-Site-Data", "*");
        response.sendRedirect(uri);
    }
    
    @Override
    public void doGet(
            final HttpServletRequest request,
            final HttpServletResponse response)
                    throws IOException, ServletException {
        logout(request, response);
    }
    
    @Override
    public void doPost(
            final HttpServletRequest request,
            final HttpServletResponse response)
                    throws IOException, ServletException {
        logout(request, response);
    }
}
