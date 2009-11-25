package com.itao.starlite.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class MonthlyInvoicedHours {
	@Id @GeneratedValue
	private Integer id;
	@Temporal(TemporalType.DATE)
	private Date date;
	private Double hours;
	@ManyToOne
	private Charter charter;
	
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Double getHours() {
		return hours;
	}
	public void setHours(Double hours) {
		this.hours = hours;
	}
	public Charter getCharter() {
		return charter;
	}
	public void setCharter(Charter c) {
		this.charter = c;
	}
	public Integer getId() {
		return id;
	}
	
	
}
