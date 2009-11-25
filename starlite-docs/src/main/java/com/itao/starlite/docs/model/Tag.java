package com.itao.starlite.docs.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Tag {
	@Id
	@GeneratedValue
	private Integer id;
	
	@Column(unique=true)
	private String tag;
	
	public Tag() {
		
	}
	
	public Tag(String tag) {
		super();
		this.tag = tag;
	}

	public String getTag() {
		return tag;
	}

	public Integer getId() {
		return id;
	}
}
