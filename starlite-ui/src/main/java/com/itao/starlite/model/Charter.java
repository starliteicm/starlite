package com.itao.starlite.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.annotations.IndexColumn;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.joda.time.DateMidnight;

import com.itao.starlite.model.types.CharterStatus;

@Entity
@TypeDef(name = "charterStatus", typeClass = CharterStatus.class)
public class Charter {
	@Id
	@GeneratedValue
	private Integer id;
	
	private Administrative administrative = new Administrative();
	
	private Resources resources = new Resources();
	
	private Pricing pricing = new Pricing();
	
	private Insurance insurance = new Insurance();
	
	private Cost cost = new Cost(); 
	
	@Embeddable
	public static class Administrative {
		private String code;
		
		private String clientName;
		private String clientCode;
		//@Type(type="enum_varchar")
		private CharterStatus status = CharterStatus.PROSPECT;
		
		private String physicalAddress1;
		private String physicalAddress2;
		private String physicalAddress3;
		private String physicalAddress4;
		private String physicalAddress5;
		
		private String invoicingAddress1;
		private String invoicingAddress2;
		private String invoicingAddress3;
		private String invoicingAddress4;
		private String invoicingAddress5;
		
		private String operatingArea;
		private String operatingBase;
		private String icao;
		
		private String currency;
		
		@Temporal(TemporalType.DATE)
		private Date startDate;
		@Temporal(TemporalType.DATE)
		private Date endDate;
		private boolean openEnded;
		
		private int terminationPeriod;

		
		
		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getClientName() {
			return clientName;
		}

		public void setClientName(String clientName) {
			this.clientName = clientName;
		}

		public String getClientCode() {
			return clientCode;
		}

		public void setClientCode(String clientCode) {
			this.clientCode = clientCode;
		}

		public CharterStatus getStatus() {
			if (status == null)
				status = CharterStatus.PROSPECT;
			return status;
		}

		public void setStatus(CharterStatus status) {
			this.status = status;
		}

		public String getPhysicalAddress1() {
			return physicalAddress1;
		}

		public void setPhysicalAddress1(String physicalAddress1) {
			this.physicalAddress1 = physicalAddress1;
		}

		public String getPhysicalAddress2() {
			return physicalAddress2;
		}

		public void setPhysicalAddress2(String physicalAddress2) {
			this.physicalAddress2 = physicalAddress2;
		}

		public String getPhysicalAddress3() {
			return physicalAddress3;
		}

		public void setPhysicalAddress3(String physicalAddress3) {
			this.physicalAddress3 = physicalAddress3;
		}

		public String getPhysicalAddress4() {
			return physicalAddress4;
		}

		public void setPhysicalAddress4(String physicalAddress4) {
			this.physicalAddress4 = physicalAddress4;
		}

		public String getPhysicalAddress5() {
			return physicalAddress5;
		}

		public void setPhysicalAddress5(String physicalAddress5) {
			this.physicalAddress5 = physicalAddress5;
		}

		public String getInvoicingAddress1() {
			return invoicingAddress1;
		}

		public void setInvoicingAddress1(String invoicingAddress1) {
			this.invoicingAddress1 = invoicingAddress1;
		}

		public String getInvoicingAddress2() {
			return invoicingAddress2;
		}

		public void setInvoicingAddress2(String invoicingAddress2) {
			this.invoicingAddress2 = invoicingAddress2;
		}

		public String getInvoicingAddress3() {
			return invoicingAddress3;
		}

		public void setInvoicingAddress3(String invoicingAddress3) {
			this.invoicingAddress3 = invoicingAddress3;
		}

		public String getInvoicingAddress4() {
			return invoicingAddress4;
		}

		public void setInvoicingAddress4(String invoicingAddress4) {
			this.invoicingAddress4 = invoicingAddress4;
		}

		public String getInvoicingAddress5() {
			return invoicingAddress5;
		}

		public void setInvoicingAddress5(String invoicingAddress5) {
			this.invoicingAddress5 = invoicingAddress5;
		}

		public String getOperatingArea() {
			return operatingArea;
		}

		public void setOperatingArea(String operatingArea) {
			this.operatingArea = operatingArea;
		}

		public String getOperatingBase() {
			return operatingBase;
		}

		public void setOperatingBase(String operatingBase) {
			this.operatingBase = operatingBase;
		}

		public String getIcao() {
			return icao;
		}

		public void setIcao(String icao) {
			this.icao = icao;
		}

		public String getCurrency() {
			return currency;
		}

		public void setCurrency(String currency) {
			this.currency = currency;
		}

		public Date getStartDate() {
			return startDate;
		}

		public void setStartDate(Date startDate) {
			this.startDate = startDate;
		}

		public Date getEndDate() {
			return endDate;
		}

		public void setEndDate(Date endDate) {
			this.endDate = endDate;
		}

		public boolean isOpenEnded() {
			return openEnded;
		}

		public void setOpenEnded(boolean openEnded) {
			this.openEnded = openEnded;
		}

		public int getTerminationPeriod() {
			return terminationPeriod;
		}

		public void setTerminationPeriod(int terminationPeriod) {
			this.terminationPeriod = terminationPeriod;
		}
	}
	
	@Embeddable
	public static class Resources {
		private int captainRequirement;
		private int copilotRequirement;
		private int flightEngineerRequirement;
		private int groundEngineerRequirement;
		private int groundCrewRequirement;
		private String comments;
		private String aircraftType;
		
		private int maximumTerm;
		private Money forfeitPerDay=new Money();
		public int getCaptainRequirement() {
			return captainRequirement;
		}
		public void setCaptainRequirement(int captainRequirement) {
			this.captainRequirement = captainRequirement;
		}
		public int getCopilotRequirement() {
			return copilotRequirement;
		}
		public void setCopilotRequirement(int copilotRequirement) {
			this.copilotRequirement = copilotRequirement;
		}
		public int getFlightEngineerRequirement() {
			return flightEngineerRequirement;
		}
		public void setFlightEngineerRequirement(int flightEngineerRequirement) {
			this.flightEngineerRequirement = flightEngineerRequirement;
		}
		public int getGroundEngineerRequirement() {
			return groundEngineerRequirement;
		}
		public void setGroundEngineerRequirement(int groundEngineerRequirement) {
			this.groundEngineerRequirement = groundEngineerRequirement;
		}
		public int getGroundCrewRequirement() {
			return groundCrewRequirement;
		}
		public void setGroundCrewRequirement(int groundCrewRequirement) {
			this.groundCrewRequirement = groundCrewRequirement;
		}
		public String getComments() {
			return comments;
		}
		public void setComments(String comments) {
			this.comments = comments;
		}
		public int getMaximumTerm() {
			return maximumTerm;
		}
		public void setMaximumTerm(int maximumTerm) {
			this.maximumTerm = maximumTerm;
		}
		public Money getForfeitPerDay() {
			if (forfeitPerDay == null)
				forfeitPerDay = new Money();
			return forfeitPerDay;
		}
		public void setForfeitPerDay(Money forfeitPerDay) {
			this.forfeitPerDay = forfeitPerDay;
		}
		public String getAircraftType() {
			return aircraftType;
		}
		public void setAircraftType(String aircraftType) {
			this.aircraftType = aircraftType;
		}
	}
	
	@Embeddable
	public static class Pricing {
		@CollectionOfElements(fetch=FetchType.LAZY)
		@IndexColumn(name="position")
		private List<Item> items;
		
		private Pricing() {
			setupDefaults();
		}

		private void setupDefaults() {
			items = new ArrayList<Item>();
			//Set up defaults
			Item item = new Item();
			item.setItem("Deposit");
			items.add(item);
			
			item = new Item();
			item.setItem("Advance Payment");
			items.add(item);
			
			item = new Item();
			item.setItem("Standing Fee per Aircraft");
			items.add(item);
			
			item = new Item();
			item.setItem("Flight Hour Rate I");
			items.add(item);
			
			item = new Item();
			item.setItem("Flight Hour Rate II");
			items.add(item);
		}
		
		@Embeddable
		public static class Item {
			private String item;
			private String status;
			private Money limit = new Money();
			private String comments;
			public String getItem() {
				return item;
			}
			public void setItem(String item) {
				this.item = item;
			}
			public String getStatus() {
				return status;
			}
			public void setStatus(String status) {
				this.status = status;
			}
			public Money getLimit() {
				if (limit == null)
					limit = new Money();
				return limit;
			}
			public String getComments() {
				return comments;
			}
			public void setComments(String comments) {
				this.comments = comments;
			}
		}
	
		public List<Item> getItems() {
			if (items.isEmpty())
				setupDefaults();
			return items;
		}
		
		public Item getItem(int i) {
			if (items.isEmpty())
				setupDefaults();
			return items.get(i);
		}
		
		public void setItem(int i, Item item) {
			items.add(i, item);
		}
	}
	
	@Embeddable
	public static class Insurance {
		@CollectionOfElements(fetch=FetchType.LAZY)
		@IndexColumn(name="position")
		private List<Item> items = new ArrayList<Item>();;
		
		private Insurance() {
			setupDefaults();
		}

		private void setupDefaults() {
			//Set up defaults
			Item item = new Item();
			item.setItem("All Risk - Hull Insurance");
			items.add(item);
			
			item = new Item();
			item.setItem("Third Party");
			items.add(item);
			
			item = new Item();
			item.setItem("Employers Liability");
			items.add(item);
			
			item = new Item();
			item.setItem("War Risk - Hull Insurance");
			items.add(item);
			
			item = new Item();
			item.setItem("War Risk - Combined");
			items.add(item);
			
			item = new Item();
			item.setItem("Performance Guarantees");
			items.add(item);
		}
		
		@Embeddable
		public static class Item {
			private String item;
			private boolean required;
			private Money limit = new Money();
			private String comments;
			private String policyNumber;
			private String brokerDetails;
			@Temporal(TemporalType.DATE)
			private Date policyExpDate;
			@Temporal(TemporalType.DATE)
			private Date premiumDueDate;
			
			public String getItem() {
				return item;
			}
			public void setItem(String item) {
				this.item = item;
			}
			public boolean isRequired() {
				return required;
			}
			public void setRequired(boolean required) {
				this.required = required;
			}
			public Money getLimit() {
				if (limit == null)
					limit = new Money();
				return limit;
			}
			public String getComments() {
				return comments;
			}
			public void setComments(String comments) {
				this.comments = comments;
			}
			public String getPolicyNumber() {
				return policyNumber;
			}
			public void setPolicyNumber(String policyNumber) {
				this.policyNumber = policyNumber;
			}
			public String getBrokerDetails() {
				return brokerDetails;
			}
			public void setBrokerDetails(String brokerDetails) {
				this.brokerDetails = brokerDetails;
			}
			public Date getPolicyExpDate() {
				return policyExpDate;
			}
			public void setPolicyExpDate(Date policyExpDate) {
				this.policyExpDate = policyExpDate;
			}
			public Date getPremiumDueDate() {
				return premiumDueDate;
			}
			public void setPremiumDueDate(Date premiumDueDate) {
				this.premiumDueDate = premiumDueDate;
			}
		}
		
		public List<Item> getItems() {
			if (items.isEmpty())
				setupDefaults();
			return items;
		}
		
		public Item getItem(int i) {
			if (items.isEmpty())
				setupDefaults();
			return items.get(i);
		}
		
		public void setItem(int i, Item item) {
			items.add(i, item);
		}
	}
	
	@Embeddable
	public static class Cost {
		@OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
		@IndexColumn(name="position")
		List<ItemGroup> itemGroups = new ArrayList<ItemGroup>();
		
		private Cost() {
			setupDefaults();
		}

		private void setupDefaults() {
			//Set up defaults
			ItemGroup itemGroup = new ItemGroup();
			itemGroup.setName("Preparation");
			itemGroups.add(itemGroup);
			
			List<ItemGroup.Item> items = itemGroup.getItems();
			ItemGroup.Item item = new ItemGroup.Item();
			item.setItem("Mobilisation");
			items.add(item);
			
			item = new ItemGroup.Item();
			item.setItem("Demobilisation");
			items.add(item);
			
			item = new ItemGroup.Item();
			item.setItem("Aircraft Assembly");
			items.add(item);
			
			item = new ItemGroup.Item();
			item.setItem("Aircraft Dissassembly");
			items.add(item);
			
			/* Fuel */
			itemGroup = new ItemGroup();
			itemGroup.setName("Fuel");
			itemGroups.add(itemGroup);
			items = itemGroup.getItems();
			
			item = new ItemGroup.Item();
			item.setItem("Operational Fuel");
			items.add(item);
			
			item = new ItemGroup.Item();
			item.setItem("Fueling Equipment");
			items.add(item);
			
			item = new ItemGroup.Item();
			item.setItem("Lubricants");
			items.add(item);
			
			item = new ItemGroup.Item();
			item.setItem("Ground Handling - Equipment");
			items.add(item);
			
			item = new ItemGroup.Item();
			item.setItem("Ground Handling - Crew");
			items.add(item);
			
			item = new ItemGroup.Item();
			item.setItem("Catering On-Board");
			items.add(item);
			
			item = new ItemGroup.Item();
			item.setItem("Additional Personnel");
			items.add(item);
			
			/* Airport Expenses */
			itemGroup = new ItemGroup();
			itemGroup.setName("Airport Expenses");
			itemGroups.add(itemGroup);
			items = itemGroup.getItems();
			
			item = new ItemGroup.Item();
			item.setItem("Landing");
			items.add(item);
			
			item = new ItemGroup.Item();
			item.setItem("Taxes");
			items.add(item);
			
			item = new ItemGroup.Item();
			item.setItem("Route Charges");
			items.add(item);
			
			item = new ItemGroup.Item();
			item.setItem("Other");
			items.add(item);
			
			/* Subsistence */
			itemGroup = new ItemGroup();
			itemGroup.setName("Subsistence");
			itemGroups.add(itemGroup);
			items = itemGroup.getItems();
			
			item = new ItemGroup.Item();
			item.setItem("Accomodation");
			items.add(item);
			
			item = new ItemGroup.Item();
			item.setItem("Meals");
			items.add(item);
			
			item = new ItemGroup.Item();
			item.setItem("Satellite Comms");
			items.add(item);
			
			item = new ItemGroup.Item();
			item.setItem("Internet Access");
			items.add(item);
			
			item = new ItemGroup.Item();
			item.setItem("TV");
			items.add(item);
			
			item = new ItemGroup.Item();
			item.setItem("Base Transport");
			items.add(item);
			
			/* Hangar Services */
			itemGroup = new ItemGroup();
			itemGroup.setName("Hangar Services");
			itemGroups.add(itemGroup);
			items = itemGroup.getItems();
			
			item = new ItemGroup.Item();
			item.setItem("Lighting");
			items.add(item);
			
			item = new ItemGroup.Item();
			item.setItem("2 Tonne Crane");
			items.add(item);
			
			item = new ItemGroup.Item();
			item.setItem("Maintenance Stands");
			items.add(item);
			
			item = new ItemGroup.Item();
			item.setItem("GPU");
			items.add(item);
			
			item = new ItemGroup.Item();
			item.setItem("Towing Tractor");
			items.add(item);
			
			/* Hangar Services */
			itemGroup = new ItemGroup();
			itemGroup.setName("Freight");
			itemGroups.add(itemGroup);
			items = itemGroup.getItems();
			
			item = new ItemGroup.Item();
			item.setItem("Local");
			items.add(item);
			
			item = new ItemGroup.Item();
			item.setItem("International");
			items.add(item);
		}
		
		@Entity
		public static class ItemGroup {
			@Id @GeneratedValue
			private Integer id;
			private String name;
			@CollectionOfElements(fetch=FetchType.LAZY)
			@IndexColumn(name="position")
			private List<Item> items = new ArrayList<Item>();
			
			@Embeddable
			public static class Item {
				private String item;
				private String status;
				private Money limit = new Money();
				private String comments;
				public String getItem() {
					return item;
				}
				public void setItem(String item) {
					this.item = item;
				}
				public String getStatus() {
					return status;
				}
				public void setStatus(String status) {
					this.status = status;
				}
				public Money getLimit() {
					if (limit == null)
						limit = new Money();
					return limit;
				}
				public String getComments() {
					return comments;
				}
				public void setComments(String comments) {
					this.comments = comments;
				}
			}
			
			public String getName() {
				return name;
			}

			public void setName(String name) {
				this.name = name;
			}

			public List<Item> getItems() {				
				return items;
			}
			
			public Item getItem(int i) {
				return items.get(i);
			}
			
			public void setItem(int i, Item item) {
				items.add(i, item);
			}
		}
		
		public List<ItemGroup> getItemGroups() {
			if (itemGroups.isEmpty())
				setupDefaults();
			return itemGroups;
		}
		
		public ItemGroup getItemGroup(int i) {
			if (itemGroups.isEmpty())
				setupDefaults();
			return itemGroups.get(i);
		}
		
		public void setItemGroup(int i, ItemGroup item) {
			itemGroups.add(i, item);
		}
		
	}

	
	
	public Integer getId() {
		return id;
	}
	
	public Date getStartDatePlain() {
		return getAdministrative().getStartDate();
	}
	
	public void setStartDatePlain(Date d) {
		getAdministrative().setStartDate(d);
	}
	
	public Date getEndDatePlain() {
		return getAdministrative().getEndDate();
	}
	
	public void setEndDatePlain(Date d) {
		getAdministrative().setEndDate(d);
	}
	

	public Administrative getAdministrative() {
		if (administrative == null)
			administrative = new Administrative();
		return administrative;
	}

	public Resources getResources() {
		if (resources == null)
			resources = new Resources();
		return resources;
	}

	public Pricing getPricing() {
		if (pricing == null)
			pricing = new Pricing();
		return pricing;
	}

	public Insurance getInsurance() {
		if (insurance == null)
			insurance = new Insurance();
		return insurance;
	}

	public Cost getCost() {
		if (cost == null)
			cost = new Cost();
		return cost;
	}
	
	
	
	
	private String client = "";
	private String description = "";
	private String location = "";
	@Type(type="charterStatus")
	private CharterStatus status = CharterStatus.PROSPECT;
	@Temporal(TemporalType.DATE)
	private Date startDate;
	@Temporal(TemporalType.DATE)
	private Date endDate;
	private String code = "";
	
	private String contract;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public DateMidnight getStartDate() {
		return new DateMidnight(getAdministrative().getStartDate());
	}
	public void setStartDate(DateMidnight startDate) {
		this.startDate = startDate.toDate();
	}
	public DateMidnight getEndDate() {
		return new DateMidnight(getAdministrative().getEndDate());
	}
	public void setEndDate(DateMidnight endDate) {
		this.endDate = endDate.toDate();
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	public String getClient() {
		return getAdministrative().getClientName();
	}
	public void setClient(String client) {
		this.client = client;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public CharterStatus getStatus() {
		return status;
	}
	public void setStatus(CharterStatus status) {
		this.status = status;
	}
	public String getFormattedStartDate() {
		return getStartDatePlain().toString().substring(0,10);
	}
	public String getFormattedEndDate() {
		return getEndDatePlain().toString().substring(0,10);
	}
	public String getContract() {
		return contract;
	}
	public void setContract(String contract) {
		this.contract = contract;
	}
	
}
