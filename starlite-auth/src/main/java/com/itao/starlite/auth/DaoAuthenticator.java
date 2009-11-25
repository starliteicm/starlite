package com.itao.starlite.auth;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;

import com.google.inject.Inject;
import com.itao.starlite.auth.dao.UserDao;

public class DaoAuthenticator implements Authenticator {
	@Inject
	private UserDao dao;
	private boolean caseSensitive = false;

	public User authenticate(String username, String password) {
		User u = dao.findById(username);
		if (u == null) {
			return null;
		}
		String passwordHash = calculateHash(password);
		if (u.getPassword().equalsIgnoreCase(passwordHash)) {
			return u;
		}
		return null;
	}
	
	/**
	 * Returns an encoding of the password. This implementation is not very secure - just returns the hashCode of the String! but its better than plaintext
	 */
	public String calculateHash(String password) {
		//return ""+password.hashCode();
		String data = password;
		if (!caseSensitive)
			data = data.toLowerCase();
		try {
			MessageDigest m = MessageDigest.getInstance("MD5");
			
			m.update(data.getBytes());
			
			byte[] encodedBytes = m.digest();
			//System.out.println(new String(Hex.encodeHex(encodedBytes)));
			return new String(Hex.encodeHex(encodedBytes));
		} catch (NoSuchAlgorithmException e) {
			
		} 
		return "";
	}

	
	public User checkHash(String username, String passwordHash) {
		try {
			User u = dao.findById(username);
			if (u == null) {
				return null;
			}
			
			if (passwordHash.equalsIgnoreCase(u.getPassword()))
				return u;
			
		} catch (Exception e) {
			
		}
		return null;
	}

	public boolean isCaseSensitive() {
		return caseSensitive;
	}
}
