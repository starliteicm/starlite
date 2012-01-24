package com.itao.starlite.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * @author Celeste Groenewald
 * <p>The flight plan is entered by Starlite and will be compared against the FlightLog coming from IndigoSat.</p> 
 */
      
@Entity
public class FlightPlan implements Comparable
{
	@Id
	@GeneratedValue
	public int flightPlanID=0;
	@Column(nullable = false, columnDefinition="int(5) default 0")
    private int internalOFPRefNumber=0; 
	@Column(nullable = false, columnDefinition="varchar(50) default ''")
    private String externalOFPRefNumber="";
	
	@Column(nullable = false, columnDefinition="varchar(50) default ''")
    private String start_LocationField=""; //14    
	@Column(nullable = false, columnDefinition="varchar(50) default ''")
	private String end_LocationField=""; //15
	
	@Column(nullable = false, columnDefinition="real(8,2) default '0.0'")
	private Double flightTime = 0.0;
	@Column(nullable = false, columnDefinition="real(8,2) default '0.0'")
	private Double hobbsOn = 0.0;
	@Column(nullable = false, columnDefinition="real(8,2) default '0.0'")
	private Double hobbsOff = 0.0;
	@Column(nullable = false, columnDefinition="real(8,2) default '0.0'")
	private Double hobbsDuration = 0.0;
	
	
	
	// Not used at this moment
	@Column(nullable = false, columnDefinition="FLOAT(8,2) default 0.0")
    private float PAX = 0.0F;  
	@Column(nullable = false, columnDefinition="FLOAT(8,2) default 0.0")
    private float freight = 0.0F;   
	@Column(nullable = false, columnDefinition="FLOAT(8,2) default 0.0")
    private float totalLoad = 0.0F;  
	
	@Column(nullable = false, columnDefinition="varchar(50) default ''")
    private String registrationField=""; //1 
	@Column(nullable = false, columnDefinition="varchar(20) default ''")
	private String invoiceNo = "";
	@Column(nullable = false, columnDefinition="varchar(100) default ''")
	private String flightType = "";
	@Column(nullable = false, columnDefinition="varchar(100) default ''")
	private String flightEngineer = "";
	@Column(nullable = false, columnDefinition="varchar(20) default ''")
	private String flightNumber = "";
	@Column(nullable = false, columnDefinition="varchar(50) default ''")
	private String ACServiceType = "";	
	@Column(nullable = false, columnDefinition="FLOAT(8,2) default 0.0")
    private float ACHrsToNextService = 0.0F; 	
	
	
	@Column(nullable = false, columnDefinition="int(8) default 0")
    private int aircraftIDField = 0;  
	@Column(nullable = false, columnDefinition="varchar(100) default ''")
    private String customer = "";
	@Column(nullable = false, columnDefinition="varchar(50) default ''")
    private String flightDateField="";    //2
	@Column(nullable = false, columnDefinition="varchar(50) default ''")
    private String generatedDateField="";    
	@Column(nullable = false, columnDefinition="varchar(50) default ''")
    private String flight_TypeField="";    //3
	@Column(nullable = false, columnDefinition="varchar(50) default ''")
    private String reference_NumberField="";    
	@Column(nullable = false, columnDefinition="varchar(100) default ''")
    private String captainField="";    //4
	@Column(nullable = false, columnDefinition="varchar(100) default ''")
	private String first_OfficerField=""; //5
	@Column(nullable = false, columnDefinition="varchar(100) default ''")
    private String cabin_CrewField="";   //6
	@Column(nullable = false, columnDefinition="varchar(100) default ''")
    private String passengersField="";   
	@Column(nullable = false, columnDefinition="varchar(50) default ''")
    private String starting_FuelField="";//7    
	@Column(nullable = false, columnDefinition="varchar(50) default ''")
    private String fuel_UpliftField="";    //8
	@Column(nullable = false, columnDefinition="varchar(50) default ''")
    private String receipt_NumberField="";    
	@Column(nullable = false, columnDefinition="varchar(50) default ''")
    private String priceField="";    
	@Column(nullable = false, columnDefinition="varchar(50) default ''")
    private String flight_ReportField="";    //9
	@Column(nullable = false, columnDefinition="varchar(50) default ''")
    private String defects_ReportField="";    
	@Column(nullable = false, columnDefinition="varchar(50) default ''")
    private String power_StartField="";    
	@Column(nullable = false, columnDefinition="varchar(50) default ''")
    private String doors_ClosedField="";    
	@Column(nullable = false, columnDefinition="varchar(50) default ''")
    private String engine_OnField="";    
	@Column(nullable = false, columnDefinition="varchar(50) default ''")
    private String block_StartField="";    
	@Column(nullable = false, columnDefinition="varchar(50) default ''")
    private String takeoffField="";    //11
	@Column(nullable = false, columnDefinition="varchar(50) default ''")
    private String landingField="";    //12
	@Column(nullable = false, columnDefinition="varchar(50) default ''")
    private String block_EndField="";    
	@Column(nullable = false, columnDefinition="varchar(50) default ''")
    private String engine_OffField="";    
	@Column(nullable = false, columnDefinition="varchar(50) default ''")
    private String doors_OpenedField="";    
	@Column(nullable = false, columnDefinition="varchar(50) default ''")
    private String power_EndField="";    
	@Column(nullable = false, columnDefinition="varchar(50) default ''")
    private String ETD="";  
	@Column(nullable = false, columnDefinition="FLOAT(8,2) default 0.0")
    private float gPS_DistanceField=0.0F;    //13
	
	@Column(nullable = false, columnDefinition="int(8) default 0")
    private int airframe_CyclesField=0;    
	@Column(nullable = false, columnDefinition="int(8) default 0")
    private int engine_CyclesField=0;    
   
    public FlightPlan()
    {
    	//nothing
    }
   
	public int getInternalOFPRefNumber() {
		return internalOFPRefNumber;
	}

	public void setInternalOFPRefNumber(int internalOFPRefNumber) {
		this.internalOFPRefNumber = internalOFPRefNumber;
	}

	public String getExternalOFPRefNumber() {
		return externalOFPRefNumber;
	}

	public void setExternalOFPRefNumber(String externalOFPRefNumber) {
		this.externalOFPRefNumber = externalOFPRefNumber;
	}

	public Double getFlightTime() {
		return flightTime;
	}

	public void setFlightTime(Double flightTime) {
		this.flightTime = flightTime;
	}

	public Double getHobbsOn() {
		return hobbsOn;
	}

	public void setHobbsOn(Double hobbsOn) {
		this.hobbsOn = hobbsOn;
	}

	public Double getHobbsOff() {
		return hobbsOff;
	}

	public void setHobbsOff(Double hobbsOff) {
		this.hobbsOff = hobbsOff;
	}

	public Double getHobbsDuration() {
		return hobbsDuration;
	}

	public void setHobbsDuration(Double hobbsDuration) {
		this.hobbsDuration = hobbsDuration;
	}

	/**
	 * @return the customer
	 */
	public String getCustomer() {
		return customer;
	}

	/**
	 * @param customer the customer to set
	 */
	public void setCustomer(String customer) {
		this.customer = customer;
	}

	/**
	 * @return the invoiceNo
	 */
	public String getInvoiceNo() {
		return invoiceNo;
	}

	/**
	 * @param invoiceNo the invoiceNo to set
	 */
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	

	/**
	 * @return the flightEngineer
	 */
	public String getFlightEngineer() {
		return flightEngineer;
	}

	/**
	 * @param flightEngineer the flightEngineer to set
	 */
	public void setFlightEngineer(String flightEngineer) {
		this.flightEngineer = flightEngineer;
	}

	/**
	 * @return the flightType
	 */
	public String getFlightType() {
		return flightType;
	}

	/**
	 * @param flightType the flightType to set
	 */
	public void setFlightType(String flightType) {
		this.flightType = flightType;
	}

	/**
	 * @return the eTD
	 */
	public String getETD() {
		return ETD;
	}

	/**
	 * @param etd the eTD to set
	 */
	public void setETD(String etd) {
		ETD = etd;
	}

	/**
	 * @return the flightNumber
	 */
	public String getFlightNumber() {
		return flightNumber;
	}

	/**
	 * @param flightNumber the flightNumber to set
	 */
	public void setFlightNumber(String flightNumber) {
		this.flightNumber = flightNumber;
	}

	/**
	 * @return the aCServiceType
	 */
	public String getACServiceType() {
		return ACServiceType;
	}

	/**
	 * @param serviceType the aCServiceType to set
	 */
	public void setACServiceType(String serviceType) {
		ACServiceType = serviceType;
	}

	/**
	 * @return the aCHrsToNextService
	 */
	public float getACHrsToNextService() {
		return ACHrsToNextService;
	}

	/**
	 * @param hrsToNextService the aCHrsToNextService to set
	 */
	public void setACHrsToNextService(float hrsToNextService) {
		ACHrsToNextService = hrsToNextService;
	}

	/**
	 * @return the pAX
	 */
	public float getPAX() {
		return PAX;
	}

	/**
	 * @param f the pAX to set
	 */
	public void setPAX(float f) {
		PAX = f;
	}

	/**
	 * @return the freight
	 */
	public float getFreight() {
		return freight;
	}

	/**
	 * @param freight the freight to set
	 */
	public void setFreight(float freight) {
		this.freight = freight;
	}

	/**
	 * @return the totalLoad
	 */
	public float getTotalLoad() {
		return totalLoad;
	}

	/**
	 * @param f the totalLoad to set
	 */
	public void setTotalLoad(float f) {
		this.totalLoad = f;
	}

	/**
	 * @param flightIDField the flightIDField to set
	 */
	public void setFlightPlanID(int flightPlanID) {
		this.flightPlanID = flightPlanID;
	}

	/**
	 * @param flightIDField the flightIDField to set
	 */
	public int getFlightPlanID() {
		return (this.flightPlanID);
	}

	
	/**
	 * @return the aircraftIDField
	 */
	public int getAircraftIDField() {
		return aircraftIDField;
	}

	/**
	 * @param aircraftIDField the aircraftIDField to set
	 */
	public void setAircraftIDField(int aircraftIDField) {
		this.aircraftIDField = aircraftIDField;
	}

	/**
	 * @return the registrationField
	 */
	public String getRegistrationField() {
		return registrationField;
	}

	/**
	 * @param registrationField the registrationField to set
	 */
	public void setRegistrationField(String registrationField) {
		this.registrationField = registrationField;
	}

	/**
	 * @return the flightDateField
	 */
	public String getFlightDateField() {
		return flightDateField;
	}

	/**
	 * @param flightDateField the flightDateField to set
	 */
	public void setFlightDateField(String flightDateField) {
		this.flightDateField = flightDateField;
	}

	/**
	 * @return the generatedDateField
	 */
	public String getGeneratedDateField() {
		return generatedDateField;
	}

	/**
	 * @param generatedDateField the generatedDateField to set
	 */
	public void setGeneratedDateField(String generatedDateField) {
		this.generatedDateField = generatedDateField;
	}

	/**
	 * @return the flight_TypeField
	 */
	public String getFlight_TypeField() {
		return flight_TypeField;
	}

	/**
	 * @param flight_TypeField the flight_TypeField to set
	 */
	public void setFlight_TypeField(String flight_TypeField) {
		this.flight_TypeField = flight_TypeField;
	}

	/**
	 * @return the reference_NumberField
	 */
	public String getReference_NumberField() {
		return reference_NumberField;
	}

	/**
	 * @param reference_NumberField the reference_NumberField to set
	 */
	public void setReference_NumberField(String reference_NumberField) {
		this.reference_NumberField = reference_NumberField;
	}

	/**
	 * @return the captainField
	 */
	public String getCaptainField() {
		return captainField;
	}

	/**
	 * @param captainField the captainField to set
	 */
	public void setCaptainField(String captainField) {
		this.captainField = captainField;
	}

	/**
	 * @return the first_OfficerField
	 */
	public String getFirst_OfficerField() {
		return first_OfficerField;
	}

	/**
	 * @param first_OfficerField the first_OfficerField to set
	 */
	public void setFirst_OfficerField(String first_OfficerField) {
		this.first_OfficerField = first_OfficerField;
	}

	/**
	 * @return the cabin_CrewField
	 */
	public String getCabin_CrewField() {
		return cabin_CrewField;
	}

	/**
	 * @param cabin_CrewField the cabin_CrewField to set
	 */
	public void setCabin_CrewField(String cabin_CrewField) {
		this.cabin_CrewField = cabin_CrewField;
	}

	/**
	 * @return the passengersField
	 */
	public String getPassengersField() {
		return passengersField;
	}

	/**
	 * @param passengersField the passengersField to set
	 */
	public void setPassengersField(String passengersField) {
		this.passengersField = passengersField;
	}

	/**
	 * @return the starting_FuelField
	 */
	public String getStarting_FuelField() {
		return starting_FuelField;
	}

	/**
	 * @param starting_FuelField the starting_FuelField to set
	 */
	public void setStarting_FuelField(String starting_FuelField) {
		this.starting_FuelField = starting_FuelField;
	}

	/**
	 * @return the fuel_UpliftField
	 */
	public String getFuel_UpliftField() {
		return fuel_UpliftField;
	}

	/**
	 * @param fuel_UpliftField the fuel_UpliftField to set
	 */
	public void setFuel_UpliftField(String fuel_UpliftField) {
		this.fuel_UpliftField = fuel_UpliftField;
	}

	/**
	 * @return the receipt_NumberField
	 */
	public String getReceipt_NumberField() {
		return receipt_NumberField;
	}

	/**
	 * @param receipt_NumberField the receipt_NumberField to set
	 */
	public void setReceipt_NumberField(String receipt_NumberField) {
		this.receipt_NumberField = receipt_NumberField;
	}

	/**
	 * @return the priceField
	 */
	public String getPriceField() {
		return priceField;
	}

	/**
	 * @param priceField the priceField to set
	 */
	public void setPriceField(String priceField) {
		this.priceField = priceField;
	}

	/**
	 * @return the flight_ReportField
	 */
	public String getFlight_ReportField() {
		return flight_ReportField;
	}

	/**
	 * @param flight_ReportField the flight_ReportField to set
	 */
	public void setFlight_ReportField(String flight_ReportField) {
		this.flight_ReportField = flight_ReportField;
	}

	/**
	 * @return the defects_ReportField
	 */
	public String getDefects_ReportField() {
		return defects_ReportField;
	}

	/**
	 * @param defects_ReportField the defects_ReportField to set
	 */
	public void setDefects_ReportField(String defects_ReportField) {
		this.defects_ReportField = defects_ReportField;
	}

	/**
	 * @return the power_StartField
	 */
	public String getPower_StartField() {
		return power_StartField;
	}

	/**
	 * @param power_StartField the power_StartField to set
	 */
	public void setPower_StartField(String power_StartField) {
		this.power_StartField = power_StartField;
	}

	/**
	 * @return the doors_ClosedField
	 */
	public String getDoors_ClosedField() {
		return doors_ClosedField;
	}

	/**
	 * @param doors_ClosedField the doors_ClosedField to set
	 */
	public void setDoors_ClosedField(String doors_ClosedField) {
		this.doors_ClosedField = doors_ClosedField;
	}

	/**
	 * @return the engine_OnField
	 */
	public String getEngine_OnField() {
		return engine_OnField;
	}

	/**
	 * @param engine_OnField the engine_OnField to set
	 */
	public void setEngine_OnField(String engine_OnField) {
		this.engine_OnField = engine_OnField;
	}

	/**
	 * @return the block_StartField
	 */
	public String getBlock_StartField() {
		return block_StartField;
	}

	/**
	 * @param block_StartField the block_StartField to set
	 */
	public void setBlock_StartField(String block_StartField) {
		this.block_StartField = block_StartField;
	}

	/**
	 * @return the takeoffField
	 */
	public String getTakeoffField() {
		return takeoffField;
	}

	/**
	 * @param takeoffField the takeoffField to set
	 */
	public void setTakeoffField(String takeoffField) {
		this.takeoffField = takeoffField;
	}

	/**
	 * @return the landingField
	 */
	public String getLandingField() {
		return landingField;
	}

	/**
	 * @param landingField the landingField to set
	 */
	public void setLandingField(String landingField) {
		this.landingField = landingField;
	}

	/**
	 * @return the block_EndField
	 */
	public String getBlock_EndField() {
		return block_EndField;
	}

	/**
	 * @param block_EndField the block_EndField to set
	 */
	public void setBlock_EndField(String block_EndField) {
		this.block_EndField = block_EndField;
	}

	/**
	 * @return the engine_OffField
	 */
	public String getEngine_OffField() {
		return engine_OffField;
	}

	/**
	 * @param engine_OffField the engine_OffField to set
	 */
	public void setEngine_OffField(String engine_OffField) {
		this.engine_OffField = engine_OffField;
	}

	/**
	 * @return the doors_OpenedField
	 */
	public String getDoors_OpenedField() {
		return doors_OpenedField;
	}

	/**
	 * @param doors_OpenedField the doors_OpenedField to set
	 */
	public void setDoors_OpenedField(String doors_OpenedField) {
		this.doors_OpenedField = doors_OpenedField;
	}

	/**
	 * @return the power_EndField
	 */
	public String getPower_EndField() {
		return power_EndField;
	}

	/**
	 * @param power_EndField the power_EndField to set
	 */
	public void setPower_EndField(String power_EndField) {
		this.power_EndField = power_EndField;
	}

	/**
	 * @return the gPS_DistanceField
	 */
	public float getGPS_DistanceField() {
		return gPS_DistanceField;
	}

	/**
	 * @param distanceField the gPS_DistanceField to set
	 */
	public void setGPS_DistanceField(float distanceField) {
		gPS_DistanceField = distanceField;
	}

	/**
	 * @return the start_LocationField
	 */
	public String getStart_LocationField() {
		return start_LocationField;
	}

	/**
	 * @param start_LocationField the start_LocationField to set
	 */
	public void setStart_LocationField(String start_LocationField) {
		this.start_LocationField = start_LocationField;
	}

	/**
	 * @return the end_LocationField
	 */
	public String getEnd_LocationField() {
		return end_LocationField;
	}

	/**
	 * @param end_LocationField the end_LocationField to set
	 */
	public void setEnd_LocationField(String end_LocationField) {
		this.end_LocationField = end_LocationField;
	}

	/**
	 * @return the airframe_CyclesField
	 */
	public int getAirframe_CyclesField() {
		return airframe_CyclesField;
	}

	/**
	 * @param airframe_CyclesField the airframe_CyclesField to set
	 */
	public void setAirframe_CyclesField(int airframe_CyclesField) {
		this.airframe_CyclesField = airframe_CyclesField;
	}

	/**
	 * @return the engine_CyclesField
	 */
	public int getEngine_CyclesField() {
		return engine_CyclesField;
	}

	/**
	 * @param engine_CyclesField the engine_CyclesField to set
	 */
	public void setEngine_CyclesField(int engine_CyclesField) {
		this.engine_CyclesField = engine_CyclesField;
	}

	@Override
	public int compareTo(Object arg0) {
		// TODO Auto-generated method stub
		FlightPlan temp = (FlightPlan)arg0;
	    final int BEFORE = -1;
	    final int EQUAL = 0;
	    final int AFTER = 1;

		if (( this.getFlightPlanID()) == temp.getFlightPlanID()) return EQUAL; 
		if (( this.getFlightPlanID()) > temp.getFlightPlanID()) return BEFORE;
		if (( this.getFlightPlanID()) < temp.getFlightPlanID()) return AFTER;

	return EQUAL;
	}
}


