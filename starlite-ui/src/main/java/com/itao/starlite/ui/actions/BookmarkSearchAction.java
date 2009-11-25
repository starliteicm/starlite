package com.itao.starlite.ui.actions;

import java.util.List;

import com.google.inject.Inject;
import com.itao.starlite.auth.User;
import com.itao.starlite.auth.UserAware;
import com.itao.starlite.auth.annotations.Permissions;
import com.itao.starlite.auth.exceptions.InsufficientPrivilagesException;
import com.itao.starlite.docs.manager.BookmarkManager;
import com.itao.starlite.docs.manager.DocumentManager;
import com.itao.starlite.docs.model.Bookmark;
import com.itao.starlite.docs.model.Folder;
import com.itao.starlite.docs.model.Tag;
import com.itao.starlite.ui.Breadcrumb;
import com.itao.starlite.ui.TagCloud;
import com.opensymphony.xwork2.ActionSupport;

@Permissions("ManagerView")
public class BookmarkSearchAction extends ActionSupport implements UserAware {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8046248390850196016L;

	public String q;
	
	public List<Bookmark> bookmarks;
	
	public String current="search";
	public Breadcrumb[] breadcrumbs = {new Breadcrumb("Search")};
	
	public Folder root;
	
	public String tagArray = "[]";
	public String cloudHtml = "";
	
	@Inject
	private BookmarkManager bookmarkManager;
	@Inject
	private DocumentManager documentManager;
	
	public User user;

	public String errorMessage;
	
	@Override
	public String execute() throws Exception {
		if (q != null)
			bookmarks = bookmarkManager.search(q);
		try {
			root = documentManager.getFolderByPath("/", user);
		} catch (InsufficientPrivilagesException e) {
			errorMessage = "Insufficient Privilages";
			return SUCCESS;
		}
		
		List<Tag> tags = bookmarkManager.findAllTags();
		
		StringBuilder buf = new StringBuilder();
		buf.append('[');
		boolean first = true;
		for (Tag t: tags) {
			if (first) {
				first = false;
			} else {
				buf.append(',');
			}
			buf.append('\'');
			buf.append(t.getTag());
			buf.append('\'');
		}
		buf.append(']');
		tagArray = buf.toString();
		TagCloud cloud = new TagCloud(bookmarkManager.findTagsWithCount());
		cloudHtml = cloud.render();
		return SUCCESS;
	}
	
	public void setUser(User arg0) {
		this.user = arg0;
	}
}
