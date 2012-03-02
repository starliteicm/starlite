package com.itao.starlite.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.itao.starlite.model.CrewMember.Passport;

@Entity
public class Actuals implements Comparable {
	@Id @GeneratedValue
	private Integer id;
	
	@ManyToOne
	private Charter charter;
	@ManyToOne
	private Aircraft aircraft;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;
	
	private String invoiceNumber;
	
	private Double capt;
	private Double aframe;
	private Integer landings;
	private Integer pax;
	
	private boolean approved = false;
	
	@ManyToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	private ApprovalGroup approvalGroup;
	
	public ApprovalGroup getApprovalGroup() {
		return approvalGroup;
	}
	public void setApprovalGroup(ApprovalGroup approvalGroup) {
		this.approvalGroup = approvalGroup;
	}
	public boolean isApproved() {
		return approved;
	}
	public void setApproved(boolean approved) {
		this.approved = approved;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Charter getCharter() {
		return charter;
	}
	public void setCharter(Charter charter) {
		this.charter = charter;
	}
	public Aircraft getAircraft() {
		return aircraft;
	}
	public void setAircraft(Aircraft aircraft) {
		this.aircraft = aircraft;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Double getCapt() {
		return capt;
	}
	public void setCapt(Double capt) {
		this.capt = capt;
	}
	public Double getAframe() {
		return aframe;
	}
	public void setAframe(Double aframe) {
		this.aframe = aframe;
	}
	public Integer getLandings() {
		return landings;
	}
	public void setLandings(Integer landings) {
		this.landings = landings;
	}
	public Integer getPax() {
		return pax;
	}
	public void setPax(Integer pax) {
		this.pax = pax;
	}
	public String getInvoiceNumber() {
		return invoiceNumber;
	}
	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}
	public Integer getId() {
		return id;
	}
	@Override
	public int compareTo(Object arg0) {
		try{
			Actuals two = (Actuals)arg0;
			
			
			if (this.id > two.id ) {return 1;}
			if (this.id < two.id ) {return -1;}
			if (this.id == two.id ) {return 0;}
			}
			catch(Exception e) {return 0;}
		        //(o1>o2 ? -1 : (o1==o2 ? 0 : 1));
			return 0;
	}	
}
