package com.itao.starlite.model;

import javax.persistence.Column;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;


/**
 * <p>Represents a Starlite AMO Job Status Button Image Defaults. Each button has default
 * sizes, values and images assiged to it, depending on the button's state.</p>
 * <p>This class has been mapped for Hibernate.</p>
 * @author Celeste Groenewald
 *
 */
@Entity
public class JobStatusBtnImageDefaults 
{
	@Id
	@GeneratedValue
	private Integer jobStatusBtnImageDefaults_id;	
	
	//Default Values
	@Column(nullable = false, columnDefinition="int(5) default 25")
	private  int defaultOffImageWidth = 25;
	@Column(nullable = false, columnDefinition="int(5) default 25")
	private  int defaultOffImageHeight = 25;
	@Column(nullable = false, columnDefinition="int(5) default 30")
	private  int defaultOnImageWidth = 30;
	@Column(nullable = false, columnDefinition="int(5) default 30")
	private  int defaultOnImageHeight = 30;
	
	@Column(nullable = false, columnDefinition="varchar(300) default 'green_light.png'")
    private String defaultOffImageWIP="green_light.png"; // image filename i.e. \\images\\green_light.png
	@Column(nullable = false, columnDefinition="varchar(300) default 'green_dark.png'")
    private String defaultOnImageWIP="green_dark.png"; // image filename i.e.  \\images\\green_dark.png

	@Column(nullable = false, columnDefinition="varchar(300) default 'orange_light.png'")
    private String defaultOffImageSUSPENDED="orange_light.png"; 
	@Column(nullable = false, columnDefinition="varchar(300) default 'orange_dark.png'")
    private String defaultOnImageSUSPENDED="orange_dark.png"; 

	@Column(nullable = false, columnDefinition="varchar(300) default 'blue_light.png'")
    private String defaultOffImageCLOSED="blue_light.png"; 
	@Column(nullable = false, columnDefinition="varchar(300) default 'blue_dark.png'")
    private String defaultOnImageCLOSED="blue_dark.png"; 

	
	public JobStatusBtnImageDefaults(){}
	
	public Integer getJobStatusBtnImageDefaults_id() {
		return jobStatusBtnImageDefaults_id;
	}
	public void setJobStatusBtnImageDefaults_id(Integer jobStatusBtnImageDefaults_id) {
		this.jobStatusBtnImageDefaults_id = jobStatusBtnImageDefaults_id;
	}
	public int getDefaultOffImageWidth() {
		return defaultOffImageWidth;
	}
	public void setDefaultOffImageWidth(int defaultOffImageWidth) {
		this.defaultOffImageWidth = defaultOffImageWidth;
	}
	
	
	public int getDefaultOffImageHeight() {
		return defaultOffImageHeight;
	}
	public void setDefaultOffImageHeight(int defaultOffImageHeight) {
		this.defaultOffImageHeight = defaultOffImageHeight;
	}
	
	
	public int getDefaultOnImageWidth() {
		return defaultOnImageWidth;
	}
	public void setDefaultOnImageWidth(int defaultOnImageWidth) {
		this.defaultOnImageWidth = defaultOnImageWidth;
	}
	
	
	public int getDefaultOnImageHeight() {
		return defaultOnImageHeight;
	}
	public void setDefaultOnImageHeight(int defaultOnImageHeight) {
		this.defaultOnImageHeight = defaultOnImageHeight;
	}

	/**
	 * @return the defaultOffImageWIP
	 */
	public String getDefaultOffImageWIP() {
		return defaultOffImageWIP;
	}

	/**
	 * @param defaultOffImageWIP the defaultOffImageWIP to set
	 */
	public void setDefaultOffImageWIP(String defaultOffImageWIP) {
		this.defaultOffImageWIP = defaultOffImageWIP;
	}

	/**
	 * @return the defaultOnImageWIP
	 */
	public String getDefaultOnImageWIP() {
		return defaultOnImageWIP;
	}

	/**
	 * @param defaultOnImageWIP the defaultOnImageWIP to set
	 */
	public void setDefaultOnImageWIP(String defaultOnImageWIP) {
		this.defaultOnImageWIP = defaultOnImageWIP;
	}

	/**
	 * @return the defaultOffImageSUSPENDED
	 */
	public String getDefaultOffImageSUSPENDED() {
		return defaultOffImageSUSPENDED;
	}

	/**
	 * @param defaultOffImageSUSPENDED the defaultOffImageSUSPENDED to set
	 */
	public void setDefaultOffImageSUSPENDED(String defaultOffImageSUSPENDED) {
		this.defaultOffImageSUSPENDED = defaultOffImageSUSPENDED;
	}

	/**
	 * @return the defaultOnImageSUSPENDED
	 */
	public String getDefaultOnImageSUSPENDED() {
		return defaultOnImageSUSPENDED;
	}

	/**
	 * @param defaultOnImageSUSPENDED the defaultOnImageSUSPENDED to set
	 */
	public void setDefaultOnImageSUSPENDED(String defaultOnImageSUSPENDED) {
		this.defaultOnImageSUSPENDED = defaultOnImageSUSPENDED;
	}

	/**
	 * @return the defaultOffImageCLOSED
	 */
	public String getDefaultOffImageCLOSED() {
		return defaultOffImageCLOSED;
	}

	/**
	 * @param defaultOffImageCLOSED the defaultOffImageCLOSED to set
	 */
	public void setDefaultOffImageCLOSED(String defaultOffImageCLOSED) {
		this.defaultOffImageCLOSED = defaultOffImageCLOSED;
	}

	/**
	 * @return the defaultOnImageCLOSED
	 */
	public String getDefaultOnImageCLOSED() {
		return defaultOnImageCLOSED;
	}

	/**
	 * @param defaultOnImageCLOSED the defaultOnImageCLOSED to set
	 */
	public void setDefaultOnImageCLOSED(String defaultOnImageCLOSED) {
		this.defaultOnImageCLOSED = defaultOnImageCLOSED;
	}
	
	
}
