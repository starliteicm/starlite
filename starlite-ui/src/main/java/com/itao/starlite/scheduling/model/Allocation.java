package com.itao.starlite.scheduling.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Allocation {
	@Id
	@GeneratedValue
	private Integer id;
	
	@Temporal(TemporalType.DATE)
	@Column(name="dateFrom")
	private Date from;
	@Temporal(TemporalType.DATE)
	@Column(name="dateTo")
	private Date to;
	
	private String notes;

	private String assignmentType;
	@Column(nullable=true)
	private Integer assignmentId;
	
	private String assignableType;
	@Column(nullable=true)
	private Integer assignableId;
	
	public Date getFrom() {
		return from;
	}

	public void setFrom(Date start) {
		this.from = start;
	}

	public Date getTo() {
		return to;
	}

	public void setTo(Date end) {
		this.to = end;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAssignmentType() {
		return assignmentType;
	}

	public void setAssignmentType(String assignmentType) {
		this.assignmentType = assignmentType;
	}

	public Integer getAssignmentId() {
		return assignmentId;
	}

	public void setAssignmentId(Integer assignmentId) {
		this.assignmentId = assignmentId;
	}

	public String getAssignableType() {
		return assignableType;
	}

	public void setAssignableType(String assignableType) {
		this.assignableType = assignableType;
	}

	public Integer getAssignableId() {
		return assignableId;
	}

	public void setAssignableId(Integer assignableId) {
		this.assignableId = assignableId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((assignableId == null) ? 0 : assignableId.hashCode());
		result = prime * result
				+ ((assignableType == null) ? 0 : assignableType.hashCode());
		result = prime * result
				+ ((assignmentId == null) ? 0 : assignmentId.hashCode());
		result = prime * result
				+ ((assignmentType == null) ? 0 : assignmentType.hashCode());
		result = prime * result + ((from == null) ? 0 : from.hashCode());
		result = prime * result + ((to == null) ? 0 : to.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Allocation other = (Allocation) obj;
		if (assignableId == null) {
			if (other.assignableId != null)
				return false;
		} else if (!assignableId.equals(other.assignableId))
			return false;
		if (assignableType == null) {
			if (other.assignableType != null)
				return false;
		} else if (!assignableType.equals(other.assignableType))
			return false;
		if (assignmentId == null) {
			if (other.assignmentId != null)
				return false;
		} else if (!assignmentId.equals(other.assignmentId))
			return false;
		if (assignmentType == null) {
			if (other.assignmentType != null)
				return false;
		} else if (!assignmentType.equals(other.assignmentType))
			return false;
		if (from == null) {
			if (other.from != null)
				return false;
		} else if (!from.equals(other.from))
			return false;
		if (to == null) {
			if (other.to != null)
				return false;
		} else if (!to.equals(other.to))
			return false;
		return true;
	}
	
	
}
