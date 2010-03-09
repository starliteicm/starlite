package com.itao.starlite.manager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.joda.time.DateMidnight;

import com.google.inject.Inject;
import com.itao.starlite.auth.User;
import com.itao.starlite.auth.manager.AuthManager;
import com.itao.starlite.dao.ActualsDao;
import com.itao.starlite.dao.AircraftDao;
import com.itao.starlite.dao.AircraftTypeDao;
import com.itao.starlite.dao.CharterDao;
import com.itao.starlite.dao.ComponentDao;
import com.itao.starlite.dao.CrewDao;
import com.itao.starlite.dao.CrewDayDao;
import com.itao.starlite.dao.ExchangeDao;
import com.itao.starlite.dao.FlightAndDutyActualsDao;
import com.itao.starlite.dao.StoreDao;
import com.itao.starlite.docs.manager.DocumentManager;
import com.itao.starlite.docs.model.Folder;
import com.itao.starlite.exceptions.CannotCreateCrewMemberException;
import com.itao.starlite.exceptions.ExistingRecordException;
import com.itao.starlite.model.Actuals;
import com.itao.starlite.model.Aircraft;
import com.itao.starlite.model.AircraftList;
import com.itao.starlite.model.AircraftType;
import com.itao.starlite.model.ApprovalGroup;
import com.itao.starlite.model.ApprovalStatus;
import com.itao.starlite.model.Charter;
import com.itao.starlite.model.CharterList;
import com.itao.starlite.model.CombinedActuals;
import com.itao.starlite.model.Component;
import com.itao.starlite.model.CrewDay;
import com.itao.starlite.model.CrewMember;
import com.itao.starlite.model.ExchangeRate;
import com.itao.starlite.model.Store;
import com.itao.starlite.model.CrewMember.FlightAndDutyActuals;
import com.itao.starlite.scheduling.model.Assignable;
import com.wideplay.warp.persist.Transactional;

public class StarliteCoreManager {
	@Inject private CharterDao charterDao;
	@Inject private CrewDao crewDao;
	@Inject private AircraftDao aircraftDao;
	@Inject private ActualsDao actualsDao;
	@Inject private CrewDayDao crewDayDao;
	@Inject private StoreDao storeDao;
	@Inject private ComponentDao componentDao;
	@Inject private FlightAndDutyActualsDao flightAndDutyActualsDao;
	@Inject private AircraftTypeDao aircraftTypeDao;
	@Inject private ExchangeDao exDao;
	
	@Inject private AuthManager authManager;
	@Inject private DocumentManager docManager;
	
	
	
	@Transactional
	public Charter createCharter(String client, String description) {
		Charter c = new Charter();
		c.setClient(client);
		c.setDescription(description);
		charterDao.makePersistent(c);
		return c;
	}
	
	@Transactional
	public Charter getCharter(Integer id) {
		return charterDao.findById(id);
	}
	
	@Transactional
	public Charter getCharterByCode(String code) {
		return charterDao.findByCode(code);
	}
	
	@Transactional
	public CharterList getUnscheduledCharters() {
		
		return null;
	}
	
	@Transactional
	public CharterList getAllCharters() {
		List<Charter> charters = charterDao.findAllOrdererdByStartDate();
		CharterList cl = new CharterList();
		cl.charterList.addAll(charters);
		return cl;
	}
	
	@Transactional
	public CharterList getAllChartersBetween(DateMidnight startDate, DateMidnight endDate) {
		List<Charter> charters = charterDao.findAllBetween(startDate, endDate);
		CharterList cl = new CharterList();
		for (Charter c: charters) {
			cl.charterList.add(c);
		}
		return cl;
	}
	
	@Transactional
	public CharterList getPresentAndFutureCharters(int offset, int limit) {
		List<Charter> charters = charterDao.findAllPresentAndFuture(offset, limit);
		CharterList cl = new CharterList();
		for (Charter c: charters) {
			cl.charterList.add(c);
		}
		return cl;
	}
	
	@Transactional
	public Charter saveCharter(Charter c) {
		return charterDao.makePersistent(c);
	}
	
	@Transactional
	public void deleteCharter(Integer id) {
		Charter c = charterDao.findById(id);
		if (c != null) {
			charterDao.makeTransient(c);
		}
	}
	
	@Transactional
	public ExchangeRate getExchangeRateByCode(String fromCode, String toCode) {
		return exDao.findExchangeByCodes(fromCode, toCode);
	}
	
	@Transactional
	public ExchangeRate saveExchange(ExchangeRate e) {
		return exDao.makePersistent(e);
	}
	
	@Transactional
	public CrewMember getCrewMember(Integer id) {
		return crewDao.findById(id);
	}
	
	@Transactional
	public CrewMember getCrewMemberByCode(String code) {
		return crewDao.findCrewMemberByCode(code);
	}
	
	@Transactional
	public List<CrewMember> getAllCrew() {
		return crewDao.findAll();
	}
	
	@Transactional
	public List<CrewMember> getAllCrewReadOnly() {
		return crewDao.findAllCrewReadOnly();
	}

	
	@Transactional
	public List<CrewMember> getAllCrewPlus() {
		List<CrewMember> crewplusList = crewDao.findAll();
		return crewplusList;
	}
	
	
	@Transactional
	public List<CrewMember> getCrewMembersByCodes(String codes) {
		return crewDao.findCrewMembersByCodes(codes);
	}
	
	@Transactional
	public CrewMember saveCrewMember(CrewMember cm) {
		if (cm.getApprovalGroup().getApprovalStatus().equals(ApprovalStatus.OPEN_FOR_EDITING))
			return crewDao.makePersistent(cm);
		throw new IllegalStateException("Crew Member is locked.");
	}
	
	@Transactional
	public Object[] createCrewMember(String title, String firstName, String lastName, User u) throws CannotCreateCrewMemberException {
		try {
			CrewMember cm = crewDao.createCrewMember(title, firstName, lastName);
			String password = authManager.generateRandomPassword();
			User user = authManager.createUserWithRoles(cm.getCode(), password, "Crew");
			
			Folder folder = docManager.createFolder("/crew/"+cm.getCode(), u);
			folder.setOwner(user);
			
			folder.setReadPermission("ReadAllCrewDocs");
			folder.setWritePermission("WriteAllCrewDocs");
			
			return new Object[] {cm, password};
		} catch (Throwable t) {
			throw new CannotCreateCrewMemberException(t);
		}
	}
	
	@Transactional
	public void deleteCrewMember(Integer id) {
		CrewMember cm = crewDao.findById(id);
		if (cm != null)
			crewDao.makeTransient(cm);
	}
	
	@Transactional
	public void addCrewFlightAndDutyActuals(String crewCode, FlightAndDutyActuals actuals) throws ExistingRecordException {
		CrewMember cm = crewDao.findCrewMemberByCode(crewCode);
		for (FlightAndDutyActuals a: cm.getFlightAndDutyActuals()) {
			if (a.getDate().getMonth() == actuals.getDate().getMonth()
			 && a.getDate().getYear() == actuals.getDate().getYear()
			) {
				throw new ExistingRecordException("Actuals already exist for this crew member, charter and period");
			}
		}
		//We won't save if everything is 0
		if (actuals.getTotal().getAmount() != 0 || actuals.getPaidAmount().getAmount() != 0)
			cm.getFlightAndDutyActuals().add(actuals);
	}
	
	@Transactional
	public Aircraft getAircraft(Integer id) {
		return aircraftDao.findById(id);
	}
	
	@Transactional
	public Aircraft getAircraftByReg(String reg) {
		return aircraftDao.findByReg(reg);
	}
	
	@Transactional
	public Map<String, Aircraft> getAircraftsById(List<String> ids) {
		Map<String, Aircraft> aircraftMap = new HashMap<String, Aircraft>();
		for (String id: ids) {
			Aircraft a = getAircraft(Integer.parseInt(id));
			if (a != null)
				aircraftMap.put(id, a);
		}
		return aircraftMap;
	}
	
	@Transactional
	public AircraftList getAllAircraft() {
		List<Aircraft> aircraft = aircraftDao.findAll();
		AircraftList al = new AircraftList();
		al.aircraftList.addAll(aircraft);
		return al;
	}
	
	@Transactional
	public Aircraft saveAircraft(Aircraft a) {
		return aircraftDao.makePersistent(a);
	}
	
	@Transactional
	public void deleteAircraft(Integer id) {
		Aircraft a = aircraftDao.findById(id);
		if (a != null)
			aircraftDao.makeTransient(a);
	}
	
	/**
	 * 
	 * @param aircraftId
	 * @param month The month number: January = 1, February = 2 etc.
	 * @param year The full year, e.g. 2008 
	 */
	@Transactional
	public List<Actuals> getActualsByAircraftByMonth(Integer aircraftId, Integer month, Integer year) {
		Aircraft a = getAircraft(aircraftId);
		if (a == null)
			return new ArrayList<Actuals>();
		return actualsDao.getActualsByAircraftByMonth(a, month, year);
	}
	
	@Transactional
	public List<Actuals> getActualsByCharterByMonth(Integer charterId, Integer month, Integer year) {
		Charter c = getCharter(charterId);
		if (c == null)
			return new ArrayList<Actuals>();
		return actualsDao.getActualsByCharterByMonth(c, month, year);
	}
	
	@Transactional
	public Map<String, Actuals> getActualsByIds(List<String> ids) {
		Map<String, Actuals> actualsMap = new HashMap<String, Actuals>();
		for (String id: ids) {
			Actuals a = actualsDao.findById(Integer.parseInt(id));
			if (a != null) {
				actualsMap.put(id, a);
			}
		}
		return actualsMap;
	}
	
	@Transactional
	public void saveActuals(Actuals a) {
		DateMidnight dm = new DateMidnight(a.getDate());
		ApprovalGroup ag = getActualsApprovalGroup(a.getAircraft().getId(), dm.getYear(), dm.getMonthOfYear());
		if (ag != null && !ag.getApprovalStatus().equals(ApprovalStatus.OPEN_FOR_EDITING))
			throw new IllegalStateException("Actuals are locked");
		actualsDao.makePersistent(a);
	}
	
	@Transactional
	public boolean hasActualsForMonth(Integer aircraftId, Integer month, Integer year) {
		Aircraft a = getAircraft(aircraftId);
		if (a == null)
			return false;
		return actualsDao.hasActualsForMonth(a, month, year);
	}
	
	@Transactional
	public List<AircraftType> getTotalActualsByAircraftType(){
		List<AircraftType> allTypes = getAircraftTypes();
		for(AircraftType at : allTypes){
			List<CombinedActuals> vals = getTotalActualsByAircraftType(at);
			at.setActualList(vals);
		}
		return allTypes;
	}
	
	@Transactional
	public List<CombinedActuals> getTotalActualsByAircraftType(AircraftType aircraftType) {
		
		List<CombinedActuals> vals = new ArrayList<CombinedActuals>();

			List<Actuals> actuals = actualsDao.getActualsByAircraftType(aircraftType);
			CombinedActuals ca = new CombinedActuals("total",actuals);
			vals.add(ca);
			
		Collections.reverse(vals);
		return vals;
	}
	
	@Transactional
	public List<CombinedActuals> getTotalActualsByAircraft(Integer aircraftId, Integer startMonth, Integer startYear, Integer endMonth, Integer endYear) {
		Integer currentMonth = startMonth;
		Integer currentYear = startYear;
		
		List<CombinedActuals> vals = new ArrayList<CombinedActuals>();
		
		DateMidnight current = new DateMidnight(startYear, startMonth, 1);
		DateMidnight end = new DateMidnight(endYear, endMonth, 1);
		while (current.compareTo(end) <= 0) {
			List<Actuals> actuals = getActualsByAircraftByMonth(aircraftId, current.getMonthOfYear(), current.getYear());
			CombinedActuals ca = new CombinedActuals(getMonthYearRep(current), actuals);
			ca.setParams("aircraftId="+aircraftId+"&month="+current.getMonthOfYear()+"&year="+current.getYear());
			vals.add(ca);
			current = current.plusMonths(1);
		}
		Collections.reverse(vals);
		return vals;
	}
	
	@Transactional
	public List<CombinedActuals> getTotalActualsByCharter(Integer charterId, Integer startMonth, Integer startYear, Integer endMonth, Integer endYear) {
		Integer currentMonth = startMonth;
		Integer currentYear = startYear;
		
		List<CombinedActuals> vals = new ArrayList<CombinedActuals>();
		
		DateMidnight current = new DateMidnight(startYear, startMonth, 1);
		DateMidnight end = new DateMidnight(endYear, endMonth, 1);
		while (current.compareTo(end) <= 0) {
			List<Actuals> actuals = getActualsByCharterByMonth(charterId, current.getMonthOfYear(), current.getYear());
			CombinedActuals ca = new CombinedActuals(getMonthYearRep(current), actuals);
			ca.setParams("charterId="+charterId+"&month="+current.getMonthOfYear()+"&year="+current.getYear());
			vals.add(ca);
			current = current.plusMonths(1);
		}
		Collections.reverse(vals);
		return vals;
	}
	
	@Transactional
	public void deleteActuals(Actuals a) {
		actualsDao.makeTransient(a);
	}
	@Transactional
	public Double getMonthlyInvoicedHours(Charter c, int year, int month) {
		DateMidnight monthDM = new DateMidnight(year, month, 1);
		return charterDao.getInvoicedHours(c, monthDM.toDate());
	}
	@Transactional
	public void setMonthlyInvoicedHours(Charter c, int year, int month, Double hours) {
		DateMidnight monthDM = new DateMidnight(year, month, 1);
		charterDao.setInvoicedHours(c, monthDM.toDate(), hours);
	}
	
	private String getMonthYearRep(DateMidnight d) {
		return getMonth(d.getMonthOfYear()) + ", " + d.getYear();
	}
	
	private String getMonth(Integer i) {
		switch (i) {
		case 1:	return "January";
		case 2:	return "Febuary";
		case 3:	return "March";
		case 4:	return "April";
		case 5:	return "May";
		case 6:	return "June";
		case 7:	return "July";
		case 8:	return "August";
		case 9:	return "September";
		case 10:	return "October";
		case 11:	return "November";
		case 12:	return "December";
		}
		return "";
	}
	
	@Transactional
	public ApprovalGroup getActualsApprovalGroup(Integer aircraftId, Integer year, Integer month) {
		return actualsDao.getActualsApprovalGroup(aircraftId, year, month);
	}
	
	@Transactional
	public void approveActuals(Integer aircraftId, Integer year, Integer month) {
		actualsDao.approveActuals(aircraftId, year, month);
	}
	
	@Transactional
	public Assignable getAssignableByNameOrReg(String nameOrReg) {
		CrewMember cm = crewDao.findByName(nameOrReg);
		if (cm == null) {
			Aircraft a = aircraftDao.findByReg(nameOrReg);
			if (a == null)
				return null;
			Assignable ass = new Assignable();
			ass.id = a.getId();
			ass.type = "Aircraft";
			ass.label = a.getRef();
			return ass;
		}
		Assignable ass = new Assignable();
		ass.id = cm.getId();
		ass.type = "CrewMember";
		ass.label = cm.getPersonal().getFirstName() + " " + cm.getPersonal().getLastName();
		return ass;
	}
	
	@Transactional
	public FlightAndDutyActuals getFlightAndDutyActualsById(Integer id) {
		return flightAndDutyActualsDao.findById(id);
	}
	
	@Transactional
	public void saveFlightAndDutyActuals(FlightAndDutyActuals actuals) {
		if (actuals.getTotal().getAmount() == 0 && actuals.getPaidAmount().getAmount() == 0) {
			if (actuals.getId() != null)
				flightAndDutyActualsDao.makeTransient(actuals);
		} else {
			flightAndDutyActualsDao.makePersistent(actuals);
		}
	}


	
	@Transactional
	public List<AircraftType> getAircraftTypes() {
		return aircraftTypeDao.findAll();
	}
	
	@Transactional
	public void saveAircraftType(AircraftType a){
		aircraftTypeDao.makePersistent(a);
	}

	public AircraftType getAircraftType(Integer typeId) {
		return aircraftTypeDao.findById(typeId);
	}
	
	public List<Aircraft> getAircraftByType(String typeName){
		return aircraftDao.findByType(typeName);
	}

	public List<CrewMember> getAdHocCrew() {
		List<CrewMember> all = getAllCrew();
		List<CrewMember> adhoc = new ArrayList<CrewMember>();
		for(CrewMember c : all){
			if("Freelance".equals(c.getRole().getEmployment())){
				adhoc.add(c);
			}
		}
		return adhoc;
	}

	public List<CrewMember> getPermCrew() {
		List<CrewMember> all = getAllCrew();
		List<CrewMember> perm = new ArrayList<CrewMember>();
		for(CrewMember c : all){
			if(!"Freelance".equals(c.getRole().getEmployment())){
				perm.add(c);
			}
		}
		return perm;
	}
	
	
	@Transactional
	public List<CrewDay> getCrewDayByCrewMemberByMonth(Integer cId, Integer month, Integer year){
		return crewDayDao.getCrewDayByCrewMemberByMonth(cId, month, year);
	}
	
	@Transactional
	public void saveCrewDay(CrewDay c){
		crewDayDao.makePersistent(c);
	}

	public CrewDay getCrewDay(Date date, CrewMember crewMember) {
		return crewDayDao.getCrewDay(date, crewMember);
	}

	public List<CrewDay> getCrewDayByCharterBetween(Integer id, Date dateFrom,Date dateTo) {
		return crewDayDao.getCrewDayByCharterBetween(id,dateFrom,dateTo);
	}

	public List<ExchangeRate> getExchangeRates() {
		return exDao.findAll();
	}

	public List<CrewDay> getCrewDayByCrewMemberBetween(Integer id,Date dateFrom, Date dateTo) {
		return crewDayDao.getCrewDayByCrewMemberBetween(id,dateFrom,dateTo);

	}

	@SuppressWarnings("unchecked")
	public TreeMap getSumCrewDays() {
		Calendar cal = Calendar.getInstance();
		Date toDate = cal.getTime();
		cal.add(Calendar.YEAR, -1);
		Date fromDate = cal.getTime();
		return crewDayDao.getSumCrewDays(fromDate, toDate);
	}
	
	@SuppressWarnings("unchecked")
	public TreeMap getSumCrewDays(String fromDateStr , String toDateStr) {
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		try {
			Date fromDate = df.parse(fromDateStr);
			Date toDate = df.parse(toDateStr);
			return crewDayDao.getSumCrewDays(fromDate, toDate);
			
		} catch (ParseException e) {
			return getSumCrewDays();
		}
	}

	//Stores
	public List<Store> getStores(){
		return storeDao.findAll();
	}
	
	public Store getStore(Integer id){
		return storeDao.findById(id);
	}
	
	public void saveStore(Store store) {
		storeDao.makePersistent(store);
	}
	
	//Components
	public List<Component> getComponents(){
		return componentDao.findAll();
	}
	
	public Component getComponent(Integer id){
		return componentDao.findById(id);
	}
	
	public void saveComponent(Component component) {
		componentDao.makePersistent(component);
	}

}
