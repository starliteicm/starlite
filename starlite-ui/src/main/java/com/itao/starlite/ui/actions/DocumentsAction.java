package com.itao.starlite.ui.actions;

import java.io.File;
import java.io.FileInputStream;
import java.util.LinkedList;
import java.util.List;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletRedirectResult;

import com.google.inject.Inject;
import com.itao.starlite.auth.User;
import com.itao.starlite.auth.UserAware;
import com.itao.starlite.auth.annotations.Permissions;
import com.itao.starlite.docs.manager.BookmarkManager;
import com.itao.starlite.docs.manager.DocumentManager;
import com.itao.starlite.docs.model.Bookmark;
import com.itao.starlite.docs.model.Document;
import com.itao.starlite.docs.model.Folder;
import com.opensymphony.xwork2.ActionSupport;

@Results({
    @Result(name="redirect", value="${returnUrl}", type=ServletRedirectResult.class)
})

@Permissions("")
public class DocumentsAction extends ActionSupport implements UserAware {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7369620997877068433L;
	public String folder;
	public List<Document> documents;
	
	@Inject
	private DocumentManager documentManager;
	@Inject
	private BookmarkManager bookmarkManager;
	
	private User user;
	public String errorMessage;
	
	@Permissions("Manager View")
	@Override
	public String execute() throws Exception {
		Folder f = documentManager.getFolderByPath(folder, user);
		if (f != null) {
			documents = new LinkedList<Document>();
			if (f.canRead(user)) {
				
				documents.addAll(f.getDocs());
			} else {
				errorMessage = "Insufficient Privilages";
			}
		}
		return super.execute();
	}
	
	public File document;
	public String documentContentType;
	public String documentFileName;
	
	public String tags;
	
	public String returnUrl;
	public String shallReturn;
	
	public String upload() throws Exception {
		LOG.info(tags+" "+folder+" "+shallReturn);
		String[] tagsArray = tags.split(" ");
		
		Document doc = new Document();
		doc.setName(documentFileName);
		doc.setContentType(documentContentType);
		
		Bookmark b = bookmarkManager.createBookmark(documentFileName, "Document", folder+"/"+documentFileName, tagsArray);
		doc.setBookmark(b);
		
		documentManager.createDocument(doc, folder, new FileInputStream(document), user);
		if (returnUrl != null)
			return "redirect";
		else if (shallReturn != null) {
			returnUrl = ServletActionContext.getRequest().getHeader("referer");
			return "redirect";
		}
		return execute();
	}
	
	public String path;
	public String delete() throws Exception {
		documentManager.deleteDocument(path, user);
		returnUrl = ServletActionContext.getRequest().getHeader("referer");
		return "redirect";
	}

	public void setUser(User arg0) {
		this.user = arg0;
	}
}
