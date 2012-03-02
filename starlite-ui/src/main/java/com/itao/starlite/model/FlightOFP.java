package com.itao.starlite.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.itao.starlite.manager.StarliteCoreManager;


/**
 * @author Celeste Groenewald
 * <p>An OFP contains up to 4 Flight Plans (Actuals).</p> 
 */
      
@Entity
public class FlightOFP
{
	@Id
	@GeneratedValue
	public int id=0;
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;
	
	@Column(nullable = false, columnDefinition="varchar(50) default ''")
	private String externalOFPRefNumber = "";
    @Temporal(TemporalType.DATE)
	private Date OFPDate;
    @Column(nullable = false, columnDefinition="varchar(100) default ''")
	private String contract = "";
    @ManyToOne(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	private Aircraft aircraft;
	@Column(nullable = false, columnDefinition="varchar(100) default ''")
	private String invoiceNo = "";
	@Column(nullable = false, columnDefinition="varchar(100) default ''")
	private String flightType = "";
	@ManyToOne(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	private FlightGCatagory gCatagory;
	
	@Column(nullable = false, columnDefinition="varchar(100) default ''")
	private String pilot = "";
	@Column(nullable = false, columnDefinition="varchar(100) default ''")
	private String coPilot = "";
	@Column(nullable = false, columnDefinition="varchar(100) default ''")
	private String AME = "";
	
	@ManyToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@Fetch(FetchMode.SUBSELECT)
	private List<FlightPlan> landings = new ArrayList<FlightPlan>();
	
	
	@Column(nullable = false, columnDefinition="FLOAT(8,2) default 0.0")
    private float PAX = 0.0F;  
	@Column(nullable = false, columnDefinition="FLOAT(8,2) default 0.0")
    private float freight = 0.0F;   
	@Column(nullable = false, columnDefinition="FLOAT(8,2) default 0.0")
    private float totalLoad = 0.0F;  
	
	@Column(nullable = false, columnDefinition="varchar(50) default ''")
	private String ACServiceType = "";	
	@Column(nullable = false, columnDefinition="FLOAT(8,2) default 0.0")
    private float ACHrsToNextService = 0.0F; 	
	
	@OneToOne(fetch=FetchType.EAGER)
	private FlightActualStatus flightActualStatus =null;
	
	@Transient
	public Integer newStatusId = 0;
	
	
	public float getPAX() {
		return PAX;
	}

	public void setPAX(float pax) {
		PAX = pax;
	}

	public float getFreight() {
		return freight;
	}

	public void setFreight(float freight) {
		this.freight = freight;
	}

	public float getTotalLoad() {
		return totalLoad;
	}

	public void setTotalLoad(float totalLoad) {
		this.totalLoad = totalLoad;
	}

	public FlightOFP(){}
	
	public void setValues(FlightOFP flight, StarliteCoreManager manager)
	{
		this.ACHrsToNextService = flight.getACHrsToNextService();
		this.ACServiceType = flight.getACServiceType();
		this.aircraft = flight.getAircraft();
		this.AME = flight.getAME();
		this.contract = flight.getContract();
		this.coPilot = flight.getCoPilot();
		this.createDate = flight.getCreateDate();
		this.externalOFPRefNumber = flight.getExternalOFPRefNumber();
		this.flightType = flight.getFlightType();
		this.gCatagory = flight.getGCatagory();
		this.invoiceNo = flight.getInvoiceNo();
		this.landings = flight.getLandings();
		this.OFPDate = flight.getOFPDate();
		this.pilot = flight.getPilot();
		this.PAX = flight.getPAX();
		this.freight = flight.getFreight();
		this.totalLoad = flight.getTotalLoad();
		this.ACHrsToNextService = flight.getACHrsToNextService();
		this.ACServiceType = flight.getACServiceType();
		if (flight.flightActualStatus == null)
		{
			this.flightActualStatus = manager.findStatusValueByID(1);
			
		}
	}
	public void setNewActualStatusValueObject(FlightActualStatus newValue)
    {
    	this.flightActualStatus = newValue;
    }
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getExternalOFPRefNumber() {
		return externalOFPRefNumber;
	}

	public void setExternalOFPRefNumber(String externalOFPRefNumber) {
		this.externalOFPRefNumber = externalOFPRefNumber;
	}

	public Date getOFPDate() {
		return OFPDate;
	}

	public void setOFPDate(Date date) {
		OFPDate = date;
	}

	public String getContract() {
		return contract;
	}

	public void setContract(String contract) {
		this.contract = contract;
	}

	public Aircraft getAircraft() {
		return aircraft;
	}

	public void setAircraft(Aircraft aircraft) {
		this.aircraft = aircraft;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getFlightType() {
		return flightType;
	}

	public void setFlightType(String flightType) {
		this.flightType = flightType;
	}

	public FlightGCatagory getGCatagory() {
		return gCatagory;
	}

	public void setGCatagory(FlightGCatagory catagory) {
		gCatagory = catagory;
	}

	public String getPilot() {
		return pilot;
	}

	public void setPilot(String pilot) {
		this.pilot = pilot;
	}

	public String getCoPilot() {
		return coPilot;
	}

	public void setCoPilot(String coPilot) {
		this.coPilot = coPilot;
	}

	public String getAME() {
		return AME;
	}

	public void setAME(String ame) {
		AME = ame;
	}

	public String getACServiceType() {
		return ACServiceType;
	}

	public void setACServiceType(String serviceType) {
		ACServiceType = serviceType;
	}

	public float getACHrsToNextService() {
		return ACHrsToNextService;
	}

	public void setACHrsToNextService(float hrsToNextService) {
		ACHrsToNextService = hrsToNextService;
	}

	public List<FlightPlan> getLandings() {
		return landings;
	}

	public void setLandings(List<FlightPlan> landings) {
		this.landings = landings;
	}

	public void setFlightActualStatus(FlightActualStatus flightActualStatus) {
		this.flightActualStatus = flightActualStatus;
	}

	public FlightActualStatus getFlightActualStatus() 
	{		
		return flightActualStatus;
	}
	

	public Integer getNewStatusId() {
		return newStatusId;
	}

	public void setNewStatusId(Integer newStatusId) {
		this.newStatusId = newStatusId;
	}


	
}



