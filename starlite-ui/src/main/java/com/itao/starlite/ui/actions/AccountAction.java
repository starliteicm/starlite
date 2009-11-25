package com.itao.starlite.ui.actions;

import com.google.inject.Inject;
import com.itao.starlite.auth.User;
import com.itao.starlite.auth.UserAware;
import com.itao.starlite.auth.manager.AuthManager;
import com.opensymphony.xwork2.ActionSupport;

public class AccountAction extends ActionSupport implements UserAware {
	/**
	 * 
	 */
	private static final long serialVersionUID = -9179091560999267666L;

	private User user;
	
	@Inject
	private AuthManager authManager; 
	
	public void setUser(User arg0) {
		this.user = arg0;
	}
	
	public String newPassword1="";
	public String newPassword2="";
	public String errorMessage = null;
	public String save() {
		if (newPassword1 == null || newPassword2 == null || newPassword1.trim().equals("") || newPassword2.trim().equals("") || !newPassword1.equals(newPassword2)) {
			errorMessage = "Passwords must be the same and not empty";
			return ERROR;
		}
		authManager.changePassword(user.getUsername(), newPassword1);
		return SUCCESS;
	}
}
