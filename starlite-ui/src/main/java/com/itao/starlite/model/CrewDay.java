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

@Entity
public class CrewDay {
	@Id @GeneratedValue
	private Integer id;
	
	@ManyToOne
	private CrewMember crewMember;
	@ManyToOne
	private Charter charter;
	@ManyToOne
	private Aircraft aircraft;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;
	
	private String activity;
	private String comments;
	private String type;
	private String position;
	private String instruments;
	private double flown;
	private String timein;
	private String timeout;
	private String hours;
	

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

	public Integer getId() {
		return id;
	}
	public void setCrewMember(CrewMember crewMember) {
		this.crewMember = crewMember;
	}
	public CrewMember getCrewMember() {
		return crewMember;
	}
	public void setActivity(String activity) {
		this.activity = activity;
	}
	public String getActivity() {
		return activity;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getComments() {
		return comments;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getType() {
		return type;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getPosition() {
		return position;
	}
	public void setInstruments(String instruments) {
		this.instruments = instruments;
	}
	public String getInstruments() {
		return instruments;
	}
	public void setFlown(double flown) {
		this.flown = flown;
	}
	public double getFlown() {
		return flown;
	}
	public void setTimein(String timein) {
		this.timein = timein;
	}
	public String getTimein() {
		return timein;
	}
	public void setTimeout(String timeout) {
		this.timeout = timeout;
	}
	public String getTimeout() {
		return timeout;
	}
	public void setHours(String hours) {
		this.hours = hours;
	}
	public String getHours() {
		return hours;
	}	
}
