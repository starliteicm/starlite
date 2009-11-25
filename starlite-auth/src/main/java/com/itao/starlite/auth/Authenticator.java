package com.itao.starlite.auth;

import com.google.inject.ImplementedBy;

@ImplementedBy(DaoAuthenticator.class)
public interface Authenticator {
	public User authenticate(String username, String password);
	public User checkHash(String username, String passwordHash);
	public String calculateHash(String data);
	public boolean isCaseSensitive();
}
