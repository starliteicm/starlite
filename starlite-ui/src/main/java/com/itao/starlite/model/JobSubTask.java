package com.itao.starlite.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;


/**
 * <p>Represents a Starlite AMO Job Task. Each JobTicket is for a specific JobSubTask. </p>
 * <p>This class has been mapped for Hibernate.</p>
 * @author Celeste Groenewald
 *
 */

@Entity
public class JobSubTask implements Comparable{

	
	@Id
	@GeneratedValue
	private int JobSubTaskID;
	
	@Column(nullable = false,unique=true, columnDefinition="varchar(30)")
	private String JobSubTaskCode = "code"+JobSubTaskID; 
	
	@Column(nullable = true, columnDefinition="varchar(254) default 'no description available'")
	private String JobSubTaskDesc = "not specified"; 
	
	@Column(nullable = true, columnDefinition="varchar(254) default 'no reference available'")
	private String JobSubTaskRef="not specified"; 

	
	public JobSubTask() {
		// TODO Auto-generated constructor stub
	}
	
	
	public int getJobSubTaskID() {
		return JobSubTaskID;
	}

	public void setJobSubTaskID(int JobSubTaskID) {
		this.JobSubTaskID = JobSubTaskID;
	}

	

	public String getJobSubTaskCode() {
		return JobSubTaskCode;
	}


	public void setJobSubTaskCode(String jobSubTaskCode) {
		JobSubTaskCode = jobSubTaskCode;
	}


	public String getJobSubTaskDesc() {
		return JobSubTaskDesc;
	}


	public void setJobSubTaskDesc(String jobSubTaskDesc) {
		JobSubTaskDesc = jobSubTaskDesc;
	}


	public String getJobSubTaskRef() {
		return JobSubTaskRef;
	}


	public void setJobSubTaskRef(String jobSubTaskRef) {
		JobSubTaskRef = jobSubTaskRef;
	}


	@Override
	public int compareTo(Object o) {
		 JobSubTask temp = (JobSubTask)o;
		    final int BEFORE = -1;
		    final int EQUAL = 0;
		    final int AFTER = 1;

		    if (( this.JobSubTaskCode.compareTo(temp.getJobSubTaskCode() )== 0)) return EQUAL;
		    if (( this.JobSubTaskCode.compareTo(temp.getJobSubTaskCode() ) < 0)) return BEFORE;
		    if (( this.JobSubTaskCode.compareTo(temp.getJobSubTaskCode() ) > 0)) return AFTER;

		return EQUAL;
	}

	

}
