<#include "/starlite.ftl">
<html>
<head>
  <title>${crewMember.personal.firstName!} ${crewMember.personal.lastName!}</title>
  <link rel="stylesheet" type="text/css" href="styles/forms.css">
  <@enableDatePickers/>
  <@enableCalculator/>
  <@enableHelp/>  
  

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
       
select.small
{
margin-left:0px;
margin-right:0px;
height:23px;
width: 120px; 
}   
select.medium
{
margin-left:0px;
margin-right:0px;
height:23px;
width: 120px; 
}  

input.medium
{
margin-left:0px;
margin-right:0px;
height:17px;
width: 120px; 
}  

 
</style>


<script type="text/javascript">

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

function validateDate(datefield,msg){ 
  
   //SET UP VARIABLES
   var dateinput  = "#"    + datefield;
   var messagediv = "#msg-" + datefield;
   var message    = msg;       
                       
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
   if(current_date > reversedate){ $(messagediv).html(message); }else{$(messagediv).html();}        
   }
   }
</script>

<script type="text/javascript">

//ONLOAD FUNCTION   
$("document").ready(function() {    
   validateDate("mediexpiry",    "Medical is expired");
   validateDate("crmexpiry",     "CRM is expired");
   validateDate("dgexpiry",      "DG is expired");
   validateDate("licenceexpiry", "Licence is expired");
   validateDate("licenceexpiry", "Licence is expired");
   validateDate("huet",          "HUET is expired");
   validateDate("hemsCert",      "HEMS is expired");
   validateDate("lpcexpiry",      "LPC is expired");
   validateDate("opcexpiry",      "OPC is expired");
   validateDate("opsManexpiry",      "Operations Manual is expired");
   validateDate("annualManexpiry",      "Annual Technical Manual is expired");
});    
   
</script>

<script type="text/javascript">
//VALIDATE MANDATORY FIELDS
   function validate(){               
      $("#msg-error").html("");      
      var errormsg = "<b>The following mandatory fields are blank: </b><br>";
      var error    = 0;                  
      var position = document.getElementById('position').options[document.getElementById('position').selectedIndex].value;             
      var empstatus= document.getElementById('empstatus').options[document.getElementById('empstatus').selectedIndex].value;      
      if(position                   == ""){ errormsg += "Position, "; error=1;}      
      if(empstatus                  == ""){ errormsg += "Employment status, "; error=1;}
      if($("#mediexpiry").val()     == ""){ errormsg += "Medical expiry, "; error=1;}
      if($("#crmexpiry").val()      == ""){ errormsg += "CRM expiry, "; error=1;}
      if($("#dgexpiry").val()       == ""){ errormsg += "DG Expiry, "; error=1;}
      if($("#licencenumber").val()  == ""){ errormsg += "licence number, "; error=1;} 
      if($("#instructorExtra").css("display") != "none"){    
        if($("#licenceexpiry").val()  == ""){ errormsg += "licence expiry, "; error=1;}
      }            
      if(error==1){
         errormsg=errormsg.substring(0,errormsg.length-2);
         $("#msg-error").html(errormsg);        
         return false;
      }else{
         document.forms.roleform.submit(); 
      }                          
   }
   
   function showPilot()
   {
     $("#showPilotExtra").toggle();
   }
   
   function showCertificates()
   {
     $("#showCertificatesExtra").toggle();
   }
   
   function instructor()
   {
     $("#instructorExtra").toggle();
     if($("#instructorExtra").css("display") == "none")
         {
         //blank out fields
         $("#licenceexpiry").val("")
         }
   }
   function englishTest(number)
   {
     if (number==6)
     {
     $("#englishTestExtra").toggle();
     if($("#englishTestExtra").css("display") == "none")
	       {
	        //blank out fields
	        $("#englishTestExtraExpiry").val("")
	       }
     }
     else
     {
     if($("#englishTestExtra").css("display") == "none")
     {
     $("#englishTestExtra").toggle();
     }
     }
      
	      
    }
</script>

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
	<form action="#" method="POST" name="roleform" id="roleform" class="smart readonly" >
	<#else>
	<form autocomplete="false" action="crewMember!save.action?id=${id}" name="roleform" id="roleform" method="POST" class="smart" enctype="multipart/form-data">
	</#if>
		<input type="hidden" name="id" value="${id!}"/>
		<input type="hidden" name="crewMember.id" value="${crewMember.code!}"/>
		<input type="hidden" name="tab" value="role"/>
		<input name="docfolder" value="/crew/${id}" type="hidden"/> 
		
		<div style="float:left; width: 500px;">
		<fieldset>
			<legend>Role</legend>
			<div class="fm-opt">
				<label for="crewMember.role.position"><span class="star">*</span>Position:</label>

				<select name="crewMember.role.position" id="position" onchange="$('#switch_role_to').attr('value', this.value);$('#switchrole').submit();"/>
				    <option value=""></option>
					<option value="Pilot" <#if crewMember.role.position?if_exists == "Pilot">selected</#if>>Pilot
					<option value="AME" <#if crewMember.role.position?if_exists == "AME">selected</#if>>AME
					<option value="Base Manager" <#if crewMember.role.position?if_exists == "Base Manager">selected</#if>>Base Manager
					<option value="Ops Admin" <#if crewMember.role.position?if_exists == "Ops Admin">selected</#if>>Ops Admin
					<option value="Systems Operator" <#if crewMember.role.position?if_exists == "Systems Operator">selected</#if>>Systems Operator
					
				</select>
			
			</div>
			<#if crewMember.role.position?if_exists != "AME" >
			<div class="fm-opt">
			
			<label for="crewMember.role.subPosition">Position Subcategory:</label>	
				<input type="text" name="crewMember.role.subPosition" <#if crewMember.role.subPosition = "Fitter" || crewMember.role.subPosition = "Avionician">value=""<#else>value="${crewMember.role.subPosition!}</#if>"/>
			</div>
			<#else>
			<div class="fm-opt">
			<label for="crewMember.role.subPosition">Position Subcategory:</label>
			<select name="crewMember.role.subPosition" id="subPosition"/>
				   	<option value="Fitter" <#if crewMember.role.subPosition?if_exists == "Fitter">selected</#if>>Fitter
					<option value="Avionician" <#if crewMember.role.subPosition?if_exists == "Avionician">selected</#if>>Avionician
			</select>
			</div>
			</#if>
            
            								
			<div class="fm-opt">
				<label for="crewMember.role.initialDate">Initial Date:</label>
				<input name="crewMember.role.initialDate" type="text" class="date-pick" value="<#if crewMember.role.initialDate??>${crewMember.role.initialDate?string('dd/MM/yyyy')}</#if>"/>
			</div>
			
			<div class="fm-opt">
                <label for="reviewDate">Review Date:</label>
                <input name="reviewDate" type="text" class="date-pick" value="<#if crewMember.role.reviewDate??>${crewMember.role.reviewDate?string('dd/MM/yyyy')}<#else>${reviewDate!}</#if>"/>
            </div>
			<div class="fm-opt">
				<label for="crewMember.role.employment"><span class="star">*</span>Employment Status:</label>
				<select name="crewMember.role.employment" id="empstatus" />
					<option value="Permanent" <#if crewMember.role.employment?if_exists == "Permanent">selected</#if>>Permanent
					<option value="Permanent (Inactive)" <#if crewMember.role.employment?if_exists == "Permanent (Inactive)">selected</#if>>Permanent (Inactive)
					<option value="Freelance" <#if crewMember.role.employment?if_exists == "Freelance">selected</#if>>Freelance
					<option value="Freelance (Inactive)" <#if crewMember.role.employment?if_exists == "Freelance (Inactive)">selected</#if>>Freelance (Inactive)
				</select>
			</div>
			<!--<div class="fm-opt">
				<label for="crewMember.role.primaryLocation">Primary Location:</label>
				<input name="crewMember.role.primaryLocation" type="text" value="${crewMember.role.primaryLocation!}"/>
			</div>
			<div class="fm-opt">
				<label for="crewMember.role.company">Company:</label>
				<input name="crewMember.role.company" type="text" value="${crewMember.role.company!}"/>
			</div>
			<div class="fm-opt">
				<label for="crewMember.role.department">Department:</label>
				<input name="crewMember.role.department" type="text" value="${crewMember.role.department!}"/>
			</div>
			<div class="fm-opt">
				<label for="crewMember.role.manager">Manager:</label>
				<input name="crewMember.role.manager" type="text" value="${crewMember.role.manager!}"/>
			</div>
			<div class="fm-opt">
				<label for="crewMember.role.telephoneExtension">Telephone Extension:</label>
				<input name="crewMember.role.telephoneExtension" type="text" value="${crewMember.role.telephoneExtension!}"/>
			</div>-->
		</fieldset>
		
		<#if crewMember.role.position?if_exists == "Pilot" || crewMember.role.position?if_exists == "AME">
		<fieldset>
			<legend>Licence</legend>
			<div class="fm-opt">
				<label for="crewMember.role.r1.number"/><span class="star">*</span>Number:</label>
				<input name="crewMember.role.r1.number" id="licencenumber" type="text" value="${crewMember.role.r1.number!}"/>
			</div>
			<div class="fm-opt">
				<label for="crewMember.role.r1.type"><span class="star">*</span>Type:</label>
				<select name="crewMember.role.r1.type" value="${crewMember.role.r1.type!}" id="licencetype">
				  <option <#if crewMember.role.r1.type?if_exists == "AME">selected</#if>>      AME
				  <option <#if crewMember.role.r1.type?if_exists == "ATPL">selected</#if>>     ATPL
				  <option <#if crewMember.role.r1.type?if_exists == "CPL">selected</#if>>      CPL
				</select>
			</div>
<!-- License Expiry Date --> 			
			<div class="fm-opt">
				<label for="licenseExpiryDate"><span class="star">*</span>Expiry Date:</label>
				<input name="licenseExpiryDate"  type="text" class="date-pick" id="licenceexpiry" value="<#if crewMember.role.r1.expiryDate??>${crewMember.role.r1.expiryDate?string('dd/MM/yyyy')}<#else>${licenseExpiryDate!}</#if>" />
			    <div class="fm-opt" id="msg-licenceexpiry" style="margin-left:90px; color:red; font-weight: bold;"></div>
			    <label for="licenceFile">Upload:</label>
			    <#if user.hasPermission("UserAdmin")>
                <input id="licenceFile" name="licenceFile" value="" type="file" />
                </#if>
                <input name="licenceTags" value="licence" type="hidden" />
                <#if licence?exists>
                  <label for="licenceUploadLink"/>&nbsp;</label>
                  <div id="licenceUploadLink"><a href='${request.contextPath}${licence.bookmark.url!}'>${licence.bookmark.name}</a><#if folder.canWrite(user)> <a onclick="return confirm('Are you sure you wish to delete this document?');" href="documents!delete.action?returnUrl=crewMember.action?id=${id}%26tab=role&path=${licence.bookmark.bookmarkedId}">&nbsp;&nbsp;&nbsp;[Delete]</a></#if></div>
                </#if>
            </div>
			<br/>
		</fieldset>
		</#if>
  
       <#if crewMember.role.position?if_exists == "AME">
       <#-- using night, game, nvg and sling fields -->
       <#-- for AME ratings S92, S330J, 407 and other -->
       <fieldset>
		<legend>Type Licence</legend>
		 	
			<div class="fm-opt">
				<label  for="crewMember.role.night">S92:</label>
				<select class="small" name="crewMember.role.night">
	    		   <#list YNOption as option >
	    			<#if option == crewMember.role.night?if_exists>	  
	    		   	<option SELECTED >${option}
	    		    </#if>
	    		    <#if option != crewMember.role.night?if_exists>
	    		    <option>${option}
	    		    </#if>
	    		   </#list>
	    		</select>
			</div>
			
			<div class="fm-opt">
				<label  for="crewMember.role.game">S330J:</label>
				<select class="small" name="crewMember.role.game">
	    		   <#list YNOption as option >
	    			<#if option == crewMember.role.game?if_exists>	  
	    		   	<option SELECTED >${option}
	    		    </#if>
	    		    <#if option != crewMember.role.game?if_exists>
	    		    <option>${option}
	    		    </#if>
	    		   </#list>
	    		</select>
			</div>		
			
			<div class="fm-opt">
				<label  for="crewMember.role.nvg">407:</label>
				<select class="small" name="crewMember.role.nvg">
	    		   <#list YNOption as option >
	    			<#if option == crewMember.role.nvg?if_exists>	  
	    		   	<option SELECTED >${option}
	    		    </#if>
	    		    <#if option != crewMember.role.nvg?if_exists>
	    		    <option>${option}
	    		    </#if>
	    		   </#list>
	    		</select>
			</div>	
			<div class="fm-opt">
				<label class="small" for="crewMember.role.sling">Other:</label>
				<input class="small" name="crewMember.role.sling" type="text"  value="${crewMember.role.sling?if_exists}" />
			</div>
						
        </fieldset>
	    </#if>

        
       <#if crewMember.role.position?if_exists == "Pilot">
       
       <input type="hidden" id="crewMember.role.instructor.number" value=${crewMember.role.instructor.number!}>
       <fieldset>
		<legend>Ratings (Pilots only)</legend>
		 	 <div class="fm-opt">
		 	 <input type="button" onclick="showPilot();return true;" value="Show\Hide Pilot Fields"/>
		 	 </div>
		 	 
		 	<#if crewMember.role.position?if_exists == "Pilot" >
		 	<div id="showPilotExtra" style="display:none;">
			<#else>
			<div id="showPilotExtra" >
			</#if>
			
		 	 <div class="fm-opt">
				<label for="crewMember.role.instructor.number"><u>Instructor:</u></label>
				<select class="small" name="crewMember.role.instructor.number">
	    		   <#list YNOption as option >
	    			<#if option == crewMember.role.instructor.number>	  
	    		   	<option SELECTED onclick='instructor();return true;' >${option}
	    		    </#if>
	    		    <#if option != crewMember.role.instructor.number>
	    		    <option onclick='instructor();return true;'>${option}
	    		    </#if>
	    		   </#list>
	    		</select>
	    		
			</div>
			<#if crewMember.role.instructor.number?if_exists == "yes" >
			<div id="instructorExtra" >
			<#else>
			<div id="instructorExtra" style="display:none;">
			</#if>	
				
            <div class="fm-opt">
				<label for="crewMember.role.instructor.quantity">Grade:</label>
     			<select  class="medium" name="crewMember.role.instructor.quantity" >
					<option <#if crewMember.role.instructor.quantity?if_exists == "Grade 1">selected</#if>>Grade 1
					<option <#if crewMember.role.instructor.quantity?if_exists == "Grade 2">selected</#if>>Grade 2
					<option <#if crewMember.role.instructor.quantity?if_exists == "Grade 3">selected</#if>>Grade 3
					<option <#if crewMember.role.instructor.quantity?if_exists == "DFE">selected</#if>>DFE
				</select>
		   </div>
<!-- Instructor Expiry Date --> 		   
		   <div class="fm-opt">
				<label  for="instructorExpiryDate"><span class="star">*</span>Expiry Date:</label>
				<input name="instructorExpiryDate"  type="text" class="date-pick" id="licenceexpiry" value="<#if crewMember.role.r2.expiryDate??>${crewMember.role.r2.expiryDate?string('dd/MM/yyyy')}<#else>${instructorExpiryDate!}</#if>" />
		   </div>
		   <div class="fm-opt">
				<label for="crewMember.role.instructor.typeS92">Instructor Aircraft Type:</label>
			<br/>	
		    <div class="fm-opt">
				<label  for="crewMember.role.instructor.typeS92">S92:</label>
				<select class="small" name="crewMember.role.instructor.typeS92">
	    		   <#list YNOption as option >
	    			<#if option == crewMember.role.instructor.typeS92>	  
	    		   	<option SELECTED >${option}
	    		    </#if>
	    		    <#if option != crewMember.role.instructor.typeS92>
	    		    <option>${option}
	    		    </#if>
	    		   </#list>
	    		</select>
			</div>
			<div class="fm-opt">
				<label  for="crewMember.role.instructor.typeS330J">S330J:</label>
				<select class="small" name="crewMember.role.instructor.typeS330J">
	    		   <#list YNOption as option >
	    			<#if option == crewMember.role.instructor.typeS330J>	  
	    		   	<option SELECTED >${option}
	    		    </#if>
	    		    <#if option != crewMember.role.instructor.typeS330J>
	    		    <option>${option}
	    		    </#if>
	    		   </#list>
	    		</select>		
	    	</div>	
			<div class="fm-opt">
				<label for="crewMember.role.instructor.typeB407">B407:</label>
				<select class="small" name="crewMember.role.instructor.typeB407">
	    		   <#list YNOption as option >
	    			<#if option == crewMember.role.instructor.typeB407>	  
	    		   	<option SELECTED >${option}
	    		    </#if>
	    		    <#if option != crewMember.role.instructor.typeB407>
	    		    <option>${option}
	    		    </#if>
	    		   </#list>
	    		</select>		
			</div>
			<div class="fm-opt">
				<label  for="crewMember.role.instructor.typeOther">Other:</label>
				<#if crewMember.role.instructor.typeOther??>
				<input  class="medium" type="text" name="crewMember.role.instructor.typeOther" value="${crewMember.role.instructor.typeOther!}"/>
				<#else>
				<input  class="medium" type="text" name="crewMember.role.instructor.typeOther" value=""/>
				</#if>
			</div>
				
			</div>
			</div>
			<div class="fm-opt">
				<label  for="crewMember.role.instrument"><u>Instrument:</u></label>
				<select class="small" name="crewMember.role.instrument">
	    		   <#list YNOption as option >
	    			<#if option == crewMember.role.instrument>	  
	    		   	<option SELECTED >${option}
	    		    </#if>
	    		    <#if option != crewMember.role.instrument>
	    		    <option>${option}
	    		    </#if>
	    		   </#list>
	    		</select>
			</div>
<!-- Instrument Expiry Date -->
            <div class="fm-opt">
                <label for="instrumentExpiryDate">Expiry Date:</label>
                <input name="instrumentExpiryDate"  type="text" class="date-pick" id="licenceexpiry" value="<#if crewMember.role.ifr.expiryDate??>${crewMember.role.ifr.expiryDate?string('dd/MM/yyyy')}<#else>${instrumentExpiryDate!}</#if>" />
            </div>
            <div class="fm-opt">
                <label for="crewMember.role.ifr.hours">Hours:</label>
                <input class="medium" name="crewMember.role.ifr.hours" type="text"  value="${crewMember.role.ifr.hours!}"/>
            </div>
			
			<div class="fm-opt">
				<label  for="crewMember.role.testPilot"><u>Test Pilot:</u></label>
				<select class="small" name="crewMember.role.testPilot">
	    		   <#list YNOption as option >
	    			<#if option == crewMember.role.testPilot>	  
	    		   	<option SELECTED >${option}
	    		    </#if>
	    		    <#if option != crewMember.role.testPilot>
	    		    <option>${option}
	    		    </#if>
	    		   </#list>
	    		</select>
			</div>
			<div class="fm-opt">
            <label for="crewMember.role.testPilotClass">&nbsp;</label>
     			<select  class="medium" name="crewMember.role.testPilotClass" >
					<option <#if crewMember.role.testPilotClass?if_exists == "Class 1">selected</#if>>Class 1
					<option <#if crewMember.role.testPilotClass?if_exists == "Class 2">selected</#if>>Class 2
					<option <#if crewMember.role.testPilotClass?if_exists == "Class 3">selected</#if>>Class 3

				</select>
			</div>
			<div class="fm-opt">
				<label  for="crewMember.role.englishTest"><u>English Test:</u></label>
				<select  class="medium" name="crewMember.role.englishTest" >
					<option onclick='englishTest(1);return true;' <#if crewMember.role.englishTest?if_exists == "Level 1">selected</#if>>Level 1
					<option onclick='englishTest(2);return true;' <#if crewMember.role.englishTest?if_exists == "Level 2">selected</#if>>Level 2
					<option onclick='englishTest(3);return true;' <#if crewMember.role.englishTest?if_exists == "Level 3">selected</#if>>Level 3
					<option onclick='englishTest(4);return true;' <#if crewMember.role.englishTest?if_exists == "Level 4">selected</#if>>Level 4
					<option onclick='englishTest(5);return true;' <#if crewMember.role.englishTest?if_exists == "Level 5">selected</#if>>Level 5
					<option onclick='englishTest(6);return true;' <#if crewMember.role.englishTest?if_exists == "Level 6">selected</#if>>Level 6
				</select>
			</div>
			<#if crewMember.role.englishTest?if_exists != "Level 6" >
			<div id="englishTestExtra" >
			<#else>
			<div id="englishTestExtra" style="display:none;">
			</#if>	
<!-- English Test Expiry Date -->			
			<div class="fm-opt">
                <label id="englishTestExpiryDate" for="crewMember.role.ets.expiryDate">Expiry Date:</label>
                <input name="englishTestExpiryDate"  type="text" class="date-pick" id="licenceexpiry" value="<#if crewMember.role.ets.expiryDate??>${crewMember.role.ets.expiryDate?string('dd/MM/yyyy')}<#else>${englishTestExpiryDate!}</#if>" />
            </div>
			</div>		
			<div class="fm-opt">
				
			<label  for="crewMember.role.night"><u>Night:</u></label>
			
				<select class="small" name="crewMember.role.night">
	    		   <#list YNOption as option >
	    			<#if option == crewMember.role.night>	  
	    		   	<option SELECTED >${option}
	    		    </#if>
	    		    <#if option != crewMember.role.night>
	    		    <option>${option}
	    		    </#if>
	    		   </#list>
	    		</select>
	    		<img class="tooltip" title="<div style='text-align:left'>The pilot concerned shall during the ninety days immediately preceding the intended flight have- <br/><b>i.</b> Executed, by night not less than 3 circuits (incl. take-offs and landings); or<br/><b>ii.</b> Passed the appropriate skill test or proficiency check prescribed in Part 61 for the helicopter night rating in the type of helicopter in which the intended flight is to be undergone.</div>" style="cursor:help;position:relative;margin-right:50;" src="images/icons/info.png"/>
	         </div>
	         <div class="fm-opt">
                <label for="crewMember.role.nightHours">Hours:</label>
                <input class="medium" name="crewMember.role.nightHours" type="text" value="${crewMember.role.nightHours!}"/>
            </div>
            <div class="fm-opt">
				<label  for="crewMember.role.nvg"><u>NVG:</u></label>
				<select class="small" name="crewMember.role.nvg">
	    		   <#list YNOption as option >
	    			<#if option == crewMember.role.nvg>	  
	    		   	<option SELECTED >${option}
	    		    </#if>
	    		    <#if option != crewMember.role.nvg>
	    		    <option>${option}
	    		    </#if>
	    		   </#list>
	    		</select>
			</div>	
			<div class="fm-opt">
                <label for="crewMember.role.nvgHours">Hours:</label>
                <input class="medium" name="crewMember.role.nvgHours" type="text" value="${crewMember.role.nvgHours!}"/>
            </div>
            <div class="fm-opt">
				<label  for="crewMember.role.sling"><u>Undersling\Winch:</u></label>
				<select class="small" name="crewMember.role.sling">
	    		   <#list YNOption as option >
	    			<#if option == crewMember.role.sling>	  
	    		   	<option SELECTED >${option}
	    		    </#if>
	    		    <#if option != crewMember.role.sling>
	    		    <option>${option}
	    		    </#if>
	    		   </#list>
	    		</select>
			</div>	
			<div class="fm-opt">
                <label for="crewMember.role.underslingHours">Hours:</label>
                <input class="medium" name="crewMember.role.underslingHours" type="text" value="${crewMember.role.underslingHours!}"/>
            </div>
			 <div class="fm-opt">				
				<label  for="crewMember.role.game"><u>Game\Culling:</u></label>
				<select class="small" name="crewMember.role.game">
	    		   <#list YNOption as option >
	    			<#if option == crewMember.role.game>	  
	    		   	<option SELECTED >${option}
	    		    </#if>
	    		    <#if option != crewMember.role.game>
	    		    <option>${option}
	    		    </#if>
	    		   </#list>
	    		</select>	
			</div>
  	        <div class="fm-opt">
                <label for="crewMember.role.gameHours">Hours:</label>
                <input class="medium" name="crewMember.role.gameHours" type="text" value="${crewMember.role.gameHours!}"/>
            </div>
            <div class="fm-opt">				
				<label  for="crewMember.role.bannerTowing"><u>Banner Towing:</u></label>
				<select class="small" name="crewMember.role.bannerTowing">
	    		   <#list YNOption as option >
	    			<#if option == crewMember.role.bannerTowing>	  
	    		   	<option SELECTED >${option}
	    		    </#if>
	    		    <#if option != crewMember.role.bannerTowing>
	    		    <option>${option}
	    		    </#if>
	    		   </#list>
	    		</select>	
			</div>
  	        <div class="fm-opt">
                <label for="crewMember.role.bannerTowingHours">Hours:</label>
                <input class="medium" name="crewMember.role.bannerTowingHours" type="text" value="${crewMember.role.bannerTowingHours!}"/>
            </div>
            <div class="fm-opt">				
				<label  for="crewMember.role.fireFighting"><u>Fire fighting:</u></label>
				<select class="small" name="crewMember.role.fireFighting">
	    		   <#list YNOption as option >
	    			<#if option == crewMember.role.fireFighting>	  
	    		   	<option SELECTED >${option}
	    		    </#if>
	    		    <#if option != crewMember.role.fireFighting>
	    		    <option>${option}
	    		    </#if>
	    		   </#list>
	    		</select>	
			</div>
  	        <div class="fm-opt">
                <label for="crewMember.role.fireFightingHours">Hours:</label>
                <input class="medium" name="crewMember.role.fireFightingHours" type="text" value="${crewMember.role.fireFightingHours!}"/>
            </div>
            <div class="fm-opt">				
				<label  for="crewMember.role.flir"><u>FLIR:</u></label>
				<select class="small" name="crewMember.role.flir">
	    		   <#list YNOption as option >
	    			<#if option == crewMember.role.flir>	  
	    		   	<option SELECTED >${option}
	    		    </#if>
	    		    <#if option != crewMember.role.flir>
	    		    <option>${option}
	    		    </#if>
	    		   </#list>
	    		</select>	
			</div>
  	        <div class="fm-opt">
                <label for="crewMember.role.flirHours">Hours:</label>
                <input class="medium" name="crewMember.role.flirHours" type="text" value="${crewMember.role.flirHours!}"/>
            </div>
            <div class="fm-opt">				
				<label  for="crewMember.role.fries"><u>FRIES:</u></label>
				<select class="small" name="crewMember.role.fries">
	    		   <#list YNOption as option >
	    			<#if option == crewMember.role.fries>	  
	    		   	<option SELECTED >${option}
	    		    </#if>
	    		    <#if option != crewMember.role.fries>
	    		    <option>${option}
	    		    </#if>
	    		   </#list>
	    		</select>	
			</div>
  	        <div class="fm-opt">
                <label for="crewMember.role.friesHours">Hours:</label>
                <input class="medium" name="crewMember.role.friesHours" type="text" value="${crewMember.role.friesHours!}"/>
            </div>
            <div class="fm-opt">				
				<label  for="crewMember.role.hems"><u>HEMS:</u></label>
				<select class="small" name="crewMember.role.hems">
	    		   <#list YNOption as option >
	    			<#if option == crewMember.role.hems>	  
	    		   	<option SELECTED >${option}
	    		    </#if>
	    		    <#if option != crewMember.role.hems>
	    		    <option>${option}
	    		    </#if>
	    		   </#list>
	    		</select>	
			</div>
  	        <div class="fm-opt">
                <label for="crewMember.role.hemsHours">Hours:</label>
                <input class="medium" name="crewMember.role.hemsHours" type="text" value="${crewMember.role.hemsHours!}"/>
            </div>
            <div class="fm-opt">				
				<label  for="crewMember.role.mountain"><u>Mountain:</u></label>
				<select class="small" name="crewMember.role.mountain">
	    		   <#list YNOption as option >
	    			<#if option == crewMember.role.mountain>	  
	    		   	<option SELECTED >${option}
	    		    </#if>
	    		    <#if option != crewMember.role.mountain>
	    		    <option>${option}
	    		    </#if>
	    		   </#list>
	    		</select>	
			</div>
  	        <div class="fm-opt">
                <label for="crewMember.role.mountainHours">Hours:</label>
                <input class="medium" name="crewMember.role.mountainHours" type="text" value="${crewMember.role.mountainHours!}"/>
            </div>
            <div class="fm-opt">				
				<label  for="crewMember.role.offshore"><u>Offshore:</u></label>
				<select class="small" name="crewMember.role.offshore">
	    		   <#list YNOption as option >
	    			<#if option == crewMember.role.offshore>	  
	    		   	<option SELECTED >${option}
	    		    </#if>
	    		    <#if option != crewMember.role.offshore>
	    		    <option>${option}
	    		    </#if>
	    		   </#list>
	    		</select>	
			</div>
  	        <div class="fm-opt">
                <label for="crewMember.role.offshoreHours">Hours:</label>
                <input class="medium" name="crewMember.role.offshoreHours" type="text" value="${crewMember.role.offshoreHours!}"/>
            </div>
             <div class="fm-opt">				
				<label  for="crewMember.role.offshoreCaptain">Captain\Co-Pilot:</label>
				<select class="small" name="crewMember.role.offshoreCaptain">
	    		   <#list pilots as option >
	    			<#if option == crewMember.role.offshoreCaptain>	  
	    		   	<option SELECTED >${option}
	    		    </#if>
	    		    <#if option != crewMember.role.offshoreCaptain>
	    		    <option>${option}
	    		    </#if>
	    		   </#list>
	    		</select>	
			</div>
			<div class="fm-opt">				
				<label  for="crewMember.role.powerline"><u>Powerline:</u></label>
				<select class="small" name="crewMember.role.powerline">
	    		   <#list YNOption as option >
	    			<#if option == crewMember.role.powerline>	  
	    		   	<option SELECTED >${option}
	    		    </#if>
	    		    <#if option != crewMember.role.powerline>
	    		    <option>${option}
	    		    </#if>
	    		   </#list>
	    		</select>	
			</div>
  	        <div class="fm-opt">
                <label for="crewMember.role.powerlineHours">Hours:</label>
                <input class="medium" name="crewMember.role.powerlineHours" type="text" value="${crewMember.role.powerlineHours!}"/>
            </div>
			<div class="fm-opt">
				<label  for="crewMember.role.test.number">Test Rated:</label>
				<select class="small" name="crewMember.role.test.number">
	    		   <#list YNOption as option >
	    			<#if option == crewMember.role.test.number>	  
	    		   	<option SELECTED >${option}
	    		    </#if>
	    		    <#if option != crewMember.role.test.number>
	    		    <option>${option}
	    		    </#if>
	    		   </#list>
	    		</select>
			</div>	
			<div class="fm-opt">
				<label for="crewMember.role.test.type">Test Grade:</label>
				<select class="medium" name="crewMember.role.test.type" >
					<option <#if crewMember.role.test.type?if_exists == "Grade 1">selected</#if>>Grade 1
					<option <#if crewMember.role.test.type?if_exists == "Grade 2">selected</#if>>Grade 2
				</select>
			</div>
  	        
	</div>		
    </fieldset>
	</#if>
    <#if crewMember.role.position?if_exists == "Pilot">
    <fieldset>
			<legend>Certificates</legend>
			<div class="fm-opt">
		 	 <input type="button" onclick="showCertificates();return true;" value="Show\Hide Certificates"/>
		 	 </div>
		 	
		 	<#if crewMember.role.position?if_exists != " " >
		 	<div id="showCertificatesExtra" style="display:none;">
			<#else>
			<div id="showCertificatesExtra">
			</#if>
		 	 
			<img class="tooltip" title="<b>Certificates</b><br/><div style='text-align:left'>Use the save button to upload the file.<br/> Under the documents tab, use the following tags to search for documents:<br/>'tag:medical'<br/>'tag:CRM'<br/>'tag:DG'<br/>'tag:HUET'<br/>tag:'HEMS' and 'tag:additionalCert'<br/>You can also use the User's ID</div>" style="cursor:help;position:relative;float:right;" src="images/icons/info.png"/>
             <div class="fm-opt" style="padding-bottom:5px;border-bottom:1px solid silver;margin-bottom:5px;margin-left:10px;">
<!-- Medical Expiry Date -->             
                <label for="medicalExpiryDate"><span class="star">*</span>Medical Expiry:</label>
                <input name="medicalExpiryDate"  type="text" class="date-pick" id="licenceexpiry" value="<#if crewMember.role.expiryDate??>${crewMember.role.expiryDate?string('dd/MM/yyyy')}<#else>${medicalExpiryDate!}</#if>" />
                <div id="msg-mediexpiry" style="color:red; font-weight: bold; margin-left: 90px;"></div>
                <label for="mediFile">Upload:</label>
                <#if user.hasPermission("UserAdmin")>
                <input id="mediFile" name="mediFile" value="" type="file"/>
                </#if>
                <input name="mediTags" value="medical ${id} certificates" type="hidden"/>
                <#if medical?exists>
                  <label for="medicalUploadedFile"/>&nbsp;</label>
                  <div id="medicalUploadedFile"><a href='${request.contextPath}${medical.bookmark.url!}'>${medical.bookmark.name}</a><#if folder.canWrite(user)> <a onclick="return confirm('Are you sure you wish to delete this document?');" href="documents!delete.action?returnUrl=crewMember.action?id=${id}%26tab=role&path=${medical.bookmark.bookmarkedId}">&nbsp;&nbsp;&nbsp;[Delete]</a></#if></div>
                </#if>
            </div> 
            <br/>   
			<div class="fm-opt" style="padding-bottom:5px;border-bottom:1px solid silver;margin-bottom:5px;margin-left:10px;">
<!-- CRM Expiry Date --> 			
				<label for="crmExpiryDate"><span class="star">*</span>CRM Expiry:</label>
				<input name="crmExpiryDate"  type="text" class="date-pick" id="licenceexpiry" value="<#if crewMember.role.crm.expiryDate??>${crewMember.role.crm.expiryDate?string('dd/MM/yyyy')}<#else>${crmExpiryDate!}</#if>" />
				<div id="msg-crmexpiry" style="color:red; font-weight: bold; margin-left: 90px;"></div>
                <label for="crmFile">Upload:</label>
                <#if user.hasPermission("UserAdmin")>
                <input id="crmFile" name="crmFile" value="" type="file"/>
                </#if>
                <input name="crmTags" value="CRM ${id} certificates" type="hidden"/>
                <#if crm?exists>
                  <label for="crmUploadedFile"/>&nbsp;</label>
                  <div id="crmUploadedFile"><a href='${request.contextPath}${crm.bookmark.url!}'>${crm.bookmark.name}</a><#if folder.canWrite(user)> <a onclick="return confirm('Are you sure you wish to delete this document?');" href="documents!delete.action?returnUrl=crewMember.action?id=${id}%26tab=role&path=${crm.bookmark.bookmarkedId}">&nbsp;&nbsp;&nbsp;[Delete]</a></#if></div>
                </#if>
            </div>
            <br/>    
			<div class="fm-opt" style="padding-bottom:5px;border-bottom:1px solid silver;margin-bottom:5px;margin-left:10px;">
<!-- DG Expiry Date -->			
				<label for="dgExpiryDate"><span class="star">*</span>DG Expiry:</label>
   				<input name="dgExpiryDate"  type="text" class="date-pick" id="licenceexpiry" value="<#if crewMember.role.dg.expiryDate??>${crewMember.role.dg.expiryDate?string('dd/MM/yyyy')}<#else>${dgExpiryDate!}</#if>" /> 				
				<div id="msg-dgexpiry" style="color:red; font-weight: bold; margin-left: 90px;"></div>
                <label for="dgFile">Upload:</label>
                <#if user.hasPermission("UserAdmin")>
                <input id="dgFile" name="dgFile" value="" type="file">
                </#if>
                <input name="dgTags" value="DG ${id} certificates" type="hidden">
                <#if dg?exists>
                  <label for="dgUploadedFile"/>&nbsp;</label>
                  <div id="dgUploadedFile"><a href='${request.contextPath}${dg.bookmark.url!}'>${dg.bookmark.name}</a><#if folder.canWrite(user)> <a onclick="return confirm('Are you sure you wish to delete this document?');" href="documents!delete.action?returnUrl=crewMember.action?id=${id}%26tab=role&path=${dg.bookmark.bookmarkedId}">&nbsp;&nbsp;&nbsp;[Delete]</a></#if></div>
                </#if> 
			</div>
			<br/>   
			<div class="fm-opt" style="padding-bottom:5px;border-bottom:1px solid silver;margin-bottom:5px;margin-left:10px;">
<!-- Huet Expiry Date --> 			
				<label for="huetExpiryDate">HUET Training:</label>
				<input name="huetExpiryDate"  type="text" class="date-pick" id="licenceexpiry" value="<#if crewMember.role.huet.expiryDate??>${crewMember.role.huet.expiryDate?string('dd/MM/yyyy')}<#else>${huetExpiryDate!}</#if>" />
				<div id="msg-huet" style="color:red; font-weight: bold; margin-left: 90px;"></div>
                <label for="huetFile">Upload:</label>
                <#if user.hasPermission("UserAdmin")>
                <input id="huetFile" name="huetFile" value="" type="file" />
                </#if>
                <input name="huetTags" value="HUET ${id} certificates" type="hidden" />
                <#if huet?exists>
                  <label for="huetUploadedFile"/>&nbsp;</label>
                  <div id="huetUploadedFile"><a href='${request.contextPath}${huet.bookmark.url!}'>${huet.bookmark.name}</a><#if folder.canWrite(user)> <a onclick="return confirm('Are you sure you wish to delete this document?');" href="documents!delete.action?returnUrl=crewMember.action?id=${id}%26tab=role&path=${huet.bookmark.bookmarkedId}">&nbsp;&nbsp;&nbsp;[Delete]</a></#if></div>
                </#if>
            </div>
            <div class="fm-opt" style="padding-bottom:5px;border-bottom:1px solid silver;margin-bottom:5px;margin-left:10px;">
<!-- HEMS Expiry Date --> 			
				<div class="fm-opt">
				<label for="hemsCertExpiryDate">HEMS:</label>
				<input name="hemsCertExpiryDate"  type="text" class="date-pick" id="licenceexpiry" value="<#if crewMember.role.hemsCert.expiryDate??>${crewMember.role.hemsCert.expiryDate?string('dd/MM/yyyy')}<#else>${hemsCertExpiryDate!}</#if>" />
			    <div class="fm-opt" id="msg-hemsCert" style="margin-left:90px; color:red; font-weight: bold;"></div>
			    <label for="hemsCertFile">Upload:</label>
			    <#if user.hasPermission("UserAdmin")>
                <input id="hemsCertFile" name="hemsCertFile" value="" type="file" />
                </#if>
                <input name="hemsCertTags" value="HEMS ${id} certificates" type="hidden" />
                <#if hemsCert?exists>
                  <label for="hemsCertUploadLink"/>&nbsp;</label>
                  <div id="hemsCertUploadLink"><a href='${request.contextPath}${hemsCert.bookmark.url!}'>${hemsCert.bookmark.name}</a><#if folder.canWrite(user)> <a onclick="return confirm('Are you sure you wish to delete this document?');" href="documents!delete.action?returnUrl=crewMember.action?id=${id}%26tab=role&path=${hemsCert.bookmark.bookmarkedId}">&nbsp;&nbsp;&nbsp;[Delete]</a></#if></div>
                </#if>
            </div>
            </div>
           <div class="fm-opt" style="padding-bottom:5px;border-bottom:1px solid silver;margin-bottom:5px;margin-left:10px;">
<!-- Additional Certificates--> 			
				<div class="fm-opt">
				<label for="additionalCertExpiryDate">Additional Certificates Upload:</label>
				<#if user.hasPermission("UserAdmin")>
                <input id="additionalCertFile" name="additionalCertFile" value="" type="file" />
                </#if>
                <input name="additionalCertTags" value="additionalCert ${id}" type="hidden" />
                <#if additionalCert?exists>
                  <label for="additionalCertUploadLink"/>&nbsp;</label>
                  <div id="additionalCertUploadLink"><a href='${request.contextPath}${additionalCert.bookmark.url!}'>${additionalCert.bookmark.name}</a><#if folder.canWrite(user)> <a onclick="return confirm('Are you sure you wish to delete this document?');" href="documents!delete.action?returnUrl=crewMember.action?id=${id}%26tab=role&path=${additionalCert.bookmark.bookmarkedId}">&nbsp;&nbsp;&nbsp;[Delete]</a></#if></div>
                </#if>
            </div>
            </div>                             
            <br/>
			<br/>
    </fieldset>
    </#if>
    
    <#if crewMember.role.position?if_exists == "AME" || crewMember.role.position?if_exists == "Base Manager" || crewMember.role.position?if_exists == "Ops Admin" || crewMember.role.position?if_exists == "Systems Operator">
    <fieldset>
			<legend>Certificates</legend>
			<img class="tooltip" title="<b>Certificates</b><br/><div style='text-align:left'>Use the save button to upload the file.<br/> Under the documents tab, use the following tags to search for documents:<br/>'tag:additionalCert'<br/>'tag:certificates'<br/>You can also use the User's ID.</div>" style="cursor:help;position:relative;float:right;" src="images/icons/info.png"/>

           <div class="fm-opt" style="padding-bottom:5px;border-bottom:1px solid silver;margin-bottom:5px;margin-left:10px;">
<!-- Additional Certificates--> 			
				<div class="fm-opt">
				<label for="additionalCertExpiryDate">Additional Certificates Upload:</label>
				<#if user.hasPermission("UserAdmin")>
                <input id="additionalCertFile" name="additionalCertFile" value="" type="file" />
                </#if>
                <input name="additionalCertTags" value="additionalCert ${id} certificates" type="hidden" />
                <#if additionalCert?exists>
                  <label for="additionalCertUploadLink"/>&nbsp;</label>
                  <div id="additionalCertUploadLink"><a href='${request.contextPath}${additionalCert.bookmark.url!}'>${additionalCert.bookmark.name}</a><#if folder.canWrite(user)> <a onclick="return confirm('Are you sure you wish to delete this document?');" href="documents!delete.action?returnUrl=crewMember.action?id=${id}%26tab=role&path=${additionalCert.bookmark.bookmarkedId}">&nbsp;&nbsp;&nbsp;[Delete]</a></#if></div>
                </#if>
            </div>
            </div>                             
            <br/>
			<br/>
    </fieldset>
    </#if>
    
    
    
		</div>



		<div style="float:left; width: 500px;">	                               
        <#if crewMember.role.position?if_exists == "Pilot">   
        
        <fieldset>
            <legend>
            Pilot Experience 
            </legend>
		<div class="fm-opt" style="padding-bottom:5px;border-bottom:1px solid silver;margin-bottom:5px;margin-left:10px;">
		 
	    <#if user.hasPermission("UserAdmin")>
                <label for="flighthoursFile">Upload:</label>
                <#if user.hasPermission("UserAdmin")>
                <input id="flighthoursFile" name="flighthoursFile" value="" type="file" />
                </#if>
                <input name="flighthoursTags" value="flighthours" type="hidden" />
                <img class="tooltip" title="<b>Document Upload</b><br/><div style='text-align:left'>Click on the save button to upload the file.<br/> Under the documents tab, use the 'tag:flighthours' to search for uploaded documents.</div>" style="cursor:help;position:relative;float:right;" src="images/icons/info.png"/>
              
                <#if flighthours?exists>
                  <label for="flighthoursFile"/>&nbsp;</label>
                  <div id="flighthoursFile"><a href='${request.contextPath}${flighthours.bookmark.url!}'>${flighthours.bookmark.name}</a><#if folder.canWrite(user)> <a onclick="return confirm('Are you sure you wish to delete this document?');" href="documents!delete.action?returnUrl=crewMember.action?id=${id}%26tab=role&path=${flighthours.bookmark.bookmarkedId}">&nbsp;&nbsp;&nbsp;[delete]</a></#if></div>
                  <h3><a href='${request.contextPath}${bookmark.url!}'>${bookmark.name}</a><#if folder.canWrite(user)> <a href="documents!delete.action?path=${bookmark.bookmarkedId}">Delete</a></#if></h3>
                </#if>
        </#if>        
            </div>
		 </fieldset>
		</#if>
		 
        <#if crewMember.role.position?if_exists == "Pilot" || crewMember.role.position?if_exists == "AME"  >   
		<fieldset>
			<legend>Proficiency Records</legend>
			<img class="tooltip" title="<b>Proficiency Records</b><br/><div style='text-align:left'>Use the save button to upload the file.<br/> Under the documents tab, use the following tags to search for documents:<br/>'tag:LPC'<br/>'tag:OPC'<br/>'tag:opsManual'<br/>'tag:annualTechManual' and 'tag:proficiency'<br/>You can also use the User's ID</div>" style="cursor:help;position:relative;float:right;" src="images/icons/info.png"/>
<!-- LPC Expiry Date -->	
            <#if crewMember.role.position?if_exists == "Pilot">	
 			<div class="fm-opt">	
				<label for="lpcExpiryDate">LPC Expiry:</label>
   				<input name="lpcExpiryDate"  type="text" class="date-pick" id="licenceexpiry" value="<#if crewMember.role.lpcCert.expiryDate??>${crewMember.role.lpcCert.expiryDate?string('dd/MM/yyyy')}<#else>${lpcExpiryDate!}</#if>" /> 				
				<div id="msg-lpcexpiry" style="color:red; font-weight: bold; margin-left: 90px;"></div>
			</div>
<!-- Life Proficiency Check (LPC)--> 			
				<div class="fm-opt">
				<label for="lpcCertFile">Life Proficiency Check(LPC) Upload:</label>
				<#if user.hasPermission("UserAdmin")>
                <input id="lpcCertFile" name="lpcCertFile" value="" type="file" />
                </#if>
                <input name="lpcCertTags" value="LPC ${id} proficiencyRecords" type="hidden" />
                <#if lpcCert?exists>
                  <label for="lpcCertUploadLink"/>&nbsp;</label>
                  <div id="lpcCertUploadLink"><a href='${request.contextPath}${lpcCert.bookmark.url!}'>${lpcCert.bookmark.name}</a><#if folder.canWrite(user)> <a onclick="return confirm('Are you sure you wish to delete this document?');" href="documents!delete.action?returnUrl=crewMember.action?id=${id}%26tab=role&path=${lpcCert.bookmark.bookmarkedId}">&nbsp;&nbsp;&nbsp;[Delete]</a></#if></div>
                </#if>
            </div>
  		   <br/>
<!-- OPC Expiry Date -->	
			<div class="fm-opt">		
				<label for="opcExpiryDate"><span class="star">*</span>OPC Expiry:</label>
   				<input name="opcExpiryDate"  type="text" class="date-pick" id="licenceexpiry" value="<#if crewMember.role.opcCert.expiryDate??>${crewMember.role.opcCert.expiryDate?string('dd/MM/yyyy')}<#else>${opcExpiryDate!}</#if>" /> 				
				<div id="msg-opcexpiry" style="color:red; font-weight: bold; margin-left: 90px;"></div>
                </div>
			    		   
<!-- Operational Proficiency Check (OPC)--> 			
				<div class="fm-opt">
				<label for="opcCertFile">Operational Proficiency Check(OPC) Upload:</label>
				<#if user.hasPermission("UserAdmin")>
                <input id="opcCertFile" name="opcCertFile" value="" type="file" />
                </#if>
                <input name="opcCertTags" value="OPC ${id} proficiencyRecords" type="hidden" />
                <#if opcCert?exists>
                  <label for="opcCertUploadLink"/>&nbsp;</label>
                  <div id="opcCertUploadLink"><a href='${request.contextPath}${opcCert.bookmark.url!}'>${opcCert.bookmark.name}</a><#if folder.canWrite(user)> <a onclick="return confirm('Are you sure you wish to delete this document?');" href="documents!delete.action?returnUrl=crewMember.action?id=${id}%26tab=role&path=${opcCert.bookmark.bookmarkedId}">&nbsp;&nbsp;&nbsp;[Delete]</a></#if></div>
                </#if>
            </div>  
            <br/>
<!-- Route Check--> 			
				<div class="fm-opt">
				<label for="routeCheckExpiryDate">Route Check :Base (Date Completed) </label>
                <input name="routeCheckExpiryDate"  type="text" class="date-pick" value="<#if crewMember.role.routCheck.expiryDate??>${crewMember.role.routCheck.expiryDate?string('dd/MM/yyyy')}<#else>${routeCheckExpiryDate!}</#if>" />
				
            </div>              
            <br/>
              </#if> 
            <label for=""><b><u>Quizzes:</u></b></label>
            <br/>
<!-- operationsManual Expiry Date -->	
			<div class="fm-opt">		
				<label for="operationsManualExpiry"><span class="star">*</span>Manual Expiry:</label>
   				<input name="operationsManualExpiry"  type="text" class="date-pick" id="licenceexpiry" value="<#if crewMember.role.operationsManualCert.expiryDate??>${crewMember.role.operationsManualCert.expiryDate?string('dd/MM/yyyy')}<#else>${operationsManualExpiry!}</#if>" /> 				
				<div id="msg-opsManexpiry" style="color:red; font-weight: bold; margin-left: 90px;"></div>
			</div>
		 
<!-- Operational Manual --> 			
				<div class="fm-opt">
				<label for="operationsManualCertFile">Operations Manual Upload:</label>
				<#if user.hasPermission("UserAdmin")>
                <input id="operationsManualCertFile" name="operationsManualCertFile" value="" type="file" />
                </#if>
                <input name="operationsManualCertTags" value="opsManual ${id} proficiencyRecords" type="hidden" />
                <#if operationsManualCert?exists>
                  <label for="operationsManualCertUploadLink"/>&nbsp;</label>
                  <div id="operationsManualCertUploadLink"><a href='${request.contextPath}${operationsManualCert.bookmark.url!}'>${operationsManualCert.bookmark.name}</a><#if folder.canWrite(user)> <a onclick="return confirm('Are you sure you wish to delete this document?');" href="documents!delete.action?returnUrl=crewMember.action?id=${id}%26tab=role&path=${operationsManualCert.bookmark.bookmarkedId}">&nbsp;&nbsp;&nbsp;[Delete]</a></#if></div>
                </#if>
            </div>  
            <br/>
            <#if crewMember.role.position?if_exists == "Pilot">
<!-- annual Tech Expiry Date -->	
            <div class="fm-opt">	
				<label for="annualTechnicalManual">Manual Expiry:</label>
   				<input name="annualTechnicalManual"  type="text" class="date-pick" id="licenceexpiry" value="<#if crewMember.role.annualTechnicalManualCert.expiryDate??>${crewMember.role.annualTechnicalManualCert.expiryDate?string('dd/MM/yyyy')}<#else>${annualTechnicalManual!}</#if>" /> 				
				<div id="msg-annualManexpiry" style="color:red; font-weight: bold; margin-left: 90px;"></div>
                
			</div>                 
<!-- Annual Technical Manual --> 			
				<div class="fm-opt">
				<label for="annualTechnicalManualCertFile">Annual Technical Upload:</label>
				<#if user.hasPermission("UserAdmin")>
                <input id="annualTechnicalManualCertFile" name="annualTechnicalManualCertFile" value="" type="file" />
                </#if>
                <input name="annualTechnicalManualCertTags" value="annualTechManual ${id} proficiencyRecords" type="hidden" />
                <#if annualTechnicalManualCert?exists>
                  <label for="annualTechnicalManualCertUploadLink"/>&nbsp;</label>
                  <div id="annualTechnicalManualCertUploadLink"><a href='${request.contextPath}${annualTechnicalManualCert.bookmark.url!}'>${annualTechnicalManualCert.bookmark.name}</a><#if folder.canWrite(user)> <a onclick="return confirm('Are you sure you wish to delete this document?');" href="documents!delete.action?returnUrl=crewMember.action?id=${id}%26tab=role&path=${annualTechnicalManualCert.bookmark.bookmarkedId}">&nbsp;&nbsp;&nbsp;[Delete]</a></#if></div>
                </#if>
            </div>  
            </#if>
          
                              
		</fieldset>
		</#if>
	
		</div>
		<hr class="clear"/>
		<#if !readOnly>                                  
		<button type="button" class="smooth" style="float:right; margin-right:10px; margin-bottom: 4px;" onclick="validate()" ><img src="images/icons/pencil.png"/>Save</button>
		<div id="msg-error"></div>
  <hr class="clear"/>
		</#if>
	</form>
	<script>$('.imageCalc').calculator({showOn: 'button', buttonImageOnly: true, buttonImage: 'images/calculator.png'});</script>
<form action="crewMember.action" id="switchrole" method="GET">
<input type="hidden" name="tab" value="role"/>
<input type="hidden" name="id" value="${crewMember.code}"/>
<input type="hidden" id="switch_role_to" name="switch_role_to" value="blah"/>"
</form>
  
</body>
</html>