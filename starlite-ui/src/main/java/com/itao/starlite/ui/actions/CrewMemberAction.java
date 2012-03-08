package com.itao.starlite.ui.actions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.DateFormat;
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
import com.itao.starlite.auth.exceptions.InsufficientPrivilagesException;
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
import com.itao.starlite.model.MyFolder;
import com.itao.starlite.model.CrewMember.FlightAndDutyActuals;
import com.itao.starlite.model.CrewMember.Passport;
import com.itao.starlite.model.CrewMember.Role;
import com.itao.starlite.model.CrewMember.FlightAndDutyActuals.Addition;
import com.itao.starlite.model.CrewMember.FlightAndDutyActuals.CharterEntry;
import com.itao.starlite.model.CrewMember.FlightAndDutyActuals.Deduction;
import com.itao.starlite.ui.Breadcrumb;
import com.itao.starlite.ui.Tab;
import com.itao.starlite.ui.jmesa.YesNoCellEditor;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;



@SuppressWarnings("serial")

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
	public String switch_role_to = "";//used to store temporary role switch to drive different form
	public String tagArray = "[]";
	private User user;
	//public Role role;
	
	public ArrayList<String> YNOption = new ArrayList<String> ();
	
	public String docfolder;
	public File document;
	public String documentContentType;
	public String documentFileName;
	public String tags;
	
	public String currency;
	public List<ExchangeRate> rates;
	
	public List<String>   passportsTags;
	public List<String>   passportsTags2;
	public List<String>   passportsTags3;
	public List<File>     passports;
	public List<String>   passportsContentType;
	public List<String>   passportsFileName;
	public List<String>   passportsId;
	public List<String>   passportsCountry;
	public List<String>   passportsNumber;
	public List<String>   passportsExpiryDate;
	public Map<String,Document> passportFiles;
	
	
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
    
    public File   hemsCertFile;
	public String hemsCertFileContentType;
    public String hemsCertFileFileName;
    public String hemsCertTags;
    
    public File   additionalCertFile;
	public String additionalCertFileContentType;
    public String additionalCertFileFileName;
    public String additionalCertTags;
    
    public File   flighthoursFile;
    public String flighthoursFileContentType;
    public String flighthoursFileFileName;
    public String flighthoursTags;
    
    public File   mediFile;
	public String mediFileContentType;
    public String mediFileFileName;
    public String mediTags;
    
    public File   licenceFile;
	public String licenceFileContentType;
    public String licenceFileFileName;
    public String licenceTags;
    
    public File   lpcCertFile;
	public String lpcCertFileContentType;
    public String lpcCertFileFileName;
    public String lpcCertTags;
    
    public File   opcCertFile;
	public String opcCertFileContentType;
    public String opcCertFileFileName;
    public String opcCertTags;
    
    public File   operationsManualCertFile;
	public String operationsManualCertFileContentType;
    public String operationsManualCertFileFileName;
    public String operationsManualCertTags;
    

    public File   annualTechnicalManualCertFile;
	public String annualTechnicalManualCertFileContentType;
    public String annualTechnicalManualCertFileFileName;
    public String annualTechnicalManualCertTags;

    public File   passport1PassFile;
	public String passport1PassFileFileContentType;
    public String passport1PassFileFileName;
    public String passport1PassFileTags;
    
    public File   passport2PassFile;
	public String passport2PassFileFileContentType;
    public String passport2PassFileFileName;
    public String passport2PassFileTags;
    
    public File   passport3PassFile;
	public String passport3PassFileFileContentType;
    public String passport3PassFileFileName;
    public String passport3PassFileTags;
    
    public File   passport1CertFile;
	public String passport1CertFileContentType;
    public String passport1CertFileFileName;
    public String passport1CertTags;
    
    
    public File   passport2CertFile;
	public String passport2CertFileContentType;
    public String passport2CertFileFileName;
    public String passport2CertTags;
    
    public File   passport3CertFile;
	public String passport3CertFileContentType;
    public String passport3CertFileFileName;
    public String passport3CertTags;
    
    public Document licence;
    public Document medical;
    public Document crm;
    public Document dg;
    public Document huet;
    public Document hemsCert;
    public Document additionalCert;
    public Document photoFile;
    public Document flighthours;
    public Document lpcCert;
    public Document opcCert;
    public Document operationsManualCert;
    public Document annualTechnicalManualCert;
    public Document passport1Cert;
    public Document passport2Cert;
    public Document passport3Cert;
    public Document passport1Pass;
    public Document passport2Pass;
    public Document passport3Pass;
    
    public List<Bookmark> additionalDocs = new ArrayList<Bookmark>();
    public List<Bookmark> passport1Docs = new ArrayList<Bookmark>();
    public List<Bookmark> passport2Docs = new ArrayList<Bookmark>();
    public List<Bookmark> passport3Docs = new ArrayList<Bookmark>();
    
    public String subPosition;
    
    public String dateOfBirth;
    public String initialDate;
    public String reviewDate;
    public String licenseExpiryDate;
    public String instructorExpiryDate;
    public String instrumentExpiryDate;
    public String englishTestExpiryDate;
    public String medicalExpiryDate;
    public String crmExpiryDate;
    public String dgExpiryDate;
    public String huetExpiryDate;
    public String hemsCertExpiryDate;
    public String lpcExpiryDate;
    public String opcExpiryDate;
    public String operationsManualExpiry;
    public String annualTechnicalManual;
    public String routeCheckExpiryDate;
    public String passportCert1ExpiryDate;
    public String passportCert2ExpiryDate;
    public String passportCert3ExpiryDate;
    
    public Double basePilotAllowance;
    public Double safetyLevel;
    
    
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
	
	public List<String> pilots = new ArrayList<String>();
	
	public boolean monthlyPayRatebox = true;

	/*------------------------------------------------------------*/
	@Override
	public String execute() throws Exception
	/*------------------------------------------------------------*/
	{
		//crewMember = manager.getCrewMember(id);
		pilots = manager.getAllActivePilots();
		prepare();
		if (!user.hasPermission("ManagerView") && !user.getUsername().equalsIgnoreCase(id))
			return "";
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
			
			passportFiles = new HashMap<String,Document>();
			folder = docManager.getFolderByPath("/crew/"+id, user);
			
			photoFile = folder.getDocumentByTag("photo");
			
			/*int count=0;
			boolean morePassports = true;
	       
			while(morePassports){
				 Document passport = folder.getDocumentByTag("passport"+count);
				 if(passport != null){
					 passportFiles.put("passport"+count,passport);
				 }
				 else{
					 morePassports = false;
				 }
				 count++;
			}
			*/
			//----- Passport 1 List --- ///
			List<Bookmark> passport1DocsTempList = (List<Bookmark>) manager.findDocsByTag("passport0",id);
			
			if ((passport1DocsTempList == null)|| (passport1DocsTempList.isEmpty() == true))
	        {
	        	passport1DocsTempList = new ArrayList<Bookmark>();
	        }
	        Collections.reverse(passport1DocsTempList);
	        if (passport1DocsTempList.size() > 5)
	        {
	        	List<Bookmark> temp = passport1DocsTempList.subList(0, 5);
	        	for (int i = 0; i< temp.size(); i++ )
	        	{
	        	this.passport1Docs.add(temp.get(i));
	        	}
	        }
	        else
	        {
	        	List<Bookmark> temp = passport1DocsTempList;
	        	for (int i = 0; i< temp.size(); i++ )
	        	{
	        	this.passport1Docs.add(temp.get(i));
	        	}
	        }
	        
	      //----- Passport 2 List --- ///
			List<Bookmark> passport2DocsTempList = (List<Bookmark>) manager.findDocsByTag("passport1",id);
			
			if ((passport2DocsTempList == null)|| (passport2DocsTempList.isEmpty() == true))
	        {
	        	passport2DocsTempList = new ArrayList<Bookmark>();
	        }
	        Collections.reverse(passport2DocsTempList);
	        if (passport2DocsTempList.size() > 5)
	        {
	        	List<Bookmark> temp = passport2DocsTempList.subList(0, 5);
	        	for (int i = 0; i< temp.size(); i++ )
	        	{
	        	this.passport2Docs.add(temp.get(i));
	        	}
	        }
	        else
	        {
	        	List<Bookmark> temp = passport2DocsTempList;
	        	for (int i = 0; i< temp.size(); i++ )
	        	{
	        	this.passport2Docs.add(temp.get(i));
	        	}
	        }	        
	        
	        
	      //----- Passport 3 List --- /// 
	        
			List<Bookmark> passport3DocsTempList = (List<Bookmark>) manager.findDocsByTag("passport2",id);
			
			if ((passport3DocsTempList == null)|| (passport3DocsTempList.isEmpty() == true))
	        {
	        	passport3DocsTempList = new ArrayList<Bookmark>();
	        }
	        Collections.reverse(passport3DocsTempList);
	        if (passport3DocsTempList.size() > 5)
	        {
	        	List<Bookmark> temp = passport3DocsTempList.subList(0, 5);
	        	for (int i = 0; i< temp.size(); i++ )
	        	{
	        	this.passport3Docs.add(temp.get(i));
	        	}
	        }
	        else
	        {
	        	List<Bookmark> temp = passport3DocsTempList;
	        	for (int i = 0; i< temp.size(); i++ )
	        	{
	        	this.passport3Docs.add(temp.get(i));
	        	}
	        }	
	        
	        /// ---- OTher Passport ---///
	        passport1Cert    = folder.getDocumentByTag("passportVisa1");
	        passport2Cert    = folder.getDocumentByTag("passportVisa2");
	        passport3Cert    = folder.getDocumentByTag("passportVisa3");
	        passport1Pass    = folder.getDocumentByTag("passport0");
	        passport2Pass    = folder.getDocumentByTag("passport1");
	        passport3Pass    = folder.getDocumentByTag("passport2");
	        
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
		
		if(tab.equals("role")){
			aircraftTypes = manager.getAircraftTypes();
		    folder = docManager.getFolderByPath("/crew/"+id, user);
//	        LOG.info(folder.getDocs());
	        licence     = folder.getDocumentByTag("licence");
	        medical     = folder.getDocumentByTag("medical");
	        crm         = folder.getDocumentByTag("CRM");
	        dg          = folder.getDocumentByTag("DG");
	        huet        = folder.getDocumentByTag("HUET");
	        hemsCert    = folder.getDocumentByTag("HEMS");
	        additionalCert = folder.getDocumentByTag("additionalCert");
	        List<Bookmark> additionalDocsTempList = (List<Bookmark>) manager.findDocsByTag("additionalCert",id);
	        
	       
	        if ((additionalDocsTempList == null)|| (additionalDocsTempList.isEmpty() == true))
	        {
	        	additionalDocsTempList = new ArrayList<Bookmark>();
	        }
	        Collections.reverse(additionalDocsTempList);
	        if (additionalDocsTempList.size() > 5)
	        {
	        	List<Bookmark> temp = additionalDocsTempList.subList(0, 5);
	        	for (int i = 0; i< temp.size(); i++ )
	        	{
	        	this.additionalDocs.add(temp.get(i));
	        	}
	        }
	        else
	        {
	        	List<Bookmark> temp = additionalDocsTempList;
	        	for (int i = 0; i< temp.size(); i++ )
	        	{
	        	this.additionalDocs.add(temp.get(i));
	        	}
	        }
	        
	        
	        
	        
	        
	        opcCert= folder.getDocumentByTag("OPC");
	        lpcCert= folder.getDocumentByTag("LPC");
	        operationsManualCert= folder.getDocumentByTag("opsManual");
	        annualTechnicalManualCert= folder.getDocumentByTag("annualTechManual");
	       
	        	        
	        flighthours = folder.getDocumentByTag("flighthours");
		}
		
		return tab;
	}
	/*------------------------------------------------------------*/
	public String profile() throws Exception
	/*------------------------------------------------------------*/
	{
		prepare();
		LOG.info(crewMember.getId());
		return "profile";
	}
	/*------------------------------------------------------------*/
	public String required() throws Exception
	/*------------------------------------------------------------*/
	{
		prepare();
		LOG.info(crewMember.getId());
		return "required";
	}
	/*------------------------------------------------------------*/
	public String photo()
	/*------------------------------------------------------------*/
	{
		return "photo";
	}
	/*------------------------------------------------------------*/
	public InputStream getPhoto()
	/*------------------------------------------------------------*/
	{
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
	/*------------------------------------------------------------*/
	public InputStream getCrmFile()
	/*------------------------------------------------------------*/
	{
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
	/*------------------------------------------------------------*/
	public InputStream getDgFile()
	/*------------------------------------------------------------*/
	{
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
	/*------------------------------------------------------------*/
	public InputStream getHuetFile()
	/*------------------------------------------------------------*/
	{
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
	/*------------------------------------------------------------*/
	public InputStream getHemsFile()
	/*------------------------------------------------------------*/
	{
		  try{
		    folder = docManager.getFolderByPath("/crew/"+id, user);
		    LOG.info(folder.getDocs());
		    Document hemsFile = folder.getDocumentByTag("HEMS");
		    LOG.info("Name:"+hemsFile.getName());
		    LOG.info("Uuid:"+hemsFile.getUuid());		    
		    return (InputStream) docManager.getDocumentData(hemsFile);
		  }
		  catch(Exception e){
			  LOG.error(e);			  			 
		  }
		  return null;
	}
	/*------------------------------------------------------------*/
	public InputStream getAdditionalFile()
	/*------------------------------------------------------------*/
	{
		  try{
		    folder = docManager.getFolderByPath("/crew/"+id, user);
		    LOG.info(folder.getDocs());
		    Document additionalFile = folder.getDocumentByTag("additionalCert");
		    LOG.info("Name:"+additionalFile.getName());
		    LOG.info("Uuid:"+additionalFile.getUuid());		    
		    return (InputStream) docManager.getDocumentData(additionalFile);
		  }
		  catch(Exception e){
			  LOG.error(e);			  			 
		  }
		  return null;
	}
	/*------------------------------------------------------------*/
	public InputStream getLpcFile()
	/*------------------------------------------------------------*/
	{
		  try{
		    folder = docManager.getFolderByPath("/crew/"+id, user);
		    LOG.info(folder.getDocs());
		    Document lpcFile = folder.getDocumentByTag("LPC");
		    LOG.info("Name:"+lpcFile.getName());
		    LOG.info("Uuid:"+lpcFile.getUuid());		    
		    return (InputStream) docManager.getDocumentData(lpcFile);
		  }
		  catch(Exception e){
			  LOG.error(e);			  			 
		  }
		  return null;
	}
	/*------------------------------------------------------------*/
	public InputStream getOpcFile()
	/*------------------------------------------------------------*/
	{
		  try{
		    folder = docManager.getFolderByPath("/crew/"+id, user);
		    LOG.info(folder.getDocs());
		    Document opcFile = folder.getDocumentByTag("OPC");
		    LOG.info("Name:"+opcFile.getName());
		    LOG.info("Uuid:"+opcFile.getUuid());		    
		    return (InputStream) docManager.getDocumentData(opcFile);
		  }
		  catch(Exception e){
			  LOG.error(e);			  			 
		  }
		  return null;
	}
	/*------------------------------------------------------------*/
	public InputStream getOperationsManualCertFile()
	/*------------------------------------------------------------*/
	{
		  try{
		    folder = docManager.getFolderByPath("/crew/"+id, user);
		    LOG.info(folder.getDocs());
		    Document operationsManualCertFile = folder.getDocumentByTag("opsManual");
		    LOG.info("Name:"+operationsManualCertFile.getName());
		    LOG.info("Uuid:"+operationsManualCertFile.getUuid());		    
		    return (InputStream) docManager.getDocumentData(operationsManualCertFile);
		  }
		  catch(Exception e){
			  LOG.error(e);			  			 
		  }
		  return null;
	}
	/*------------------------------------------------------------*/
	public InputStream getAnnualTechnicalManualCertFile()
	/*------------------------------------------------------------*/
	{
		  try{
		    folder = docManager.getFolderByPath("/crew/"+id, user);
		    LOG.info(folder.getDocs());
		    Document annualTechnicalManualCertFile = folder.getDocumentByTag("annualTechManual");
		    LOG.info("Name:"+annualTechnicalManualCertFile.getName());
		    LOG.info("Uuid:"+annualTechnicalManualCertFile.getUuid());		    
		    return (InputStream) docManager.getDocumentData(annualTechnicalManualCertFile);
		  }
		  catch(Exception e){
			  LOG.error(e);			  			 
		  }
		  return null;
	}
	/*------------------------------------------------------------*/
	public InputStream getPassport1PassFile()
	/*------------------------------------------------------------*/
	{
		  try{
		    folder = docManager.getFolderByPath("/crew/"+id, user);
		    LOG.info(folder.getDocs());
		    Document passport1PassFile = folder.getDocumentByTag("passport0");
		    LOG.info("Name:"+passport1PassFile.getName());
		    LOG.info("Uuid:"+passport1PassFile.getUuid());		    
		    return (InputStream) docManager.getDocumentData(passport1PassFile);
		  }
		  catch(Exception e){
			  LOG.error(e);			  			 
		  }
		  return null;
	}
	/*------------------------------------------------------------*/
	/*------------------------------------------------------------*/
	public InputStream getPassport2PassFile()
	/*------------------------------------------------------------*/
	{
		  try{
		    folder = docManager.getFolderByPath("/crew/"+id, user);
		    LOG.info(folder.getDocs());
		    Document passport2PassFile = folder.getDocumentByTag("passport1");
		    LOG.info("Name:"+passport2PassFile.getName());
		    LOG.info("Uuid:"+passport2PassFile.getUuid());		    
		    return (InputStream) docManager.getDocumentData(passport2PassFile);
		  }
		  catch(Exception e){
			  LOG.error(e);			  			 
		  }
		  return null;
	}
	/*------------------------------------------------------------*/
	/*------------------------------------------------------------*/
	public InputStream getPassport3PassFile()
	/*------------------------------------------------------------*/
	{
		  try{
		    folder = docManager.getFolderByPath("/crew/"+id, user);
		    LOG.info(folder.getDocs());
		    Document passport3PassFile = folder.getDocumentByTag("passport2");
		    LOG.info("Name:"+passport3PassFile.getName());
		    LOG.info("Uuid:"+passport3PassFile.getUuid());		    
		    return (InputStream) docManager.getDocumentData(passport3PassFile);
		  }
		  catch(Exception e){
			  LOG.error(e);			  			 
		  }
		  return null;
	}
	/*------------------------------------------------------------*/
	/*------------------------------------------------------------*/
	public InputStream getPassport1CertFile()
	/*------------------------------------------------------------*/
	{
		  try{
		    folder = docManager.getFolderByPath("/crew/"+id, user);
		    LOG.info(folder.getDocs());
		    Document passport1CertFile = folder.getDocumentByTag("passportCertificate1");
		    LOG.info("Name:"+passport1CertFile.getName());
		    LOG.info("Uuid:"+passport1CertFile.getUuid());		    
		    return (InputStream) docManager.getDocumentData(passport1CertFile);
		  }
		  catch(Exception e){
			  LOG.error(e);			  			 
		  }
		  return null;
	}
	/*------------------------------------------------------------*/
	public InputStream getPassport2CertFile()
	/*------------------------------------------------------------*/
	{
		  try{
		    folder = docManager.getFolderByPath("/crew/"+id, user);
		    LOG.info(folder.getDocs());
		    Document passport2CertFile = folder.getDocumentByTag("passportCertificate2");
		    LOG.info("Name:"+passport2CertFile.getName());
		    LOG.info("Uuid:"+passport2CertFile.getUuid());		    
		    return (InputStream) docManager.getDocumentData(passport2CertFile);
		  }
		  catch(Exception e){
			  LOG.error(e);			  			 
		  }
		  return null;
	}
	/*------------------------------------------------------------*/
	public InputStream getPassport3CertFile()
	/*------------------------------------------------------------*/
	{
		  try{
		    folder = docManager.getFolderByPath("/crew/"+id, user);
		    LOG.info(folder.getDocs());
		    Document CertFile = folder.getDocumentByTag("passportCertificate3");
		    LOG.info("Name:"+CertFile.getName());
		    LOG.info("Uuid:"+CertFile.getUuid());		    
		    return (InputStream) docManager.getDocumentData(CertFile);
		  }
		  catch(Exception e){
			  LOG.error(e);			  			 
		  }
		  return null;
	}
	/*------------------------------------------------------------*/
 	public InputStream getFlighthoursFile()
 	/*------------------------------------------------------------*/
 	{
		  try{
		    folder = docManager.getFolderByPath("/crew/"+id, user);
		    LOG.info(folder.getDocs());
		    Document flighthoursFile = folder.getDocumentByTag("flighthours");
		    LOG.info("Name:"+flighthoursFile.getName());
		    LOG.info("Uuid:"+flighthoursFile.getUuid());		    
		    return (InputStream) docManager.getDocumentData(flighthoursFile);
		  }
		  catch(Exception e){
			  LOG.error(e);			  			 
		  }
		  return null;
	}
	
	public String tableHtml;
	/*------------------------------------------------------------*/
	private String setupFlight()
	/*------------------------------------------------------------*/
	{
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
				.column("basePilotRate").withStyle("text-align:right").called("SB Pilot")
				.column("basePilotDays").called("Days")
				.column("safetyLevelRate").withStyle("text-align:right").called("Safety Level")
				.column("safetyLevelDays").called("Days")
				.column("deductionTotal").called("Deductions")
				.column("additionTotal").called("Contributions")
				.column("total").called("Total Due").withStyle("text-align:right")
				.column("paidDate").called("Date Paid").asDate("dd/MM/yyyy")
				.column("paidAmount").called("Amount Paid").withStyle("text-align:right")
				.render();

		}
		return "flight";
	}
	/*------------------------------------------------------------*/	
	@SuppressWarnings("deprecation")
	public String saveRange() throws Exception
	/*------------------------------------------------------------*/
	{
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
		    to.setHours(23);
		    to.setMinutes(59);
		    to.setSeconds(59);
			
			if(from.before(to)){
				//while(!to.after(cal.getTime())){
				while(to.after(cal.getTime())){
					
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
	/*------------------------------------------------------------*/	
	public String saveHours() throws Exception
	/*------------------------------------------------------------*/
	{
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
			String tail        =  ServletActionContext.getRequest().getParameter(hoursMonth+"-"+day+"_registration");
			String chart       =  ServletActionContext.getRequest().getParameter(hoursMonth+"-"+day+"_charter");
			String flown       =  ServletActionContext.getRequest().getParameter(hoursMonth+"-"+day+"_flown");
			
		    String timein      =  ServletActionContext.getRequest().getParameter(hoursMonth+"-"+day+"_timein");
			String timeout     =  ServletActionContext.getRequest().getParameter(hoursMonth+"-"+day+"_timeout");
			String hours       =  ServletActionContext.getRequest().getParameter(hoursMonth+"-"+day+"_hours");
			
			LOG.info(hoursMonth+"-"+day+"|"+cDId+"|"+activity+"|"+comment+"|"+type+"|"+position+"|"+instruments+"|"+tail+"|"+chart+"|"+flown+"|"+timein+"|"+timeout+"|"+hours);
			//LOG.info(hoursMonth+"-"+day+"|"+cDId+"|"+activity+"|"+comment+"|"+type+"|"+position+"|"+instruments+"|"+tail+"|"+chart+"|");
			
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
	/*------------------------------------------------------------*/
	@SuppressWarnings("unchecked")
	private String setupHours() throws Exception
	/*------------------------------------------------------------*/
	{
		
		int startYear = 2009;
		int startMonth = 1;
		boolean moreMonths = true;
		Calendar cal = Calendar.getInstance();
		int currMonth = cal.get(Calendar.MONTH)+1;
		int currYear = cal.get(Calendar.YEAR);
		months = new TreeMap<String,TreeMap>(Collections.reverseOrder());
		
		while(moreMonths){
		if(startYear < currYear){
			for(int i=1; i< 13; i++){
				if(i < 10){
					months.put(""+startYear+"-0"+i,new TreeMap());
				}
				else{
					months.put(""+startYear+"-"+i,new TreeMap());
				}
			}
		}
		else if (startYear == currYear) {
			for(int i=1; i<=currMonth; i++){
				if(i < 10){
					months.put(""+startYear+"-0"+i,new TreeMap());
				}
				else{
					months.put(""+startYear+"-"+i,new TreeMap());
				}
			}
		}
		else{
			moreMonths = false;
		}
		startYear++;
		}
		
		allCharters=manager.getAllCharters().charterList;
		allAircraft=manager.getAllAircraft().aircraftList;
		
		
		if(hoursMonth != null){

		
		
		SimpleDateFormat monthformat = new SimpleDateFormat("yyyy-MM");
		SimpleDateFormat dayformat = new SimpleDateFormat("dd");
		
		cal = Calendar.getInstance();
		cal.setTime(monthformat.parse(hoursMonth));
		cal.set(Calendar.DAY_OF_MONTH, 1);
		
		String nowMonth = monthformat.format(cal.getTime());
		
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
		
			
		months.put(nowMonth,days);
		
		}
		
		//provide month list
		return "hours";
	}
	/*------------------------------------------------------------*/

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

	/*------------------------------------------------------------*/
	public String addAddition() throws Exception
	/*------------------------------------------------------------*/
	{
		if (crewMember.getId() == null) {
			errorMessage = "Unknown Crew Member";
			return SUCCESS;
		} else {
			
			LOG.info("amount:"+amount+" amountUSD:"+amountUSD);
			
			if(amountUSD == 0.0){
				Addition add = new Addition();
				add.setEntered(amount);
				add.setCurrency(currency);
				//LOG.info("RAND:"+amount);
				add.setReason(reason);
				//get xchange rate and convert amount (rand)
				ExchangeRate ex =  manager.getExchangeRateByCode(currency, "USD");
				add.setExchangeRate(ex.getAmount());
				//LOG.info("XCHANGE:"+ex.getAmount());
				Money converted = ex.convert(amount);
				add.setAmount(converted);
				actuals.getAdditions().put(reason,add);
			}
			else {
				Addition add = new Addition();
				Money converted = new Money("USD",amountUSD);
				LOG.info("USD:"+converted.getAmountAsDouble());
				add.setReason(reason);
				add.setCurrency(currency);
				//get xchange rate and convert amount (rand)
				ExchangeRate ex =  manager.getExchangeRateByCode(currency, "USD");
				Double exAmount = ex.getAmount();
				ex.setAmount(1/exAmount);
				add.setExchangeRate( 1/ exAmount);
				LOG.info("XCHANGE RATE:"+(1/exAmount));
				Money entered = ex.convert(converted.getAmountAsDouble());
				ex.setAmount(exAmount);
				LOG.info("ENTERED:"+entered);
				add.setAmount(converted);
				add.setEntered(entered.getAmountAsDouble());
				actuals.getAdditions().put(reason,add);
			}
			
			//LOG.info("XCHANGE converted:"+converted);
			
			//save actuals
			try {
				if (actuals.getId() == null) {
					addFlightActuals();
					notificationMessage = "Addition Saved - Actuals Added";
				} else {
					manager.saveFlightAndDutyActuals(actuals);
					notificationMessage = "Addition Saved - Actuals saved";
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
	/*------------------------------------------------------------*/
	//remove a deduction from a crewMember
	public String remAddition() throws Exception
	/*------------------------------------------------------------*/
	{
		if (crewMember.getId() == null) {
			errorMessage = "Unknown Crew Member";
			return SUCCESS;
		} else {
			actuals.getAdditions().remove(reason);
			try {
				if (actuals.getId() == null) {
					manager.addCrewFlightAndDutyActuals(crewMember.getCode(), actuals);
					notificationMessage = "Addition Removed - Actuals added";
				} else {
					manager.saveFlightAndDutyActuals(actuals);
					notificationMessage = "Addition Removed - Actuals saved";
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
	/*------------------------------------------------------------*/
	//add a deduction to a crewMember
	public String addDeduction() throws Exception
	/*------------------------------------------------------------*/
	{
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
		//this.actualsId = actuals.getId();
		return "redirect-addFlightActuals";
	}
	/*------------------------------------------------------------*/
	//remove a deduction from a crewMember
	public String remDeduction() throws Exception
	/*------------------------------------------------------------*/
	{
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
	/*------------------------------------------------------------*/
	public String addFlightActuals() throws Exception
	/*------------------------------------------------------------*/
	{
		
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
						if (ce.getAreaDays() == 0 && ce.getDailyDays() == 0 && ce.getFlightDays() == 0 && ce.getBasePilotDays() == 0 && ce.getSafetyLevelDays() == 0 && ce.getInstructorDays() == 0)
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
						int basePilot = parseInt(ServletActionContext.getRequest().getParameter("newEntryBasePilot"+i));
						int safetyLevel = parseInt(ServletActionContext.getRequest().getParameter("newEntrySafetyLevel"+i));
						int instructor = parseInt(ServletActionContext.getRequest().getParameter("newEntryInstructor"+i));
						int discomfort = parseInt(ServletActionContext.getRequest().getParameter("newEntryDiscomfort"+i));
						System.out.println(key + " - " + area +", " + daily + ", " + flight + ", " + basePilot + ", "+ safetyLevel + ", "+instructor+", "+discomfort);

						i++;

						if (area != 0 || daily != 0 || flight != 0 ||basePilot !=0 ||safetyLevel !=0 || instructor != 0) {
							CharterEntry ce = new CrewMember.FlightAndDutyActuals.CharterEntry();
							ce.setCharter(key1);
							ce.setAircraft(key2);
							ce.setAreaDays(area);
							ce.setDailyDays(daily);
							ce.setFlightDays(flight);
							ce.setbasePilotDays(basePilot);
							ce.setSafetyLevelDays(safetyLevel);
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
						this.actualsId = actuals.getId();
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
	/*------------------------------------------------------------*/
	private int parseInt(String i)
	/*------------------------------------------------------------*/
	{
		if (i == null || i.trim().length()==0)
			return 0;
		try {
			int val = Integer.parseInt(i);
			return val;
		} catch (NumberFormatException e) {
			return 0;
		}
	}
	/*------------------------------------------------------------*/
	public List<Document> docs;
	public Folder folder;
	/*------------------------------------------------------------*/
	public String docs() throws Exception
	/*------------------------------------------------------------*/
	{
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
	/*------------------------------------------------------------*/
	public String create()
	/*------------------------------------------------------------*/
	{
		//crewMember = new CrewMember();
		breadcrumbs = Breadcrumb.toArray(
				new Breadcrumb("Crew", "crew.action"),
				new Breadcrumb("New Crew Member")
			);
		Tab personalTab = new Tab("Personal", "#", true);
		tableTabs = new Tab[] {personalTab};
		return SUCCESS;
	}
/*-------------------------------------------------------------------*/
	private void setDates()
/*-------------------------------------------------------------------*/	
	{
		Date r = null;
		String empty = "";

//DOB
		if ((dateOfBirth != null) && (empty.compareToIgnoreCase(dateOfBirth) != 0) )
		{
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			
			try {
				r = df.parse(dateOfBirth);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.crewMember.getPersonal().setDateOfBirth(r);
		}
//Initial Date
		if ((initialDate != null) && (empty.compareToIgnoreCase(initialDate) != 0) )
		{
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			
			try {
				r = df.parse(initialDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.crewMember.getRole().setInitialDate(r);
		}
//Review Date
		if ((reviewDate != null) && (empty.compareToIgnoreCase(reviewDate) != 0) )
		{
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			
			try {
				r = df.parse(reviewDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.crewMember.getRole().setReviewDate(r);
		}
//Instrument Expiry Date
		if ((this.instrumentExpiryDate != null) && (empty.compareToIgnoreCase(this.instrumentExpiryDate) != 0))
		{
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			
			try {
				r = df.parse(this.instrumentExpiryDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.crewMember.getRole().getIfr().setExpiryDate(r);
		}
//License Expiry Date (R1)		
		if ( (this.licenseExpiryDate != null) && (empty.compareToIgnoreCase(this.licenseExpiryDate) != 0))
		{
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			
			try {
				r = df.parse(this.licenseExpiryDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.crewMember.getRole().getR1().setExpiryDate(r);
    	}
//Instructor Expiry Date (R2)		
		if ((this.instructorExpiryDate != null)&& (empty.compareToIgnoreCase(this.instructorExpiryDate) != 0 ))
		{
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			
			try {
				r = df.parse(this.instructorExpiryDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.crewMember.getRole().getR2().setExpiryDate(r);
    	}		
//English Test Expiry Date		
		if ((this.englishTestExpiryDate != null) && (empty.compareToIgnoreCase(this.englishTestExpiryDate) != 0))
		{
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			
			try {
				r = df.parse(this.englishTestExpiryDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.crewMember.getRole().getEts().setExpiryDate(r);
		}
//Medical Expiry Date			
		if ((this.medicalExpiryDate != null)&& (empty.compareToIgnoreCase(this.medicalExpiryDate) !=0))
		{
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			
			try {
				r = df.parse(this.medicalExpiryDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.crewMember.getRole().setExpiryDate(r);
		}
//CRM Expiry Date			
		if ((this.crmExpiryDate != null)&& (empty.compareToIgnoreCase(this.crmExpiryDate) != 0))
		{
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			
			try {
				r = df.parse(this.crmExpiryDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.crewMember.getRole().getCrm().setExpiryDate(r);
		}
//DG Expiry Date		
		if ((this.dgExpiryDate != null)&& (empty.compareToIgnoreCase(this.dgExpiryDate) !=0 ))
		{
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			
			try {
				r = df.parse(this.dgExpiryDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.crewMember.getRole().getDg().setExpiryDate(r);
		}
//Huet Expiry Date		
		if ((this.huetExpiryDate != null)&& (empty.compareToIgnoreCase(this.huetExpiryDate) !=0 ))
		{
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			
			try {
				r = df.parse(this.huetExpiryDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.crewMember.getRole().getHuet().setExpiryDate(r);
		}
//Hems Expiry Date		
		if ((this.hemsCertExpiryDate != null) && (empty.compareToIgnoreCase(this.hemsCertExpiryDate) != 0 ))
		{
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			
			try {
				r = df.parse(this.hemsCertExpiryDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.crewMember.getRole().getHemsCert().setExpiryDate(r);
		}	
//LPC Expiry Date		
		if ((this.lpcExpiryDate != null) && (empty.compareToIgnoreCase(this.lpcExpiryDate) != 0 ))
		{
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			
			try {
				r = df.parse(this.lpcExpiryDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.crewMember.getRole().getLpcCert().setExpiryDate(r);
		}			
//OPC Expiry Date		
		if ((this.opcExpiryDate != null) && (empty.compareToIgnoreCase(this.opcExpiryDate) != 0))
		{
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			
			try {
				r = df.parse(this.opcExpiryDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.crewMember.getRole().getOpcCert().setExpiryDate(r);
		}			
//operationsManual Expiry Date		
		if ((this.operationsManualExpiry != null) && (empty.compareToIgnoreCase(this.operationsManualExpiry) != 0 ))
		{
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			
			try {
				r = df.parse(this.operationsManualExpiry);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.crewMember.getRole().getOperationsManualCert().setExpiryDate(r);
		}		
//annualTechManual Expiry Date		
		if ((this.annualTechnicalManual != null) && (empty.compareToIgnoreCase(this.annualTechnicalManual) != 0 ))
		{
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			
			try {
				r = df.parse(this.annualTechnicalManual);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.crewMember.getRole().getAnnualTechnicalManualCert().setExpiryDate(r);
		}		
		
//Route Check Completion Date	
		if ((this.routeCheckExpiryDate != null) && (empty.compareToIgnoreCase(this.routeCheckExpiryDate) != 0))
		{
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			
			try {
				r = df.parse(this.routeCheckExpiryDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.crewMember.getRole().getRoutCheck().setExpiryDate(r);
		}				
	    
//Passport 1 Certificate Expiry Date	
		if ((this.passportCert1ExpiryDate != null) && (empty.compareToIgnoreCase(this.passportCert1ExpiryDate) != 0))
		{
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			
			try {
				r = df.parse(this.passportCert1ExpiryDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (crewMember.getPassport().isEmpty() == false)
			{
			if (crewMember.getPassport().get(0) != null)
			{
			this.crewMember.getPassport().get(0).getCertificate().setExpiryDate(r);
			}
			}
		}
//Passport 2 Certificate Expiry Date	
		if ((this.passportCert2ExpiryDate != null) && (empty.compareToIgnoreCase(this.passportCert2ExpiryDate) != 0))
		{
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			
			try {
				r = df.parse(this.passportCert2ExpiryDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (crewMember.getPassport().isEmpty() == false)
			{
			if (crewMember.getPassport().get(1) != null)
			{
			this.crewMember.getPassport().get(1).getCertificate().setExpiryDate(r);
			}
			}
		}				
//Passport 3 Certificate Expiry Date	
		if ((this.passportCert3ExpiryDate != null) && (empty.compareToIgnoreCase(this.passportCert3ExpiryDate) != 0))
		{
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			
			try {
				r = df.parse(this.passportCert3ExpiryDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (crewMember.getPassport().isEmpty() == false)
			{
			if (crewMember.getPassport().get(2) != null)
			{
			this.crewMember.getPassport().get(2).getCertificate().setExpiryDate(r);
			}
			}
		}			
	    
  }//serDates()
	/*-------------------------------------------------------------------*/
	public String save() throws Exception
	/*-------------------------------------------------------------------*/
	{
		if (this.basePilotAllowance != null)
		{
			if (crewMember != null){this.crewMember.getPayments().setBasePilotAllowance(basePilotAllowance);}
		}
		//set passports
		
		
		ArrayList<Passport> cmPassports = new ArrayList<CrewMember.Passport>();
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
		
		//Set dates, otherwise they're not saved. Must be after the Passports are created. 
		setDates();
		
		
		this.crewMember = manager.saveCrewMember(crewMember);
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
			
	    try{
            if(licenceFile!= null){         
                LOG.info(licenceTags+" "+docfolder);
                String[] tagsArray = licenceTags.split(" ");
                Document doc = new Document();
                doc.setName(licenceFileFileName);                
                doc.setContentType(licenceFileContentType);
                Bookmark b = bookmarkManager.createBookmark(licenceFileFileName, "Document", docfolder+"/"+ licenceFileFileName, tagsArray);
                doc.setBookmark(b);
                docManager.createDocument(doc, docfolder, new FileInputStream(licenceFile), user);
            }
          }
          catch(Exception e){
                LOG.error(e);
          }
	    
	    try{
            if(crmFile!= null){         
                LOG.info(crmTags+" "+docfolder);
                String[] tagsArray = crmTags.split(" ");
                Document doc = new Document();
                doc.setName(crmFileFileName);                
                doc.setContentType(crmFileContentType);
                Bookmark b = bookmarkManager.createBookmark(crmFileFileName, "Document", docfolder+"/"+ crmFileFileName, tagsArray);
                doc.setBookmark(b);
                docManager.createDocument(doc, docfolder, new FileInputStream(crmFile), user);
            }
          }
          catch(Exception e){
                LOG.error(e);
          }
          
          try{
              if(mediFile!= null){         
                  LOG.info(mediTags+" "+docfolder+docfolder+"/"+ mediFileFileName);
                  String[] tagsArray = mediTags.split(" ");
                  Document doc = new Document();
                  doc.setName(mediFileFileName);                
                  doc.setContentType(mediFileContentType);
                  Bookmark b = bookmarkManager.createBookmark(mediFileFileName, "Document", docfolder+"/"+ mediFileFileName, tagsArray);
                  doc.setBookmark(b);
                  docManager.createDocument(doc, docfolder, new FileInputStream(mediFile), user);
              }
            }
            catch(Exception e){
                  LOG.error(e);
            }
          
  	    try{
            if(dgFile!= null){         
                LOG.info(dgTags+" "+docfolder+" "+docfolder+"/"+ dgFileFileName);
                String[] tagsArray = dgTags.split(" ");
                Document doc = new Document();
                doc.setName(dgFileFileName);
                doc.setContentType(dgFileContentType);
                Bookmark b = bookmarkManager.createBookmark(dgFileFileName, "Document", docfolder+"/"+ dgFileFileName, tagsArray);
                doc.setBookmark(b);
                docManager.createDocument(doc, docfolder, new FileInputStream(dgFile), user);
            }
          }
          catch(Exception e){
                LOG.error("DG error: "+e);
                e.printStackTrace();
          }
  	      try{
            if(huetFile!= null){         
                LOG.info(huetTags+" "+docfolder+" "+docfolder+"/"+ huetFileFileName);
                String[] tagsArray = huetTags.split(" ");
                Document doc = new Document();
                doc.setName(huetFileFileName);
                doc.setContentType(huetFileContentType);
                Bookmark b = bookmarkManager.createBookmark(huetFileFileName, "Document", docfolder+"/"+ huetFileFileName, tagsArray);
                doc.setBookmark(b);
                docManager.createDocument(doc, docfolder, new FileInputStream(huetFile), user);
            }
          }
          catch(Exception e){
        	  LOG.error("HUET error: "+e);
              e.printStackTrace();
          }
          try{
              if(hemsCertFile!= null){         
                  LOG.info(hemsCertTags+" "+docfolder+" "+docfolder+"/"+ hemsCertFileFileName);
                  String[] tagsArray = hemsCertTags.split(" ");
                  Document doc = new Document();
                  doc.setName(hemsCertFileFileName);
                  doc.setContentType(hemsCertFileContentType);
                  Bookmark b = bookmarkManager.createBookmark(hemsCertFileFileName, "Document", docfolder+"/"+ hemsCertFileFileName, tagsArray);
                  doc.setBookmark(b);
                  docManager.createDocument(doc, docfolder, new FileInputStream(hemsCertFile), user);
              }
            }
            catch(Exception e){
          	  LOG.error("HEMS Cert error: "+e);
                e.printStackTrace();
            }
//Additional Certificates            
            try{
                if(additionalCertFile!= null){         
                    LOG.info(additionalCertTags+" "+docfolder+" "+docfolder+"/"+ additionalCertFileFileName);
                    String[] tagsArray = additionalCertTags.split(" ");
                    Document doc = new Document();
                    doc.setName(additionalCertFileFileName);
                    doc.setContentType(additionalCertFileContentType);
                    Bookmark b = bookmarkManager.createBookmark(additionalCertFileFileName, "Document", docfolder+"/"+ additionalCertFileFileName, tagsArray);
                    doc.setBookmark(b);
                    docManager.createDocument(doc, docfolder, new FileInputStream(additionalCertFile), user);
                }
              }
              catch(Exception e){
            	  LOG.error("Additional Cert error: "+e);
                  e.printStackTrace();
              }
//LPC Certificates            
              try{
                  if(lpcCertFile!= null){         
                      LOG.info(additionalCertTags+" "+docfolder+" "+docfolder+"/"+ lpcCertFileFileName);
                      String[] tagsArray = lpcCertTags.split(" ");
                      Document doc = new Document();
                      doc.setName(lpcCertFileFileName);
                      doc.setContentType(lpcCertFileContentType);
                      Bookmark b = bookmarkManager.createBookmark(lpcCertFileFileName, "Document", docfolder+"/"+ lpcCertFileFileName, tagsArray);
                      doc.setBookmark(b);
                      docManager.createDocument(doc, docfolder, new FileInputStream(lpcCertFile), user);
                  }
                }
                catch(Exception e){
              	  LOG.error("LPC Cert error: "+e);
                    e.printStackTrace();
                }  
//OPC Certificates            
                try{
                    if(opcCertFile!= null){         
                        LOG.info(opcCertTags+" "+docfolder+" "+docfolder+"/"+ opcCertFileFileName);
                        String[] tagsArray = opcCertTags.split(" ");
                        Document doc = new Document();
                        doc.setName(opcCertFileFileName);
                        doc.setContentType(opcCertFileContentType);
                        Bookmark b = bookmarkManager.createBookmark(opcCertFileFileName, "Document", docfolder+"/"+ opcCertFileFileName, tagsArray);
                        doc.setBookmark(b);
                        docManager.createDocument(doc, docfolder, new FileInputStream(opcCertFile), user);
                    }
                  }
                  catch(Exception e){
                	  LOG.error("OPC Cert error: "+e);
                      e.printStackTrace();
                  }   
//operationsManualCert Certificates            
                  try{
                      if(operationsManualCertFile!= null){         
                          LOG.info(operationsManualCertTags+" "+docfolder+" "+docfolder+"/"+ operationsManualCertFileFileName);
                          String[] tagsArray = operationsManualCertTags.split(" ");
                          Document doc = new Document();
                          doc.setName(operationsManualCertFileFileName);
                          doc.setContentType(operationsManualCertFileContentType);
                          Bookmark b = bookmarkManager.createBookmark(operationsManualCertFileFileName, "Document", docfolder+"/"+ operationsManualCertFileFileName, tagsArray);
                          doc.setBookmark(b);
                          docManager.createDocument(doc, docfolder, new FileInputStream(operationsManualCertFile), user);
                      }
                    }
                    catch(Exception e){
                  	  LOG.error("operationsManualCert Cert error: "+e);
                        e.printStackTrace();
                    }  
//operationsManualCert Certificates            
                    try{
                        if(annualTechnicalManualCertFile!= null){         
                            LOG.info(annualTechnicalManualCertTags+" "+docfolder+" "+docfolder+"/"+ annualTechnicalManualCertFileFileName);
                            String[] tagsArray = annualTechnicalManualCertTags.split(" ");
                            Document doc = new Document();
                            doc.setName(annualTechnicalManualCertFileFileName);
                            doc.setContentType(annualTechnicalManualCertFileContentType);
                            Bookmark b = bookmarkManager.createBookmark(annualTechnicalManualCertFileFileName, "Document", docfolder+"/"+ annualTechnicalManualCertFileFileName, tagsArray);
                            doc.setBookmark(b);
                            docManager.createDocument(doc, docfolder, new FileInputStream(annualTechnicalManualCertFile), user);
                        }
                      }
                      catch(Exception e){
                    	  LOG.error("annualTechnical Cert error: "+e);
                          e.printStackTrace();
                      } 
//Passport 1 {Actual Doc}            
                      try{
                          if(passport1PassFile!= null){         
                              LOG.info(passport1PassFileTags+" "+docfolder+" "+docfolder+"/"+ passport1PassFileFileName);
                              String[] tagsArray = passport1PassFileTags.split(" ");
                              Document doc = new Document();
                              doc.setName(passport1PassFileFileName);
                              doc.setContentType(passport1PassFileFileContentType);
                              Bookmark b = bookmarkManager.createBookmark(passport1PassFileFileName, "Document", docfolder+"/"+ passport1PassFileFileName, tagsArray);
                              doc.setBookmark(b);
                              docManager.createDocument(doc, docfolder, new FileInputStream(passport1PassFile), user);
                          }
                        }
                        catch(Exception e){
                      	  LOG.error("passport1 Pass error: "+e);
                            e.printStackTrace();
                        }  
//Passport 2 {Actual Doc}            
                        try{
                            if(passport2PassFile!= null){         
                                LOG.info(passport2PassFileTags+" "+docfolder+" "+docfolder+"/"+ passport2PassFileFileName);
                                String[] tagsArray = passport2PassFileTags.split(" ");
                                Document doc = new Document();
                                doc.setName(passport2PassFileFileName);
                                doc.setContentType(passport2PassFileFileContentType);
                                Bookmark b = bookmarkManager.createBookmark(passport2PassFileFileName, "Document", docfolder+"/"+ passport2PassFileFileName, tagsArray);
                                doc.setBookmark(b);
                                docManager.createDocument(doc, docfolder, new FileInputStream(passport2PassFile), user);
                            }
                          }
                          catch(Exception e){
                        	  LOG.error("passport2 Pass error: "+e);
                              e.printStackTrace();
                          }  
//Passport 3 {Actual Doc}            
                      try{
                          if(passport3PassFile!= null){         
                              LOG.info(passport3PassFileTags+" "+docfolder+" "+docfolder+"/"+ passport3PassFileFileName);
                              String[] tagsArray = passport3PassFileTags.split(" ");
                              Document doc = new Document();
                              doc.setName(passport3PassFileFileName);
                              doc.setContentType(passport3PassFileFileContentType);
                              Bookmark b = bookmarkManager.createBookmark(passport3PassFileFileName, "Document", docfolder+"/"+ passport3PassFileFileName, tagsArray);
                              doc.setBookmark(b);
                              docManager.createDocument(doc, docfolder, new FileInputStream(passport3PassFile), user);
                          }
                        }
                        catch(Exception e){
                      	  LOG.error(" Pass error: "+e);
                            e.printStackTrace();
                        }      
//Passport 1 Certificates            
                      try{
                          if(passport1CertFile!= null){         
                              LOG.info(passport1CertTags+" "+docfolder+" "+docfolder+"/"+ passport1CertFileFileName);
                              String[] tagsArray = passport1CertTags.split(" ");
                              Document doc = new Document();
                              doc.setName(passport1CertFileFileName);
                              doc.setContentType(passport1CertFileContentType);
                              Bookmark b = bookmarkManager.createBookmark(passport1CertFileFileName, "Document", docfolder+"/"+ passport1CertFileFileName, tagsArray);
                              doc.setBookmark(b);
                              docManager.createDocument(doc, docfolder, new FileInputStream(passport1CertFile), user);
                          }
                        }
                        catch(Exception e){
                      	  LOG.error("passport1 Cert error: "+e);
                            e.printStackTrace();
                        }        
//Passport 2 Certificates            
                        try{
                            if(passport2CertFile!= null){         
                                LOG.info(passport2CertTags+" "+docfolder+" "+docfolder+"/"+ passport2CertFileFileName);
                                String[] tagsArray = passport2CertTags.split(" ");
                                Document doc = new Document();
                                doc.setName(passport2CertFileFileName);
                                doc.setContentType(passport2CertFileContentType);
                                Bookmark b = bookmarkManager.createBookmark(passport2CertFileFileName, "Document", docfolder+"/"+ passport2CertFileFileName, tagsArray);
                                doc.setBookmark(b);
                                docManager.createDocument(doc, docfolder, new FileInputStream(passport2CertFile), user);
                            }
                          }
                          catch(Exception e){
                        	  LOG.error("passport2 Cert error: "+e);
                              e.printStackTrace();
                          }    
//Passport 3 Certificates       
                          try{
                              if(passport3CertFile!= null){         
                                  LOG.info(passport3CertTags+" "+docfolder+" "+docfolder+"/"+ passport3CertFileFileName);
                                  String[] tagsArray = passport3CertTags.split(" ");
                                  Document doc = new Document();
                                  doc.setName(passport3CertFileFileName);
                                  doc.setContentType(passport3CertFileContentType);
                                  Bookmark b = bookmarkManager.createBookmark(passport3CertFileFileName, "Document", docfolder+"/"+ passport3CertFileFileName, tagsArray);
                                  doc.setBookmark(b);
                                  docManager.createDocument(doc, docfolder, new FileInputStream(passport3CertFile), user);
                              }
                            }
                            catch(Exception e){
                          	  LOG.error(" Cert error: "+e);
                                e.printStackTrace();
                            }                                
                                          
              
          try{
              if(flighthoursFile!= null){         
                  LOG.info(flighthoursTags+" "+docfolder+" "+docfolder+"/"+ flighthoursFileFileName);
                  String[] tagsArray = flighthoursTags.split(" ");
                  Document doc = new Document();
                  doc.setName(flighthoursFileFileName);
                  doc.setContentType(flighthoursFileContentType);
                  Bookmark b = bookmarkManager.createBookmark(flighthoursFileFileName, "Document", docfolder+"/"+ flighthoursFileFileName, tagsArray);
                  doc.setBookmark(b);
                  docManager.createDocument(doc, docfolder, new FileInputStream(flighthoursFile), user);
              }
            }
            catch(Exception e){
          	  LOG.error("flighthours error: "+e);
                e.printStackTrace();
            }
       this.crewMember = this.manager.saveCrewMember(crewMember);
	return execute();
	}

	public Integer actualsId;

/*-----------------------------------------------------*/	
	public void prepare() throws Exception
/*-----------------------------------------------------*/	
	{
		this.YNOption = new ArrayList<String>();
		this.YNOption.add("yes");
		this.YNOption.add("no");
		
		if (id == null) {
			crewMember = new CrewMember();
		
		} else {
			crewMember = manager.getCrewMemberByCode(id);
			
			if (switch_role_to!="") {
				crewMember.getRole().setPosition(switch_role_to);
			}
			if (actualsId == null) {
				actuals = new CrewMember.FlightAndDutyActuals(
						crewMember.getPayments().getMonthlyBaseRate(),
						crewMember.getPayments().getAreaAllowance(),
						crewMember.getPayments().getInstructorAllowance(),
						crewMember.getPayments().getDailyAllowance(),
						crewMember.getPayments().getFlightAllowance(),
						crewMember.getPayments().getBasePilotAllowance(),
						crewMember.getPayments().getSafetyLevelAllowance()
						
						
				);
			
			} else {
				actuals = manager.getFlightAndDutyActualsById(actualsId);
				Calendar cal = Calendar.getInstance();
				cal.setTime(actuals.getDate());
				currentMonth = cal.get(Calendar.MONTH);
				currentYear = cal.get(Calendar.YEAR);
				//This needs to be reset so that if the user has unticked the box
				//the correct value is saved.
				
				if (actualsCompleted){}
					//actuals.setPayMonthlyRate(false);
			}
		}
	}
	/*-----------------------------------------------------*/
	@SuppressWarnings("unchecked")
	private void prepareTabs()
	/*------------------------------------------------------------*/
	{
		String idStr = "";
		if (id != null) {
			idStr=id;
			
			this.crewMember = this.manager.getCrewMemberByCode(idStr);
		/*	try{
			List<FlightAndDutyActuals> acts = crewMember.getFlightAndDutyActuals();
			Collections.sort(acts);
			FlightAndDutyActuals lastAct = acts.get(acts.size()-1);
			this.actualsId = lastAct.getId();
			}
			catch(Exception e){}
			*/
    		this.reviewDate = this.crewMember.getRole().getStringReviewDate();
    		
    		try	{this.dateOfBirth = this.crewMember.getPersonal().getStringDateOfBirthDate();}
    		catch (Exception e)	{this.dateOfBirth = "";}
    		
    		try	{this.initialDate = this.crewMember.getRole().getStringInitialDate();}
    		catch (Exception e)	{this.initialDate = "";}
    		
    		try	{this.instructorExpiryDate = this.crewMember.getRole().getR2().getStringExpiryDate();}
    		catch (Exception e)	{this.instructorExpiryDate = "";}
    		
    		try	{this.instrumentExpiryDate = this.crewMember.getRole().getIfr().getStringExpiryDate();}
    		catch (Exception e)	{this.instrumentExpiryDate = "";}
    		
    		try	{this.englishTestExpiryDate = this.crewMember.getRole().getEts().getStringExpiryDate();}
    		catch (Exception e)	{this.englishTestExpiryDate = "";}
    		
    		try {this.medicalExpiryDate = this.crewMember.getRole().getStringExpiryDate();}
    		catch (Exception e)	{this.medicalExpiryDate = "";}
    		
    		try {this.crmExpiryDate = this.crewMember.getRole().getCrm().getStringExpiryDate();}
    		catch (Exception e)	{this.crmExpiryDate = "";}
    		
    		try {this.dgExpiryDate = this.crewMember.getRole().getDg().getStringExpiryDate();}
    		catch (Exception e)	{this.dgExpiryDate = "";}
    		
    		try {this.huetExpiryDate = this.crewMember.getRole().getHuet().getStringExpiryDate();}
    		catch (Exception e)	{this.huetExpiryDate = "";}
    		
    		try {this.hemsCertExpiryDate = this.crewMember.getRole().getHemsCert().getStringExpiryDate();}
    		catch (Exception e)	{this.hemsCertExpiryDate = "";}
    		
    		try {this.lpcExpiryDate = this.crewMember.getRole().getLpcCert().getStringExpiryDate();}
    		catch (Exception e)	{this.lpcExpiryDate = "";}
    		
    		try {this.opcExpiryDate = this.crewMember.getRole().getOpcCert().getStringExpiryDate();}
    		catch (Exception e)	{this.opcExpiryDate = "";}
    		
    		try {this.operationsManualExpiry = this.crewMember.getRole().getOperationsManualCert().getStringExpiryDate();}
    		catch (Exception e)	{this.operationsManualExpiry = "";}
    		
    		try {this.annualTechnicalManual = this.crewMember.getRole().getAnnualTechnicalManualCert().getStringExpiryDate();}
    		catch (Exception e)	{this.annualTechnicalManual = "";}
    		
    		try {this.routeCheckExpiryDate = this.crewMember.getRole().getRoutCheck().getStringExpiryDate();}
    		catch (Exception e)	{this.routeCheckExpiryDate = "";}
			
    		if (crewMember.getPassport().isEmpty()== true)
    	    {
    			//create 3 empty passports for new crewmembers
    	    	Passport temp1 = new Passport();
    	    	Passport temp2 = new Passport();
    	    	Passport temp3 = new Passport();
    	    	ArrayList<Passport> tempList = new ArrayList<Passport>();
    	    	tempList.add(temp1);
    	    	tempList.add(temp2);
    	    	tempList.add(temp3);
    	    	this.crewMember.setPassport(tempList);
    	    }
    		
   /* 		if (crewMember.getPassport().isEmpty()==false)
    		{
    			ArrayList<Passport> tempList = new ArrayList<Passport>();
    			//for existing (old system crew members) that do not have 3 passports - create the missing passports
    			for (int i = 0; i < 3; i++)
    	    	{
    				try 
    				{
    					Passport temp = this.crewMember.getPassport().get(i);
    					tempList.add(temp);
    				}
    				catch(Exception e)
    				{
    					Passport temp = new Passport();
    					tempList.add(temp);
    				}
    	    	}
    			this.crewMember.setPassport(tempList);
    			
    		}
	*/		
    	    if (crewMember.getPassport().isEmpty()==false)
    	    {
    	    	//make sure that the certificates have dates assigned to them. This is for new and old crewmembers.
    	    	for (int i =0; i< this.crewMember.getPassport().size(); i++)
    	    	{
    	    		if (this.crewMember.getPassport().get(i) != null)
    	    		{
    	    			if (i==0) {this.passportCert1ExpiryDate = this.crewMember.getPassport().get(i).getCertificate().getStringExpiryDate();}
    	    			if (i==1) {this.passportCert2ExpiryDate = this.crewMember.getPassport().get(i).getCertificate().getStringExpiryDate();}
    	    			if (i==2) {this.passportCert3ExpiryDate = this.crewMember.getPassport().get(i).getCertificate().getStringExpiryDate();}
    	    		}
    	    	}
    	    }
		}

		
		
		List<Tab> tabList = new ArrayList<Tab>();
		
		if(user.hasRead("crewPersonal")){
		Tab personalTab = new Tab("Personal", "crewMember.action?id="+idStr, tab.equals("personal"));
		tabList.add(personalTab);
		}
		
		if(user.hasRead("crewBank")){
		Tab bankingTab = new Tab("Banking", "crewMember.action?tab=banking&id="+idStr, tab.equals("banking"));
		tabList.add(bankingTab);
		}
		
		if(user.hasRead("crewRole")){
		Tab roleTab = new Tab("Role", "crewMember.action?tab=role&id="+idStr, tab.equals("role"));
		tabList.add(roleTab);
		
		try {
			folder = docManager.getFolderByPath("/crew/"+id, user);
			passport1Cert    = folder.getDocumentByTag("passportVisa1");
	        passport2Cert    = folder.getDocumentByTag("passportVisa2");
	        passport3Cert    = folder.getDocumentByTag("passportVisa3");
	        passport3Pass    = folder.getDocumentByTag("passport2");
	        passport2Pass    = folder.getDocumentByTag("passport1");
	        passport1Pass    = folder.getDocumentByTag("passport0");
		} catch (InsufficientPrivilagesException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        
		}
		
		if(user.hasRead("crewPay")){
		Tab paymentsTab = new Tab("Payments", "crewMember.action?tab=payments&id="+idStr, tab.equals("payments"));
		tabList.add(paymentsTab);
		}
		
		if(user.hasRead("crewPDW")){
		Tab flightAndDutyTab = new Tab("PDW", "crewMember.action?tab=flight&id="+idStr, tab.equals("flight"));
		tabList.add(flightAndDutyTab);
		}
		
		if(user.hasWrite("crewCont")){
		Tab hours = new Tab("On Contract", "crewMember.action?tab=hours&id="+idStr, tab.equals("hours"));
		tabList.add(hours);
		}
		
		if(user.hasRead("crewDoc")){
		Tab documentsTab = new Tab("Additional Documents", "crewMember!docs.action?tab=documents&id="+idStr, tab.equals("documents"));
		tabList.add(documentsTab);
		}
		
		if(user.hasRead("crewRev")){
		if (user.hasPermission("ManagerView")){
			Tab reviewTab = new Tab("Comments", "crewMember.action?tab=review&id="+idStr, tab.equals("review"));
			tabList.add(reviewTab);
		}
		}
		
		if(user.hasRead("crewAssign")){
		if (user.hasPermission("ManagerView")){
		    Tab assignmentsTab = new Tab("Rostering", "crewMember!assignments.action?tab=assignments&id="+idStr, tab.equals("assignments"));
		    tabList.add(assignmentsTab);
        }
		}

		tableTabs = new Tab[tabList.size()];
		int count = 0;
		for(Tab tab : tabList){
			tableTabs[count] = tab;
			count++;
		}
	}
	/*------------------------------------------------------------*/	

    public String fromPage = "";
    /*------------------------------------------------------------*/
	public String assignments()
	/*------------------------------------------------------------*/
	{
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
	/*------------------------------------------------------------*/
	public void setUser(User arg0)
	/*------------------------------------------------------------*/
	{
		user = arg0;
	}
	/*------------------------------------------------------------*/
	public User getUser()
	/*------------------------------------------------------------*/
	{
		return user;
	}
	/*------------------------------------------------------------*/
	@Permissions("Approve")
	public String review() throws Exception
	/*------------------------------------------------------------*/
	{
		String approvalKey = approvalsManager.review(crewMember.getApprovalGroup().getId(), 1000*60*5);
		ServletActionContext.getRequest().getSession().setAttribute("approvalKey-"+crewMember.getApprovalGroup().getId(), approvalKey);
		prepare();
		notificationMessage = "Crew Member locked for 5 minutes";
		return execute();
	}
	/*------------------------------------------------------------*/
	@Permissions("Approve")
	public String approve() throws Exception
	/*------------------------------------------------------------*/
	{
		String approvalKey = (String) ServletActionContext.getRequest().getSession().getAttribute("approvalKey-"+crewMember.getApprovalGroup().getId());
		approvalsManager.approve(crewMember.getApprovalGroup().getId(), approvalKey);
		prepare();
		notificationMessage = "Crew Member has been approved";
		return execute();
	}
	/*------------------------------------------------------------*/
	@Permissions("Approve")
	public String open() throws Exception
	/*------------------------------------------------------------*/
	{
		String approvalKey = (String) ServletActionContext.getRequest().getSession().getAttribute("approvalKey-"+crewMember.getApprovalGroup().getId());
		approvalsManager.open(crewMember.getApprovalGroup().getId(), approvalKey);
		prepare();
		notificationMessage = "Crew Member has been opened for editing";
		return execute();
	}
	/*------------------------------------------------------------*/
}
