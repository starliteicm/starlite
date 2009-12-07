package com.itao.starlite.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.TreeMap;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.IndexColumn;
import org.hibernate.annotations.OrderBy;

import com.itao.starlite.model.CrewMember.FlightAndDutyActuals.Deduction;

/**
 * <p>Represents a Starlite Crew Member. The data for the Crew Member is split
 * in to a number of sections, each represented by an Nested Class. The Nested Classes
 * are instantiated when a CrewMember object is constructed. This class is immutable, 
 * so new instances of any of the nested classes cannot be assigned.
 * Modifications to a Crew Member's data is done through the Nested Class instances.</p>
 * 
 * <p>This class has been mapped for Hibernate, storing each Nested class as a component.</p>
 * @author Jason Choy
 * @author Jonathan Elliott
 */
@Entity
public class CrewMember implements Cloneable {
	@Id
	@GeneratedValue
	private Integer id;

	@Column(unique=true)
	private String code;
	
	@Column(nullable=false)
	private boolean active=true;
	
	private Personal personal = new Personal();
	private Banking banking = new Banking();
	private Role role = new Role();
	private Payments payments = new Payments();
//	private FlightAndDuty flightAndDuty = new FlightAndDuty();
	private Review review = new Review();
	
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	@Fetch(FetchMode.SUBSELECT)
	@OrderBy(clause="date desc")
	private List<FlightAndDutyActuals> flightAndDutyActuals = new ArrayList<FlightAndDutyActuals>();
	
	@OneToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	private ApprovalGroup approvalGroup = new ApprovalGroup();
	
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	@Fetch(FetchMode.SUBSELECT)
	private List<Passport> passports = new ArrayList<Passport>();
	
	/*
	 * Nested Classes - These are used to partition the data into manageable sections
	 */
	
	@Entity
	public class Passport{			
		public String passportNumber;
		@Temporal(TemporalType.DATE)
		public Date expiryDate;
		public String country;
		@Id @GeneratedValue
		public Integer id;
		public Passport(){}
		public Passport(String _passportNumber, String _passportCountry, Date _passportExpiry){
			this.setPassportNumber(_passportNumber);
			this.setCountry(_passportCountry);
			this.setExpiryDate(_passportExpiry);
		}
		public void setPassportNumber(String passportNumber) {
			this.passportNumber = passportNumber;
		}
		public String getPassportNumber() {
			return passportNumber;
		}
		public void setExpiryDate(Date passportExpiryDate) {
			this.expiryDate = passportExpiryDate;
		}
		public Date getExpiryDate() {
			return expiryDate;
		}
		public void setCountry(String passportCountry) {
			this.country = passportCountry;
		}
		public String getCountry() {
			return country;
		}
		public void setId(Integer id) {
			this.id = id;
		}
		public Integer getId() {
			return id;
		}
		public String toString(){
			return "{id:"+getId()+",country:"+getCountry()+",number:"+getPassportNumber()+",expiry:"+getExpiryDate()+"}";
		}
	}
	
	public List<Passport> getPassports(){
	  if(passports == null){
		  passports = new LinkedList<Passport>();
	  }
	  if(personal.passportCountry != null){
		  //first passport
		  passports.add(new Passport(personal.passportNumber,personal.passportCountry,personal.passportExpiryDate));
	  }
	  passports.add(new Passport()); 
	  
	  System.out.println("GET:"+passports);
	  return passports;
	}
	
	public void setPassports(List<Passport> passports) {
		System.out.println("SET:"+passports);
		this.passports = passports;
	}
	
	
	@Embeddable
	public static class Personal {
		private String lastName;
		private String firstName;
		private String secondName;
		private String preferedName;
		private String title;
		private String gender;
		private String status;
		private String classification;
		
		private String address1;
		private String address2;
		private String address3;
		private String address4;
		private String address5;

		private String postalAddress;
		private String postalTown;
		private String postalCode;
		private String postalCountry;
		
		private String mobilePhone;
		private String mobilePhone2;
		private String homePhone;
		private String homeFax;
		private String businessPhone;
		private String businessFax;
		private String email;
		private String alternateEmail;

		@Temporal(TemporalType.DATE)
		private Date dateOfBirth;
		private String nationality;
		private String idNumber;

		private String passportNumber;
		@Temporal(TemporalType.DATE)
		private Date passportExpiryDate;
		private String passportCountry;

		private String  nextOfKinLastName;
		private String  nextOfKinFirstName;
		private String  nextOfKinMobilePhone;
		private String  nextOfKinHomePhone;
		private String  nextOfKinRelation;
		private String  nextOfKinEldestChildName;
		private Integer nextOfKinNumberOfChildren;
		private String  nextOfKinAddress1;
		private String  nextOfKinAddress2;
		private String  nextOfKinAddress3;
		private String  nextOfKinAddress4;
		private String  nextOfKinAddress5;
		
		private String emergencyContactName;
		private String emergencyContactNumber;
		private String emergencyContactRelationship;
		
		private String alternativeEmergencyContactName;
		private String alternativeEmergencyContactNumber;
		private String alternativeEmergencyContactRelationship;
		
		public String getLastName() {
			return lastName;
		}

		public void setLastName(String lastName) {
			this.lastName = lastName;
		}

		public String getFirstName() {
			return firstName;
		}

		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}

		public String getSecondName() {
			return secondName;
		}

		public void setSecondName(String secondName) {
			this.secondName = secondName;
		}

		public String getFullName(){
			if("".equals(secondName) || secondName == null){
				return firstName+" "+lastName;
			}
			return firstName+" "+secondName+" "+lastName;
		}
		
		public void setPreferedName(String preferedName){
			this.preferedName = preferedName;
		}
		
		public String getPreferedName(){
			return preferedName;
		}
		
		public String getAddress1() {
			return address1;
		}

		public void setAddress1(String address1) {
			this.address1 = address1;
		}

		public String getAddress2() {
			return address2;
		}

		public void setAddress2(String address2) {
			this.address2 = address2;
		}

		public String getAddress3() {
			return address3;
		}

		public void setAddress3(String address3) {
			this.address3 = address3;
		}

		public String getAddress4() {
			return address4;
		}

		public void setAddress4(String address4) {
			this.address4 = address4;
		}

		public String getAddress5() {
			return address5;
		}

		public void setAddress5(String address5) {
			this.address5 = address5;
		}

		public String getMobilePhone() {
			return mobilePhone;
		}

		public void setMobilePhone(String mobilePhone) {
			this.mobilePhone = mobilePhone;
		}

		public String getHomePhone() {
			return homePhone;
		}

		public void setHomePhone(String homePhone) {
			this.homePhone = homePhone;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public Date getDateOfBirth() {
			return dateOfBirth;
		}

		public void setDateOfBirth(Date dateOfBirth) {
			this.dateOfBirth = dateOfBirth;
		}

		public String getNationality() {
			return nationality;
		}

		public void setNationality(String nationality) {
			this.nationality = nationality;
		}

		public String getIdNumber() {
			return idNumber;
		}

		public void setIdNumber(String idNumber) {
			this.idNumber = idNumber;
		}

		public String getPassportNumber() {
			return passportNumber;
		}

		public void setPassportNumber(String passportNumber) {
			this.passportNumber = passportNumber;
		}

		public Date getPassportExpiryDate() {
			return passportExpiryDate;
		}

		public void setPassportExpiryDate(Date passportExpiryDate) {
			this.passportExpiryDate = passportExpiryDate;
		}

		public String getNextOfKinLastName() {
			return nextOfKinLastName;
		}

		public void setNextOfKinLastName(String nextOfKinLastName) {
			this.nextOfKinLastName = nextOfKinLastName;
		}

		public String getNextOfKinFirstName() {
			return nextOfKinFirstName;
		}

		public void setNextOfKinFirstName(String nextOfKinFirstName) {
			this.nextOfKinFirstName = nextOfKinFirstName;
		}

		public String getNextOfKinMobilePhone() {
			return nextOfKinMobilePhone;
		}

		public void setNextOfKinMobilePhone(String nextOfKinMobilePhone) {
			this.nextOfKinMobilePhone = nextOfKinMobilePhone;
		}

		public String getNextOfKinHomePhone() {
			return nextOfKinHomePhone;
		}

		public void setNextOfKinHomePhone(String nextOfKinHomePhone) {
			this.nextOfKinHomePhone = nextOfKinHomePhone;
		}

		public String getNextOfKinRelation() {
			return nextOfKinRelation;
		}

		public void setNextOfKinRelation(String nextOfKinRelation) {
			this.nextOfKinRelation = nextOfKinRelation;
		}
		
		public void setTitle(String title) {
			this.title = title;
		}

		public String getTitle() {
			return title;
		}
		
		public void setGender(String gender) {
			this.gender = gender;
		}

		public String getGender() {
			return gender;
		}

		public void setClassification(String classification) {
			this.classification = classification;
		}

		public String getClassification() {
			return classification;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public String getStatus() {
			return status;
		}

		private Personal() {}

		public void setMobilePhone2(String mobilePhone2) {
			this.mobilePhone2 = mobilePhone2;
		}

		public String getMobilePhone2() {
			return mobilePhone2;
		}

		public void setPostalAddress(String postalAddress) {
			this.postalAddress = postalAddress;
		}

		public String getPostalAddress() {
			return postalAddress;
		}

		public void setPostalTown(String postalTown) {
			this.postalTown = postalTown;
		}

		public String getPostalTown() {
			return postalTown;
		}

		public void setPostalCode(String postalCode) {
			this.postalCode = postalCode;
		}

		public String getPostalCode() {
			return postalCode;
		}

		public void setPostalCountry(String postalCountry) {
			this.postalCountry = postalCountry;
		}

		public String getPostalCountry() {
			return postalCountry;
		}

		public void setHomeFax(String homeFax) {
			this.homeFax = homeFax;
		}

		public String getHomeFax() {
			return homeFax;
		}

		public void setBusinessPhone(String businessPhone) {
			this.businessPhone = businessPhone;
		}

		public String getBusinessPhone() {
			return businessPhone;
		}

		public void setBusinessFax(String businessFax) {
			this.businessFax = businessFax;
		}

		public String getBusinessFax() {
			return businessFax;
		}

		public void setAlternateEmail(String alternateEmail) {
			this.alternateEmail = alternateEmail;
		}

		public String getAlternateEmail() {
			return alternateEmail;
		}

		public void setNextOfKinEldestChildName(String nextOfKinEldestChildName) {
			this.nextOfKinEldestChildName = nextOfKinEldestChildName;
		}

		public String getNextOfKinEldestChildName() {
			return nextOfKinEldestChildName;
		}

		public void setNextOfKinNumberOfChildren(
				Integer nextOfKinNumberOfChildren) {
			this.nextOfKinNumberOfChildren = nextOfKinNumberOfChildren;
		}

		public Integer getNextOfKinNumberOfChildren() {
			return nextOfKinNumberOfChildren;
		}

		public void setNextOfKinAddress1(String nextOfKinAddress1) {
			this.nextOfKinAddress1 = nextOfKinAddress1;
		}

		public String getNextOfKinAddress1() {
			return nextOfKinAddress1;
		}

		public void setNextOfKinAddress2(String nextOfKinAddress2) {
			this.nextOfKinAddress2 = nextOfKinAddress2;
		}

		public String getNextOfKinAddress2() {
			return nextOfKinAddress2;
		}

		public void setNextOfKinAddress3(String nextOfKinAddress3) {
			this.nextOfKinAddress3 = nextOfKinAddress3;
		}

		public String getNextOfKinAddress3() {
			return nextOfKinAddress3;
		}

		public void setNextOfKinAddress4(String nextOfKinAddress4) {
			this.nextOfKinAddress4 = nextOfKinAddress4;
		}

		public String getNextOfKinAddress4() {
			return nextOfKinAddress4;
		}

		public void setNextOfKinAddress5(String nextOfKinAddress5) {
			this.nextOfKinAddress5 = nextOfKinAddress5;
		}

		public String getNextOfKinAddress5() {
			return nextOfKinAddress5;
		}

		public void setEmergencyContactName(String emergencyContactName) {
			this.emergencyContactName = emergencyContactName;
		}

		public String getEmergencyContactName() {
			return emergencyContactName;
		}

		public void setEmergencyContactNumber(String emergencyContactNumber) {
			this.emergencyContactNumber = emergencyContactNumber;
		}

		public String getEmergencyContactNumber() {
			return emergencyContactNumber;
		}

		public void setEmergencyContactRelationship(
				String emergencyContactRelationship) {
			this.emergencyContactRelationship = emergencyContactRelationship;
		}

		public String getEmergencyContactRelationship() {
			return emergencyContactRelationship;
		}

		public void setAlternativeEmergencyContactName(
				String alternativeEmergencyContactName) {
			this.alternativeEmergencyContactName = alternativeEmergencyContactName;
		}

		public String getAlternativeEmergencyContactName() {
			return alternativeEmergencyContactName;
		}

		public void setAlternativeEmergencyContactNumber(
				String alternativeEmergencyContactNumber) {
			this.alternativeEmergencyContactNumber = alternativeEmergencyContactNumber;
		}

		public String getAlternativeEmergencyContactNumber() {
			return alternativeEmergencyContactNumber;
		}

		public void setAlternativeEmergencyContactRelationship(
				String alternativeEmergencyContactRelationship) {
			this.alternativeEmergencyContactRelationship = alternativeEmergencyContactRelationship;
		}

		public String getAlternativeEmergencyContactRelationship() {
			return alternativeEmergencyContactRelationship;
		}

		public void setPassportCountry(String passportCountry) {
			this.passportCountry = passportCountry;
		}

		public String getPassportCountry() {
			return passportCountry;
		}


	}

	@Embeddable
	public static class Banking {
		private String bankName;
		private String branchCode;
		private String accountName;
		private String accountNumber;
		
		private String address1;
		private String address2;
		private String address3;
		private String address4;
		private String address5;
		
		private String swift;
		private String iban;
		private String bic;

		public String getBankName() {
			return bankName;
		}

		public void setBankName(String bankName) {
			this.bankName = bankName;
		}

		public String getBranchCode() {
			return branchCode;
		}

		public void setBranchCode(String branchCode) {
			this.branchCode = branchCode;
		}

		public String getAccountName() {
			return accountName;
		}

		public void setAccountName(String accountName) {
			this.accountName = accountName;
		}

		public String getAccountNumber() {
			return accountNumber;
		}

		public void setAccountNumber(String accountNumber) {
			this.accountNumber = accountNumber;
		}

		public String getAddress1() {
			return address1;
		}

		public void setAddress1(String address1) {
			this.address1 = address1;
		}

		public String getAddress2() {
			return address2;
		}

		public void setAddress2(String address2) {
			this.address2 = address2;
		}

		public String getAddress3() {
			return address3;
		}

		public void setAddress3(String address3) {
			this.address3 = address3;
		}

		public String getAddress4() {
			return address4;
		}

		public void setAddress4(String address4) {
			this.address4 = address4;
		}

		public String getAddress5() {
			return address5;
		}

		public void setAddress5(String address5) {
			this.address5 = address5;
		}

		public String getSwift() {
			return swift;
		}

		public void setSwift(String swift) {
			this.swift = swift;
		}

		public String getIban() {
			return iban;
		}

		public void setIban(String iban) {
			this.iban = iban;
		}

		public String getBic() {
			return bic;
		}

		public void setBic(String bic) {
			this.bic = bic;
		}

		private Banking() {}
	}

	@Embeddable
	public static class Role {
		private String employment;
		private String company;
		private String department;
		private String manager;
		private String position;
		private String primaryLocation;
		@Temporal(TemporalType.DATE)
		private Date initialDate;
		private String medicalClass;
		private String medicalAid;
		private String medicalAidNumber;
		private String drivingLicenceNumber;
		private String drivingLicenceIssued;
		private String telephoneExtension;
		
		private Date lastDate;
		private String lastType;
		private Double totalHours;
		
		@Temporal(TemporalType.DATE)
		private Date expiryDate;
		
		private Certificate r1 = new Certificate();
		private Certificate r2 = new Certificate();
		private Certificate crm = new Certificate();
		private Certificate dg = new Certificate();
		private Certificate ifr = new Certificate();
		private Certificate instructor = new Certificate();
		private Certificate test = new Certificate();
		private Certificate huet = new Certificate();
		
		private String night;
		private String nvg;
		private String sling;
		private String game;		
		
		@CollectionOfElements(fetch=FetchType.LAZY)
		@Fetch(FetchMode.SUBSELECT)
		@IndexColumn(name="position")
		private List<Certificate> conversions = new LinkedList<Certificate>();

		public void setEmployment(String employment) {
			this.employment = employment;
		}

		public String getEmployment() {
			return employment;
		}

		public String getPosition() {
			return position;
		}

		public void setPosition(String position) {
			this.position = position;
		}

		public String getPrimaryLocation() {
			return primaryLocation;
		}

		public void setPrimaryLocation(String primaryLocation) {
			this.primaryLocation = primaryLocation;
		}

		public Date getInitialDate() {
			return initialDate;
		}

		public void setInitialDate(Date initialDate) {
			this.initialDate = initialDate;
		}

		public String getMedicalClass() {
			return medicalClass;
		}

		public void setMedicalClass(String medicalClass) {
			this.medicalClass = medicalClass;
		}

		public Date getExpiryDate() {
			return expiryDate;
		}

		public void setExpiryDate(Date expiryDate) {
			this.expiryDate = expiryDate;
		}

		public Certificate getR1() {
			if (r1 == null)
				r1 = new Certificate();
			return r1;
		}

		public Certificate getR2() {
			if (r2 == null)
				r2 = new Certificate();
			return r2;
		}

		public Certificate getCrm() {
			if (crm == null)
				crm = new Certificate();
			return crm;
		}

		public Certificate getDg() {
			if (dg == null)
				dg = new Certificate();
			return dg;
		}

		public Certificate getIfr() {
			if (ifr == null)
				ifr = new Certificate();
			return ifr;
		}
		
		public Certificate getInstructor() {
			if (instructor == null)
				instructor = new Certificate();
			return instructor;
		}
		
		public Certificate getTest() {
			if (test == null)
				test = new Certificate();
			return test;
		}

		public Certificate getHuet() {
			if (huet == null)
				huet = new Certificate();
			return huet;
		}

		public synchronized List<Certificate> getConversions() {
			if (conversions == null) {
				conversions = new ArrayList<Certificate>(0);
			}
			HashMap<String,Certificate> cons = new HashMap<String,Certificate>();
			ListIterator<Certificate> listIterator = conversions.listIterator();
			while (listIterator.hasNext()) {
				Certificate c = listIterator.next();
				if (c == null || (c.getExpiryDate() == null && c.getIssueDate() == null && (c.getNumber() == null || c.getNumber().trim().length() == 0))){
					listIterator.remove();
				}
			    else if(cons.containsKey(c.getNumber())){
					listIterator.remove();
				}
				else{
					cons.put(c.getNumber(), c);
				}
			}
			return conversions;
		}
		
//		public synchronized List<Certificate> getConversionsList() {
//			if (conversions == null) {
//				conversions = new ArrayList<Certificate>(0);
//			}
//			return conversions;
//		}
		
		public synchronized void setConversions(List<Certificate> conversions) {
			HashMap<String,Certificate> cons = new HashMap<String,Certificate>();
			for(Certificate c : conversions){
				cons.put(c.getNumber(), c);
			}
			this.conversions = new LinkedList<Certificate>(cons.values());
		}
		
//		public synchronized Certificate getConversion(int index) {
//			/**
//			 * This is to allow struts to add new conversions
//			 */
//			if (index >= getConversions().size()) {
//				Certificate c = new Certificate();
//				getConversions().add(c);
//				return c;
//			}
//			return getConversions().get(index);
//		}
//		
//		public synchronized void setConversion(int index, Certificate conversion) {
//			index = Math.min(index, getConversions().size());
//			getConversions().add(index, conversion);
//		}

		private Role() {}

		public void setCompany(String company) {
			this.company = company;
		}

		public String getCompany() {
			return company;
		}

		public void setDepartment(String department) {
			this.department = department;
		}

		public String getDepartment() {
			return department;
		}

		public void setManager(String manager) {
			this.manager = manager;
		}

		public String getManager() {
			return manager;
		}

		public void setMedicalAid(String medicalAid) {
			this.medicalAid = medicalAid;
		}

		public String getMedicalAid() {
			return medicalAid;
		}

		public void setMedicalAidNumber(String medicalAidNumber) {
			this.medicalAidNumber = medicalAidNumber;
		}

		public String getMedicalAidNumber() {
			return medicalAidNumber;
		}

		public void setDrivingLicenceNumber(String drivingLicenceNumber) {
			this.drivingLicenceNumber = drivingLicenceNumber;
		}

		public String getDrivingLicenceNumber() {
			return drivingLicenceNumber;
		}

		public void setDrivingLicenceIssued(String drivingLicenceIssued) {
			this.drivingLicenceIssued = drivingLicenceIssued;
		}

		public String getDrivingLicenceIssued() {
			return drivingLicenceIssued;
		}

		public void setTelephoneExtension(String telephoneExtension) {
			this.telephoneExtension = telephoneExtension;
		}

		public String getTelephoneExtension() {
			return telephoneExtension;
		}

		public void setLastDate(Date lastDate) {
			this.lastDate = lastDate;
		}

		public Date getLastDate() {
			return lastDate;
		}

		public void setLastType(String lastType) {
			this.lastType = lastType;
		}

		public String getLastType() {
			return lastType;
		}

		public void setTotalHours(Double totalHours) {
			this.totalHours = totalHours;
		}

		public Double getTotalHours() {
			return totalHours;
		}

		public void setNight(String night) {
			this.night = night;
		}

		public String getNight() {
			return night;
		}

		public void setNvg(String nvg) {
			this.nvg = nvg;
		}

		public String getNvg() {
			return nvg;
		}

		public void setSling(String sling) {
			this.sling = sling;
		}

		public String getSling() {
			return sling;
		}

		public void setGame(String game) {
			this.game = game;
		}

		public String getGame() {
			return game;
		}


	}
	
	@Embeddable
	public static class Payments {
		@Column(nullable=false)
		private Money monthlyBaseRate = new Money();
		
		@Column(nullable=false)
		private Money areaAllowance = new Money();
		@Column(nullable=false)
		private Money instructorAllowance = new Money();
		@Column(nullable=false)
		private Money dailyAllowance = new Money();
		@Column(nullable=false)
		private Money flightAllowance = new Money();
		
		@Column(length=3)
		private String currency;
		
		public Money getMonthlyBaseRate() {
			if (monthlyBaseRate == null)
				monthlyBaseRate = new Money();
			return monthlyBaseRate;
		}
		public Money getAreaAllowance() {
			if (areaAllowance == null)
				areaAllowance = new Money();
			return areaAllowance;
		}
		public Money getInstructorAllowance() {
			if (instructorAllowance == null)
				instructorAllowance = new Money();
			return instructorAllowance;
		}
		public Money getDailyAllowance() {
			if (dailyAllowance == null)
				dailyAllowance = new Money();
			return dailyAllowance;
		}
		public Money getFlightAllowance() {
			if (flightAllowance == null)
				flightAllowance = new Money();
			return flightAllowance;
		}
		
		public String getCurrency() {
			return currency;
		}

		public void setCurrency(String currency) {
			this.currency = currency;
			monthlyBaseRate.setCurrencyCode(currency);
			areaAllowance.setCurrencyCode(currency);
			instructorAllowance.setCurrencyCode(currency);
			dailyAllowance.setCurrencyCode(currency);
			flightAllowance.setCurrencyCode(currency);
		}
		
		private Payments() {}
	}

//	@Embeddable
//	public static class FlightAndDuty {
//		private Money areaAllowance;
//		private Money instructorAllowance;
//		private Money dailyAllowance;
//		private Money flightAllowance;
//		
//		private Money sundryType1Amount;
//		private String sundryType1Comments;
//		
//		private Money sundryType2Amount;
//		private String sundryType2Comments;
//		
//		public Money getAreaAllowance() {
//			if (areaAllowance == null)
//				areaAllowance = new Money();
//			return areaAllowance;
//		}
//		public Money getInstructorAllowance() {
//			if (instructorAllowance == null)
//				instructorAllowance = new Money();
//			return instructorAllowance;
//		}
//		public Money getDailyAllowance() {
//			if (dailyAllowance == null)
//				dailyAllowance = new Money();
//			return dailyAllowance;
//		}
//		public Money getFlightAllowance() {
//			if (flightAllowance == null)
//				flightAllowance = new Money();
//			return flightAllowance;
//		}
//		public Money getSundryType1Amount() { 
//			if (sundryType1Amount == null)
//				sundryType1Amount = new Money();
//			return sundryType1Amount;
//		}
//		public String getSundryType1Comments() {
//			return sundryType1Comments;
//		}
//		public void setSundryType1Comments(String sundryType1Comments) {
//			this.sundryType1Comments = sundryType1Comments;
//		}
//		public Money getSundryType2Amount() {
//			if (sundryType2Amount == null)
//				sundryType2Amount = new Money();
//			return sundryType2Amount;
//		}
//		public String getSundryType2Comments() {
//			return sundryType2Comments;
//		}
//		public void setSundryType2Comments(String sundryType2Comments) {
//			this.sundryType2Comments = sundryType2Comments;
//		}
//		
//		
//	}
	
	@Embeddable
	public static class Review {
		private String comments;

		public String getComments() {
			return comments;
		}

		public void setComments(String comments) {
			this.comments = comments;
		}
	}
	
	@Entity
	public static class FlightAndDutyActuals {
		@Id @GeneratedValue
		private Integer id;
		
		@Temporal(TemporalType.DATE)
		private Date date;
		
		@Column(nullable=false)
		private Money monthlyRate;
		private boolean payMonthlyRate;
		
		@Column(nullable=false)
		private Money areaRate;
		
		
		@Column(nullable=false)
		private Money instructorRate;
		
		@Column(nullable=false)
		private Money dailyRate;
		
		
		@Column(nullable=false)
		private Money flightRate;
		
		@Temporal(TemporalType.DATE)
		private Date paidDate;
		private Money paidAmount;
		
		@Temporal(TemporalType.DATE)
		private Date emailDate;
		
		@CollectionOfElements
		private Map<String, CharterEntry> entries = new HashMap<String, CharterEntry>();

		@CollectionOfElements
		private Map<String, Deduction> deductions = new HashMap<String, Deduction>();
		
		@OneToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
		private ApprovalGroup approvalGroup = new ApprovalGroup();
		
		public ApprovalGroup getApprovalGroup() {
			return approvalGroup;
		}
		public Integer getId() {
			return id;
		}
		public Date getDate() {
			return date;
		}
		public Money getMonthlyRate() {
			return monthlyRate;
		}
		public Map<String, CharterEntry> getEntries() {
			return entries;
		}
		public void setEntries(Map<String, CharterEntry> entries) {
			this.entries = entries;
		}
		public Map<String, Deduction> getDeductions() {
			return deductions;
		}
		public void setDeductions(Map<String, Deduction> deductions) {
			this.deductions = deductions;
		}
		public boolean isPayMonthlyRate() {
			return payMonthlyRate;
		}
		public Money getAreaRate() {
			return areaRate;
		}
		public int getAreaDays() {
			int sum =0;
			for (CharterEntry e: entries.values()) {
				sum += e.getAreaDays();
			}
			return sum;
		}
		public Money getInstructorRate() {
			return instructorRate;
		}
		public int getInstructorDays() {
			int sum =0;
			for (CharterEntry e: entries.values()) {
				sum += e.getInstructorDays();
			}
			return sum;
		}
		public Money getDailyRate() {
			return dailyRate;
		}
		public int getDailyDays() {
			int sum =0;
			for (CharterEntry e: entries.values()) {
				sum += e.getDailyDays();
			}
			return sum;
		}
		public Money getFlightRate() {
			return flightRate;
		}
		public int getFlightDays() {
			int sum =0;
			for (CharterEntry e: entries.values()) {
				sum += e.getFlightDays();
			}
			return sum;
		}
		public Money getDeductionTotal() {
			Money sum = new Money("USD",0.0);
			for (Deduction d: deductions.values()) {
				if(d != null){
					sum = sum.add(d.getAmount());
				}
			}
			return sum;
		}
		public Date getPaidDate() {
			return paidDate;
		}
		public Money getPaidAmount() {
			return paidAmount;
		}
		public Date getEmailDate() {
			return emailDate;
		}
		public void setEmailDate(Date date) {
			this.emailDate = date;
		}
		public void setDate(Date date) {
			this.date = date;
		}
		public void setPayMonthlyRate(boolean payMonthlyRate) {
			this.payMonthlyRate = payMonthlyRate;
		}
		public void setPaidDate(Date paidDate) {
			this.paidDate = paidDate;
		}
		public void setPaidAmount(Money paidAmount) {
			this.paidAmount = paidAmount;
		}
		
		public Money getTotal() {
			int monthlyHours = (payMonthlyRate? 1: 0);
			Money total = getMonthlyRate().multiply(monthlyHours);
			total = total.add(getDailyRate().multiply(getDailyDays()));
			total = total.add(getFlightRate().multiply(getFlightDays()));
			total = total.add(getAreaRate().multiply(getAreaDays()));
			total = total.add(getInstructorRate().multiply(getInstructorDays()));
			total = total.subtract(getDeductionTotal());
		
			
			return total;
		}
		
		public FlightAndDutyActuals() {}
		
		public FlightAndDutyActuals(Money monthlyRate, Money areaRate,
				Money instructorRate, Money dailyRate, Money flightRate) {
			super();
			this.monthlyRate = monthlyRate;
			this.areaRate = areaRate;
			this.instructorRate = instructorRate;
			this.dailyRate = dailyRate;
			this.flightRate = flightRate;			
		}
		
		@Embeddable
		public static class Deduction{
			private String reason;
			private Money amount;
			private double exchangeRate;
			private double rand;
			public String getReason() {
				return reason;
			}
			public void setReason(String reason) {
				this.reason = reason;
			}
			public Money getAmount() {
				return amount;
			}
			public void setAmount(Money amount) {
				this.amount = amount;
			}
			public double getExchangeRate() {
				return exchangeRate;
			}
			public void setExchangeRate(double exchangeRate) {
				this.exchangeRate = exchangeRate;
			}
			public double getRand() {
				return rand;
			}
			public String getUSD(){
			 return CurrencyFormatter.getInstance(Currency.getInstance("USD"),false).format(amount.getAmount());
			}
			public void setRand(double rand) {
				this.rand = rand;
			}
			
			public String getRandStr(){
				return CurrencyFormatter.getInstance(Currency.getInstance("ZAR"),false).format(new Double(rand*100).longValue());
			}
			
		}
		
		@Embeddable
		public static class CharterEntry {
			private String charter;
			private String aircraft;
			private int    areaDays;
			private int    instructorDays;
			private int    dailyDays;
			private int    flightDays;
			
			public int getAreaDays() {
				return areaDays;
			}
			public void setAreaDays(int areaDays) {
				this.areaDays = areaDays;
			}
			public int getInstructorDays() {
				return instructorDays;
			}
			public void setInstructorDays(int instructorDays) {
				this.instructorDays = instructorDays;
			}
			public int getDailyDays() {
				return dailyDays;
			}
			public void setDailyDays(int dailyDays) {
				this.dailyDays = dailyDays;
			}
			public int getFlightDays() {
				return flightDays;
			}
			public void setFlightDays(int flightDays) {
				this.flightDays = flightDays;
			}
			public void setAircraft(String aircraft) {
				this.aircraft = aircraft;
			}
			public String getAircraft() {
				return aircraft;
			}
			public void setCharter(String charter) {
				this.charter = charter;
			}
			public String getCharter() {
				return charter;
			}

		}
	}
	
	/*
	 * Getters - No Setters, this top-level class is immutable.
	 * Values are changed on inner classes.
	 */
	
	public Integer getId() {
		return id;
	}

	public Personal getPersonal() {
		if (personal == null)
			personal = new Personal();
		return personal;
	}

	public Banking getBanking() {
		if (banking == null)
			banking = new Banking();
		return banking;
	}

	public Role getRole() {
		if (role == null)
			role = new Role();
		return role;
	}

	public Payments getPayments() {
		if (payments == null)
			payments = new Payments();
		return payments;
	}
	
//	public FlightAndDuty getFlightAndDuty() {
//		if (flightAndDuty == null)
//			flightAndDuty = new FlightAndDuty();
//		return flightAndDuty;
//	}
	
	public Review getReview() {
		if (review == null)
			review = new Review();
		return review;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public List<FlightAndDutyActuals> getFlightAndDutyActuals() {
		if (flightAndDutyActuals == null)
			flightAndDutyActuals = new ArrayList<FlightAndDutyActuals>();
		return flightAndDutyActuals;
	}

	public ApprovalGroup getApprovalGroup() {
		if (approvalGroup == null)
			approvalGroup = new ApprovalGroup();
		return approvalGroup;
	}
    
    public FlightAndDutyActuals getFlightAndDutyActualsForMonth( String month ) {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMM");
        for (FlightAndDutyActuals actual : getFlightAndDutyActuals()) {
            if ( month.equals(df.format(actual.getDate())) ) {
                return actual;
            }
        }
        return new FlightAndDutyActuals(
						getPayments().getMonthlyBaseRate(),
						getPayments().getAreaAllowance(),
						getPayments().getInstructorAllowance(),
						getPayments().getDailyAllowance(),
						getPayments().getFlightAllowance()
				   );
    }
    
    
    
    //JasperReports
    public String title;
	public String type;
    public String category;
    public String startDate;
    public String endDate;
    public String procDate;
    public String paymentType;
    public String paymentDays;
    public String paymentRate;
    public String paymentTotal;
    public String total;
  
    public String getTitle() {
		return title;
	}

	public String getType() {
		return type;
	}

	public String getCategory() {
		return category;
	}

	public String getStartDate() {
		return startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public String getProcDate() {
		return procDate;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public String getPaymentDays() {
		return paymentDays;
	}

	public String getPaymentRate() {
		return paymentRate;
	}

	public String getPaymentTotal() {
		return paymentTotal;
	}

	public String getTotal() {
		return total;
	}



    
    public CrewMember addPayments(String type, String days, String rate, String paytotal, double _total ){
    	
    	if(!days.equals("-")){
    		int totalPaymentDays =  new Integer(this.paymentDays).intValue();
    		totalPaymentDays += new Integer(days).intValue();
    		this.paymentDays  = ""+totalPaymentDays;
    	}
    	
    	Money totalPaymentTotal = new Money(this.paymentTotal);
    	
    	
    	Money newtotalPaymentTotal = totalPaymentTotal.add(new Money(paytotal));
    	
    	Money moneytotal = new Money();
    	moneytotal.setAmountAsDouble(_total);
    	this.total        = moneytotal.toString();
    	
    	this.paymentTotal = newtotalPaymentTotal.toString();
    	
    	
    	//System.out.println("Adding paymenttotal for type("+paymentType+") :"+totalPaymentTotal+" + "+new Money(paytotal).toString()+" = "+ newtotalPaymentTotal.toString()+" ("+ this.paymentTotal+")");
    	//System.out.println("setting total for type("+paymentType+") :"+_total+" ("+this.total+")");
    	return this;
    }
    
    public void setPayments(String _fromDate, String _toDate, String _procDate, Boolean advice, String _category, String _paymentType, String _paymentDays, String _paymentRate, String _paymentTotal, double _total){
    	if(advice){
    	   this.title        = "Pay Advice";
    	   this.type         = "period";
       	   this.paymentRate  = "";
    	}
    	else{
    		this.title        = "Pay Slip";
    		this.type         = "month";
        	this.paymentRate  = _paymentRate;
    	}
    	this.startDate    = _fromDate;
    	this.endDate      = _toDate;
    	this.procDate     = _procDate;
    	this.category     = _category;
    	this.paymentType  = _paymentType;
    	this.paymentDays  = _paymentDays;
    	this.paymentTotal = _paymentTotal;
    	Money moneytotal = new Money();
    	moneytotal.setAmountAsDouble(_total);
    	this.total        = moneytotal.toString();
    }

    
	/*
	 * getPaymentsClones: clone this object for each payment type from either all months up between 
	 * @param dateFrom to @param dateTo or the current month
	 */
    public List<CrewMember> getPaymentsClones(String dateFrom, String dateTo, String date){
    	
    	category = "Contract";
    	try{
    	if("manager".equals( (""+this.role.getPosition()).toLowerCase() )){
    		category = "Monthly";
    	}
    	}
    	catch(Exception e){
    	    //oh well
    		e.printStackTrace();
    	}
    	
    	List<CrewMember> clones = new ArrayList<CrewMember>() ;
    	CrewMember cm = null;

    	SimpleDateFormat mydf = new SimpleDateFormat("dd/MM/yyyy");
    	String today = mydf.format(Calendar.getInstance().getTime());
    	
    	SimpleDateFormat df = new SimpleDateFormat("yyyyMM");
    	
    	Calendar cal = Calendar.getInstance();
    	
    	if(date == null){date = "";}
    	if(!"".equals(date)){
    	
    	try{
    	cal.setTime(mydf.parse(date));
    	}
    	catch(Exception pe){
    		pe.printStackTrace();
    		//do last month
    		cal.add(Calendar.MONTH,-1);
    	}
    	}
    	List<FlightAndDutyActuals> fdaList = new ArrayList<FlightAndDutyActuals>();
    	boolean advice = false;



    	if(dateFrom == null){dateFrom = "";}
    	if(dateTo == null){dateTo = "";}

    	if(!( ("".equals(dateFrom)) && ("".equals(dateTo)) )){  
    		try {	
    			advice = true;
    			Date from = mydf.parse(dateFrom);
    			Date to  = mydf.parse(dateTo);
    			cal.setTime(from);
    			cal.set(Calendar.DAY_OF_MONTH,1);    		
    			dateFrom = mydf.format(cal.getTime());
    			cal.setTime(to);
    			cal.add(Calendar.MONTH,1); 
    			cal.set(Calendar.DAY_OF_MONTH,0); 
    			dateTo   = mydf.format(cal.getTime());
    			from = mydf.parse(dateFrom);
    			to  = mydf.parse(dateTo);

    			for (FlightAndDutyActuals actual : getFlightAndDutyActuals()) {
    				//check actual month is between dateFrom, dateTo
    				if((actual.getDate().after(from) && actual.getDate().before(to)) || (mydf.format(actual.getDate()).equals(dateFrom)) ){
    					//if so then add to fda list
    					fdaList.add(actual);	
    				}
    			}
    		}
    		catch (ParseException e) {
    			e.printStackTrace();
    		}

    	}
    	else {
    		FlightAndDutyActuals fda = getFlightAndDutyActualsForMonth(df.format(cal.getTime()));
    		//only add if has paid date
    		if(fda.getPaidDate() != null){
    			fdaList.add(fda);
    		}
    		//set startDate & endDate
    		cal.set(Calendar.DAY_OF_MONTH,1);    		
    		dateFrom = mydf.format(cal.getTime()); 
    		cal.add(Calendar.MONTH,1); 
    		cal.set(Calendar.DAY_OF_MONTH,0); 
    		
    		dateTo   = mydf.format(cal.getTime());
    	}
    	

    	
    	this.startDate  = dateFrom;
    	this.endDate    = dateTo;
    	this.procDate   = today;
    	Map<String,CrewMember> storedCMs = new TreeMap<String,CrewMember>();
    	double fdatotal = 0.0; 
    	for(FlightAndDutyActuals fda : fdaList){
    		fdatotal += fda.getTotal().getAmountAsDouble();
		try {
			if(fda.isPayMonthlyRate()){
				String type = "Monthly";
				if(storedCMs.containsKey(type)){
					cm = storedCMs.get(type);
					cm = cm.addPayments(type,"-",fda.getMonthlyRate().toString(),fda.getMonthlyRate().toString(),fdatotal);
				}
				else{
					cm = (CrewMember) clone();
					cm.setPayments(dateFrom,dateTo,today,advice,category,type,"-",fda.getMonthlyRate().toString(),fda.getMonthlyRate().toString(),fdatotal);
				}
				storedCMs.put(type, cm);
				//clones.add(cm);
			}			
			if(fda.getAreaDays() != 0){
				String type = "Area";
				if(storedCMs.containsKey(type)){
					cm = storedCMs.get(type);
					cm = cm.addPayments(type,""+fda.getAreaDays(),fda.getAreaRate().toString(),fda.getAreaRate().multiply(fda.getAreaDays()).toString(),fdatotal);
				}
				else{
					cm = (CrewMember) clone();
					cm.setPayments(dateFrom,dateTo,today,advice,category,type,""+fda.getAreaDays(),fda.getAreaRate().toString(),fda.getAreaRate().multiply(fda.getAreaDays()).toString(),fdatotal);
				}
				storedCMs.put(type, cm);
				//clones.add(cm);
			}
			if(fda.getDailyDays() != 0){
				String type = "Daily";
				if(storedCMs.containsKey(type)){
					cm = storedCMs.get(type);
					cm = cm.addPayments(type,""+fda.getDailyDays(),fda.getDailyRate().toString(),fda.getDailyRate().multiply(fda.getDailyDays()).toString(),fdatotal);
				}
				else{
					cm = (CrewMember) clone();
					cm.setPayments(dateFrom,dateTo,today,advice,category,type,""+fda.getDailyDays(),fda.getDailyRate().toString(),fda.getDailyRate().multiply(fda.getDailyDays()).toString(),fdatotal);
				}
				storedCMs.put(type, cm);
				//clones.add(cm);
			}
			if(fda.getInstructorDays() != 0){
				String type = "Instructor";
				if(storedCMs.containsKey(type)){
					cm = storedCMs.get(type);
					cm = cm.addPayments(type,""+fda.getInstructorDays(),fda.getInstructorRate().toString(),fda.getInstructorRate().multiply(fda.getInstructorDays()).toString(),fdatotal);
				}
				else{
					cm = (CrewMember) clone();
					cm.setPayments(dateFrom,dateTo,today,advice,category,type,""+fda.getInstructorDays(),fda.getInstructorRate().toString(),fda.getInstructorRate().multiply(fda.getInstructorDays()).toString(),fdatotal);
				}
				storedCMs.put(type, cm);
				//clones.add(cm);
			}
			if(fda.getFlightDays() != 0){
				String type = "Flight";
				if(storedCMs.containsKey(type)){
					cm = storedCMs.get(type);
					cm = cm.addPayments(type,""+fda.getFlightDays(),fda.getFlightRate().toString(),fda.getFlightRate().multiply(fda.getFlightDays()).toString(),fdatotal);
				}
				else{
					cm = (CrewMember) clone();
					cm.setPayments(dateFrom,dateTo,today,advice,category,type,""+fda.getFlightDays(),fda.getFlightRate().toString(),fda.getFlightRate().multiply(fda.getFlightDays()).toString(),fdatotal);
				}
				storedCMs.put(type, cm);
			}
			if(fda.getDeductions().size() != 0){
				
				for(String key: fda.getDeductions().keySet()){
					Deduction d = fda.getDeductions().get(key);
				String type = ""+d.getReason();
				if(storedCMs.containsKey(type)){
					cm = storedCMs.get("Z"+type);
					cm = cm.addPayments(type,"1",d.getAmount().toString(),d.getAmount().toString(),fdatotal);
				}
				else{
					cm = (CrewMember) clone();
					cm.setPayments(dateFrom,dateTo,today,advice,"Deduction",type,"1",d.getAmount().toString(),d.getAmount().toString(),fdatotal);
				}
				storedCMs.put("Z"+type, cm);
				//clones.add(cm);
				}
			}
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
    	}
    	
    	clones = new ArrayList<CrewMember>(storedCMs.values());
    	
    	return clones;
    }
    

	public Object clone() throws CloneNotSupportedException {
    	try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			throw(e);
		}
    }
    
}
