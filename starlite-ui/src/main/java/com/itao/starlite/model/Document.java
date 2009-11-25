package com.itao.starlite.model;

import javax.persistence.Embeddable;

@Embeddable
public class Document {
	private String docUrl;
	private Boolean approved;
	
	public String getDocUrl() {
		return docUrl;
	}
	public void setDocUrl(String docUrl) {
		this.docUrl = docUrl;
	}
	public boolean isApproved() {
		return approved != null && approved;
	}
	public void setApproved(boolean approved) {
		this.approved = approved;
	}
}
