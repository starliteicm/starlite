package com.itao.starlite.ui.actions;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletRedirectResult;

import com.itao.starlite.auth.User;
import com.itao.starlite.auth.UserAware;
import com.itao.starlite.auth.annotations.Permissions;
import com.opensymphony.xwork2.ActionSupport;

@ParentPackage("prepare")
@Results({
	@Result(name="management", value="user!welcome.action", type=ServletRedirectResult.class),
    @Result(name="crew", value="crewMember.action?id=${user.username}", type=ServletRedirectResult.class)
})
@Permissions("")
public class IndexAction extends ActionSupport implements UserAware {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3932501985283829578L;
	private User user;
	
	@Override
	public String execute() throws Exception {
		if (user.hasPermission("ManagerView")) {
			return "management";
		} else {
			return "crew";
		}
	}

	public void setUser(User arg0) {
		user = arg0;	
	}

	public User getUser() {
		return user;
	}
	
	
}
