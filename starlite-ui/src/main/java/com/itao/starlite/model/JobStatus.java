package com.itao.starlite.model;



import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.google.inject.Inject;
import com.itao.starlite.manager.StarliteCoreManager;


/**
 * <p>Represents a Starlite AMO JobStatus. </p>
 * <p>This class has been mapped for Hibernate.</p>
 * @author Celeste Groenewald
 *
 */


@SuppressWarnings("unchecked")
@Entity
public class JobStatus implements Cloneable, Comparable {
	
    @Id
	@GeneratedValue
	private Integer jobstatus_id;
	@Column(unique=true)
    private String jobstatus_value;
	

		
    public JobStatus() 
    {
    	this.jobstatus_value = "OPEN";
    	
    }
	
     
	public int getJobStatusID() {
		return this.jobstatus_id;
	}
	public void setJobStatusID(int id) {
		this.jobstatus_id = id;
	}
	
	public void setJobStatusValue(String value) 
	{
		this.jobstatus_value = value;
		
	}

	public String getJobStatusValue() {
		return this.jobstatus_value;
	}
	
	public String toString(){
		return "{id:"+jobstatus_id+",jobstatus_value:"+getJobStatusValue()+"}";
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
		// TODO Auto-generated method stub
		
		    JobStatus temp = (JobStatus)arg0;
		    final int BEFORE = -1;
		    final int EQUAL = 0;
		    final int AFTER = 1;

		    if (( this.jobstatus_value.compareTo(temp.jobstatus_value )==0)) return EQUAL;
		    if (( this.jobstatus_value.compareTo(temp.jobstatus_value )== -1)) return BEFORE;
		    if (( this.jobstatus_value.compareTo(temp.jobstatus_value )== 1)) return AFTER;

		return EQUAL;
	}

}
