package com.itao.starlite.ui.actions;

import java.util.List;

import com.google.inject.Inject;
import com.itao.starlite.auth.annotations.Permissions;
import com.itao.starlite.manager.StarliteCoreManager;
import com.itao.starlite.model.CombinedActuals;
import com.itao.starlite.ui.Breadcrumb;
import com.opensymphony.xwork2.ActionSupport;

@Permissions("ManagerView")
public class AircraftMonthlyTotalsAction extends ActionSupport{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6384908136820888767L;

	public Integer id;
	
	public List<CombinedActuals> totals;
	public String tableHtml;
	public String current="aircraft";
	public Breadcrumb[] breadcrumbs;
	
	@Inject
	private StarliteCoreManager manager;
	@Override
	public String execute() throws Exception {
		return SUCCESS;
	}
	
}
