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



/**
 * <p>Represents a Starlite Flight Actual Status. OPEN, TO APPROVE, APPROVED, AWAIT READY, READY, WAITING FOR RELEASE, RELEASED</p>
 * <p>This class has been mapped for Hibernate.</p>
 * @author Celeste Groenewald
 *
 */


@SuppressWarnings("unchecked")
@Entity
public class FlightActualStatus implements Cloneable, Comparable {
	
    @Id
	@GeneratedValue
	private Integer flightActualStatusID;
	@Column(unique=true)
    private String flightActualStatusValue;
			
    public FlightActualStatus() 
    {
    	this.flightActualStatusValue = "OPEN";
    	
    }
	
   
	/**
	 * @return the flightActualStatusID
	 */
	public Integer getFlightActualStatusID() {
		return flightActualStatusID;
	}



	/**
	 * @param flightActualStatusID the flightActualStatusID to set
	 */
	public void setFlightActualStatusID(Integer flightActualStatusID) {
		this.flightActualStatusID = flightActualStatusID;
	}



	/**
	 * @return the flightActualStatusValue
	 */
	public String getFlightActualStatusValue() {
		return flightActualStatusValue;
	}



	/**
	 * @param flightActualStatusValue the flightActualStatusValue to set
	 */
	public void setFlightActualStatusValue(String flightActualStatusValue) {
		this.flightActualStatusValue = flightActualStatusValue;
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
		
		    FlightActualStatus temp = (FlightActualStatus)arg0;
		    final int BEFORE = -1;
		    final int EQUAL = 0;
		    final int AFTER = 1;

		    if (( this.flightActualStatusValue.compareTo(temp.flightActualStatusValue )==0)) return EQUAL;
		    if (( this.flightActualStatusValue.compareTo(temp.flightActualStatusValue )== -1)) return BEFORE;
		    if (( this.flightActualStatusValue.compareTo(temp.flightActualStatusValue )== 1)) return AFTER;

		return EQUAL;
	}

}
