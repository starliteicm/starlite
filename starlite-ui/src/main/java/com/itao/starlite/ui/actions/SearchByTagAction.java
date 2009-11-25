package com.itao.starlite.ui.actions;

import java.util.List;

import com.google.inject.Inject;
import com.itao.starlite.auth.annotations.Permissions;
import com.itao.starlite.docs.manager.BookmarkManager;
import com.itao.starlite.docs.model.Bookmark;
import com.opensymphony.xwork2.ActionSupport;

@Permissions("ManagerView")
public class SearchByTagAction extends ActionSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6100556628842489551L;

	public String tags;
	
	@Inject
	private BookmarkManager bookmarkManager;
	
	public List<Bookmark> bookmarks;
	
	@Override
	public String execute() throws Exception {
		bookmarks = bookmarkManager.findAllBookmarksWithTags(tags.split(","));
		return SUCCESS;
	}
}
