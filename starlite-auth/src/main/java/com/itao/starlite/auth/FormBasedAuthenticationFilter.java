package com.itao.starlite.auth;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.inject.Inject;
import com.itao.starlite.auth.manager.AuthManager;

public class FormBasedAuthenticationFilter implements Filter {
	//private String loginPage = "login.action";
	@Inject
	private Authenticator authenticator;
	
	@Inject
	private com.itao.starlite.auth.dao.UserDao userDao;
	
	@Inject
	private AuthManager authManager;
	
	private boolean rememberMeEnabled = false;
	
	public void destroy() {
		
	}

	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest hReq = (HttpServletRequest)req;
		HttpServletResponse hResp = (HttpServletResponse)resp;
		
		HttpSession session = hReq.getSession();
		User authenticatedUser = (User) session.getAttribute("userObj");
		
		/*
		 * To begin with, delete any old StarliteAuth cookies.
		 * This can be taken out in the future, but its not a huge overhead.
		 */
		Cookie[] cookies = hReq.getCookies();
		if (cookies != null) {
			for (Cookie c: cookies) {
				if (c.getName().equals("StarliteAuth")) {
					c.setMaxAge(0);
					hResp.addCookie(c);
				}
			}
		}
		
		/*
		 * If the user already exists in the session retrieve their
		 * details from the Dao again to ensure the details are fresh
		 */
		if (authenticatedUser != null) {
			authenticatedUser = userDao.findById(authenticatedUser.getUsername());
			session.setAttribute("userObj", authenticatedUser);
			authManager.setCurrentUser(authenticatedUser);
		}
		if (authenticatedUser == null && rememberMeEnabled) {
			//String interrupted = hReq.getRequestURI();
			//Cookie[] cookies = hReq.getCookies();
			
			Cookie authCookie = null;
			if (cookies != null) {
				for (Cookie c: cookies) {
					//Check that the cookie is the starlite cookie, and that it is intended
					//to be stored. This is important when cookie has been marked for deletion,
					//but hasn't been removed from the system.
					if (c.getName().equals("Starlite") && c.getMaxAge() != 0) {
						authCookie = c;
						break;
					}
				}
			}
			
			if (authCookie != null) {
				String val = authCookie.getValue();
				if (val != null) {
					int indexOf = val.indexOf(':');
					if (indexOf != -1) {
						String username = val.substring(0, indexOf);
						if (val.length() > indexOf+1) {
							String passwordHash = val.substring(indexOf+1);
							authenticatedUser = authenticator.checkHash(username, passwordHash);
							session.setAttribute("userObj", authenticatedUser);
							authManager.setCurrentUser(authenticatedUser);
							authCookie.setMaxAge(2592000);
							authCookie.setPath(((HttpServletRequest)req).getContextPath());
							hResp.addCookie(authCookie);
						}
					}
				}
			}
//			if (authenticatedUser == null) {
//				if (authCookie != null) {
//					authCookie.setMaxAge(1000000000);
//					hResp.addCookie(authCookie);
//				}
//				session.setAttribute("interruptedRequestURI", interrupted);
//				hResp.sendRedirect(loginPage);
//			}
		} 
		
//		if (authenticatedUser != null) {
		
		//authManager.setCurrentThreadUser(authenticatedUser);
		if (hReq.getRequestURI().endsWith(".action")) {
			hResp.setHeader("Cache-Control", "no-cache, private, no-store");
			hResp.setHeader("Expires", "-1");
			hResp.setHeader("Pragma", "no-cache");
		}
//			chain.doFilter(req, resp);
//		}
		chain.doFilter(req, resp);
	}


	public void init(FilterConfig arg0) throws ServletException {
		//Do Nothing
	}

	
//	public void init(FilterConfig arg0) throws ServletException {
//		String ctxPath = arg0.getServletContext().getContextPath();
//		loginPage = ctxPath + arg0.getInitParameter("loginPage");
//	}
}
