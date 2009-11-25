package com.itao.starlite.docs.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity
public class Bookmark {
	@Id
	@GeneratedValue
	private Integer id;
	
	@ManyToMany(fetch=FetchType.EAGER)
	private Set<Tag> tags = new HashSet<Tag>();
	
	private String name;
	
	@ManyToOne(fetch=FetchType.EAGER)
	private BookmarkType bookmarkType;
	private String bookmarkedId;
	
	public Bookmark() {
		
	}
	
	public Bookmark(Set<Tag> tags, String name,
			BookmarkType bookmarkType, String bookmarkedId) {
		super();
		this.tags = tags;
		this.name = name;
		this.bookmarkType = bookmarkType;
		this.bookmarkedId = bookmarkedId;
	}

	public Set<Tag> getTags() {
		return tags;
	}

	public String getUrl() {
		return bookmarkType.resolve(bookmarkedId);
	}
	
	public void addTag(Tag tag) {
		tags.add(tag);
	}
	
	public void removeTag(Tag tag) {
		tags.remove(tag);
	}

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public BookmarkType getBookmarkType() {
		return bookmarkType;
	}

	public String getBookmarkedId() {
		return bookmarkedId;
	}
}
