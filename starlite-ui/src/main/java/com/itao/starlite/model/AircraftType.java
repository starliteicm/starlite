package com.itao.starlite.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.IndexColumn;

@Entity
public class AircraftType {
	@Id
	@GeneratedValue
	private Integer id;
	private String name;
	private String description;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription(){
		return description;
	}
	public void setDescription(String description){
		this.description = description;
	}
	
	@Transient
	private List<CombinedActuals> actuals;
	public void setActualList(List<CombinedActuals> vals) {
		this.actuals = vals;
	}
	public List<CombinedActuals> getActualList(){
		return actuals;
	}
	
}
