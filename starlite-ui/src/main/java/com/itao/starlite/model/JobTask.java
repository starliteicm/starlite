package com.itao.starlite.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;


/**
 * <p>Represents a Starlite AMO Job Task. Each JobTicket is for a specific JobTask. </p>
 * <p>This class has been mapped for Hibernate.</p>
 * @author Celeste Groenewald
 *
 */

@Entity
public class JobTask implements Comparable{

	
	@Id
	@GeneratedValue
	private int jobTaskID;
	
	@Column(unique=true, nullable = false)
	private String jobTaskValue = "TaskX"; 
	
	@Column(nullable = true)
	private String jobTaskNotes="TaskX Notes"; 

	
	public JobTask() {
		// TODO Auto-generated constructor stub
	}
	
	
	public int getJobTaskID() {
		return jobTaskID;
	}

	public void setJobTaskID(int jobTaskID) {
		this.jobTaskID = jobTaskID;
	}

	public String getJobTaskValue() {
		return jobTaskValue;
	}

	public void setJobTaskValue(String jobTaskValue) {
		this.jobTaskValue = jobTaskValue;
	}

	public String getJobTaskNotes() {
		return jobTaskNotes;
	}

	public void setJobTaskNotes(String jobTaskNotes) {
		this.jobTaskNotes = jobTaskNotes;
	}


	@Override
	public int compareTo(Object o) {
		 JobTask temp = (JobTask)o;
		    final int BEFORE = -1;
		    final int EQUAL = 0;
		    final int AFTER = 1;

		    if (( this.jobTaskValue.compareTo(temp.getJobTaskValue() )== 0)) return EQUAL;
		    if (( this.jobTaskValue.compareTo(temp.getJobTaskValue() )< 0)) return BEFORE;
		    if (( this.jobTaskValue.compareTo(temp.getJobTaskValue() )> 0)) return AFTER;

		return EQUAL;
	}

	

}
