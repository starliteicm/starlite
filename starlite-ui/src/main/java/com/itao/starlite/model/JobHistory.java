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
	private String jobSubTaskCode;
	
	@Column(nullable=false)
	private String jobSubTaskDesc;
	
	
	

	@Column(nullable=false)
	private String jobAircraft;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date jobTimeStamp;
	

	@Column(nullable=false)
	private String jobStatus;
	
	@Column(nullable = true, columnDefinition="FLOAT(15,2) default 0.0")
	private Double totalTaskHours = 0.0;
	
	@Column(nullable = true, columnDefinition="varchar(50) default ''")
	private String changedBy = "";
	
	@Column(nullable = true, columnDefinition="varchar(250) default ''")
	private String changedByReason = "";
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date changedByDate;
	
	@Column(nullable = true, columnDefinition="varchar(50) default ''")
	private String changedByOldValue = "";
	
	@Column(nullable = true, columnDefinition="varchar(50) default ''")
	private String changedByNewValue = "";
	
	@Column(nullable = true, columnDefinition="varchar(250) default ''")
	private String subTaskValue = "";
	
	@Column(nullable = true, columnDefinition="int(20)")
	private Integer parentTicketNo;
	
	@Column(nullable = true, columnDefinition="int(5) default 0")
	private Integer captureEdit;
	
	
	
	
    public Double getTotalTaskHours() {
		return totalTaskHours;
	}



	public void setTotalTaskHours(Double hours) {
		this.totalTaskHours = hours;
	}



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


	public String getJobSubTaskCode() {
		return jobSubTaskCode;
	}



	public void setJobSubTaskCode(String jobSubTaskCode) {
		this.jobSubTaskCode = jobSubTaskCode;
	}



	public String getJobSubTaskDesc() {
		return jobSubTaskDesc;
	}



	public void setJobSubTaskDesc(String jobSubTaskDesc) {
		this.jobSubTaskDesc = jobSubTaskDesc;
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




	public String getChangedBy() {
		return changedBy;
	}



	public void setChangedBy(String changedBy) {
		this.changedBy = changedBy;
	}



	public String getChangedByReason() {
		return changedByReason;
	}



	public void setChangedByReason(String changedByReason) {
		this.changedByReason = changedByReason;
	}



	public Date getChangedByDate() {
		return changedByDate;
	}



	public void setChangedByDate(Date changedByDate) {
		this.changedByDate = changedByDate;
	}



	public String getChangedByOldValue() {
		return changedByOldValue;
	}



	public void setChangedByOldValue(String changedByOldValue) {
		this.changedByOldValue = changedByOldValue;
	}



	public String getChangedByNewValue() {
		return changedByNewValue;
	}



	public void setChangedByNewValue(String changedByNewValue) {
		this.changedByNewValue = changedByNewValue;
	}



	public String getSubTaskValue() {
		return subTaskValue;
	}



	public void setSubTaskValue(String subTaskValue) {
		this.subTaskValue = subTaskValue;
	}



	public Integer getParentTicketNo() {
		return parentTicketNo;
	}



	public void setParentTicketNo(Integer parentTicketNo) {
		this.parentTicketNo = parentTicketNo;
	}



	public Integer getCaptureEdit() {
		return captureEdit;
	}



	public void setCaptureEdit(Integer captureEdit) {
		this.captureEdit = captureEdit;
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

