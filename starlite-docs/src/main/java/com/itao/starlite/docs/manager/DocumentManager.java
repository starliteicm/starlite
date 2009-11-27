package com.itao.starlite.docs.manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.restlet.util.ByteUtils;

import com.google.inject.Inject;
import com.itao.starlite.auth.User;
import com.itao.starlite.auth.exceptions.InsufficientPrivilagesException;
import com.itao.starlite.auth.manager.AuthManager;
import com.itao.starlite.docs.dao.DocumentDao;
import com.itao.starlite.docs.dao.FolderDao;
import com.itao.starlite.docs.exceptions.FolderAlreadyExistsException;
import com.itao.starlite.docs.exceptions.IllegalPathException;
import com.itao.starlite.docs.model.Document;
import com.itao.starlite.docs.model.Folder;
import com.wideplay.warp.persist.Transactional;

public class DocumentManager {
	@Inject
	private DocumentDao documentDao;
	
	@Inject
	private FolderDao folderDao;
	
	@Inject
	private AuthManager authManager;
	
	private String docsFolderPath = System.getProperty("user.home")+"/starlite-docs/";
	
	public DocumentManager() {
		File dir = new File(docsFolderPath);
		if (!dir.exists()) {
			dir.mkdirs();
		}
	}
	
//	@Transactional
//	public Document getDocumentInfo(Integer documentId) {
//		return documentInfoDao.findById(documentId);
//	}
	
	@Transactional
	public Document getDocumentByPath(String path, User user) throws InsufficientPrivilagesException {
		int lastSlash = path.lastIndexOf('/');
		String folderPath = path.substring(0, lastSlash);
		Folder f = folderDao.getFolderByPath(folderPath, user);
		if (!f.canRead(user))
			throw new InsufficientPrivilagesException();
		Document doc = f.getDocumentByName(path.substring(lastSlash+1));
		return doc;
	}
	
//	@Transactional
//	public Folder getFolder(Integer folder) {
//		return folderDao.findById(folder);
//	}
	
	@Transactional
	public Folder getFolderByPath(String path, User user) throws InsufficientPrivilagesException {
		if (!path.startsWith("/"))
			path = "/"+path;
		return folderDao.getFolderByPath(path, user);
	}
	
	@Transactional
	public Folder createFolder(String path, User user) throws IllegalPathException, FolderAlreadyExistsException, InsufficientPrivilagesException {
		if (path.equals("/"))
			throw new FolderAlreadyExistsException("Folder: '" + path + "'");
		
		int lastIndex = path.lastIndexOf('/');
		
		while (lastIndex == path.length()-1 && lastIndex > 0) {
			path = path.substring(0, path.length()-1);
			lastIndex = path.lastIndexOf('/');
		}
		
		if (lastIndex < 0) {
			throw new IllegalPathException("Illegal Path");
		}
		
		String parentPath = path.substring(0, lastIndex);
		String lastPart = path.substring(lastIndex+1);
		
		Folder parent = getFolderByPath(parentPath, user);
		
		if (!parent.canWrite(user))
			throw new InsufficientPrivilagesException();
		return parent.addSubFolder(lastPart);
	}
	
//	@Transactional
//	public void deleteDocument(Integer id) {
//		Document docInfo = documentInfoDao.findById(id);
//		if (docInfo != null)
//			documentInfoDao.makeTransient(docInfo);
//		
//		File f = new File(path+id);
//		f.delete();
//	}
	
	@Transactional
	public List<Document> retrieveAllDocuments() {
		return documentDao.findAll();
	}
	
	@Transactional
	public Set<Document> retrieveAllDocumentsInFolder(String folderName, User user) throws InsufficientPrivilagesException {
		Folder f = folderDao.getFolderByPath(folderName, user);
		if (!f.canRead(user))
			throw new InsufficientPrivilagesException();
		Set<Document> docs = f.getDocs();
		return docs;
	}
	
	@Transactional
	public Object[] getDocumentWithData(String path, User user) throws InsufficientPrivilagesException {
		Document doc = getDocumentByPath(path, user);
		
		File f = new File(docsFolderPath+doc.getUuid());
		try {
			return new Object[] {doc, new FileInputStream(f)};
		} catch (FileNotFoundException e) {
			return null;
		}
	}
	
	@Transactional
	public InputStream getDocumentData(Document doc){
		File f = new File(docsFolderPath+doc.getUuid());
		try {
			return new FileInputStream(f);
		} catch (FileNotFoundException e) {
			return null;
		}
	}
	
	@Transactional
	public void createDocument(Document info, String folder, InputStream docData, User user) throws IOException {
		boolean created = false;
		File file = null;
		try {
			info.setId(null);
			documentDao.makePersistent(info);
			
			String uuid = UUID.randomUUID().toString();
			
			file = new File(docsFolderPath+uuid);
			while (file.exists()) {
				uuid = UUID.randomUUID().toString();
				file = new File(docsFolderPath+uuid);
			}
			
			created = file.createNewFile();
			
			info.setUuid(uuid);
			
			Folder f = folderDao.getFolderByPath(folder, user);

			if (f == null) {
				if (folder != null && !folder.equals("")) {
					f = new Folder();
					f.setPath(folder);
					folderDao.makePersistent(f);
				}
			}
			
			if (f == null) {
				throw new IOException("Internal Error");
			}
			
			if (!f.canWrite(user))
				throw new InsufficientPrivilagesException();
			
			f.getDocs().add(info);
			
			Integer id = info.getId();
			OutputStream out = new FileOutputStream(file);
			ByteUtils.write(docData, out);
			out.flush();
			out.close();
			File fi = new File(docsFolderPath+id);
			info.setSize((int)fi.length());
			documentDao.makePersistent(info);
		} catch (Throwable t) {
			if (created && file != null) {
				file.delete();
			}
			throw new RuntimeException(t);
		}
	}
	
	@Transactional
	public void deleteDocument(String path, User user) throws InsufficientPrivilagesException {
		String folderPath = path.substring(0, path.lastIndexOf('/'));
		Folder f = getFolderByPath(folderPath, user);
		if (!f.getOwner().getUsername().equals(user.getUsername()) && !f.canWrite(user))
			throw new InsufficientPrivilagesException("User " + user + " does not have permission to delete documents from this folder");
		
		Document doc = f.getDocumentByName(path.substring(path.lastIndexOf('/')+1));
		File docFile = new File(docsFolderPath+doc.getUuid());
		f.removeDocument(doc);
		documentDao.makeTransient(doc);
		docFile.delete();
	}
}
