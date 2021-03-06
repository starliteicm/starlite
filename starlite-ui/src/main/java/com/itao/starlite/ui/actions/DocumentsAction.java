package com.itao.starlite.ui.actions;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

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
import com.itao.starlite.docs.model.Tag;
import com.itao.starlite.manager.StarliteCoreManager;
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
	@Inject
	private StarliteCoreManager manager;
	
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
	
	public File   document;
	public String documentContentType;
	public String documentFileName;
	
	public String tags;
	
	public String returnUrl;
	public String shallReturn;
	
	public String id;
	
	public String upload() throws Exception 
	{
		boolean pass = true;
		try{
			
		
		LOG.info(tags+" "+folder+" "+shallReturn);
		String[] tagsArray = tags.split(" ");
		
		Document doc = new Document();
		doc.setName(documentFileName);
		doc.setContentType(documentContentType);
		
		Bookmark b = bookmarkManager.createBookmark(documentFileName, "Document", folder+"/"+documentFileName, tagsArray);
		doc.setBookmark(b);
		
		documentManager.createDocument(doc, folder, new FileInputStream(document), user);
		}
		catch(Exception ex)
		{
			this.errorMessage="Unable to upload document. Possible Reason: "+ex.getMessage().toString();
			pass = false;
			return ERROR;
		}
		if (pass)
		{
		if (returnUrl != null)
			return "redirect";
		else if (shallReturn != null) {
			returnUrl = ServletActionContext.getRequest().getHeader("referer");
			return "redirect";
		}
		}
		return execute();
	}
	
	public String path;
	public String delete() throws Exception 
	{
		try
		{
		documentManager.deleteDocument(path, user);
		}
		catch (Exception e)
		{
			try
			{
				//manager.deleteDocByName(path,id);
				//find the file and then delete
				//File target = new File(path);
				//target.delete();	
				
				
			}
			catch (Exception ee)
			{
				this.errorMessage = "Unable to delete the file: "+ path;
			}
		}
		if (returnUrl != null)
			return "redirect";
		
		returnUrl = ServletActionContext.getRequest().getHeader("referer");
		return "redirect";
	}

	public String archive() throws Exception 
	{
		try
		{
			String newTags = "";
			String[] filenameParts = path.split("/");
			String filename = filenameParts[filenameParts.length-1];
			String crewMember = filenameParts[filenameParts.length-2];
			Document doc1 = documentManager.getDocumentByPath(path, user);
			Bookmark bookMark = doc1.getBookmark();
			Set<Tag> tagSet = bookMark.getTags();
			Iterator<Tag> litr = tagSet.iterator(); 
			
			while (litr.hasNext())
			{
				String empty = "";
				
				Tag tempTag = (Tag) litr.next();
				if (empty.compareToIgnoreCase(tempTag.getTag()) != 0)
				{
					if (crewMember.compareToIgnoreCase(tempTag.getTag()) != 0 )
					{
						newTags = newTags + tempTag.getTag() + "_archived";
					}
					else {newTags = newTags + tempTag.getTag(); }
				    
					bookMark.removeTag(tempTag);
						
					if (tagSet.iterator().hasNext())
					{
						newTags = newTags + " ";
					}
					
				}
			}
			String[] tagsArray = newTags.split(" ");
			Bookmark b = bookmarkManager.createBookmark(filename, "Document", path, tagsArray);
			doc1.setBookmark(b);
			
			
			
			int i=0;
		}
		catch (Exception e)
		{
			try
			{
				
				
				
			}
			catch (Exception ee)
			{
				
			}
		}
		if (returnUrl != null)
			return "redirect";
		
		returnUrl = ServletActionContext.getRequest().getHeader("referer");
		return "redirect";
	}
	public void setUser(User arg0) {
		this.user = arg0;
	}
}
