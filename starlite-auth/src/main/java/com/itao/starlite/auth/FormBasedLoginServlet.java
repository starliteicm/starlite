package com.itao.starlite.auth;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.google.inject.Inject;
import com.itao.starlite.auth.manager.AuthManager;

public class FormBasedLoginServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3388263250304784591L;
	private static final Log LOG = LogFactory.getLog(FormBasedLoginServlet.class);
	@Inject
	private Authenticator authenticator;
	private String errorPage;
	@SuppressWarnings("unused")
	private String defaultSuccessPage, defaultLogoutPage;
	
	private boolean rememberMeEnabled = false;
	
	@Inject
	private AuthManager authManager;
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		String ctxPath = config.getServletContext().getContextPath();
		errorPage = ctxPath + config.getInitParameter("errorPage");
		defaultSuccessPage = ctxPath + config.getInitParameter("defaultSuccessPage");
		defaultLogoutPage = ctxPath + config.getInitParameter("defaultLogoutPage");
	}
	
	
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		User u = authenticator.authenticate(username, password);
		if (u == null) {
			LOG.info("Unsuccessful login. Attempted user: " + username);
			req.getRequestDispatcher("/").forward(req, resp);
		} else {
			HttpSession session = req.getSession();
			session.setAttribute("userObj", u);
			authManager.setCurrentUser(u);
			//authManager.setCurrentThreadUser(u);
			
			String rememberMe = req.getParameter("rememberMe");
		
			//If rememberMe is ticked and the request is 
			if (rememberMeEnabled && rememberMe != null) {
				Cookie cookie = new Cookie("Starlite", username+":"+u.getPassword());
			//	Set the cookie to expire after 30 days
				cookie.setMaxAge(2592000);
				cookie.setPath(((HttpServletRequest)req).getContextPath());
				resp.addCookie(cookie);
			}
			getServletContext().getRequestDispatcher("/").forward(req, resp);
			LOG.info(username + " logged in");
		}
	}
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String doLogout = req.getParameter("logout");
		if (doLogout != null) {
			Cookie[] cookies = req.getCookies();
			Cookie authCookie = null;
			for (Cookie c: cookies) {
				if (c.getName().equals("Starlite")) {
					authCookie = c;
					break;
				}
			}
			if (authCookie == null)
				authCookie = new Cookie("Starlite", "");
			authCookie.setPath(req.getContextPath());
			authCookie.setMaxAge(0);
			resp.addCookie(authCookie);
			
			HttpSession session = req.getSession();
			
			User currentUser = (User) session.getAttribute("userObj");
			
			session.invalidate();
			authManager.setCurrentUser(null);
			//session.removeAttribute("userObj");
			//session.setAttribute("userObj", null);
			
			//resp.getWriter().write("You have been successfully logged out.");
			//resp.flushBuffer();
			getServletContext().getRequestDispatcher("/").forward(req, resp);
			//getServletContext().getRequestDispatcher("/").forward(req, resp);
			if (currentUser != null) {
				LOG.info(currentUser.getUsername() + " logged out");
			}
		}
	}
}
