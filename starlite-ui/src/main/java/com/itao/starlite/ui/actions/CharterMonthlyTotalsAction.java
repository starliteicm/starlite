package com.itao.starlite.ui.actions;

import java.util.List;

import com.google.inject.Inject;
import com.itao.starlite.auth.annotations.Permissions;
import com.itao.starlite.manager.StarliteCoreManager;
import com.itao.starlite.model.CombinedActuals;
import com.itao.starlite.ui.Breadcrumb;
import com.opensymphony.xwork2.ActionSupport;

@Permissions("ManagerView")
public class CharterMonthlyTotalsAction extends ActionSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7871272419705014410L;

	public Integer id;
	
	public List<CombinedActuals> totals;
	public String tableHtml;
	public String current="charters";
	public Breadcrumb[] breadcrumbs;
	
	@Inject
	private StarliteCoreManager manager;
	
	@Override
	public String execute() throws Exception {
		return SUCCESS;
	}
}
