package com.itao.starlite.ui.actions;

import com.google.inject.Inject;
import com.itao.starlite.auth.annotations.Permissions;
import com.itao.starlite.auth.manager.AuthManager;
import com.opensymphony.xwork2.ActionSupport;

@Permissions("Administrator")
public class AddStaffAction extends ActionSupport {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1877440471631727761L;
	public boolean completed = false;
	public String errorMessage;
	public String notificationMessage;
	
	public String newUsername;
	public String newUserRole;
	public String password;
	
	@Inject
	private AuthManager authManager;
	
	@Override
	public String execute() throws Exception {
		if (completed) {
			if (newUsername != null && newUserRole != null && !newUsername.trim().equals("") && !newUserRole.trim().equals("")) {
				String password = authManager.generateRandomPassword();
				authManager.createUserWithRoles(newUsername, password, newUserRole);
				notificationMessage = newUsername + " created with password: " +password;
			} else {
				errorMessage = "Incomplete";
			}
			return SUCCESS;
		} else {
			return SUCCESS;
		}
	}
}
