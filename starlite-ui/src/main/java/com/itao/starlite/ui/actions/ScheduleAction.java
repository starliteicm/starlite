package com.itao.starlite.ui.actions;

import com.itao.starlite.auth.annotations.Permissions;
import com.itao.starlite.ui.Breadcrumb;
import com.opensymphony.xwork2.ActionSupport;

@Permissions("ManagerView")
public class ScheduleAction extends ActionSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4976526944195187866L;
	public String current="schedule";
	public Breadcrumb[] breadcrumbs = {new Breadcrumb("Schedule")};
}
