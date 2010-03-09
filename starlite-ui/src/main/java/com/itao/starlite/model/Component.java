package com.itao.starlite.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
public class Component {
	@Id
	@GeneratedValue
	private Integer id;
	
	//INFO
	private String type;
	private String name;
	private String description;
	private String number;
	private String serial;
	
	//MANUFACTURE
	private String manufacturer;
	@Temporal(TemporalType.DATE)
	private Date manufacturedDate;
	
	//TRACKING
	
	private Double timeBetweenOverhaul;
	private Double hoursRun;
	
	@Temporal(TemporalType.DATE)
	private Date installDate;
	@Temporal(TemporalType.TIME)
	private Date installTime;
	private Double hoursOnInstall;
	
	@Temporal(TemporalType.DATE)
	private Date expiryDate;
	private Double expiryHours;
	
	//CALC
	//currentHours
	//Hours Remaining
	//Days  Remaining
	//Percentage
	//Time since install
	
	private String condition;
	private String serviceLifeLimited;
	
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
	
	private String status;
	
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	@Fetch(FetchMode.SUBSELECT)
	private List<ComponentHistory> history = new ArrayList<ComponentHistory>();
	
	@Entity()
	public class ComponentHistory{
		@Id
		@GeneratedValue
		private Integer id;
		
		@Temporal(TemporalType.DATE)
		private Date date;
		@Temporal(TemporalType.TIME)
		private Date time;
		private String user;
		
		private String field;
		private String from;
		private String to;
		public void setId(Integer id) {
			this.id = id;
		}
		public Integer getId() {
			return id;
		}
		public Date getDate() {
			return date;
		}
		public void setDate(Date date) {
			this.date = date;
		}
		public Date getTime() {
			return time;
		}
		public void setTime(Date time) {
			this.time = time;
		}
		public String getUser() {
			return user;
		}
		public void setUser(String user) {
			this.user = user;
		}
		public String getField() {
			return field;
		}
		public void setField(String field) {
			this.field = field;
		}
		public String getFrom() {
			return from;
		}
		public void setFrom(String from) {
			this.from = from;
		}
		public String getTo() {
			return to;
		}
		public void setTo(String to) {
			this.to = to;
		}
		
	}
	
	
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	@Fetch(FetchMode.SUBSELECT)
	private List<ComponentValuation> valuations = new ArrayList<ComponentValuation>();
	
	@Entity()
	public class ComponentValuation{
		@Id
		@GeneratedValue
		private Integer id; 
		
		@Temporal(TemporalType.DATE)
		private Date date;
		@Temporal(TemporalType.TIME)
		private Date time;
		private String user;
		
		private Double marketVal;
		private String marketCurrency;
		
		private Double replaceVal;
		private String replaceCurrency;
		
		private Double purchaseVal;
		private String purchaseCurrency;
		
		public Integer getId() {
			return id;
		}
		public void setId(Integer id) {
			this.id = id;
		}
		public Date getDate() {
			return date;
		}
		public void setDate(Date date) {
			this.date = date;
		}
		public Date getTime() {
			return time;
		}
		public void setTime(Date time) {
			this.time = time;
		}
		public Double getMarketVal() {
			return marketVal;
		}
		public void setMarketVal(Double marketVal) {
			this.marketVal = marketVal;
		}
		public String getMarketCurrency() {
			return marketCurrency;
		}
		public void setMarketCurrency(String marketCurrency) {
			this.marketCurrency = marketCurrency;
		}
		public Double getReplaceVal() {
			return replaceVal;
		}
		public void setReplaceVal(Double replaceVal) {
			this.replaceVal = replaceVal;
		}
		public String getReplaceCurrency() {
			return replaceCurrency;
		}
		public void setReplaceCurrency(String replaceCurrency) {
			this.replaceCurrency = replaceCurrency;
		}
		public Double getPurchaseVal() {
			return purchaseVal;
		}
		public void setPurchaseVal(Double purchaseVal) {
			this.purchaseVal = purchaseVal;
		}
		public String getPurchaseCurrency() {
			return purchaseCurrency;
		}
		public void setPurchaseCurrency(String purchaseCurrency) {
			this.purchaseCurrency = purchaseCurrency;
		}
		public void setUser(String user) {
			this.user = user;
		}
		public String getUser() {
			return user;
		}
	}
	
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	@Fetch(FetchMode.SUBSELECT)
	private List<ComponentLocation> locations = new ArrayList<ComponentLocation>();
	
	@Entity()
	public class ComponentLocation{
		@Id
		@GeneratedValue
		private Integer id; 
		
		private String location;
		private String bin;
		private Integer current;
		
		public void setLocation(String location) {
			this.location = location;
		}
		public String getLocation() {
			return location;
		}
		public void setBin(String bin) {
			this.bin = bin;
		}
		public String getBin() {
			return bin;
		}
		public void setId(Integer id) {
			this.id = id;
		}
		public Integer getId() {
			return id;
		}
		public void setCurrent(Integer current) {
			this.current = current;
		}
		public Integer getCurrent() {
			return current;
		}
		
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public Date getManufacturedDate() {
		return manufacturedDate;
	}

	public void setManufacturedDate(Date manufacturedDate) {
		this.manufacturedDate = manufacturedDate;
	}

	public Double getTimeBetweenOverhaul() {
		return timeBetweenOverhaul;
	}

	public void setTimeBetweenOverhaul(Double timeBetweenOverhaul) {
		this.timeBetweenOverhaul = timeBetweenOverhaul;
	}

	public Double getHoursRun() {
		return hoursRun;
	}

	public void setHoursRun(Double hoursRun) {
		this.hoursRun = hoursRun;
	}

	public Date getInstallDate() {
		return installDate;
	}

	public void setInstallDate(Date installDate) {
		this.installDate = installDate;
	}

	public Date getInstallTime() {
		return installTime;
	}

	public void setInstallTime(Date installTime) {
		this.installTime = installTime;
	}

	public Double getHoursOnInstall() {
		return hoursOnInstall;
	}

	public void setHoursOnInstall(Double hoursOnInstall) {
		this.hoursOnInstall = hoursOnInstall;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public Double getExpiryHours() {
		return expiryHours;
	}

	public void setExpiryHours(Double expiryHours) {
		this.expiryHours = expiryHours;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getServiceLifeLimited() {
		return serviceLifeLimited;
	}

	public void setServiceLifeLimited(String serviceLifeLimited) {
		this.serviceLifeLimited = serviceLifeLimited;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<ComponentHistory> getHistory() {
		return history;
	}

	public void setHistory(List<ComponentHistory> history) {
		this.history = history;
	}

	public List<ComponentValuation> getValuations() {
		return valuations;
	}

	public void setValuations(List<ComponentValuation> valuations) {
		this.valuations = valuations;
	}

	public List<ComponentLocation> getLocations() {
		return locations;
	}

	public void setLocations(List<ComponentLocation> locations) {
		this.locations = locations;
	}

	public void addLocation(Integer locationId, String location, String bin, Integer quantity) {
		// TODO Auto-generated method stub	
	}

}
