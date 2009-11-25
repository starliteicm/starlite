package com.itao.starlite.ui.actions;

import org.joda.time.DateMidnight;

import com.itao.starlite.auth.User;
import com.itao.starlite.auth.UserAware;
import com.opensymphony.xwork2.ActionSupport;

@SuppressWarnings("serial")
public class ReportsAction extends ActionSupport implements UserAware {
	public int month, year;

	public boolean notAuthorised = false;

	public String current="reports";

	private User user;

	@Override
	public String execute() throws Exception {

		if (!user.hasPermission("ManagerEdit"))
			notAuthorised = true;

		DateMidnight dm = new DateMidnight();
		month = dm.getMonthOfYear();
		year = dm.getYear();
		return SUCCESS;
	}

	public void setUser(User arg0) {
		this.user = arg0;
	}

}
