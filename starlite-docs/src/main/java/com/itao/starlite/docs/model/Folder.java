package com.itao.starlite.docs.model;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.OrderBy;

import com.itao.starlite.auth.User;
import com.itao.starlite.docs.exceptions.FolderAlreadyExistsException;

@Entity
public class Folder {
	@Id
	@GeneratedValue
	private Integer id;
	private String path;
	
	@OneToMany(fetch=FetchType.EAGER, cascade={CascadeType.ALL})
	private List<Folder> subFolders;
	
	@OneToMany(fetch=FetchType.EAGER)
	private Set<Document> docs = new HashSet<Document>();
	
	@OneToOne(fetch=FetchType.EAGER)
	private User owner;
	
	private String readPermission;
	private String writePermission;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@OrderBy(clause="name asc")
	public Set<Document> getDocs() {
		return docs;
	}

	public void setDocs(Set<Document> docs) {
		this.docs = docs;
	}
	
	public synchronized Document getDocumentByName(String name) {
		for (Document doc: docs) {
			if (doc.getName().equals(name))
				return doc;
		}
		return null;
	}
	
	public synchronized Document getDocumentByTag(String tag){
		for(Document doc: docs){
			for(Tag t : doc.getBookmark().getTags()){
				if(tag.equals(t.getTag())){
					return doc;
				}
			}
		}
		return null;
	}
	
	public synchronized void addDocument(Document doc) {
		for (Document d: docs) {
			if (d.getName().equals(doc.getName())) {
				return;
			}
		}
		docs.add(doc);
	}

	public List<Folder> getSubFolders() {
		Collections.sort(subFolders, new Comparator<Folder>() {

			public int compare(Folder o1, Folder o2) {
				if (o1.getPath().equals("Standard"))
					return -1;
				if (o2.getPath().equals("Standard"))
					return 1;
				return o1.path.compareTo(o2.path);
			}
			
		});
		return subFolders;
	}

	public void setSubFolders(List<Folder> subFolders) {
		this.subFolders = subFolders;
	}	
	
	public synchronized Folder addSubFolder(String f) throws FolderAlreadyExistsException {
		for (Folder sf: subFolders) {
			if (sf.getPath().equals(f))
				throw new FolderAlreadyExistsException();
		}
		Folder newFolder = new Folder();
		newFolder.setPath(f);
		subFolders.add(newFolder);
		return newFolder;
	}

	public String getReadPermission() {
		return readPermission;
	}

	public void setReadPermission(String readPermission) {
		this.readPermission = readPermission;
	}

	public String getWritePermission() {
		return writePermission;
	}

	public void setWritePermission(String writePermission) {
		this.writePermission = writePermission;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}
	
	public boolean canRead(User user) {
		if (user == null)
			return false;
		return isOwner(user) || user.hasPermission(readPermission) || user.hasPermission("AllDocsRead");
	}
	
	public boolean canWrite(User user) {
		if (user == null)
			return false;
		return isOwner(user) || user.hasPermission(writePermission) || user.hasPermission("AllDocsWrite");
	}
	
	public boolean canExecute(User user) {
		return true;
//		if (user == null)
//			return false;
//		return isOwner(user) || user.hasPermission(readPermission) || user.hasPermission("AllDocsRead");
	}
	
	private boolean isOwner(User user) {
		if (user == null)
			return false;
		return user.getUsername().equals(owner.getUsername());
	}
	
	public synchronized void removeDocument(Document doc) {
		docs.remove(doc);
	}
}
