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
import javax.persistence.OneToOne;
import javax.persistence.Transient;



@Entity
public class FlightActuals 
{
	@Id
	@GeneratedValue
	private int flightActualsID;

	@OneToOne(optional = true, fetch=FetchType.EAGER)
	private FlightLog flightLog = null;
	@OneToOne(optional = true, fetch=FetchType.EAGER)
	private FlightPlan flightPlan = null;

	@OneToOne(fetch=FetchType.EAGER)
	private FlightActualStatus flightActualStatus =null;

	@Transient
	private FlightPlan flightVariance = null;
	
	@Transient
	public Integer newStatusId = 0;
	
	public FlightActuals()
	{
		//set status to OPEN
		
		//this.flightActualStatus.setFlightActualStatusValue("OPEN");
	}

	 @SuppressWarnings("unused")
		public void setNewActualStatusValueObject(FlightActualStatus newValue)
	    {
	    	this.flightActualStatus = newValue;
	    }
	 
	/**
	 * @return the flightActualsID
	 */
	public int getFlightActualsID() {
		return flightActualsID;
	}

	/**
	 * @param flightActualsID the flightActualsID to set
	 */
	public void setFlightActualsID(int flightActualsID) {
		this.flightActualsID = flightActualsID;
	}

	/**
	 * @return the flightLog
	 */
	public FlightLog getFlightLog() {
		return flightLog;
	}

	/**
	 * @param flightLog the flightLog to set
	 */
	public void setFlightLog(FlightLog flightLog) {
		this.flightLog = flightLog;
	}

	/**
	 * @return the flightPlan
	 */
	public FlightPlan getFlightPlan() {
		return flightPlan;
	}

	/**
	 * @param flightPlan the flightPlan to set
	 */
	public void setFlightPlan(FlightPlan flightPlan) 
	{
		if (this.flightPlan == null)
		{
			this.flightPlan = new FlightPlan();
		}
		this.flightPlan.setFlightPlanID(flightPlan.getFlightPlanID());
		this.flightPlan.setACHrsToNextService(flightPlan.getACHrsToNextService());
		this.flightPlan.setACServiceType(flightPlan.getACServiceType());
		this.flightPlan.setAircraftIDField(flightPlan.getAircraftIDField());
		this.flightPlan.setAirframe_CyclesField(flightPlan.getAirframe_CyclesField());
		this.flightPlan.setBlock_EndField(flightPlan.getBlock_EndField());
		this.flightPlan.setBlock_StartField(flightPlan.getBlock_StartField());
		this.flightPlan.setCabin_CrewField(flightPlan.getCabin_CrewField());
		this.flightPlan.setCaptainField(flightPlan.getCaptainField());
		this.flightPlan.setCustomer(flightPlan.getCustomer());
		this.flightPlan.setDefects_ReportField(flightPlan.getDefects_ReportField());
		this.flightPlan.setDoors_ClosedField(flightPlan.getDoors_ClosedField());
		this.flightPlan.setDoors_OpenedField(flightPlan.getDoors_OpenedField());
		this.flightPlan.setEnd_LocationField(flightPlan.getEnd_LocationField());
		this.flightPlan.setEngine_CyclesField(flightPlan.getEngine_CyclesField());
		this.flightPlan.setEngine_OffField(flightPlan.getEngine_OffField());
		this.flightPlan.setEngine_OnField(flightPlan.getEngine_OnField());
		this.flightPlan.setETD(flightPlan.getETD());
		this.flightPlan.setFirst_OfficerField(flightPlan.getFirst_OfficerField());
		this.flightPlan.setFlight_ReportField(flightPlan.getFlight_ReportField());
		this.flightPlan.setFlight_TypeField(flightPlan.getFlight_TypeField());
		this.flightPlan.setFlightDateField(flightPlan.getFlightDateField());
		this.flightPlan.setFlightEngineer(flightPlan.getFlightEngineer());
		this.flightPlan.setFlightNumber(flightPlan.getFlightNumber());
		this.flightPlan.setFlightType(flightPlan.getFlightType());
		this.flightPlan.setFreight(Float.valueOf(flightPlan.getFreight()));
		this.flightPlan.setFuel_UpliftField(flightPlan.getFuel_UpliftField());
		this.flightPlan.setGeneratedDateField(flightPlan.getGeneratedDateField());
		this.flightPlan.setGPS_DistanceField(flightPlan.getGPS_DistanceField());
		this.flightPlan.setInvoiceNo(flightPlan.getInvoiceNo());
		this.flightPlan.setLandingField(flightPlan.getLandingField());
		this.flightPlan.setPassengersField(flightPlan.getPassengersField());
		this.flightPlan.setPAX(flightPlan.getPAX());
		this.flightPlan.setPower_EndField(flightPlan.getPower_EndField());
		this.flightPlan.setPower_StartField(flightPlan.getPower_StartField());
		this.flightPlan.setPriceField(flightPlan.getPriceField());
		this.flightPlan.setReceipt_NumberField(flightPlan.getReceipt_NumberField());
		this.flightPlan.setReference_NumberField(flightPlan.getReference_NumberField());
		this.flightPlan.setRegistrationField(flightPlan.getRegistrationField());
		this.flightPlan.setStart_LocationField(flightPlan.getStart_LocationField());
		this.flightPlan.setStarting_FuelField(flightPlan.getStarting_FuelField());
		this.flightPlan.setTakeoffField(flightPlan.getTakeoffField());
		this.flightPlan.setTotalLoad(flightPlan.getTotalLoad());
		
		
		
		
		
	}

	/**
	 * @return the flightActualStatus
	 */
	public FlightActualStatus getFlightActualStatus() {
		return flightActualStatus;
	}

	/**
	 * @param flightActualStatus the flightActualStatus to set
	 */
	public void setFlightActualStatus(FlightActualStatus flightActualStatus) {
		this.flightActualStatus = flightActualStatus;
	}

	/**
	 * @return the flightVarience
	 */
	public FlightPlan getFlightVariance() 
	{
		final int EQUAL = 0;
				
		this.flightVariance = new FlightPlan();
		
		//Registration Field
		if (this.flightPlan.getRegistrationField().trim().compareToIgnoreCase(this.flightLog.getRegistrationField().trim()) == EQUAL)
		{this.flightVariance.setRegistrationField("");}
		else {this.flightVariance.setRegistrationField((this.flightLog.getRegistrationField()));}
		
		//flightDateField
		if (this.flightPlan.getFlightDateField().trim().compareToIgnoreCase(this.flightLog.getFlightDateField().trim()) == EQUAL)
		{this.flightVariance.setFlightDateField("");}
		else {this.flightVariance.setFlightDateField((this.flightLog.getFlightDateField()));}
		
		//getFlightDateField
		if (this.flightPlan.getGeneratedDateField().trim().compareToIgnoreCase(this.flightLog.getGeneratedDateField().trim()) == EQUAL)
		{this.flightVariance.setGeneratedDateField("");}
		else {this.flightVariance.setGeneratedDateField((this.flightLog.getGeneratedDateField()));}
		
		//flight_TypeField
		if (this.flightPlan.getFlight_TypeField().trim().compareToIgnoreCase(this.flightLog.getFlight_TypeField().trim()) == EQUAL)
		{this.flightVariance.setFlight_TypeField("");}
		else {this.flightVariance.setFlight_TypeField((this.flightLog.getFlight_TypeField()));}
		
		//reference_NumberField
		if (this.flightPlan.getReference_NumberField().trim().compareToIgnoreCase(this.flightLog.getReference_NumberField().trim()) == EQUAL)
		{this.flightVariance.setReference_NumberField("");}
		else {this.flightVariance.setReference_NumberField((this.flightLog.getReference_NumberField()));}
		
		//captainField
		if (this.flightPlan.getCaptainField().trim().compareToIgnoreCase(this.flightLog.getCaptainField().trim()) == EQUAL)
		{this.flightVariance.setCaptainField("");}
		else {this.flightVariance.setCaptainField((this.flightLog.getCaptainField()));}
		
		//first_OfficerField
		if (this.flightPlan.getFirst_OfficerField().trim().compareToIgnoreCase(this.flightLog.getFirst_OfficerField().trim()) == EQUAL)
		{this.flightVariance.setFirst_OfficerField("");}
		else {this.flightVariance.setFirst_OfficerField((this.flightLog.getFirst_OfficerField()));}
		
		//cabin_CrewField
		//if (this.flightPlan.getCabin_CrewField().trim().compareToIgnoreCase(this.flightLog.getCabin_CrewField().trim()) == EQUAL)
		//{this.flightVariance.setCabin_CrewField("");}
		//else {this.flightVariance.setCabin_CrewField((this.flightLog.getCabin_CrewField()));}

		//flight Engineer is the Cabin Crew ?
		if (this.flightPlan.getFlightEngineer().trim().compareToIgnoreCase(this.flightLog.getCabin_CrewField().trim()) == EQUAL)
		{this.flightVariance.setFlightEngineer("");}
		else {this.flightVariance.setFlightEngineer((this.flightLog.getCabin_CrewField()));}

		
		//passengersField
		if (this.flightPlan.getPassengersField().trim().compareToIgnoreCase(this.flightLog.getPassengersField().trim()) == EQUAL)
		{this.flightVariance.setPassengersField("");}
		else {this.flightVariance.setPassengersField((this.flightLog.getPassengersField()));}
		
		//starting_FuelField
		if (this.flightPlan.getStarting_FuelField().trim().compareToIgnoreCase(this.flightLog.getStarting_FuelField().trim()) == EQUAL)
		{this.flightVariance.setStarting_FuelField("");}
		else {this.flightVariance.setStarting_FuelField((this.flightLog.getStarting_FuelField()));}
		
		//fuel_UpliftField
		if (this.flightPlan.getFuel_UpliftField().trim().compareToIgnoreCase(this.flightLog.getFuel_UpliftField().trim()) == EQUAL)
		{this.flightVariance.setFuel_UpliftField("");}
		else {this.flightVariance.setFuel_UpliftField((this.flightLog.getFuel_UpliftField()));}
		
		//receipt_NumberField
		if (this.flightPlan.getReceipt_NumberField().trim().compareToIgnoreCase(this.flightLog.getReceipt_NumberField().trim()) == EQUAL)
		{this.flightVariance.setReceipt_NumberField("");}
		else {this.flightVariance.setReceipt_NumberField((this.flightLog.getReceipt_NumberField()));}
		
		//priceField
		if (this.flightPlan.getPriceField().trim().compareToIgnoreCase(this.flightLog.getPriceField().trim()) == EQUAL)
		{this.flightVariance.setPriceField("");}
		else {this.flightVariance.setPriceField((this.flightLog.getPriceField()));}
		
		//flight_ReportField
		if (this.flightPlan.getFlight_ReportField().trim().compareToIgnoreCase(this.flightLog.getFlight_ReportField().trim()) == EQUAL)
		{this.flightVariance.setFlight_ReportField("");}
		else {this.flightVariance.setFlight_ReportField((this.flightLog.getFlight_ReportField()));}
		
		//defects_ReportField
		if (this.flightPlan.getDefects_ReportField().trim().compareToIgnoreCase(this.flightLog.getDefects_ReportField().trim()) == EQUAL)
		{this.flightVariance.setDefects_ReportField("");}
		else {this.flightVariance.setDefects_ReportField((this.flightLog.getDefects_ReportField()));}
		
		//power_StartField
		if (this.flightPlan.getPower_StartField().trim().compareToIgnoreCase(this.flightLog.getPower_StartField().trim()) == EQUAL)
		{this.flightVariance.setPower_StartField("");}
		else {this.flightVariance.setPower_StartField((this.flightLog.getPower_StartField()));}
		
		//doors_ClosedField
		if (this.flightPlan.getDoors_ClosedField().trim().compareToIgnoreCase(this.flightLog.getDoors_ClosedField().trim()) == EQUAL)
		{this.flightVariance.setDoors_ClosedField("");}
		else {this.flightVariance.setDoors_ClosedField((this.flightLog.getDoors_ClosedField()));}
		
		//engine_OnField
		if (this.flightPlan.getEngine_OnField().trim().compareToIgnoreCase(this.flightLog.getEngine_OnField().trim()) == EQUAL)
		{this.flightVariance.setEngine_OnField("");}
		else {this.flightVariance.setEngine_OnField((this.flightLog.getEngine_OnField()));}
		
		//block_StartField
		if (this.flightPlan.getBlock_StartField().trim().compareToIgnoreCase(this.flightLog.getBlock_StartField().trim()) == EQUAL)
		{this.flightVariance.setBlock_StartField("");}
		else {this.flightVariance.setBlock_StartField((this.flightLog.getBlock_StartField()));}
		
		//takeoffField
		if (this.flightPlan.getTakeoffField().trim().compareToIgnoreCase(this.flightLog.getTakeoffField().trim()) == EQUAL)
		{this.flightVariance.setTakeoffField("");}
		else {this.flightVariance.setTakeoffField((this.flightLog.getTakeoffField()));}
		
		//landingField
		if (this.flightPlan.getLandingField().trim().compareToIgnoreCase(this.flightLog.getLandingField().trim()) == EQUAL)
		{this.flightVariance.setLandingField("");}
		else {this.flightVariance.setLandingField((this.flightLog.getLandingField()));}
		
		//block_EndField
		if (this.flightPlan.getBlock_EndField().trim().compareToIgnoreCase(this.flightLog.getBlock_EndField().trim()) == EQUAL)
		{this.flightVariance.setBlock_EndField("");}
		else {this.flightVariance.setBlock_EndField((this.flightLog.getBlock_EndField()));}
		
		
		//engine_OffField
		if (this.flightPlan.getEngine_OffField().trim().compareToIgnoreCase(this.flightLog.getEngine_OffField().trim()) == EQUAL)
		{this.flightVariance.setEngine_OffField("");}
		else {this.flightVariance.setEngine_OffField((this.flightLog.getEngine_OffField()));}
		
		//doors_OpenedField
		if (this.flightPlan.getDoors_OpenedField().trim().compareToIgnoreCase(this.flightLog.getDoors_OpenedField().trim()) == EQUAL)
		{this.flightVariance.setDoors_OpenedField("");}
		else {this.flightVariance.setDoors_OpenedField((this.flightLog.getDoors_OpenedField()));}
		
		//power_EndField
		if (this.flightPlan.getPower_EndField().trim().compareToIgnoreCase(this.flightLog.getPower_EndField().trim()) == EQUAL)
		{this.flightVariance.setPower_EndField("");}
		else {this.flightVariance.setPower_EndField((this.flightLog.getPower_EndField()));}
		
		//gPS_DistanceField
		this.flightVariance.setGPS_DistanceField(this.flightLog.getGPS_DistanceField()-this.flightPlan.getGPS_DistanceField());
		
		
		//start_LocationField
		if (this.flightPlan.getStart_LocationField().trim().compareToIgnoreCase(this.flightLog.getStart_LocationField().trim()) == EQUAL)
		{this.flightVariance.setStart_LocationField("");}
		else {this.flightVariance.setStart_LocationField((this.flightLog.getStart_LocationField()));}
		

		//end_LocationField
		if (this.flightPlan.getEnd_LocationField().trim().compareToIgnoreCase(this.flightLog.getEnd_LocationField().trim()) == EQUAL)
		{this.flightVariance.setEnd_LocationField("");}
		else {this.flightVariance.setEnd_LocationField((this.flightLog.getEnd_LocationField()));}
		

		//airframe_CyclesField	
		this.flightVariance.setAirframe_CyclesField(this.flightLog.getAirframe_CyclesField()-this.flightPlan.getAirframe_CyclesField());
		
		//engine_CyclesField
		this.flightVariance.setEngine_CyclesField(this.flightLog.getEngine_CyclesField()-this.flightPlan.getEngine_CyclesField());
				
		return flightVariance;
	}

	/**
	 * @param flightVarience the flightVarience to set
	 */
	public void setFlightVariance(FlightPlan flightVarience) {
		this.flightVariance = flightVarience;
	}
	
	
	
}
