package com.itao.starlite.ui.actions;


import com.itao.starlite.auth.User;
import com.itao.starlite.auth.UserAware;
import com.itao.starlite.ui.Breadcrumb;
import com.opensymphony.xwork2.ActionSupport;

public class ComponentAction extends ActionSupport implements UserAware {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3932501985283829578L;
	private User user;
	
	public String current="Components";
	public Breadcrumb[] breadcrumbs = {new Breadcrumb("Components")};
	
	@Override
	public String execute() throws Exception {
		return SUCCESS;
	}

	public void setUser(User arg0) {
		user = arg0;	
	}

	public User getUser() {
		return user;
	}
	
	
}
