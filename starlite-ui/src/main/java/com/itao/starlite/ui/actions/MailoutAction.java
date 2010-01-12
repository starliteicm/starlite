package com.itao.starlite.ui.actions;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

import org.jfree.util.Log;

import com.google.inject.Inject;
import com.itao.starlite.auth.User;
import com.itao.starlite.auth.UserAware;
import com.itao.starlite.auth.annotations.Permissions;
import com.itao.starlite.auth.exceptions.InsufficientPrivilagesException;
import com.itao.starlite.docs.manager.DocumentManager;
import com.itao.starlite.docs.model.Folder;
import com.itao.starlite.manager.StarliteCoreManager;
import com.itao.starlite.model.CrewMember;
import com.itao.starlite.ui.Breadcrumb;
import com.itao.starlite.ui.service.MailService;
import com.opensymphony.xwork2.ActionSupport;

@Permissions("ManagerView")
public class MailoutAction extends ActionSupport implements UserAware {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2250524649483938062L;
	public String current="Mailout";
	public Breadcrumb[] breadcrumbs = {new Breadcrumb("Mailout")};
	
	public Folder root;
	public List<CrewMember> crew;
	public String   from;
	public String[] docs;
	public String[] mems;
	
	public List<String> docsSelected = new ArrayList<String>();
	public List<String> memsSelected = new ArrayList<String>();
	
	public String   subject;
	public String   emails;
	public String   message;
	public String   reciept;

	@Inject
	private DocumentManager documentManager;
	@Inject
	private StarliteCoreManager manager;
    @Inject
    private MailService mailService;
	
	public User user;

	public String errorMessage;
	public String notificationMessage;
	
	@SuppressWarnings("unchecked")
	public String sendMail() throws Exception {
		List<String> docs = new ArrayList<String>();
		List<String> mems = new ArrayList<String>();
		if(this.docs != null){
			docs = new ArrayList<String>(Arrays.asList(this.docs));
			Collections.sort(docs, String.CASE_INSENSITIVE_ORDER);

		}
		if(this.mems != null){
			mems = new ArrayList<String>(Arrays.asList(this.mems));
			
		}
		docsSelected = (List<String>) ((ArrayList) docs).clone();
		memsSelected = (List<String>) ((ArrayList) mems).clone();
		
		
		System.out.println("Sending mail: - "+from+"\n"+subject+"\n"+mems+"\n"+emails+"\n"+docs+"\n"+message);
		
		if(!"".equals(emails)){
		for(String add : emails.split(",|;")){
			mems.add(add.trim());
		}
		}
		
		boolean rr = false;
		if("yes".equals(reciept)){
			rr =true;
		}
		
		Map<String,Boolean> notok = mailService.checkEmailAddrsOk(mems);
		if(notok.size() == 0){
			Map<String,File> attachmentFiles = mailService.getFilesForAttachment(this.docs,user);
			mailService.sendAttachedMessage(from, subject, mems, rr, attachmentFiles, message);
			notificationMessage="Mail Sent";
			clear();
		}
		else{
			errorMessage="Some of the Email addresses Entered do not appear valid: "+notok.keySet();
		}
		return execute();
	}
	
	//clear fields
	public void clear(){
	  docsSelected = new ArrayList<String>();
	  memsSelected = new ArrayList<String>();
	  from=null;
	  subject=null;
	  mems=null;
	  emails=null;
	  docs=null;
	  message=null;
	}
	
	@Override
	public String execute() throws Exception {
		Log.info("mailout - "+user);
		crew = manager.getAllCrew();
		TreeMap<String,CrewMember> ordered = new TreeMap<String,CrewMember>();
		for(CrewMember cm : crew){
			if(cm.getCode() != null){
			   ordered.put(cm.getCode(), cm);
			}
		}
		crew = new ArrayList<CrewMember>(ordered.values());
		
		try {
			root = documentManager.getFolderByPath("/distribution/", user);
		} catch (InsufficientPrivilagesException e) {
			errorMessage = "Insufficient Privilages";
			return SUCCESS;
		}
		return SUCCESS;
	}
	
	public void setUser(User arg0) {
		this.user = arg0;
	}
	
}
