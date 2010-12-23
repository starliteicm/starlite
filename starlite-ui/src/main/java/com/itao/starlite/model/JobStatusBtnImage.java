package com.itao.starlite.model;


import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.ManyToOne;




/**
 * <p>Represents a Starlite AMO Job Status Button Image. Each button has an image and 
 * default sizes.</p>
 * <p>This class has been mapped for Hibernate.</p>
 * @author Celeste Groenewald
 *
 */
@Entity
public class JobStatusBtnImage 
{
	@Id
	@GeneratedValue
	private int JobStatusBtnImageId;
	@Column(nullable = true)
	private String jobStatusBtnImageToolTip;
	
	//Default Values
	@ManyToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	private JobStatusBtnImageDefaults jsBtnImgDefs = new JobStatusBtnImageDefaults();
	
	public JobStatusBtnImage()
	{
		
	}
	
	public int getJobStatusBtnImageId() {
		return JobStatusBtnImageId;
	}
	public void setJobStatusBtnImageId(int jobStatusBtnImageId) {
		JobStatusBtnImageId = jobStatusBtnImageId;
	}
	
	
	public String getJobStatusBtnImageToolTip() {
		return jobStatusBtnImageToolTip;
	}
	public void setJobStatusBtnImageToolTip(String jobStatusBtnImageToolTip) {
		this.jobStatusBtnImageToolTip = jobStatusBtnImageToolTip;
	}


	/**
	 * @return the jsBtnImgDefs
	 */
	public JobStatusBtnImageDefaults getJsBtnImgDefs() {
		return jsBtnImgDefs;
	}


	
	

}
