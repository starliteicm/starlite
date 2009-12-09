<#include "/starlite.ftl">
<html>
<head>
  <title>${crewMember.personal.firstName!} ${crewMember.personal.lastName!}</title>
  <@enableJQuery/>
  <@enableDatePickers/>
  <@enableHelp/>
<script language="javascript">
  function validate(){            
        var title = document.getElementById('title').options[document.getElementById('title').selectedIndex].value;                                                       
        var firstname          = $("#firstname").val();
        var lastname           = $("#lastname").val();
        var countryofissue     = $("#countryofissue").val();
        var passportno         = $("#passportno").val();        
        var expiry             = $("#expiry").val();
        var address1           = $("#address1").val();
        var address2           = $("#address2").val();
        var address3           = $("#address3").val();
        var address4           = $("#address4").val();
        var postaladdress      = $("#postaladdress").val();
        var postaltown         = $("#postaltown").val();
        var postalcode         = $("#postalcode").val();
        var mobilephone        = $("#mobilephone").val();
        var homephone          = $("#homephone").val();
        var email              = $("#email").val();        
        var gender = document.getElementById('gender').options[document.getElementById('gender').selectedIndex].value;        
        var dob                = $("#dob").val();
        var nationality        = $("#nationality").val();
        var maritalstatus      = document.getElementById('maritalstatus').options[document.getElementById('maritalstatus').selectedIndex].value;        
        var nokfirstname       = $("#nokfirstname").val();
        var noklastname        = $("#noklastname").val();
        var nokmobilephone     = $("#nokmobilephone").val();
        var nokhomephone       = $("#nokhomephone").val();
        var nokrelation        = $("#nokrelation").val();
        var nokaddress1        = $("#nokaddress1").val();
        var nokaddress2        = $("#nokaddress2").val();
        var nokaddress3        = $("#nokaddress3").val();
        var nokaddress4        = $("#nokaddress4").val();
        var ecdcontactname     = $("#ecdcontactname").val();
        var ecdcontactrelation = $("#ecdcontactrelation").val();        
        
        var errormsg = "<b> The following mandatory fields are blank: </b><br>";
        var error    = 0;
        
        if(title == ""){ errormsg += "Title, "; error=1;}
        if(firstname == ""){ errormsg += "First name, "; error=1;}
        if(lastname   == ""){ errormsg += "Last name, "; error=1;}      
        if(countryofissue== ""){ errormsg += "Country of issue, "; error=1;}
        if(passportno  == ""){ errormsg += "Passport number, "; error=1;}
        if(expiry  == ""){ errormsg += "Passport expiry,   "; error=1;}
        if(address1  == ""){ errormsg += "Address 1, "; error=1;}
        if(address2  == ""){ errormsg += "Address 2, "; error=1;}
        if(address3  == ""){ errormsg += "Address 3, "; error=1;}
        if(address4  == ""){ errormsg += "Address 4, "; error=1;}
        if(postaladdress  == ""){ errormsg += "Postal address, "; error=1;}
        if(postaltown  == ""){ errormsg += "Postal town, "; error=1;}
        if(postalcode  == ""){ errormsg += "Postal code, "; error=1;}
        if(mobilephone  == ""){ errormsg += "Mobile phone, "; error=1;}
        if(homephone  == ""){ errormsg += "Home phone, "; error=1;}
        if(email  == ""){ errormsg += "Email, "; error=1;}
        if(gender  == ""){ errormsg += "Gender, "; error=1;}
        if(dob  == ""){ errormsg += "Date of birth, "; error=1;}
        if(nationality  == ""){ errormsg += "Nationality, "; error=1;}
        if(maritalstatus  == ""){ errormsg += "Marital status, "; error=1;}
        if(nokfirstname  == ""){ errormsg += "Next of kin's first name, "; error=1;}
        if(noklastname  == ""){ errormsg += "Next of kin's second name, "; error=1;}
        if(nokmobilephone  == ""){ errormsg += "Next of kin's mobile number, "; error=1;}
        if(nokhomephone  == ""){ errormsg += "Next of kin's home phone, "; error=1;}
        if(nokrelation  == ""){ errormsg += "Relation to next of kin, "; error=1;}
        if(nokaddress1  == ""){ errormsg += "Next of kin's address 1, "; error=1;}
        if(nokaddress2  == ""){ errormsg += "Next of kin's address 2, "; error=1;}
        if(nokaddress3  == ""){ errormsg += "Next of kin's address 3, "; error=1;}
        if(nokaddress4  == ""){ errormsg += "Next of kin's address 4, "; error=1;}
        if(ecdcontactname  == ""){ errormsg += "Emergency contact's name, "; error=1;}
        if(ecdcontactrelation  == ""){ errormsg += "Relation to emergency contact, "; error=1;}
                
        if(error==1){ 
        errormsg=errormsg.substring(0,errormsg.length-2);        
        $("#msg-error").html(errormsg); return false;
        }else{
        $("#msg-error").html(""); document.forms.personalform.submit(); 
        }                                                               
  }
</script>

<script type="text/javascript">

var passportexpired=0;

//CHECK LICENCE EXPIRY DATE
//this adds a leading zero if the day/month is one digit e.g 1 becomes 01
function addZero(tc){
   var timeConstruct=""+tc; //force timeConstruct into string 
   if(timeConstruct.length==1){
      timeConstruct =  "0" + timeConstruct;  
      return timeConstruct;
   }else{
      return timeConstruct;                    
 }
}

function validityPeriod(datefield,msg){ 
  
   //SET UP VARIABLES
   var dateinput  = "#"    + datefield;
   var messagediv = "#msg-" + datefield;
   var message    = "<font color='orange'>" + msg + "</font>";       
                       
   //PROCESS EXPIRY DATE
   var expiry = $(dateinput).val();
   if (expiry==null){
   }else{   
   var exp = expiry.split('/');
   
   //add leading zeros to day and month if needed   
   expirymonth = addZero(exp[1]);
   expiryday   = addZero(exp[0]);
   expiryyear  = exp[2];
             
   //concatenate date and convert to integer
   reversedatestr = expiryyear + expirymonth + expiryday;   
   reversedate=parseInt(reversedatestr);
   
   //GENERATE AND PROCESS CURRENT DATE
   var date = new Date();
   var date6MonthsFromNow = new Date(date.getTime() + (182*24*60*60*1000));
   
   var m = date6MonthsFromNow.getMonth()+1  + "";
   var d = date6MonthsFromNow.getDate()     + "";
   var y = date6MonthsFromNow.getFullYear() + "";
   
   //add leading zeros to day and month if needed
   currentday   = addZero(d); 
   currentmonth = addZero(m);
   currentyear  = y;
       
   //concatenate date and convert to integer
   current_datestr = (currentyear + "" + currentmonth + "" + currentday);      
   current_date=parseInt(current_datestr);
   
   //COMPARE CURRENT DATE AND EXPIRY DATE THEN SHOW OR HIDE MESSAGE   
   if(current_date > reversedate){ $(messagediv).html(message); }else{$(messagediv).html();}        
   }
  }

function validateDate(datefield,msg){ 
  
   //SET UP VARIABLES
   var dateinput  = "#"    + datefield;
   var messagediv = "#msg-" + datefield;
   var message    = "<font color='red'>" + msg + "</font>";       
                       
   //PROCESS EXPIRY DATE
   var expiry = $(dateinput).val();
   if (expiry==null){
   }else{   
   var exp = expiry.split('/');
   
   //add leading zeros to day and month if needed   
   expirymonth = addZero(exp[1]);
   expiryday   = addZero(exp[0]);
   expiryyear  = exp[2];
   
   //concatenate date and convert to integer
   reversedatestr = expiryyear + expirymonth + expiryday;   
   reversedate=parseInt(reversedatestr);
   
   //GENERATE AND PROCESS CURRENT DATE
   var date = new Date();
   var m = date.getMonth()+1  + "";
   var d = date.getDate()     + "";
   var y = date.getFullYear() + "";
   
   //add leading zeros to day and month if needed
   currentday   = addZero(d); 
   currentmonth = addZero(m);
   currentyear  = y;
   
   //concatenate date and convert to integer
   current_datestr = (currentyear + "" + currentmonth + "" + currentday);      
   current_date=parseInt(current_datestr);
   
   //COMPARE CURRENT DATE AND EXPIRY DATE THEN SHOW OR HIDE MESSAGE   
   if(current_date > reversedate){ $(messagediv).html(message); passportexpired=1; }else{$(messagediv).html(); passportexpired=0; }        
   }
   }
</script>

<script type="text/javascript">

//ONLOAD FUNCTION   
$("document").ready(function() {                                
   validateDate("passportsExpiryDate","Passport expired");
   if(passportexpired==0){      
      validityPeriod("passportsExpiryDate","Passport nearly expired");
   }
});    
   
</script>

  <style type="text/css"> 
  
    .star{
       color:red;
       }
       
     #msg-error{
       font-size:12px;
       color:red;
       padding:5px;
       text-align:center;       
     }
     
  </style>

</head>

<body>

	<#if crewMember.approvalGroup.approvalStatus.toString() == "UNDER_REVIEW">
		<h3>Locked</h3>
	</#if>
	
	<#if crewMember.approvalGroup.approvalStatus.toString() == "APPROVED">
		<h3>Approved</h3>
	</#if>
	
	<#if user.hasPermission("Approve")>
	<div id="toolbar">
		<#if crewMember.approvalGroup.approvalStatus.toString() == "OPEN_FOR_EDITING">
		<a href="${request.contextPath}/crewMember!review.action?id=${id}&tab=${tab}"><img src="${request.contextPath}/images/icons/lock.png"/>Begin Approval</a>
		</#if>
		<#if crewMember.approvalGroup.approvalStatus.toString() == "UNDER_REVIEW">
		<a href="${request.contextPath}/crewMember!approve.action?id=${id}&tab=${tab}"><img src="${request.contextPath}/images/icons/accept.png"/>Approve</a>
		<a href="${request.contextPath}/crewMember!open.action?id=${id}&tab=${tab}"><img src="${request.contextPath}/images/icons/lock_open.png"/>Re-Open</a>
		</#if>
		<#if crewMember.approvalGroup.approvalStatus.toString() == "APPROVED">
		<a href="${request.contextPath}/crewMember!open.action?id=${id}&tab=${tab}"><img src="${request.contextPath}/images/icons/lock_open.png"/>Re-Open</a>
		</#if>
		<hr class="clear"/>
	</div>
	<br/>
	</#if>

	<@subTabs/>
		
	<#if readOnly>
	<form action="#" method="POST" class="smart readonly" style="clear:left;">
	<#else>
	<form action="crewMember!save.action" name="personalform" method="POST" class="smart" style="clear:left;" enctype="multipart/form-data">
	</#if>
		<input type="hidden" name="id" value="${id!}"/>
		<input type="hidden" name="crewMember.id" value="${crewMember.code!}"/>
		<input type="hidden" name="tab" value="personal"/>
		
		<div style="padding-top:25px;position:absolute;left:950px;"><button type="button" onclick="window.location='crewMember!profile.action?id=${crewMember.code!}';" class="smooth" style="float:right; margin-right:10px; margin-bottom: 4px;"><img src="images/icons/printer.png"/> Print</button></div>
		
		<div style="float:left; width: 500px;">
		<fieldset>
		<legend>Photo</legend>
		<div class="fm-opt" style="height:120px;">
		    <label for="image" style="float:left;">&nbsp;</label>
		    <div style="float:left;padding:10px;margin:10px;border:1px solid silver;width:100px;height:100px">
		        <img name="image" src="crewMember!photo.action?id=${id!}" style="width:100px;height:100px;">
		    </div>
		</div>
		<br/>
		<div style="">
		    <div class="fm-opt">
                <label for="photo">Upload:</label>
                <input name="document" type="file" value=""/>
                <input type="hidden" name="tags" value="photo">
                <input type="hidden" name="docfolder" value="/crew/${id!}"/>
            </div>
		</div>		
		</fieldset>

		<fieldset>
			<legend>Name</legend>
			<div class="fm-opt">
				<label for="crewMember.personal.title"><span class="star">*</span>Title:</label>
				<#assign title=crewMember.personal.title!/>
	    		<select name="crewMember.personal.title" id="title">
	    		    <option> 
	    			<option value="Mr" <#if title == 'Mr'>selected</#if> >Mr
	    			<option value="Master" <#if title == 'Master'>selected</#if> >Master
	    			<option value="Mrs" <#if title == 'Mrs'>selected</#if> >Mrs
	    			<option value="Miss" <#if title == 'Miss'>selected</#if> >Miss
	    			<option value="Ms" <#if title == 'Ms'>selected</#if> >Ms
	    			<option value="Doctor" <#if title == 'Doctor'>selected</#if> >Doctor
	    			<option value="Professor" <#if title == 'Professor'>selected</#if> >Professor
	    			<option value="Sir" <#if title == 'Sir'>selected</#if> >Sir
	    		</select>
	    		<br/>
	    	</div>
			<div class="fm-opt">
				<label for="crewMember.personal.firstName"><span class="star">*</span>First Name:</label>
				<input name="crewMember.personal.firstName" id="firstname" type="text" value="${crewMember.personal.firstName!}"/>
			</div>
			<div class="fm-opt">
				<label for="crewMember.personal.secondName">Second Name:</label>
				<input name="crewMember.personal.secondName" type="text" value="${crewMember.personal.secondName!}"/>
			</div>
			<div class="fm-opt">
				<label for="crewMember.personal.lastName"><span class="star">*</span>Last Name:</label>
				<input name="crewMember.personal.lastName" id="lastname" type="text" value="${crewMember.personal.lastName!}"/>
			</div>
			<div class="fm-opt">
                <label for="crewMember.personal.preferedName">Prefered Name:</label>
                <input name="crewMember.personal.preferedName" type="text" value="${crewMember.personal.preferedName!}"/>
            </div>
		</fieldset>
		
	    <!--<fieldset>
			<legend>Passport</legend>
			<div class="fm-opt">
				<label for="crewMember.personal.passportCountry"><span class="star">*</span>Country of Issue:</label>
				<input name="crewMember.personal.passportCountry" id="countryofissue" type="text" value="${crewMember.personal.passportCountry!}"/>
			</div>
			<div class="fm-opt">
				<label for="crewMember.personal.passportNumber"><span class="star">*</span>Passport Number:</label>
				<input name="crewMember.personal.passportNumber" id="passportno" type="text" value="${crewMember.personal.passportNumber!}"/>
			</div>

			<div class="fm-opt">
				<label for="crewMember.personal.passportExpiryDate"><span class="star">*</span>Expiry Date:</label>
				<input name="crewMember.personal.passportExpiryDate" id="expiry" class="date-pick" type="text" value="<#if crewMember.personal.passportExpiryDate??>${crewMember.personal.passportExpiryDate?string('dd/MM/yyyy')}</#if>"/>				
			</div>
			
			<div class="fm-opt">
                <label for="crewMember.personal.passportNumber">Upload:</label>
                <input name="passport" type="file" value=""/>
                <input type="hidden" name="passportTags" value="passport">                
            </div>
		</fieldset>-->
		
		<fieldset>
            <legend>Passports
            <img class="tooltip" title="Passports: <br/><br/> To Add a passport fill in all the required fields then save.<br/><br/> To Remove a passport, clear any of the fields and then save."  style="background-color:white;cursor:help;position:absolute;padding:10px;padding-top:0px;"  src="images/icons/info.png"/>
            </legend>
		
		<#assign count=0>
		<#list crewMember.passport as passport>
		
		<#if count != 0>
		<div style="margin-top:20px;margin-left:10px;width:100%;border-top:1px dotted silver;">&nbsp;</div>      
		</#if>
		
		<#assign count=count +1>
		
		<input type="hidden" name="passportsId" value="${passport.id!}">
		
		<div class="fm-opt">
		  <label for="passportsCountry"><span class="star">*</span>Country of Issue:</label>
		  <input type="text" name="passportsCountry" value="${passport.country!}" />
		</div>
		<div class="fm-opt">
		  <label for="passportsNumber"><span class="star">*</span>Passport Number:</label>
		  <input type="text" name="passportsNumber" value="${passport.passportNumber!}" />
		</div>
		<div class="fm-opt">
          <label for="passportsExpiryDate" ><span class="star">*</span>Expiry Date:</label>
		  <#if passport.expiryDate??>
		    <input type="text" id="passportsExpiryDate" name="passportsExpiryDate" class="date-pick" value="${passport.expiryDate?string('dd/MM/yyyy')}" />
		  <#else>
		    <input type="text" id="passportsExpiryDate" name="passportsExpiryDate" class="date-pick" value="" />
		  </#if>
		</div>
				<div class="fm-opt" id="msg-passportsExpiryDate" style="margin-left:90px; font-weight: bold;"></div>     			  
  <div class="fm-opt">
          <label for="passports"><span class="star">*</span>Upload:</label>
          <input type="file" name="passports" value="" />
          <input type="hidden" name="passportsTags" value="passport" />
        </div>


		</#list>
		
		<div style="margin-top:20px;margin-left:10px;width:100%;border-top:1px dotted silver;">&nbsp;</div>      
	    
	    <input type="hidden" name="passportsId" value="">
	    
	    <div class="fm-opt">
          <label for="passportsCountry"><span class="star">*</span>Country of Issue:</label>
          <input type="text" name="passportsCountry" value="" />
        </div>
        <div class="fm-opt">
          <label for="passportsNumber"><span class="star">*</span>Passport Number:</label>
          <input type="text" name="passportsNumber" value="" />
        </div>
        <div class="fm-opt">
          <label for="passportsExpiryDate"><span class="star">*</span>Expiry Date:</label>
          <input type="text" name="passportsExpiryDate" class="date-pick" value="" />
        </div>
        <div class="fm-opt">
          <label for="passports"><span class="star">*</span>Upload:</label>
          <input type="file" name="passports" value="" />
          <input type="hidden" name="passportsTags" value="passport" />
        </div>	
        
        
		
		</fieldset>
		
		<fieldset>
			<legend>Address</legend>
			<div class="fm-opt">
				<label for="crewMember.personal.address1"><span class="star">*</span>Line 1:</label>
				<input name="crewMember.personal.address1" id="address1" type="text" value="${crewMember.personal.address1!}"/>
			</div>
			<div class="fm-opt">
				<label for="crewMember.personal.address2"><span class="star">*</span>Line 2:</label>
				<input name="crewMember.personal.address2" id="address2" type="text" value="${crewMember.personal.address2!}"/>
			</div>
			<div class="fm-opt">
				<label for="crewMember.personal.address3"><span class="star">*</span>Line 3:</label>
				<input name="crewMember.personal.address3" id="address3" type="text" value="${crewMember.personal.address3!}"/>
			</div>
			<div class="fm-opt">
				<label for="crewMember.personal.address4"><span class="star">*</span>Line 4:</label>
				<input name="crewMember.personal.address4" id="address4" type="text" value="${crewMember.personal.address4!}"/>
			</div>
			<div class="fm-opt">
				<label for="crewMember.personal.address5">Line 5:</label>
				<input name="crewMember.personal.address5" type="text" value="${crewMember.personal.address5!}"/>
			</div>
			<br/>
			<div class="fm-opt">
				<label for="crewMember.personal.postalAddress"><span class="star">*</span>Postal Address:</label>
				<input name="crewMember.personal.postalAddress" id="postaladdress" type="text" value="${crewMember.personal.postalAddress!}"/>
			</div>
			<div class="fm-opt">
				<label for="crewMember.personal.postalTown"><span class="star">*</span>Postal Town:</label>
				<input name="crewMember.personal.postalTown" id="postaltown" type="text" value="${crewMember.personal.postalTown!}"/>
			</div>
			<div class="fm-opt">
				<label for="crewMember.personal.postalCountry">Postal Country:</label>
				<input name="crewMember.personal.postalCountry" type="text" value="${crewMember.personal.postalCountry!}"/>
			</div>
			<div class="fm-opt">
				<label for="crewMember.personal.postalCode"><span class="star">*</span>Postal Code:</label>
				<input name="crewMember.personal.postalCode" id="postalcode" type="text" value="${crewMember.personal.postalCode!}"/>
			</div>
		</fieldset>
		<fieldset>
			<legend>Contact Details</legend>
			<div class="fm-opt">
				<label for="crewMember.personal.mobilePhone"><span class="star">*</span>Mobile Phone:</label>
				<input name="crewMember.personal.mobilePhone" id="mobilephone" type="text" value="${crewMember.personal.mobilePhone!}"/>
			</div>
			<div class="fm-opt">
				<label for="crewMember.personal.mobilePhone2">Alt. Mobile Phone:</label>
				<input name="crewMember.personal.mobilePhone2" type="text" value="${crewMember.personal.mobilePhone2!}"/>
			</div>
			<div class="fm-opt">
				<label for="crewMember.personal.homePhone"><span class="star">*</span>Home Phone:</label>
				<input name="crewMember.personal.homePhone" id="homephone" type="text" value="${crewMember.personal.homePhone!}"/>
			</div>
			<div class="fm-opt">
				<label for="crewMember.personal.homeFax">Home Fax:</label>
				<input name="crewMember.personal.homeFax" type="text" value="${crewMember.personal.homeFax!}"/>
			</div>
			<div class="fm-opt">
				<label for="crewMember.personal.businessPhone">Business Phone:</label>
				<input name="crewMember.personal.businessPhone" type="text" value="${crewMember.personal.businessPhone!}"/>
			</div>
			<div class="fm-opt">
				<label for="crewMember.personal.businessFax">Business Fax:</label>
				<input name="crewMember.personal.businessFax" type="text" value="${crewMember.personal.businessFax!}"/>
			</div>
			<div class="fm-opt">
				<label for="crewMember.personal.email"><span class="star">*</span>Email:</label>
				<input name="crewMember.personal.email" id="email" type="text" value="${crewMember.personal.email!}"/>
			</div>
			<div class="fm-opt">
				<label for="crewMember.personal.alternateEmail">Alt. Email:</label>
				<input name="crewMember.personal.alternateEmail" type="text" value="${crewMember.personal.alternateEmail!}"/>
			</div>
		</fieldset>	
		
		<fieldset>
		    <legend>Medical</legend>
		    <div class="fm-opt">
                <label for="crewMember.role.medicalAid">Medical Aid:</label>
                <input name="crewMember.role.medicalAid" type="text" value="${crewMember.role.medicalAid!}"/>
            </div>
            <div class="fm-opt">
                <label for="crewMember.role.medicalAidNumber">Aid Number:</label>
                <input name="crewMember.role.medicalAidNumber" type="text" value="${crewMember.role.medicalAidNumber!}"/>
            </div>
        </fieldset>     
		
		
		</div>
		<div style="float:left; width: 500px;">
		<fieldset>
			<legend>Personal</legend>
			<div class="fm-opt">
				<label for="crewMember.personal.status"><span class="star">*</span>Gender:</label>
				<#assign gender=crewMember.personal.gender!/>
				<select name="crewMember.personal.gender" id="gender">
					<option>
					<option value="Male" <#if gender == 'Male'>selected</#if>>Male
					<option value="Female" <#if gender == 'Female'>selected</#if>>Female
					<option value="Other" <#if gender == 'Other'>selected</#if>>Other
				</select>
			</div>
			<div class="fm-opt">
				<label for="crewMember.personal.dateOfBirth"><span class="star">*</span>Date Of Birth:</label>
				<input name="crewMember.personal.dateOfBirth" id="dob" type="text" class="date-pick" value="<#if crewMember.personal.dateOfBirth??>${crewMember.personal.dateOfBirth?string('dd/MM/yyyy')}</#if>"/>
			</div>
			<div class="fm-opt">
				<label for="crewMember.personal.nationality"><span class="star">*</span>Nationality:</label>
				<input name="crewMember.personal.nationality" id="nationality" type="text" value="${crewMember.personal.nationality!}"/>
			</div>
			<div class="fm-opt">
				<label for="crewMember.personal.status"><span class="star">*</span>Marital Status:</label>
				<#assign status=crewMember.personal.status!/>
				<select name="crewMember.personal.status" id="maritalstatus" >
					<option>
					<option value="Divorced" <#if status == 'Divorced'>selected</#if>>Divorced
					<option value="Engaged" <#if status == 'Engaged'>selected</#if>>Engaged
					<option value="Married" <#if status == 'Married'>selected</#if>>Married
					<option value="Single" <#if status == 'Single'>selected</#if>>Single
					<option value="Widowed" <#if status == 'Widowed'>selected</#if>>Widowed
				</select>
			</div>
			<div class="fm-opt">
				<label for="crewMember.personal.idNumber">ID Number:</label>
				<input name="crewMember.personal.idNumber" type="text" value="${crewMember.personal.idNumber!}"/>
			</div>
		</fieldset>

        <fieldset>
            <legend>Driving Licence</legend>
            <div class="fm-opt">
                <label for="crewMember.role.drivingLicenceNumber">Driving Licence:</label>
                <input name="crewMember.role.drivingLicenceNumber" type="text" value="${crewMember.role.drivingLicenceNumber!}"/>
            </div>
            <div class="fm-opt">
                <label for="crewMember.role.drivingLicenceIssued">Driving Licence Issued:</label>
                <input name="crewMember.role.drivingLicenceIssued" type="text" value="${crewMember.role.drivingLicenceIssued!}"/>
            </div>
        </fieldset>

		<fieldset>
			<legend>Next Of Kin</legend>
			<div class="fm-opt">
				<label for="crewMember.personal.nextOfKinFirstName"><span class="star">*</span>First Name:</label>
				<input name="crewMember.personal.nextOfKinFirstName" id="nokfirstname" type="text" value="${crewMember.personal.nextOfKinFirstName!}"/>
			</div>
			<div class="fm-opt">
				<label for="crewMember.personal.nextOfKinLastName"><span class="star">*</span>Last Name:</label>
				<input name="crewMember.personal.nextOfKinLastName" id="noklastname" type="text" value="${crewMember.personal.nextOfKinLastName!}"/>
			</div>
			<div class="fm-opt">
				<label for="crewMember.personal.nextOfKinMobilePhone"><span class="star">*</span>Mobile Phone</label>
				<input name="crewMember.personal.nextOfKinMobilePhone" id="nokmobilephone" type="text" value="${crewMember.personal.nextOfKinMobilePhone!}"/>
			</div>
			<div class="fm-opt">
				<label for="crewMember.personal.nextOfKinHomePhone"><span class="star">*</span>Home Phone:</label>
				<input name="crewMember.personal.nextOfKinHomePhone" id="nohomephone" type="text" value="${crewMember.personal.nextOfKinHomePhone!}"/>
			</div>
			<div class="fm-opt">
				<label for="crewMember.personal.nextOfKinRelation"><span class="star">*</span>Relation:</label>
				<input name="crewMember.personal.nextOfKinRelation" id="nokrelation" type="text" value="${crewMember.personal.nextOfKinRelation!}"/>
			</div>
			<br/>
			<div class="fm-opt">
				<label for="crewMember.personal.nextOfKinAddress1"><span class="star">*</span>Address Line 1:</label>
				<input name="crewMember.personal.nextOfKinAddress1" id="nokaddress1" type="text" value="${crewMember.personal.nextOfKinAddress1!}"/>
			</div>
			<div class="fm-opt">
				<label for="crewMember.personal.nextOfKinAddress2"><span class="star">*</span>Address Line 2:</label>
				<input name="crewMember.personal.nextOfKinAddress2" id="nokaddress2" type="text" value="${crewMember.personal.nextOfKinAddress2!}"/>
			</div>
			<div class="fm-opt">
				<label for="crewMember.personal.nextOfKinAddress3"><span class="star">*</span>Address Line 3:</label>
				<input name="crewMember.personal.nextOfKinAddress3" id="nokaddress3" type="text" value="${crewMember.personal.nextOfKinAddress3!}"/>
			</div>
			<div class="fm-opt">
				<label for="crewMember.personal.nextOfKinAddress4"><span class="star">*</span>Address Line 4:</label>
				<input name="crewMember.personal.nextOfKinAddress4" id="nokaddress4" type="text" value="${crewMember.personal.nextOfKinAddress4!}"/>
			</div>
			<div class="fm-opt">
				<label for="crewMember.personal.nextOfKinAddress5">Address Line 5:</label>
				<input name="crewMember.personal.nextOfKinAddress5" type="text" value="${crewMember.personal.nextOfKinAddress5!}"/>
			</div>
		</fieldset>
		<fieldset>
			<legend>Emergency Contact Details (Different from Next of Kin)</legend>
			<div class="fm-opt">
				<label for="crewMember.personal.emergencyContactName"><span class="star">*</span>Contact Name</label>
				<input name="crewMember.personal.emergencyContactName" id="ecdcontactname" type="text" value="${crewMember.personal.emergencyContactName!}"/>
			</div>
			<div class="fm-opt">
				<label for="crewMember.personal.emergencyContactRelationship"><span class="star">*</span>Contact Relation</label>
				<input name="crewMember.personal.emergencyContactRelationship" id="ecdcontactrelation" type="text" value="${crewMember.personal.emergencyContactRelationship!}"/>
			</div>
			<div class="fm-opt">
				<label for="crewMember.personal.emergencyContactNumber">Contact Number</label>
				<input name="crewMember.personal.emergencyContactNumber" type="text" value="${crewMember.personal.emergencyContactNumber!}"/>
			</div>
			<br/>
			<div class="fm-opt">
				<label for="crewMember.personal.alternativeEmergencyContactName">Alt. Contact Name</label>
				<input name="crewMember.personal.alternativeEmergencyContactName" type="text" value="${crewMember.personal.alternativeEmergencyContactName!}"/>
			</div>
			<div class="fm-opt">
				<label for="crewMember.personal.alternativeEmergencyContactRelationship">Alt. Contact Relation</label>
				<input name="crewMember.personal.alternativeEmergencyContactRelationship" type="text" value="${crewMember.personal.alternativeEmergencyContactRelationship!}"/>
			</div>
			<div class="fm-opt">
				<label for="crewMember.personal.alternativeEmergencyContactNumber">Alt. Contact Number</label>
				<input name="crewMember.personal.alternativeEmergencyContactNumber" type="text" value="${crewMember.personal.alternativeEmergencyContactNumber!}"/>
			</div>
		</fieldset>
		</div>
		<hr class="clear"/>
		<#if !readOnly>
		<button type="button" onclick="validate();" class="smooth" style="float:right; margin-right:10px; margin-bottom: 4px;"><img src="images/icons/pencil.png"/> Save</button>
		<div id="msg-error"></div>
  <hr class="clear"/>
		</#if>
	</form>
</body>
</html>