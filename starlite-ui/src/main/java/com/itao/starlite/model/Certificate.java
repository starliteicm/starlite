package com.itao.starlite.model;

import java.util.Date;

import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Embeddable
public class Certificate {
	
	private String number;
	private String quantity;
	private String type;
	private Double hours;
	
	@Temporal(TemporalType.DATE)
	private Date expiryDate;
	
	@Temporal(TemporalType.DATE)
	private Date issueDate;

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public Date getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}

	public void setHours(Double hours) {
		this.hours = hours;
	}

	public Double getHours() {
		return hours;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}
}
