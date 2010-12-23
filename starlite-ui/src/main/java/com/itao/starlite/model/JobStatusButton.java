/**
 * 
 */
package com.itao.starlite.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.ManyToOne;


/**
 * <p>Represents a single Starlite AMO Job Status Button. A button has a state (ON or OFF)
 * and a status (OPEN\WIP, SUSPENDED or CLOSED.</p>
 * <p>This class has been mapped for Hibernate.</p>
 * @author Celeste Groenewald
 *
 */

@Entity
public class JobStatusButton  
{
	@Id
	@GeneratedValue
	private int jobStatusBtnID;
	
	@ManyToOne(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	private JobStatus jobStatus = new JobStatus();   //Open or WIP or Suspended or Close
	
	@Column(nullable = false, columnDefinition="int(5) default 0")
	private int jobStatusBtnState=0; //0 is off; 1 is on
	
	@Column(nullable = false, columnDefinition="varchar(50) default 'green_light.png' ")
	private String currentImagePath ="";
	@Column(nullable = false, columnDefinition="int(5) default 25")
	private int currentImageWidth = 25;
	@Column(nullable = false, columnDefinition="int(5) default 25")
	private int currentImageHeight = 25;
	
	
	public JobStatusButton() 
	{
		//Values set when the Status is set
	}
	public int getJobStatusBtnID()
	{
		return(this.jobStatusBtnID);
	}
	
	public void setJobStatusBtnID(int jobStatusBtnID)
	{
		this.jobStatusBtnID = jobStatusBtnID;
		
	}
	
	
	public JobStatus getJobStatus()
	{
		return(this.jobStatus);
	}
	
	public void setJobStatus(JobStatus jobStatus)
	{
		this.jobStatus = jobStatus;
		this.currentImageHeight = jobStatus.getJsBtnImg().getJsBtnImgDefs().getDefaultOffImageHeight();
		this.currentImageWidth = jobStatus.getJsBtnImg().getJsBtnImgDefs().getDefaultOffImageWidth();
		
		setOffImageFromStatusValue();
		
     }

	private void setOffImageFromStatusValue()
	{
		if ((jobStatus.getJobStatusValue().compareTo("WIP") == 0) || ((jobStatus.getJobStatusValue().compareTo("OPEN") == 0)))
		{
			this.currentImagePath = jobStatus.getJsBtnImg().getJsBtnImgDefs().getDefaultOffImageWIP();
		}
		else if (jobStatus.getJobStatusValue().compareTo("SUSPENDED") == 0)
		{
			this.currentImagePath = jobStatus.getJsBtnImg().getJsBtnImgDefs().getDefaultOffImageSUSPENDED();
		}
		else
		{
			this.currentImagePath = jobStatus.getJsBtnImg().getJsBtnImgDefs().getDefaultOffImageCLOSED();
		}
	}
	
	private void setOnImageFromStatusValue()
	{
		if ((jobStatus.getJobStatusValue().compareTo("WIP") == 0) || ((jobStatus.getJobStatusValue().compareTo("OPEN") == 0)))
		{
			this.currentImagePath = jobStatus.getJsBtnImg().getJsBtnImgDefs().getDefaultOnImageWIP();
		}
		else if (jobStatus.getJobStatusValue().compareTo("SUSPENDED") == 0)
		{
			this.currentImagePath = jobStatus.getJsBtnImg().getJsBtnImgDefs().getDefaultOnImageSUSPENDED();
		}
		else
		{
			this.currentImagePath = jobStatus.getJsBtnImg().getJsBtnImgDefs().getDefaultOnImageCLOSED();
		}
	}
	
	public int getJobStatusBtnState() {
		return this.jobStatusBtnState;
	}

	public void setJobStatusBtnState(int jobStatusBtnState) {
		this.jobStatusBtnState = jobStatusBtnState; // 0 is off, 1 is on
		switch (this.jobStatusBtnState)
		{
		case 0: {//OFF
			     this.currentImageHeight = this.jobStatus.getJsBtnImg().getJsBtnImgDefs().getDefaultOffImageHeight();
			     this.currentImageWidth = this.jobStatus.getJsBtnImg().getJsBtnImgDefs().getDefaultOffImageWidth();
			     this.setOffImageFromStatusValue();
			     break;
		        }
		case 1: {//ON
			    this.currentImageHeight = this.jobStatus.getJsBtnImg().getJsBtnImgDefs().getDefaultOnImageHeight();
		        this.currentImageWidth = this.jobStatus.getJsBtnImg().getJsBtnImgDefs().getDefaultOnImageWidth();
		        this.setOnImageFromStatusValue();
			    break;
		        }
		}
	}

	
	public int getCurBtnWidth() 
	{
		return(this.currentImageWidth);
	
	}
	
	public int getCurBtnHeight() 
	{
		return(this.currentImageHeight);
	
	}
	public String getCurBtnImgPath() 
	{
		return(this.currentImagePath);
	
	}
	public String getCurBtnStatusValue() 
	{
		String value = this.jobStatus.getJobStatusValue();
		if (value.compareTo("WIP") == 0)
		{
			value = "WORK on";
		}
		else if (value.compareTo("SUSPENDED") == 0)
		{
			value = "SUSPEND";
		}
		else if (value.compareTo("CLOSED") == 0)
		{
			value = "CLOSE";
		}
		return(value);
	
	}
	/**
	 * @return the currentImagePath
	 */
	
	public void setCurrentImagePath(String currentImagePath) {
		this.currentImagePath = currentImagePath;
	}
	
	/**
	 * @param currentImageWidth the currentImageWidth to set
	 */
	public void setCurrentImageWidth(int currentImageWidth) {
		this.currentImageWidth = currentImageWidth;
	}
	
	/**
	 * @param currentImageHeight the currentImageHeight to set
	 */
	public void setCurrentImageHeight(int currentImageHeight) {
		this.currentImageHeight = currentImageHeight;
	}
}
