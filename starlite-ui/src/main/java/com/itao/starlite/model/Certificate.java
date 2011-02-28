package com.itao.starlite.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Embeddable
public class Certificate {
	
	@Column(nullable = true, columnDefinition="varchar(10) default 'no'")
	private String number;
	private String quantity;
	@Column(nullable = true, columnDefinition="varchar(10) default 'no'")
	private String typeS92;   //this was previously called 'type'
	@Column(nullable = true, columnDefinition="varchar(10) default 'no'")
	private String typeS330J; //
	@Column(nullable = true, columnDefinition="varchar(10) default 'no'")
	private String typeB407;
	@Column(nullable = true, columnDefinition="varchar(50) default 'no'")
	private String typeOther;
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

/*	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}
*/
	public String getTypeS92() {
		return typeS92;
	}

	public void setTypeS92(String typeS92) {
		this.typeS92 = typeS92;
	}

	public String getTypeS330J() {
		return typeS330J;
	}

	public void setTypeS330J(String typeS330J) {
		this.typeS330J = typeS330J;
	}

	public String getTypeB407() {
		return typeB407;
	}

	public void setTypeB407(String typeB407) {
		this.typeB407 = typeB407;
	}

	public String getTypeOther() {
		return typeOther;
	}

	public void setTypeOther(String typeOther) {
		this.typeOther = typeOther;
	}
}
