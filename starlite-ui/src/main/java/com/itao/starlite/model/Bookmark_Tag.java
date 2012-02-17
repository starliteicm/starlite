package com.itao.starlite.model;


import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.itao.starlite.docs.model.Bookmark;
import com.itao.starlite.docs.model.Tag;

@Entity
public class Bookmark_Tag 
{
	private Integer bookmark_id;
	private Integer tags_id;
	

	public Bookmark_Tag()
	{
		
		
	}
	
	
	public Integer getBookmark_id() {
		return bookmark_id;
	}
	public void setBookmark_id(Integer bookmark_id) {
		this.bookmark_id = bookmark_id;
	}
	public Integer getTags_id() {
		return tags_id;
	}
	public void setTags_id(Integer tags_id) {
		this.tags_id = tags_id;
	}

}
