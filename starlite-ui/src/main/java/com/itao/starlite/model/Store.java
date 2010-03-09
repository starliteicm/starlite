package com.itao.starlite.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Store {
	@Id
	@GeneratedValue
	private Integer id;

	private String type;
	private String code;
	private String seccode;
	private String description;
	
	public String getLocation(){
		return code + seccode;
	}
	
	public static Store createStore(String locationIdentifier){
		if(locationIdentifier != null){
			if(locationIdentifier.length() == 5){
				Store store = new Store();
				store.setCode(locationIdentifier.substring(0, 2));
				store.setSeccode(locationIdentifier.substring(2, 5));
				if("MS".equals(store.getCode())){store.setType("Mobile");}
				else if("WS".equals(store.getCode())){store.setType("Workshop");}
				else if("DR".equals(store.getCode())){store.setType("Depot");}
				else{store.setType("Store");}
				return store;
			}	
		}
		return null;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setSeccode(String seccode) {
		this.seccode = seccode;
	}
	public String getSeccode() {
		return seccode;
	}
	
}
