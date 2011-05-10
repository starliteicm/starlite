package com.itao.starlite.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author Celeste Groenewald
 * <p>A G-Catagory is for use on an OFP.</p> 
 */
      
@Entity
public class FlightGCatagory implements Comparable
{
	@Id
	@GeneratedValue
	private int id=0;
	@Column(nullable = false, columnDefinition="varchar(100) default 'G1 - Acrobatic Operations'")
	private String value="";
	

     public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getValue() {
		return value;
	}


	public void setValue(String value) {
		this.value = value;
	}


	public FlightGCatagory(){}


	@Override

	public int compareTo(Object arg0) {
		FlightGCatagory temp = (FlightGCatagory)arg0;
	    final int BEFORE = -1;
	    final int EQUAL = 0;
	    final int AFTER = 1;

	    if (( this.value.compareTo(temp.getValue() )==0)) return EQUAL;
	    if (( this.value.compareTo(temp.getValue() )< 0)) return BEFORE;
	    if (( this.value.compareTo(temp.getValue() )> 0)) return AFTER;

	return EQUAL;
	}
     
     
}
