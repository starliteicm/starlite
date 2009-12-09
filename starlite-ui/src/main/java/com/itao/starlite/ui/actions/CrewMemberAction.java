package com.itao.starlite.ui.actions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletRedirectResult;
import org.apache.struts2.dispatcher.StreamResult;
import org.jfree.util.Log;
import org.joda.time.DateMidnight;

import com.google.inject.Inject;
import com.itao.jmesa.dsl.entities.Table;
import com.itao.starlite.auth.User;
import com.itao.starlite.auth.UserAware;
import com.itao.starlite.auth.annotations.Permissions;
import com.itao.starlite.docs.manager.BookmarkManager;
import com.itao.starlite.docs.manager.DocumentManager;
import com.itao.starlite.docs.model.Bookmark;
import com.itao.starlite.docs.model.Document;
import com.itao.starlite.docs.model.Folder;
import com.itao.starlite.docs.model.Tag;
import com.itao.starlite.exceptions.ExistingRecordException;
import com.itao.starlite.manager.ApprovalsManager;
import com.itao.starlite.manager.StarliteCoreManager;
import com.itao.starlite.model.Aircraft;
import com.itao.starlite.model.AircraftType;
import com.itao.starlite.model.ApprovalStatus;
import com.itao.starlite.model.Charter;
import com.itao.starlite.model.CrewDay;
import com.itao.starlite.model.CrewMember;
import com.itao.starlite.model.ExchangeRate;
import com.itao.starlite.model.Money;
import com.itao.starlite.model.CrewMember.FlightAndDutyActuals.CharterEntry;
import com.itao.starlite.model.CrewMember.FlightAndDutyActuals.Deduction;
import com.itao.starlite.ui.Breadcrumb;
import com.itao.starlite.ui.Tab;
import com.itao.starlite.ui.jmesa.YesNoCellEditor;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;



@SuppressWarnings("serial")
@ParentPackage("prepare")
@Results({
    @Result(name="unauthorised", type=ServletRedirectResult.class, value="unauthorised.html"),
    @Result(name="redirect-hours", type=ServletRedirectResult.class, value="crewMember.action?id=${id}&tab=hours&notificationMessage=Saved"),
    @Result(name="photo", type=StreamResult.class, value="photo", params={"inputName","photo","contentType","image","contentDisposition","inline"} ),
    @Result(name="redirect-addFlightActuals", type=ServletRedirectResult.class, value="crewMember!addFlightActuals.action?id=${id}&tab=flight&actualsId=${actuals.id}&errorMessage=${errorMessage}&notificationMessage=${notificationMessage}")
})
@Permissions("ManagerView || OwnDetails")
public class CrewMemberAction extends ActionSupport implements Preparable, UserAware {
	public CrewMember crewMember;
	public List<AircraftType> aircraftTypes;
	public String id;	
	public String current="crew";
	public Breadcrumb[] breadcrumbs;
	public Tab[] tableTabs;
	public String tab = "personal";
	public String tagArray = "[]";
	private User user;
	
	public String docfolder;
	public File document;
	public String documentContentType;
	public String documentFileName;
	public String tags;
	
	public String currency;
	public List<ExchangeRate> rates;
	
	public List<String> passportsTags;
	public List<File> passports;
	public List<String> passportsContentType;
	public List<String> passportsFileName;
	
	public List<String> passportsId;
	public List<String> passportsCountry;
	public List<String> passportsNumber;
	public List<String> passportsExpiryDate;
	
	
	public SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	public SimpleDateFormat mysqlFormat = new SimpleDateFormat("yyyy-MM-dd");
	public String dateFrom;
	public String dateTo;
	public String activity;
	public String chart;
	public String tail;
	
	public File   crmFile;
	public String crmFileContentType;
    public String crmFileFileName;
	public String crmTags;
	
    public File   dgFile;
	public String dgFileContentType;
    public String dgFileFileName;
    public String dgTags;
    
    public File   huetFile;
	public String huetFileContentType;
    public String huetFileFileName;
    public String huetTags;
    
    @SuppressWarnings("unchecked")
	public TreeMap<String, TreeMap> months;
	public String hoursMonth;

	@Inject
	private StarliteCoreManager manager;
	@Inject
	private DocumentManager docManager;
	@Inject
	private BookmarkManager bookmarkManager;
	@Inject
	private ApprovalsManager approvalsManager;

	public boolean readOnly = false;
	public boolean notAuthorised = false;

	public int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
	public int currentYear = Calendar.getInstance().get(Calendar.YEAR);
	
	@Override
	public String execute() throws Exception {
		//crewMember = manager.getCrewMember(id);
		if (!user.hasPermission("ManagerView") && !user.getUsername().equalsIgnoreCase(id))
			return "unauthorised";
		breadcrumbs = Breadcrumb.toArray(
			new Breadcrumb("Crew", "crew.action"),
			new Breadcrumb(crewMember.getPersonal().getFirstName() + " " + crewMember.getPersonal().getLastName())
		);

		prepareTabs();

		if (!crewMember.getApprovalGroup().getApprovalStatus().equals(ApprovalStatus.OPEN_FOR_EDITING)
				&& (tab.equals("personal") || tab.equals("banking") || tab.equals("role") || tab.equals("payments"))
			) {
				readOnly = true;
			}

		if (tab.equals("personal")){
			return SUCCESS;
		}

		if (tab.toLowerCase().equals("review") && !user.hasPermission("ManagerEdit"))
			notAuthorised = true;

		if (tab.toLowerCase().equals("payments") && !user.hasPermission("ManagerEdit"))
			notAuthorised = true;

		if (tab.toLowerCase().equals("flight") && !user.hasPermission("ManagerEdit"))
			notAuthorised = true;

		if (!user.hasPermission("ManagerEdit")) {
			readOnly = true;
		}

		if (user.getUsername().equalsIgnoreCase(id)) {
			if (tab.equals("payments") || tab.equals("flight"))
				readOnly = true;
		}

		if (tab.equals("flight"))
			return setupFlight();
		
		if (tab.equals("hours"))
			return setupHours();
		
		if(tab.equals("role"))
			aircraftTypes = manager.getAircraftTypes();
		
		return tab;
	}

	public String profile() throws Exception{
		prepare();
		LOG.info(crewMember.getId());
		return "profile";
	}
	
	public String photo(){
		return "photo";
	}
	
	public String crmFile(){
		return "crmFile";
	}
	
	public String dgFile(){
		return "dgFile";
	}
	
	public String huetFile(){
		return "huetFile";
	}
	
	
	public InputStream getPhoto(){
	  try{
	    folder = docManager.getFolderByPath("/crew/"+id, user);
	    LOG.info(folder.getDocs());
	    Document photo = folder.getDocumentByTag("photo");
	    LOG.info("Name:"+photo.getName());
	    LOG.info("Uuid:"+photo.getUuid());
	    return (InputStream) docManager.getDocumentData(photo);
	  }
	  catch(Exception e){
		  LOG.error(e);
		  LOG.error(ServletActionContext.getServletContext().getRealPath("/images/icons/user.png"));
		  File def = new File(ServletActionContext.getServletContext().getRealPath("/images/icons/user.png"));
		  try {
			return new FileInputStream(def);
		} catch (FileNotFoundException e1) {
			LOG.error(e1);
			return null;
		}
	  }
	}
	
	public InputStream getCrmFile(){
		  try{
		    folder = docManager.getFolderByPath("/crew/"+id, user);
		    LOG.info(folder.getDocs());
		    Document crmFile = folder.getDocumentByTag("CRM");
		    LOG.info("Name:"+crmFile.getName());
		    LOG.info("Uuid:"+crmFile.getUuid());		    
		    return (InputStream) docManager.getDocumentData(crmFile);
		  }
		  catch(Exception e){
			  LOG.error(e);			  			 
		  }
		  return null;
	}

	public InputStream getDgFile(){
		  try{
		    folder = docManager.getFolderByPath("/crew/"+id, user);
		    LOG.info(folder.getDocs());
		    Document dgFile = folder.getDocumentByTag("DG");
		    LOG.info("Name:"+dgFile.getName());
		    LOG.info("Uuid:"+dgFile.getUuid());		    
		    return (InputStream) docManager.getDocumentData(dgFile);
		  }
		  catch(Exception e){
			  LOG.error(e);			  			 
		  }
		  return null;
	}
	
	public InputStream getHuetFile(){
		  try{
		    folder = docManager.getFolderByPath("/crew/"+id, user);
		    LOG.info(folder.getDocs());
		    Document huetFile = folder.getDocumentByTag("HUET");
		    LOG.info("Name:"+huetFile.getName());
		    LOG.info("Uuid:"+huetFile.getUuid());		    
		    return (InputStream) docManager.getDocumentData(huetFile);
		  }
		  catch(Exception e){
			  LOG.error(e);			  			 
		  }
		  return null;
	}	
	
	public String tableHtml;
	private String setupFlight() {
		if (crewMember.getFlightAndDutyActuals().isEmpty()) {
			tableHtml = "No Records Found";
		}
		else {
			tableHtml = Table.create("crewFlightHoursTable").of(crewMember.getFlightAndDutyActuals())
				.withColumns()
				.column("date").asDate("MMMM, yyyy")
					.link("crewMember!addFlightActuals.action?id="+id+"&tab=flight&actualsId=${id}")
				.column("monthlyRate").withStyle("text-align:right")
				.column("payMonthlyRate").as(YesNoCellEditor.class.getName())
				.column("areaRate").withStyle("text-align:right").called("Daily")
				.column("areaDays").called("Days")
				.column("discomfortTotal").called("Discomfort")
				.column("dailyRate").withStyle("text-align:right").called("Training")
				.column("dailyDays").called("Days")
				.column("instructorRate").withStyle("text-align:right")
				.column("instructorDays").called("Days")
				.column("flightRate").withStyle("text-align:right").called("Travel")
				.column("flightDays").called("Days")
				.column("deductionTotal").called("Deductions")
				.column("total").called("Total Due").withStyle("text-align:right")
				.column("paidDate").called("Date Paid").asDate("dd/MM/yyyy")
				.column("paidAmount").called("Amount Paid").withStyle("text-align:right")
				.render();

		}
		return "flight";
	}
	
	
	public String saveRange() throws Exception{
		prepare();
		Aircraft aircraft =  null;
		Charter charter = null;
		
		if(tail != null){
			if(tail != ""){
				aircraft = manager.getAircraft(new Integer(tail));
			}
		}
		if(chart != null){
			if(chart != ""){
				charter  = manager.getCharter(new Integer(chart));
			}
		}
		
		try {
			Date from = df.parse(dateFrom);
			Date to   = df.parse(dateTo);
			Calendar cal = Calendar.getInstance();
		    cal.setTime(from);
			
			if(from.before(to)){
				while(cal.getTime().before(to)){
					
					String date = mysqlFormat.format(cal.getTime());
					CrewDay cd = null;
					cd = manager.getCrewDay(cal.getTime(),crewMember);
					if(cd == null){
						cd = new CrewDay(null,date,activity,null,null,null,null,aircraft,charter,crewMember,null,null,null,null);
					}
					else {
						cd.setActivity(activity);
						cd.setAircraft(aircraft);
						cd.setCharter(charter);
					}
					manager.saveCrewDay(cd);
					cal.add(Calendar.DAY_OF_MONTH,1);
				}
			}	
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "redirect-hours";
	}
	
	
	public String saveHours() throws Exception{
		prepare();
		for(int i = 1; i < 32; i++){
			
			String day = ""+i;
			if(i < 10){
				day = "0"+day;
			}
			
			String cDId        =  ServletActionContext.getRequest().getParameter(hoursMonth+"-"+day+"_id");
			String activity    =  ServletActionContext.getRequest().getParameter(hoursMonth+"-"+day+"_activity");
			String comment     =  ServletActionContext.getRequest().getParameter(hoursMonth+"-"+day+"_comment");
			String type        =  ServletActionContext.getRequest().getParameter(hoursMonth+"-"+day+"_type");
			String position    =  ServletActionContext.getRequest().getParameter(hoursMonth+"-"+day+"_position");
			String instruments =  ServletActionContext.getRequest().getParameter(hoursMonth+"-"+day+"_instruments");
			String tail        =  ServletActionContext.getRequest().getParameter(hoursMonth+"-"+day+"_tail");
			String chart       =  ServletActionContext.getRequest().getParameter(hoursMonth+"-"+day+"_charter");
			String flown       =  ServletActionContext.getRequest().getParameter(hoursMonth+"-"+day+"_flown");
			String timein      =  ServletActionContext.getRequest().getParameter(hoursMonth+"-"+day+"_timein");
			String timeout     =  ServletActionContext.getRequest().getParameter(hoursMonth+"-"+day+"_timeout");
			String hours       =  ServletActionContext.getRequest().getParameter(hoursMonth+"-"+day+"_hours");
			
			LOG.info(hoursMonth+"-"+day+"|"+cDId+"|"+activity+"|"+comment+"|"+type+"|"+position+"|"+instruments+"|"+tail+"|"+chart+"|"+flown+"|"+timein+"|"+timeout+"|"+hours);
			
			Aircraft aircraft = null;
			Charter charter = null;
			
			Integer crewDayId = null;
			if (cDId != null) {
				if(cDId != ""){
					crewDayId = new Integer(cDId);
				}
			}
			
			if(activity != null){
			if(activity != ""){
				if(tail != null){
					if(tail != ""){
						aircraft = manager.getAircraft(new Integer(tail));
					}
				}
				if(chart != null){
					if(chart != ""){
						charter  = manager.getCharter(new Integer(chart));
					}
				}
				Double flownHours = new Double(0.0);
				if(flown != null){
					if(flown != ""){
						flownHours = new Double(flown);
					}
				}
				
				manager.saveCrewDay(new CrewDay(crewDayId,hoursMonth+"-"+day,activity,comment,type,position,instruments,aircraft,charter,crewMember,flownHours,timein,timeout,hours));
			}
		    }
			
		}
		
		return "redirect-hours";
	}
	
	@SuppressWarnings("unchecked")
	private String setupHours(){
		
		String start = "2009-01";
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, 1);
		
		months = new TreeMap<String,TreeMap>(Collections.reverseOrder());
		allCharters=manager.getAllCharters().charterList;
		allAircraft=manager.getAllAircraft().aircraftList;
		
		SimpleDateFormat monthformat = new SimpleDateFormat("yyyy-MM");
		SimpleDateFormat dayformat = new SimpleDateFormat("dd");
		String nowMonth = monthformat.format(cal.getTime());
		
		while(!start.equals(nowMonth)){
			
			//LOG.info(nowMonth);
			
			 
			
			List<CrewDay> crewDays = manager.getCrewDayByCrewMemberByMonth(new Integer(crewMember.getId()),new Integer(nowMonth.substring(0, 4)),new Integer(nowMonth.substring(5, nowMonth.length())));
			
			Map<String,CrewDay> crewDayMap = new HashMap<String,CrewDay>();
			for(CrewDay cd : crewDays){
				crewDayMap.put(dayformat.format(cd.getDate()), cd);
			}
			
			
			TreeMap days = new TreeMap();
			
			while(nowMonth.equals(monthformat.format(cal.getTime()))){	
				String day = dayformat.format(cal.getTime());
				Map dayMap = new HashMap();
				dayMap.put("day", cal.get(Calendar.DAY_OF_WEEK));
				dayMap.put("crewDay", crewDayMap.get(day));
				days.put(day,dayMap);
				cal.add(Calendar.DAY_OF_MONTH, 1);
				//LOG.info(day);
			}
			
			cal.add(Calendar.DAY_OF_MONTH, -1);
			cal.set(Calendar.DAY_OF_MONTH, 1);

			months.put(nowMonth,days);
			
			cal.add(Calendar.MONTH, -1);
			nowMonth = monthformat.format(cal.getTime());
		  
		}
		//provide month list
		return "hours";
	}
	

	public String errorMessage;
	public String notificationMessage;
	public CrewMember.FlightAndDutyActuals actuals;
	public String charterCode;
	public boolean actualsCompleted = false;
	public int month, year;
	public List<Charter> allCharters;
	public List<Aircraft> allAircraft;
	
	public String reason;
	public double amount;
	public double amountUSD;
	
	//add a deduction to a crewMember
	public String addDeduction() throws Exception{
		if (crewMember.getId() == null) {
			errorMessage = "Unknown Crew Member";
			return SUCCESS;
		} else {
			
			LOG.info("amount:"+amount+" amountUSD:"+amountUSD);
			
			if(amountUSD == 0.0){
				Deduction deduction = new Deduction();
				deduction.setEntered(amount);
				deduction.setCurrency(currency);
				//LOG.info("RAND:"+amount);
				deduction.setReason(reason);
				//get xchange rate and convert amount (rand)
				ExchangeRate ex =  manager.getExchangeRateByCode(currency, "USD");
				deduction.setExchangeRate(ex.getAmount());
				//LOG.info("XCHANGE:"+ex.getAmount());
				Money converted = ex.convert(amount);
				deduction.setAmount(converted);
				actuals.getDeductions().put(reason,deduction);
			}
			else {
				Deduction deduction = new Deduction();
				Money converted = new Money("USD",amountUSD);
				LOG.info("USD:"+converted.getAmountAsDouble());
				deduction.setReason(reason);
				deduction.setCurrency(currency);
				//get xchange rate and convert amount (rand)
				ExchangeRate ex =  manager.getExchangeRateByCode(currency, "USD");
				Double exAmount = ex.getAmount();
				ex.setAmount(1/exAmount);
				deduction.setExchangeRate( 1/ exAmount);
				LOG.info("XCHANGE RATE:"+(1/exAmount));
				Money entered = ex.convert(converted.getAmountAsDouble());
				ex.setAmount(exAmount);
				LOG.info("ENTERED:"+entered);
				deduction.setAmount(converted);
				deduction.setEntered(entered.getAmountAsDouble());
				actuals.getDeductions().put(reason,deduction);
			}
			
			//LOG.info("XCHANGE converted:"+converted);
			
			//save actuals
			try {
				if (actuals.getId() == null) {
					addFlightActuals();
					notificationMessage = "Deduction Saved - Actuals Added";
				} else {
					manager.saveFlightAndDutyActuals(actuals);
					notificationMessage = "Deduction Saved - Actuals saved";
				}
				crewMember = manager.getCrewMemberByCode(crewMember.getCode());
			} catch (ExistingRecordException e) {
				errorMessage = e.getMessage();
			}
		}
		allCharters=manager.getAllCharters().charterList;
		allAircraft=manager.getAllAircraft().aircraftList;
		
		breadcrumbs = Breadcrumb.toArray(
				new Breadcrumb("Crew", "crew.action"),
				new Breadcrumb(crewMember.getPersonal().getFirstName() + " " + crewMember.getPersonal().getLastName())
		);
		tab= "flight";
		prepareTabs();
		return "redirect-addFlightActuals";
	}

	//remove a deduction from a crewMember
	public String remDeduction() throws Exception{
		if (crewMember.getId() == null) {
			errorMessage = "Unknown Crew Member";
			return SUCCESS;
		} else {
			actuals.getDeductions().remove(reason);
			try {
				if (actuals.getId() == null) {
					manager.addCrewFlightAndDutyActuals(crewMember.getCode(), actuals);
					notificationMessage = "Deduction Removed - Actuals added";
				} else {
					manager.saveFlightAndDutyActuals(actuals);
					notificationMessage = "Deduction Removed - Actuals saved";
				}
				crewMember = manager.getCrewMemberByCode(crewMember.getCode());
			} catch (ExistingRecordException e) {
				errorMessage = e.getMessage();
			}
			//save actuals
		}
		allCharters=manager.getAllCharters().charterList;
		allAircraft=manager.getAllAircraft().aircraftList;
		
		breadcrumbs = Breadcrumb.toArray(
				new Breadcrumb("Crew", "crew.action"),
				new Breadcrumb(crewMember.getPersonal().getFirstName() + " " + crewMember.getPersonal().getLastName())
		);
		tab= "flight";
		prepareTabs();
		return "addFlightActuals";
	}
	
	
	public String addFlightActuals() throws Exception {
		
		if("".equals(errorMessage)){errorMessage = null; }
		if("".equals(notificationMessage)){notificationMessage = null; }
		
		
		if (crewMember.getId() == null) {
			errorMessage = "Unknown Crew Member";
			return SUCCESS;
		} else {
			allCharters=manager.getAllCharters().charterList;
			allAircraft=manager.getAllAircraft().aircraftList;
			rates=manager.getExchangeRates();
			
			breadcrumbs = Breadcrumb.toArray(
					new Breadcrumb("Crew", "crew.action"),
					new Breadcrumb(crewMember.getPersonal().getFirstName() + " " + crewMember.getPersonal().getLastName())
			);
			if (!actualsCompleted) {
				if (crewMember.getPayments().getCurrency() == null) {
					errorMessage = "Currency is not set.";
					tab = "payments";
					return execute();
				}
				tab = "flight";
				prepareTabs();
				return "addFlightActuals";
			} else {
				if (user.hasPermission("ManagerEdit")) {
					Date date = new DateMidnight(year, month, 1).toDate();
					actuals.setDate(date);

					HashMap<String,String> tbRem = new HashMap<String,String>();
					for (String code: actuals.getEntries().keySet()) {
						CharterEntry ce = actuals.getEntries().get(code);
						if (ce.getAreaDays() == 0 && ce.getDailyDays() == 0 && ce.getFlightDays() == 0 && ce.getInstructorDays() == 0)
							tbRem.put(code,"remove");
					}
					
					for(String rem : tbRem.keySet()){
						actuals.getEntries().remove(rem);
					}

					int i=0;
					String key1 = ServletActionContext.getRequest().getParameter("newEntryFirKey"+i);
					String key2 = ServletActionContext.getRequest().getParameter("newEntrySecKey"+i);
					String key = key1;
					if((key1 != null) && (key2 != null)){
						key = key1 +"_"+ key2;
					}
					while (key != null) {
						int area = parseInt(ServletActionContext.getRequest().getParameter("newEntryArea"+i));
						int daily = parseInt(ServletActionContext.getRequest().getParameter("newEntryDaily"+i));
						int flight = parseInt(ServletActionContext.getRequest().getParameter("newEntryFlight"+i));
						int instructor = parseInt(ServletActionContext.getRequest().getParameter("newEntryInstructor"+i));
						int discomfort = parseInt(ServletActionContext.getRequest().getParameter("newEntryDiscomfort"+i));
						System.out.println(key + " - " + area +", " + daily + ", " + flight + ", " + instructor+", "+discomfort);

						i++;

						if (area != 0 || daily != 0 || flight != 0 || instructor != 0) {
							CharterEntry ce = new CrewMember.FlightAndDutyActuals.CharterEntry();
							ce.setCharter(key1);
							ce.setAircraft(key2);
							ce.setAreaDays(area);
							ce.setDailyDays(daily);
							ce.setFlightDays(flight);
							ce.setDiscomfort(discomfort);
							ce.setInstructorDays(instructor);
							actuals.getEntries().put(key, ce);
						}
						key1 = ServletActionContext.getRequest().getParameter("newEntryFirKey"+i);
						key2 = ServletActionContext.getRequest().getParameter("newEntrySecKey"+i);
						if((key1 != null) && (key2 != null)){
							key = key1 +"_"+ key2;
						}
						else{
							key = key1;
						}
					}

					try {
						if (actuals.getId() == null) {
							manager.addCrewFlightAndDutyActuals(crewMember.getCode(), actuals);
							notificationMessage = "Actuals added successfully";
						} else {
							manager.saveFlightAndDutyActuals(actuals);
							notificationMessage = "Actuals saved";
						}
						crewMember = manager.getCrewMemberByCode(crewMember.getCode());
					} catch (ExistingRecordException e) {
						errorMessage = e.getMessage();
					}
					tab = "flight";
					return execute();
				}
			}
		}
		return execute();
	}

	private int parseInt(String i) {
		if (i == null || i.trim().length()==0)
			return 0;
		try {
			int val = Integer.parseInt(i);
			return val;
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	public List<Document> docs;
	public Folder folder;
	public String docs() throws Exception {
		if (id == null) {
			return ERROR;
		}
		tab = "documents";
		docs = new LinkedList<Document>();
		folder = docManager.getFolderByPath("/crew/"+id, user);
		if (folder != null) {
			if (folder.canRead(user))
				docs.addAll(folder.getDocs());
			else {
				errorMessage = "Insufficient Privilages";
			}
		}
		Collections.sort(docs, new Comparator<Document>() {

			public int compare(Document o1, Document o2) {
				return o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase());
			}

		});
		breadcrumbs = Breadcrumb.toArray(
				new Breadcrumb("Crew", "crew.action"),
				new Breadcrumb(crewMember.getPersonal().getFirstName() + " " + crewMember.getPersonal().getLastName())
			);
		prepareTabs();

		List<Tag> tags = bookmarkManager.findAllTags();

		StringBuilder buf = new StringBuilder();
		buf.append('[');
		boolean first = true;
		for (Tag t: tags) {
			if (first) {
				first = false;
			} else {
				buf.append(',');
			}
			buf.append('\'');
			buf.append(t.getTag());
			buf.append('\'');
		}
		buf.append(']');
		tagArray = buf.toString();
		return "docs";
	}

	public String create() {
		//crewMember = new CrewMember();
		breadcrumbs = Breadcrumb.toArray(
				new Breadcrumb("Crew", "crew.action"),
				new Breadcrumb("New Crew Member")
			);
		Tab personalTab = new Tab("Personal", "#", true);
		tableTabs = new Tab[] {personalTab};
		return SUCCESS;
	}

	public String save() throws Exception {
		
		LOG.info("Saving Crew Member "+crewMember.getPersonal().getFullName());
		
		//set passports
		LinkedList<CrewMember.Passport> cmPassports = new LinkedList<CrewMember.Passport>();
		int index = 0;
		if(passportsNumber != null){
		for(String passportNumber : passportsNumber ){
			
			String passC  = passportsCountry.get(index);
			String passE  = passportsExpiryDate.get(index);
			String passId = passportsId.get(index);
			
			if((passportNumber != null)&&(!"".equals(passportNumber))&&(passC != null)&&(!"".equals(passC))&&(passE != null)&&(!"".equals(passE))){
			CrewMember.Passport p = new CrewMember.Passport();
			p.setPassportNumber(passportNumber);
			p.setCountry(passC);
			p.setExpiryDate(df.parse(passE));
			if(passId != null){
				if(!passId.equals("")){
					p.id = new Integer(passId);
				}
			}
			cmPassports.add(p);
			}
			index++;			
		}
		crewMember.setPassport(cmPassports);
		}
		
		manager.saveCrewMember(crewMember);
		try{
		  if(document != null){
			LOG.info(tags+" "+docfolder);
			String[] tagsArray = tags.split(" ");
			Document doc = new Document();
			doc.setName(documentFileName);
			doc.setContentType(documentContentType);
			Bookmark b = bookmarkManager.createBookmark(documentFileName, "Document", docfolder+"/"+documentFileName, tagsArray);
			doc.setBookmark(b);
			docManager.createDocument(doc, docfolder, new FileInputStream(document), user);
		  }
		}
		catch(Exception e){
			LOG.error(e);
		}
				
		LOG.info(passportsFileName);
		LOG.info(passportsTags);
		LOG.info(passportsContentType);
		
		LOG.info(passportsCountry);
		LOG.info(passportsNumber);
		LOG.info(passportsExpiryDate);
		
	    boolean morePassports = true;
	    int count = 0;
	    if(passports != null){
		while(morePassports){			
			if(count < passports.size()){			
			    File passport = passports.get(count);
			    String passportTags = passportsTags.get(count);
			    String passportFileName = passportsFileName.get(count);
			    String passportContentType = passportsContentType.get(count);
			    passportTags = passportTags + count;
			    try{
				    if(passport != null){
					    LOG.info(passportTags+" "+docfolder);
					    String[] tagsArray = passportTags.split(" ");
					    Document doc = new Document();
					    doc.setName(passportFileName);
					    doc.setContentType(passportContentType);
					    Bookmark b = bookmarkManager.createBookmark(passportFileName, "Document", docfolder+"/"+passportFileName, tagsArray);
					    doc.setBookmark(b);
					    docManager.createDocument(doc, docfolder, new FileInputStream(passport), user);
				    }
			    }
			    catch(Exception e){
			    	LOG.error(e);
			    }
			count++;
			}
			else{
				morePassports = false;
			}	
		}
	    }
	    try{
            if(crmFile!= null){         
                LOG.info(tags+" "+docfolder);
                String[] tagsArray = crmTags.split(" ");
                Document doc = new Document();
                doc.setName(crmFileFileName);                
                doc.setContentType(crmFileContentType);
                Bookmark b = bookmarkManager.createBookmark(crmFileFileName, "Document", docfolder+"/"+ crmFileFileName, tagsArray);
                doc.setBookmark(b);
                docManager.createDocument(doc, docfolder, new FileInputStream(crmFile), user);
            }else{            	
            }
          }
          catch(Exception e){
                LOG.error(e);
          }
  	    try{
            if(dgFile!= null){         
                LOG.info(dgTags+" "+docfolder);
                String[] tagsArray = tags.split(" ");
                Document doc = new Document();
                doc.setName(dgFileFileName);
                doc.setContentType(dgFileContentType);
                Bookmark b = bookmarkManager.createBookmark(dgFileFileName, "Document", docfolder+"/"+ dgFileFileName, tagsArray);
                doc.setBookmark(b);
                docManager.createDocument(doc, docfolder, new FileInputStream(dgFile), user);
            }else{            	
            }
          }
          catch(Exception e){
                LOG.error(e);
          }
  	    try{
            if(huetFile!= null){         
                LOG.info(huetTags+" "+docfolder);
                String[] tagsArray = tags.split(" ");
                Document doc = new Document();
                doc.setName(huetFileFileName);
                doc.setContentType(huetFileContentType);
                Bookmark b = bookmarkManager.createBookmark(huetFileFileName, "Document", docfolder+"/"+ huetFileFileName, tagsArray);
                doc.setBookmark(b);
                docManager.createDocument(doc, docfolder, new FileInputStream(huetFile), user);
            }else{            	
            }
          }
          catch(Exception e){
                LOG.error(e);
          }
	return execute();
	}

	public Integer actualsId;
	public void prepare() throws Exception {
		if (id == null) {
			crewMember = new CrewMember();
		} else {
			crewMember = manager.getCrewMemberByCode(id);
			if (actualsId == null) {
				actuals = new CrewMember.FlightAndDutyActuals(
						crewMember.getPayments().getMonthlyBaseRate(),
						crewMember.getPayments().getAreaAllowance(),
						crewMember.getPayments().getInstructorAllowance(),
						crewMember.getPayments().getDailyAllowance(),
						crewMember.getPayments().getFlightAllowance()
				);
			} else {
				actuals = manager.getFlightAndDutyActualsById(actualsId);
				Calendar cal = Calendar.getInstance();
				cal.setTime(actuals.getDate());
				currentMonth = cal.get(Calendar.MONTH);
				currentYear = cal.get(Calendar.YEAR);
				//This needs to be reset so that if the user has unticked the box
				//the correct value is saved.
				if (actualsCompleted)
					actuals.setPayMonthlyRate(false);
			}
		}
	}

	private void prepareTabs() {
		String idStr = "";
		if (id != null) {
			idStr=id;
		}
		Tab personalTab = new Tab("Personal", "crewMember.action?id="+idStr, tab.equals("personal"));
		Tab bankingTab = new Tab("Banking", "crewMember.action?tab=banking&id="+idStr, tab.equals("banking"));
		Tab roleTab = new Tab("Role", "crewMember.action?tab=role&id="+idStr, tab.equals("role"));
		Tab paymentsTab = new Tab("Payments", "crewMember.action?tab=payments&id="+idStr, tab.equals("payments"));
		Tab documentsTab = new Tab("Documents", "crewMember!docs.action?tab=documents&id="+idStr, tab.equals("documents"));
		Tab reviewTab = new Tab("Review", "crewMember.action?tab=review&id="+idStr, tab.equals("review"));
		Tab flightAndDutyTab = new Tab("PDW", "crewMember.action?tab=flight&id="+idStr, tab.equals("flight"));
		Tab hours = new Tab("On Contract", "crewMember.action?tab=hours&id="+idStr, tab.equals("hours"));
		Tab assignmentsTab = new Tab("Assignments", "crewMember!assignments.action?tab=assignments&id="+idStr, tab.equals("assignments"));

		if (user.hasPermission("ManagerView"))
			tableTabs = new Tab[] {personalTab, bankingTab, roleTab, paymentsTab, flightAndDutyTab, hours, documentsTab, reviewTab, assignmentsTab};
		else
			tableTabs = new Tab[] {personalTab, bankingTab, roleTab, paymentsTab, flightAndDutyTab, hours, documentsTab};
	}

    public String fromPage = "";
	public String assignments() {
		tab = "assignments";

        if ("".equals(fromPage)) {
            breadcrumbs = Breadcrumb.toArray(
				new Breadcrumb("Crew", "crew.action"),
				new Breadcrumb(crewMember.getPersonal().getFirstName() + " " + crewMember.getPersonal().getLastName())
			);
        }
        else {
            breadcrumbs = Breadcrumb.toArray(
				new Breadcrumb("Crew", "crew.action"),
                new Breadcrumb("Reports", fromPage),
				new Breadcrumb(crewMember.getPersonal().getFirstName() + " " + crewMember.getPersonal().getLastName())
			);
        }

		prepareTabs();

		return "assignments";
	}

	public void setUser(User arg0) {
		user = arg0;
	}

	public User getUser() {
		return user;
	}

	@Permissions("Approve")
	public String review() throws Exception {
		String approvalKey = approvalsManager.review(crewMember.getApprovalGroup().getId(), 1000*60*5);
		ServletActionContext.getRequest().getSession().setAttribute("approvalKey-"+crewMember.getApprovalGroup().getId(), approvalKey);
		prepare();
		notificationMessage = "Crew Member locked for 5 minutes";
		return execute();
	}

	@Permissions("Approve")
	public String approve() throws Exception {
		String approvalKey = (String) ServletActionContext.getRequest().getSession().getAttribute("approvalKey-"+crewMember.getApprovalGroup().getId());
		approvalsManager.approve(crewMember.getApprovalGroup().getId(), approvalKey);
		prepare();
		notificationMessage = "Crew Member has been approved";
		return execute();
	}

	@Permissions("Approve")
	public String open() throws Exception {
		String approvalKey = (String) ServletActionContext.getRequest().getSession().getAttribute("approvalKey-"+crewMember.getApprovalGroup().getId());
		approvalsManager.open(crewMember.getApprovalGroup().getId(), approvalKey);
		prepare();
		notificationMessage = "Crew Member has been opened for editing";
		return execute();
	}
}
