package com.itao.starlite.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Aircraft {
	@Id
	@GeneratedValue
	private Integer id;
	private String ref;
	
	private String ownership;
	private String type;
	private String model;
	private String licence;
	
	private String cargoSize;
	private String licences;
	private String performanceCounter;
	
	private Certificate certificateOfRegistration;
	private Certificate aircraftOperatingCertificate;
	private Certificate certificateOfAirworthiness;
	
	private String aircraftMaintenanceOrganisation;
	private String livery;
	private Integer engines;
	
	//CONFIG
	private Integer nvg;
	private Integer flir;
	private Integer night;
	private Integer floa;
	private Integer indi;
	private Integer tcas;
	private Integer hoist;
	private Integer cargo;
	private Integer bambi;
	private Integer vip;
	private Integer troop;
	private Integer ferry;
	private Integer fdr;
	private Integer air;
	
	private Integer mmel;
	
	public Certificate getCertificateOfRegistration() {
		if (certificateOfRegistration == null)
			certificateOfRegistration = new Certificate();
		return certificateOfRegistration;
	}
	public void setCertificateOfRegistration(Certificate certificateOfRegistration) {
		this.certificateOfRegistration = certificateOfRegistration;
	}
	public Certificate getAircraftOperatingCertificate() {
		if (aircraftOperatingCertificate == null)
			aircraftOperatingCertificate = new Certificate();
		return aircraftOperatingCertificate;
	}
	public void setAircraftOperatingCertificate(Certificate aircraftOperatingCertificate) {
		this.aircraftOperatingCertificate = aircraftOperatingCertificate;
	}
	public Certificate getCertificateOfAirworthiness() {
		if (certificateOfAirworthiness == null)
			certificateOfAirworthiness = new Certificate();
		return certificateOfAirworthiness;
	}
	public void setCertificateOfAirworthiness(Certificate certificateOfAirworthiness) {
		this.certificateOfAirworthiness = certificateOfAirworthiness;
	}
	
	public String getAircraftMaintenanceOrganisation() {
		return aircraftMaintenanceOrganisation;
	}
	public void setAircraftMaintenanceOrganisation(
			String aircraftMaintenanceOrganisation) {
		this.aircraftMaintenanceOrganisation = aircraftMaintenanceOrganisation;
	}
	public String getLivery() {
		return livery;
	}
	public void setLivery(String livery) {
		this.livery = livery;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getRef() {
		return ref;
	}
	public void setRef(String ref) {
		this.ref = ref;
	}
	public String getOwnership() {
		return ownership;
	}
	public void setOwnership(String ownership) {
		this.ownership = ownership;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getLicence() {
		return licence;
	}
	public void setLicence(String licence) {
		this.licence = licence;
	}
	
	public String getCargoSize() {
		return cargoSize;
	}
	public void setCargoSize(String cargoSize) {
		this.cargoSize = cargoSize;
	}
	public String getLicences() {
		return licences;
	}
	public void setLicences(String licences) {
		this.licences = licences;
	}
	public String getPerformanceCounter() {
		return performanceCounter;
	}
	public void setPerformanceCounter(String performanceCounter) {
		this.performanceCounter = performanceCounter;
	}
	public Integer getEngines() {
		return this.engines;
	}
	public void setEngines(Integer engines) {
		this.engines = engines;
	}
	
	public Integer getNvg() {
		return nvg;
	}

	public void setNvg(Integer nvg) {
		this.nvg = nvg;
	}

	public Integer getFlir() {
		return flir;
	}

	public void setFlir(Integer flir) {
		this.flir = flir;
	}

	public Integer getNight() {
		return night;
	}

	public void setNight(Integer night) {
		this.night = night;
	}

	public Integer getFloa() {
		return floa;
	}

	public void setFloa(Integer floa) {
		this.floa = floa;
	}

	public Integer getIndi() {
		return indi;
	}

	public void setIndi(Integer indi) {
		this.indi = indi;
	}

	public Integer getTcas() {
		return tcas;
	}

	public void setTcas(Integer tcas) {
		this.tcas = tcas;
	}

	public Integer getHoist() {
		return hoist;
	}

	public void setHoist(Integer hoist) {
		this.hoist = hoist;
	}

	public Integer getCargo() {
		return cargo;
	}

	public void setCargo(Integer cargo) {
		this.cargo = cargo;
	}

	public Integer getBambi() {
		return bambi;
	}

	public void setBambi(Integer bambi) {
		this.bambi = bambi;
	}

	public Integer getVip() {
		return vip;
	}

	public void setVip(Integer vip) {
		this.vip = vip;
	}

	public Integer getTroop() {
		return troop;
	}

	public void setTroop(Integer troop) {
		this.troop = troop;
	}

	public Integer getFerry() {
		return ferry;
	}

	public void setFerry(Integer ferry) {
		this.ferry = ferry;
	}

	public Integer getFdr() {
		return fdr;
	}

	public void setFdr(Integer fdr) {
		this.fdr = fdr;
	}

	public Integer getAir() {
		return air;
	}

	public void setAir(Integer air) {
		this.air = air;
	}

	public Integer getMmel() {
		return mmel;
	}

	public void setMmel(Integer mmel) {
		this.mmel = mmel;
	}

	
}
