package com.itao.starlite.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import org.hibernate.annotations.OrderBy;

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
	
	private Double airframeHours;
	
	//CALC
	//currentHours
	//Hours Remaining
	//Days  Remaining
	//Percentage
	//Time since install
	//lifeExpiresHours
	
	public Double getRemainingHours(){
		return timeBetweenOverhaul - getCurrentHours();
	}
	
	public long getRemainingHoursPercent(){
		return Math.round(((timeBetweenOverhaul - getCurrentHours())/timeBetweenOverhaul)*100.0);
	}
	
	public Double getLifeExpiresHours(){
		return timeBetweenOverhaul + hoursOnInstall - hoursRun;
	}
	
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
	public static class ComponentHistory{
		@Id
		@GeneratedValue
		private Integer id;
		
		@Temporal(TemporalType.DATE)
		private Date date;
		@Temporal(TemporalType.TIME)
		private Date time;
		private String user;
		private String type;
		private String field;
		private String fromVal;
		private String toVal;
		private String description;
		private String location;
		
		public ComponentHistory(){};
		
		public ComponentHistory(Date now, Date now2, String user, String type,
				String field, String from, String to, String desc, String location) {
			setDate(now);
			setTime(now2);
			setUser(user);
			setField(field);
			setFromVal(from);
			setToVal(to);
			setType(type);
			setDescription(desc);
			setLocation(location);
		}

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
		public String getFromVal() {
			return fromVal;
		}
		public void setFromVal(String from) {
			this.fromVal = from;
		}
		public String getToVal() {
			return toVal;
		}
		public void setToVal(String to) {
			this.toVal = to;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public String getDescription() {
			return description;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getType() {
			return type;
		}

		public void setLocation(String location) {
			this.location = location;
		}

		public String getLocation() {
			return location;
		}
		
	}
	
	
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	@Fetch(FetchMode.SUBSELECT)
	private List<ComponentValuation> valuations = new ArrayList<ComponentValuation>();
	
	@Entity()
	public static class ComponentValuation{
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
		
		public ComponentValuation(){}
		
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
	public static class ComponentLocation{
		@Id
		@GeneratedValue
		private Integer id; 
		
		private String batch;
		private String location;
		private String bin;
		private Integer current;
		private Integer quantity;
		private String status;
		
		public ComponentLocation(){}
		
		public void setLocation(String location) {
			if(location != null){location = location.toUpperCase();}
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
		public void setQuantity(Integer quantity) {
			this.quantity = quantity;
		}
		public Integer getQuantity() {
			return quantity;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public String getStatus() {
			return status;
		}

		public void setBatch(String batch) {
			this.batch = batch;
		}

		public String getBatch() {
			return batch;
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

	public void updateLocation(Integer locationId, String location, String bin, Integer quantity, Integer current) {
		if(locationId != null){
			ComponentLocation toRem = null;
			
			for(ComponentLocation l : locations){
				if(locationId.equals(l.getId())){
					if(current != 0){
					l.setLocation(location);
					l.setBin(bin);
					l.setCurrent(current);
					l.setQuantity(quantity);
					}
					else{
						toRem = l;
					}
				}
			}
			
			if(toRem != null){
				locations.remove(toRem);
			}
			
		}
		else{
			boolean add = true;
			
			for(ComponentLocation l : locations){
				if((location.equals(l.getLocation()))&&(bin.equals(l.getBin()))){
					add = false;
				}
			}
			
			if(add){
			ComponentLocation l = new ComponentLocation();
			l.setLocation(location);
			l.setBin(bin);
			l.setCurrent(current);
			l.setQuantity(quantity);
			locations.add(l);
			}
		}
	}


	public Double getCurrentHours() {
		return airframeHours - hoursOnInstall + hoursRun ;
	}

	public void setAirframeHours(Double airframeHours) {
		this.airframeHours = airframeHours;
	}

	public Double getAirframeHours() {
		return airframeHours;
	}

	public void updateValuation(Integer componentValuationId, String valDate, String valTime,
			String username, Double marketVal, String marketCurrency,
			Double purchaseVal, String purchaseCurrency, Double replacementVal,
			String replacementCurrency) throws ParseException {
	
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat tf = new SimpleDateFormat("HH:mm");
		
		if(componentValuationId != null){
			for(ComponentValuation val : valuations){
				if(componentValuationId.equals(val.getId())){
					val.setDate(df.parse(valDate));
					val.setTime(tf.parse(valTime));
					val.setUser(username);
					val.setMarketVal(marketVal);
					val.setMarketCurrency(marketCurrency);
					val.setPurchaseVal(purchaseVal);
					val.setPurchaseCurrency(purchaseCurrency);
					val.setReplaceVal(replacementVal);
					val.setReplaceCurrency(replacementCurrency);
				}
			}
		}
		else{
		ComponentValuation val = new ComponentValuation();
		val.setDate(df.parse(valDate));
		val.setTime(tf.parse(valTime));
		val.setUser(username);
		val.setMarketVal(marketVal);
		val.setMarketCurrency(marketCurrency);
		val.setPurchaseVal(purchaseVal);
		val.setPurchaseCurrency(purchaseCurrency);
		val.setReplaceVal(replacementVal);
		val.setReplaceCurrency(replacementCurrency);
		valuations.add(val);
		}
	}
	
	public List<ComponentHistory> getChanges(Component c, String user){
		
		List<ComponentHistory> hist = new ArrayList<ComponentHistory>();
		
		Date now = Calendar.getInstance().getTime();
		
		if(this.getAir() != null){hist.add(new ComponentHistory(now,now,user,"update","air",""+c.getAir(),""+getAir(),"Component Update",null));}
		else{if(c.getAir() != null){hist.add(new ComponentHistory(now,now,user,"update","air",""+c.getAir(),"blank","Component Update",null));}}

		if(this.getAirframeHours() != null){hist.add(new ComponentHistory(now,now,user,"update","airframeHours",""+c.getAirframeHours(),""+getAirframeHours(),"Component Update",null));}
		else{if(c.getAirframeHours() != null){hist.add(new ComponentHistory(now,now,user,"update","airframeHours",""+c.getAirframeHours(),"blank","Component Update",null));}}

		if(this.getAirframeHours() != null){hist.add(new ComponentHistory(now,now,user,"update","airframeHours",""+c.getAirframeHours(),""+getAirframeHours(),"Component Update",null));}
		else{if(c.getAirframeHours() != null){hist.add(new ComponentHistory(now,now,user,"update","airframeHours",""+c.getAirframeHours(),"blank","Component Update",null));}}

		return hist;
	}

	public void updateLocation(String user, String type, String batch, String location, String bin, Integer quantity, Integer current, String note) {
	
		System.out.println("Updating Location - "+type);
		
		Date now = Calendar.getInstance().getTime();
		
		if("Purchase".equals(type)){
			//Add to location
			ComponentLocation l = null;
			Integer previous = 0;
			for(ComponentLocation loc : locations){
				if((loc.location.equals(location))&&(loc.bin.equals(bin))){
					l = loc; 
					previous = loc.getQuantity();
				}
			}
			//Add to new Location
			if(l == null){
				l = new ComponentLocation();
				l.setLocation(location);
				l.setBin(bin);
				l.setCurrent(current);
				l.setBatch(batch);
				l.setQuantity(quantity);
				locations.add(l);
			}
			else {
				l.setQuantity(l.getQuantity() + quantity);
				l.setBatch(batch);
			}
			history.add(new ComponentHistory(now,now,user,"transaction","Purchase",""+previous,""+l.getQuantity(),note,l.getLocation()+" "+l.getBin()));

        }
        else if("Repair".equals(type)){
        	//remove quantity from current location
        	ComponentLocation rem = null;
        	Integer previous = 0;
			for(ComponentLocation loc : locations){
				if(loc.getId() ==  current){
					rem = loc; 
					previous = rem.getQuantity();
					rem.setQuantity(rem.getQuantity() - quantity);
					if(rem.getQuantity()<=0){
						locations.remove(rem);
						history.add(new ComponentHistory(now,now,user,"transaction","Repair",""+previous,""+rem.getQuantity(),note,rem.getLocation()+" "+rem.getBin()));
					}
				}
			}
			previous = 0;
        	//Add to location
			ComponentLocation l = null;
			for(ComponentLocation loc : locations){
				if((loc.location.equals(location))&&(loc.bin.equals(bin))&&(loc.status.equals(status))){
					l = loc; 
					previous = loc.getQuantity();
				}
			}
			//Add to new Location
			if(l == null){
				l = new ComponentLocation();
				l.setLocation(location);
				l.setBin(bin);
				l.setStatus("Repair");
				l.setCurrent(current);
				l.setQuantity(quantity);
				l.setBatch(batch);
				locations.add(l);
			}
			else {
				l.setQuantity(l.getQuantity() + quantity);
				l.setBatch(batch);
				l.setStatus("Repair");
			}
			
			history.add(new ComponentHistory(now,now,user,"transaction","Repair",""+previous,""+l.getQuantity(),note,l.getLocation()+" "+l.getBin()));
			
        }
        else if("Move".equals(type)){
        	//remove quantity from current location
        	ComponentLocation rem = null;
        	Integer previous = 0;
			for(ComponentLocation loc : locations){
				if(loc.getId() ==  current){
					rem = loc; 
					previous = rem.getQuantity();
					rem.setQuantity(rem.getQuantity() - quantity);
					if(rem.getQuantity()<=0){
						locations.remove(rem);
						history.add(new ComponentHistory(now,now,user,"transaction","Move",""+previous,""+rem.getQuantity(),note,rem.getLocation()+" "+rem.getBin()));
					}
				}
			}
			previous = 0;
        	//Add to location
			ComponentLocation l = null;
			for(ComponentLocation loc : locations){
				if((loc.location.equals(location))&&(loc.bin.equals(bin))&&(loc.status.equals(status))){
					l = loc; 
					previous = loc.getQuantity();
				}
			}
			//Add to new Location
			if(l == null){
				l = new ComponentLocation();
				l.setLocation(location);
				l.setBin(bin);
				l.setCurrent(current);
				l.setQuantity(quantity);
				locations.add(l);
			}
			else {
				l.setQuantity(l.getQuantity() + quantity);
			}
			history.add(new ComponentHistory(now,now,user,"transaction","Move",""+previous,""+l.getQuantity(),note,l.getLocation()+" "+l.getBin()));

		}
		else if("Reserve".equals(type)){
        	//remove quantity from current location
        	ComponentLocation rem = null;
        	Integer previous = 0;
			for(ComponentLocation loc : locations){
				if(loc.getId() ==  current){
					rem = loc; 
					previous = rem.getQuantity();
					rem.setQuantity(rem.getQuantity() - quantity);
					if(rem.getQuantity()<=0){
						locations.remove(rem);
						history.add(new ComponentHistory(now,now,user,"transaction","Reserve",""+previous,""+rem.getQuantity(),note,rem.getLocation()+" "+rem.getBin()));
					}
				}

			}
			previous = 0;
        	//Add to location
			ComponentLocation l = null;
			for(ComponentLocation loc : locations){
				if((loc.location.equals(location))&&(loc.bin.equals(bin))&&(loc.status.equals(status))){
					l = loc; 
					previous = loc.getQuantity();
				}
			}
			//Add to new Location
			if(l == null){
				l = new ComponentLocation();
				l.setLocation(location);
				l.setBin(bin);
				l.setStatus("Reserve");
				l.setCurrent(current);
				l.setQuantity(quantity);
				locations.add(l);
			}
			else {
				l.setQuantity(l.getQuantity() + quantity);
			}
			history.add(new ComponentHistory(now,now,user,"transaction","Reserve",""+previous,""+rem.getQuantity(),note,rem.getLocation()+" "+rem.getBin()));

		}
		else if("Sell".equals(type)){	
			ComponentLocation rem = null;
			for(ComponentLocation loc : locations){
				if(loc.getId() ==  current){
					rem = loc; 
					Integer previous = rem.getQuantity();
					rem.setQuantity(rem.getQuantity() - quantity);
					if(rem.getQuantity()<=0){
						locations.remove(rem);
						history.add(new ComponentHistory(now,now,user,"transaction","Sell",""+previous,""+rem.getQuantity(),note,rem.getLocation()+" "+rem.getBin()));

					}
				}
			}
		}
		else if("Scrap".equals(type)){
			ComponentLocation rem = null;
			System.out.println("Current:"+current);
			for(ComponentLocation loc : locations){
				
				if(loc.getId().equals(current)){	
					rem = loc; 
					Integer previous = rem.getQuantity();
					rem.setQuantity(rem.getQuantity() - quantity);
					System.out.println("Current:"+current+" =? "+loc.getId()+" previous:"+previous+" qty:"+rem.getQuantity());
					if(rem.getQuantity()<=0){
						locations.remove(rem);
					}
					history.add(new ComponentHistory(now,now,user,"transaction","Scrap",""+previous,""+rem.getQuantity(),note,rem.getLocation()+" "+rem.getBin()));
				}
			}
		}
		else if("Consume".equals(type)){	
			ComponentLocation rem = null;
			for(ComponentLocation loc : locations){
				if(loc.getId() ==  current){
					rem = loc; 
					Integer previous = rem.getQuantity();
					rem.setQuantity(rem.getQuantity() - quantity);
					if(rem.getQuantity()<=0){
						locations.remove(rem);
						history.add(new ComponentHistory(now,now,user,"transaction","Consume",""+previous,""+rem.getQuantity(),note,rem.getLocation()+" "+rem.getBin()));
					}
				}
			}
		}
		
	}

}
