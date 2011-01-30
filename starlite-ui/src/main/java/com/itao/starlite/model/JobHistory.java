package com.itao.starlite.model;



import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;



/**
 * <p>Represents a Starlite AMO Job History. </p>
 * <p>This class has been mapped for Hibernate.</p>
 * @author Celeste Groenewald
 *
 */


@Entity
public class JobHistory implements Cloneable, Comparable {
	@Id
	@GeneratedValue
	private Integer jobhistoryID;

	@Column(nullable=false)
	private String assignedTo;
	
	@Column(nullable=false)
	private String jobTaskValue;
	
	@Column(nullable=false)
	private String jobAircraft;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date jobTimeStamp;
	

	@Column(nullable=false)
	private String jobStatus;
	
    public JobHistory() {}
	


	/**
	 * @return the jobhistoryID
	 */
	public Integer getJobhistoryID() {
		return jobhistoryID;
	}




	/**
	 * @param jobhistoryID the jobhistoryID to set
	 */
	public void setJobhistoryID(Integer jobhistoryID) {
		this.jobhistoryID = jobhistoryID;
	}




	/**
	 * @return the assignedTo
	 */
	public String getAssignedTo() {
		return assignedTo;
	}




	/**
	 * @param assignedTo the assignedTo to set
	 */
	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}




	/**
	 * @return the jobTaskValue
	 */
	public String getJobTaskValue() {
		return jobTaskValue;
	}




	/**
	 * @param jobTaskValue the jobTaskValue to set
	 */
	public void setJobTaskValue(String jobTaskValue) {
		this.jobTaskValue = jobTaskValue;
	}




	/**
	 * @return the jobAircraft
	 */
	public String getJobAircraft() {
		return jobAircraft;
	}




	/**
	 * @param jobAircraft the jobAircraft to set
	 */
	public void setJobAircraft(String jobAircraft) {
		this.jobAircraft = jobAircraft;
	}




	

	/**
	 * @return the jobTimeStamp
	 */
	public Date getJobTimeStamp() {
		return jobTimeStamp;
	}




	/**
	 * @param jobTimeStamp the jobTimeStamp to set
	 */
	public void setJobTimeStamp(Date jobTimeStamp) {
		this.jobTimeStamp = jobTimeStamp;
	}




	/**
	 * @return the jobStatus
	 */
	public String getJobStatus() {
		return jobStatus;
	}




	/**
	 * @param jobStatus the jobStatus to set
	 */
	public void setJobStatus(String jobStatus) {
		this.jobStatus = jobStatus;
	}




	public Object clone() throws CloneNotSupportedException {
    	try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			throw(e);
		}
    }



	@Override
	public int compareTo(Object arg0) {

		JobHistory temp = (JobHistory)arg0;
	    final int BEFORE = -1;
	    final int EQUAL = 0;
	    final int AFTER = 1;

		if (( this.jobhistoryID == temp.getJobhistoryID())) return EQUAL; 
		if (( this.jobhistoryID > temp.getJobhistoryID())) return BEFORE;
		if (( this.jobhistoryID < temp.getJobhistoryID())) return AFTER;
		
		
		return 0;
	}


	

    
}

