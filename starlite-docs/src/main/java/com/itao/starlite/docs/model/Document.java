package com.itao.starlite.docs.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.itao.starlite.auth.User;

@Entity
public class Document {
	@Id
	@GeneratedValue
	private Integer id;
	
	private String name;
	private String contentType;
	private Integer size;
	
	private String uuid;
	
	@OneToOne(fetch=FetchType.EAGER)
	private User owner;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateAdded = new Date();
	
	@OneToOne(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	private Bookmark bookmark;
	
	@Temporal(TemporalType.DATE)
	private Date expiryDate;
	private boolean approved;

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public boolean isApproved() {
		return approved;
	}

	public void setApproved(boolean approved) {
		this.approved = approved;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public Date getDateAdded() {
		return dateAdded;
	}

	public void setDateAdded(Date dateAdded) {
		this.dateAdded = dateAdded;
	}

	public Bookmark getBookmark() {
		return bookmark;
	}

	public void setBookmark(Bookmark bookmark) {
		this.bookmark = bookmark;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
}
