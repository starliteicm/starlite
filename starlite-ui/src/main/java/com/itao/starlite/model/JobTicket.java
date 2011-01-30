package com.itao.starlite.model;
import java.util.ArrayList;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.google.inject.Inject;
import com.itao.starlite.manager.StarliteCoreManager;


/**
 * <p>Represents a Starlite AMO Job Ticket. Each ticket has three buttons, a status depending on
 * which button is turned on, a CrewMember as a assignee and submitter and a JobHistory. Each
 * ticket is for a specific Aircraft and JobTask combination.</p>
 * <p>This class has been mapped for Hibernate.</p>
 * @author Celeste Groenewald
 *
 */

@Entity
public class JobTicket implements Comparable{

	@Id
	@GeneratedValue
	private int jobTicketID;
	
	@ManyToOne(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	private JobTask jobTask = new JobTask();
	
	@ManyToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	private Aircraft aircraft = new Aircraft();
	
	@ManyToOne(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	private CrewMember submittedBy = new CrewMember();
	
	@ManyToOne(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	private CrewMember assignedTo = new CrewMember();
	
		
	@ManyToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@Fetch(FetchMode.SUBSELECT)
	private List<JobHistory> jobTicketHistory = 
		new ArrayList<JobHistory>();
	
	@ManyToOne(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	private JobStatus jobTicketStatus = new JobStatus();
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(columnDefinition="TIMESTAMP DEFAULT NOW()" )
	private Date createDate = new Date();
	@Temporal(TemporalType.TIMESTAMP)
	private Date startTime;;
	@Temporal(TemporalType.TIMESTAMP)
	private Date endTime;
	
	@Column(nullable = false, columnDefinition="varchar(100) default 'no description available'")
	private String jobSubTask="";
	
	
	
	public JobTicket() {
		
	}

    @Inject
	public void createJobTicket(JobTask jobTask, Aircraft aircraft, CrewMember submittedBy, CrewMember assignedTo, StarliteCoreManager manager, String jobSubTask )
	{
		this.jobTask = jobTask;
		this.aircraft = aircraft;
		this.submittedBy = submittedBy;
		this.assignedTo = assignedTo;
		JobStatus status = null;
		boolean WIPTickets = manager.userHasWIPTickets(assignedTo.getCode());
		if (WIPTickets)
		{
			//if the user has WIP tickets, then suspend this ticket
			status = manager.getJobStatusValue("SUSPENDED");
		}
		else
		{
			//if the user doesn't have WIP tickets, then WIP this ticket
			status = manager.getJobStatusValue("WIP");
		}
		
		this.jobTicketStatus = status;
		this.jobSubTask = jobSubTask;
		
		
	}

	public int getJobTicketID() {
		return jobTicketID;
	}



	public void setJobTicketID(int jobTicketID) {
		this.jobTicketID = jobTicketID;
	}



	public JobTask getJobTask() {
		return jobTask;
	}



	public void setJobTask(JobTask jobTask) {
		this.jobTask = jobTask;
	}



	public Aircraft getAircraft() {
		return aircraft;
	}



	public void setAircraft(Aircraft aircraft) {
		this.aircraft = aircraft;
	}



	public CrewMember getSubmittedBy() {
		return submittedBy;
	}



	public void setSubmittedBy(CrewMember submittedBy) {
		this.submittedBy = submittedBy;
	}



	public CrewMember getAssignedTo() {
		return assignedTo;
	}



	public void setAssignedTo(CrewMember assignedTo) {
		this.assignedTo = assignedTo;
	}

	public JobStatus getJobTicketStatus() {
		return jobTicketStatus;
	}



	public void setJobTicketStatus(JobStatus jobTicketStatus) {
		this.jobTicketStatus = jobTicketStatus;
	}



	public Date getCreateDate() {
		return createDate;
	}



	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}



	public Date getStartTime() {
		return startTime;
	}



	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}



	public Date getEndTime() {
		return endTime;
	}



	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	public String getJobSubTask() {
		return jobSubTask;
	}

	public void setJobSubTask(String jobSubTask) {
		this.jobSubTask = jobSubTask;
	}

	/**
	 * @return the jobTicketHistory
	 */
	public List<JobHistory> getJobTicketHistory() {
		return jobTicketHistory;
	}

	/**
	 * @param jobTicketHistory the jobTicketHistory to set
	 */
	public void setJobTicketHistory(List<JobHistory> jobTicketHistory) {
		this.jobTicketHistory = jobTicketHistory;
	}

	public void addJobHistory(JobHistory history)
	{
		boolean found = false;
		
		for (int i=0; i<this.jobTicketHistory.size(); i++)
		{
			JobHistory temp = this.jobTicketHistory.get(i);
			//a ticket will never be open more than once.
			if ((temp.getJobStatus().compareTo("OPEN") == 0) && (history.getJobStatus().compareTo("OPEN") == 0))
			{
				found = true;
				break;
			}
		}
		
		if (found == false)
		{
			//if OPEN wasn't found, then add it.
			//It will never have more than one open!
			this.jobTicketHistory.add(history);
		}
		
	}

	@Override
	public int compareTo(Object arg0) {
		// TODO Auto-generated method stub
		JobTicket temp = (JobTicket)arg0;
	    final int BEFORE = -1;
	    final int EQUAL = 0;
	    final int AFTER = 1;

		if (( this.getJobTicketID()== temp.getJobTicketID())) return EQUAL; 
		if (( this.getJobTicketID() > temp.getJobTicketID())) return BEFORE;
		if (( this.getJobTicketID() < temp.getJobTicketID())) return AFTER;

	return EQUAL;
	}


}
