package com.itao.starlite.auth.manager;

import java.util.List;
import java.util.Random;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Hex;
import org.apache.struts2.ServletActionContext;

import com.google.inject.Inject;
import com.itao.starlite.auth.Authenticator;
import com.itao.starlite.auth.Role;
import com.itao.starlite.auth.User;
import com.itao.starlite.auth.dao.PermissionDao;
import com.itao.starlite.auth.dao.RoleDao;
import com.itao.starlite.auth.dao.UserDao;
import com.wideplay.warp.persist.Transactional;

public class AuthManager {
	@Inject
	private UserDao userDao;
	@Inject
	private RoleDao roleDao;
	@Inject
	private PermissionDao permissionDao;
	
	@Inject
	private Authenticator authenticator;
	
	private Random r = new Random(System.currentTimeMillis());
	
	private ThreadLocal<User> currentUser = new ThreadLocal<User>();
	
	@Transactional
	public void changePassword(String username, String newPassword) {
		com.itao.starlite.auth.User u = userDao.findById(username);
		if (!authenticator.isCaseSensitive())
			newPassword = newPassword.toLowerCase();
		String passwordHash = authenticator.calculateHash(newPassword); 
		u.setPassword(passwordHash);
		userDao.makePersistent(u);
		
		try {
			HttpServletRequest req = ServletActionContext.getRequest();
			HttpServletResponse resp = ServletActionContext.getResponse();
			if (req != null && resp != null) {
				/*
				 * Check whether an auth cookie already exists, and if so
				 * check the user referred to by the cookie. If the cookie
				 * user is the current logged in user, than update the cookie
				 * with the new credentials.
				 */
				Cookie[] cookies = req.getCookies();
				for (Cookie c: cookies) {
					if (c.getName().equals("Starlite") && c.getMaxAge() != 0) {
						String val = c.getValue();
						String[] parts = val.split(":");
						if (parts.length > 0) {
							String cUsername = parts[0];
							if (cUsername.equals(username)) {
								c.setMaxAge(2592000);
								c.setPath(req.getContextPath());
								c.setValue(username+":"+passwordHash);
								resp.addCookie(c);
							}
						}
					}
				}
			}
		} catch (Throwable t) {}
	}
	
	@Transactional
	public User createUserWithRoles(String username, String password, String... roleNames) {
		User u = new User();
		u.setUsername(username);
		if (!authenticator.isCaseSensitive())
			password = password.toLowerCase();
		u.setPassword(authenticator.calculateHash(password));
		
		List<Role> roles = roleDao.selectRolesByNames(roleNames);
		for (Role r: roles) {
			u.addRole(r);
		}
		userDao.makePersistent(u);
		return u;
	}
	
	//Generates a random 6 character Hexadecimal password
	public String generateRandomPassword() {
		byte[] bytes = new byte[3];
		r.nextBytes(bytes);
		return new String(Hex.encodeHex(bytes)).toLowerCase();
	}
	
	public void setCurrentUser(User user) {
		currentUser.set(user);
	}
	
	public User getCurrentUser() {
		return currentUser.get();
	}
	
	public User getSuperUser() {
		return userDao.findById("jason");
	}
	
	public User getUser(String username) {
		return userDao.findById(username);
	}
}
