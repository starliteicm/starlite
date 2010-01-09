<#include "/starlite.ftl">
<html>
<head>  
<link rel="stylesheet" type="text/css" href="styles/forms.css">  
<link rel="stylesheet" type="text/css" media="print" href="styles/profile-print.css">
<style type="text/css">
   
.field{	
	}  

#datetime{
 float:right;
 } 
  
</style>                       

</head>
<body>
<div id="datetime">
<script type="text/javascript">
var currentTime = new Date()
var hours = currentTime.getHours()
var minutes = currentTime.getMinutes()
var month = currentTime.getMonth() + 1
var day = currentTime.getDate()
var year = currentTime.getFullYear()

document.write(month + "/" + day + "/" + year)
document.write(" - ")

if (minutes < 10){
minutes = "0" + minutes
}
document.write(hours + ":" + minutes + " ")
if(hours > 11){
document.write("PM")
} else {
document.write("AM")
}
</script>
</div>
   <div class="profile-left">
   <div style="float:left; width: 500px;">			
			<div class="fm-opt" style="padding-left:120px;">			
			    <img name="image" src="crewMember!photo.action?id=${id!}" style="width:100px;height:100px;">
			</div>
			
		<hr class="clear"/>
  <div style="padding-top:25px;position:absolute;left:950px;" class="button"><button type="button" onclick="window.print()" class="smooth" style="float:right; margin-right:10px; margin-bottom: 4px;"><img src="images/icons/printer.png"/> Print</button></div>		
			<div class="fm-opt">											
				<label for="title" style="height:auto;">Title:</label>
				<div class="field" style="width:300px;height:auto;" name="title">${crewMember.personal.title!} &nbsp;</div>
			</div>
			<div class="fm-opt">							
				<label for="fname" style="height:auto;">First name:</label>
				<div class="field" style="position:absolute;left:195px;width:150px;height:auto;" name="fname">${crewMember.personal.firstName!} &nbsp;</div>				
                <div class="field" style="visibility:hidden;width:300px;height:auto;" name="fname">${crewMember.personal.firstName!} &nbsp;</div>             
			</div>
            <div class="fm-opt">							
				<label for="lname" style="height:auto;">Last name:</label>
				<div class="field" style="position:absolute;left:195px;width:150px;height:auto;" name="lname">${crewMember.personal.lastName!} &nbsp; </div>				
                <div class="field" style="visibility:hidden;width:300px;height:auto;" name="lname">${crewMember.personal.lastName!} &nbsp; </div>             
			</div>
			
			<div class="fm-opt">                         
                <label for="gender">Gender:</label>
                <div class="field" name="gender">${crewMember.personal.gender!}&nbsp;</div>             
            </div>                                                             
            <div class="fm-opt">                            
                <label for="dateOfBirth">Date Of Birth:</label>
                <div class="field" name="dateOfBirth">${crewMember.personal.dateOfBirth!}&nbsp;</div>               
            </div>
            <div class="fm-opt">                            
                <label for="nationality">Nationality:</label>
                <div class="field" name="nationality">${crewMember.personal.nationality!}&nbsp;</div>               
            </div>
            <div class="fm-opt">                            
                <label for="status">Marital Status:</label>
                <div class="field" name="status">${crewMember.personal.status!}&nbsp;</div>             
            </div>
            
            <br/>
			 
   <div class="fm-opt">							
				<label for="coi" style="height:auto;">Country of issue:</label>
				<div class="field" style="position:absolute;left:195px;width:150px;height:auto;" name="coi">${crewMember.personal.passportCountry!}&nbsp;</div>				
                <div class="field" style="visibility:hidden;width:300px;height:auto;" name="coi">${crewMember.personal.passportCountry!}&nbsp;</div>              
			</div>
   <div class="fm-opt">							
				<label for="fname" style="height:auto;">Passport number:</label>
				<div class="field" style="position:absolute;left:195px;width:150px;height:auto;" name="lname">${crewMember.personal.passportNumber!}&nbsp;</div>				
                <div class="field" style="visibility:hidden;width:300px;height:auto;" name="lname">${crewMember.personal.passportNumber!}&nbsp;</div>             
			</div>
   <div class="fm-opt">							
				<label for="fname" style="height:auto;">Passport expiry date:</label>
				<div class="field" style="position:absolute;left:195px;width:150px;height:auto;" name="lname">${crewMember.personal.passportExpiryDate!}&nbsp;</div>				
                <div class="field" style="visibility:hidden;width:300px;height:auto;" name="lname">${crewMember.personal.passportExpiryDate!}&nbsp;</div>             
			</div>            
			
			<br/>
			
			<div class="fm-opt" >							
				<label for="address1" style="height:auto;">Address 1:</label>
				<div class="field" style="position:absolute;left:195px;width:150px;height:auto;" name="address1">${crewMember.personal.address1!}&nbsp;</div>				
                <div class="field" style="visibility:hidden;width:300px;height:auto;" name="address1">${crewMember.personal.address1!}&nbsp;</div>                
			</div>
   <div class="fm-opt">							
				<label for="address2" style="height:auto;">Address 2:</label>
				<div class="field" style="position:absolute;left:195px;width:150px;height:auto;" name="address2">${crewMember.personal.address2!}&nbsp;</div>				
                <div class="field" style="visibility:hidden;width:300px;height:auto;" name="address2">${crewMember.personal.address2!}&nbsp;</div>                

			</div>	
   <div class="fm-opt">							
				<label for="address3" style="height:auto;">Address 3:</label>
				<div class="field" style="position:absolute;left:195px;width:150px;height:auto;" name="address3">${crewMember.personal.address3!}&nbsp;</div>				
                <div class="field" style="visibility:hidden;width:300px;height:auto;" name="address3">${crewMember.personal.address3!}&nbsp;</div>                

			</div>	
   <div class="fm-opt">							
				<label for="address4" style="height:auto;">Address 4:</label>
				<div class="field" style="position:absolute;left:195px;width:150px;height:auto;" name="address4">${crewMember.personal.address4!}&nbsp;</div>				
                <div class="field" style="visibility:hidden;width:300px;height:auto;" name="address4">${crewMember.personal.address4!}&nbsp;</div>                

			</div>   	
   <br>
    <div class="fm-opt">							
				<label for="postalAddress" style="height:auto;">Postal Address:</label>
				<div class="field" style="position:absolute;left:195px;width:150px;height:auto;" name="postalAddress">${crewMember.personal.postalAddress!}&nbsp;</div>
                <div class="field" style="visibility:hidden;width:300px;height:auto;" name="postalAddress">${crewMember.personal.postalAddress!}&nbsp;</div>				
			</div>
   <div class="fm-opt">							
				<label for="postalTown" style="height:100%;">Postal Town:</label>
				<div class="field" style="position:absolute;left:195px;width:150px;height:auto;" name="postalTown">${crewMember.personal.postalTown!}&nbsp;</div>
				<div class="field" style="visibility:hidden;width:300px;height:auto;" name="postalTown">${crewMember.personal.postalTown!}&nbsp;</div>
				
			</div>  
   <div class="fm-opt">							
				<label for="postalCode" style="height:auto;">Postal Code:</label>
				<div class="field" style="position:absolute;left:195px;width:150px;height:auto;" name="postalCode">${crewMember.personal.postalCode!}&nbsp;</div>				
                <div class="field" style="visibility:hidden;width:300px;height:auto;" name="postalCode">${crewMember.personal.postalCode!}&nbsp;</div>                
			</div>			
		
<br>	
			
		 <div class="fm-opt">							
				<label for="mobilePhone" style="height:auto;">Mobile Phone:</label>
				<div class="field" style="position:absolute;left:195px;width:150px;height:auto;" name="mobilePhone">${crewMember.personal.mobilePhone!}&nbsp;</div>				
                <div class="field" style="visibility:hidden;width:300px;height:auto;" name="mobilePhone">${crewMember.personal.mobilePhone!}&nbsp;</div>              
			</div>
			<div class="fm-opt">							
				<label for="homePhone" style="height:auto;">Home Phone:</label>
				<div class="field" style="position:absolute;left:195px;width:150px;height:auto;" name="homePhone">${crewMember.personal.homePhone!}&nbsp;</div>				
                <div class="field" style="visibility:hidden;width:300px;height:auto;" name="homePhone">${crewMember.personal.homePhone!}&nbsp;</div>              
			</div>
			<div class="fm-opt">							
				<label for="email" style="height:auto;">Email:</label>
				<div class="field" style="position:absolute;left:195px;width:150px;height:auto;" name="email">${crewMember.personal.email!}&nbsp;</div>				
                <div class="field" style="visibility:hidden;width:300px;height:auto;" name="email">${crewMember.personal.email!}&nbsp;</div>              
			</div>
  
           <br/>
  
			<div class="fm-opt">							
				<label for="medicalAid" style="height:auto;">Medical Aid:</label>
				<div class="field" style="position:absolute;left:195px;width:150px;height:auto;" name="medicalAid">${crewMember.role.medicalAid!}&nbsp;</div>				
                <div class="field" style="visibility:hidden;width:300px;height:auto;" name="medicalAid">${crewMember.role.medicalAid!}&nbsp;</div>                
			</div>
						<div class="fm-opt">							
				<label for="medicalAidNumber" style="height:auto;">Aid Number:</label>
				<div class="field" style="position:absolute;left:195px;width:150px;height:auto;" name="medicalAidNumber">${crewMember.role.medicalAidNumber!}&nbsp;</div>				
                <div class="field" style="visibility:hidden;width:300px;height:auto;" name="medicalAidNumber">${crewMember.role.medicalAidNumber!}&nbsp;</div>                
			</div>
			
			
			
        
            <br/>
         <div class="fm-opt">                           
                <label for="nextOfKinFirstName">NOK First Name:</label>
                <div class="field" name="nextOfKinFirstName">${crewMember.personal.nextOfKinFirstName!}&nbsp;</div>             
            </div>
            <div class="fm-opt">                            
                <label for="nextOfKinLastName">NOK Last Name:</label>
            <div class="field" name="nextOfKinLastName">${crewMember.personal.nextOfKinLastName!} </div/>               
            </div>
            <div class="fm-opt">    
                    <label for="nextOfKinMobilePhone">NOK Mobile Phone:</label>
                <div class="field" name="nextOfKinMobilePhone">${crewMember.personal.nextOfKinMobilePhone!}&nbsp;</div>             
            </div>
            <div class="fm-opt">                            
                <label for="nextOfKinHomePhone">NOK Home Phone:</label>
                <div class="field" name="nextOfKinHomePhone">${crewMember.personal.nextOfKinHomePhone!}&nbsp;</div>             
            </div>
            <div class="fm-opt">                            
                <label for="nextOfKinRelation">NOK Relation:</label>
                <div class="field" name="nextOfKinRelation">${crewMember.personal.nextOfKinRelation!}&nbsp;</div>               
            </div>
   
            <div class="fm-opt">                            
                <label for="nextOfKinAddress1">NOK Address Line 1:</label>
                <div class="field" name="nextOfKinAddress1">${crewMember.personal.nextOfKinAddress1!}&nbsp;</div>               
            </div>
            <div class="fm-opt">                            
                <label for="nextOfKinAddress2">NOK Address Line 2:</label>
                <div class="field" name="nextOfKinAddress2">${crewMember.personal.nextOfKinAddress2!}&nbsp;</div>               
            </div>
            <div class="fm-opt">                            
                <label for="nextOfKinAddress3">NOK Address Line 3:</label>
                <div class="field" name="nextOfKinAddress3">${crewMember.personal.nextOfKinAddress3!}&nbsp;</div>               
            </div>
            <div class="fm-opt">                            
                <label for="nextOfKinAddress4">NOK Address Line 4:</label>
                <div class="field" name="nextOfKinAddress4">${crewMember.personal.nextOfKinAddress4!}&nbsp;</div>               
            </div>
        
            <br/>
         <div class="fm-opt">                           
                <label for="emergencyContactName">EC Contact Name:</label>
                <div class="field" name="emergencyContactName">${crewMember.personal.emergencyContactName!} </div/>             
            </div>
            <div class="fm-opt">                            
                <label for="emergencyContactRelationship">EC Contact Relation:</label>
                <div class="field" name="emergencyContactRelationship">${crewMember.personal.emergencyContactRelationship!}&nbsp;</div>             
            </div>
			
			
   </div>
</div>

<div class="profile-right">
  <div style="float:left; width: 500px;">
  				 
		    <div class="fm-opt">
                <label for="crewMember.banking.bankName">Bank Name:</label>
                <div class="field" name="crewMember.banking.bankName" >${crewMember.banking.bankName!}&nbsp;</div> 
            </div>
            <div class="fm-opt">
                <label for="crewMember.banking.branchCode">Branch Code:</label>
                <div class="field" name="crewMember.banking.branchCode" >${crewMember.banking.branchCode!}&nbsp;</div> 
            </div>
            <div class="fm-opt">
                <label for="crewMember.banking.address1">Bank Address 1:</label>
                <div class="field" name="crewMember.banking.address1" >${crewMember.banking.address1!}&nbsp;</div> 
            </div>
            <div class="fm-opt">
                <label for="crewMember.banking.address2">Bank Address 2:</label>
                <div class="field" name="crewMember.banking.address2" >${crewMember.banking.address2!}&nbsp;</div> 
            </div>
            
            <div class="fm-opt">
                <label for="crewMember.banking.accountName">Account Name:</label>
                <div class="field" name="crewMember.banking.accountName" >${crewMember.banking.accountName!}&nbsp;</div> 
            </div>
            <div class="fm-opt">
                <label for="crewMember.banking.accountNumber">Account Number:</label>
                <div class="field" name="crewMember.banking.accountNumber" >${crewMember.banking.accountNumber!}&nbsp;</div> 
            </div>
            <div class="fm-opt">
                <label for="crewMember.banking.swift">SWIFT:</label>
                <div class="field" name="crewMember.banking.swift" >${crewMember.banking.swift!}&nbsp;</div> 
            </div>
            
            <br/>
            
            <div class="fm-opt">
                <label for="crewMember.role.position">Position:</label>
                <div class="field" name="crewMember.role.position" >${crewMember.role.position!}&nbsp;</div>
            </div>
            <div class="fm-opt">
                <label for="crewMember.role.employment">Employment Status:</label>
                <div class="field" name="crewMember.role.employment" >${crewMember.role.employment!}&nbsp;</div>
            </div>
            
            <br/>
            
            <#if crewMember.role.position?if_exists != "Base Manager">
            <div class="fm-opt">
                <label for="crewMember.role.r1.number"/>Number:</label>
                <div class="field" name="crewMember.role.r1.number" >${crewMember.role.r1.number!}&nbsp;</div>
            </div>
            <div class="fm-opt">
                <label for="crewMember.role.r1.type">Type:</label>
                <div class="field" name="crewMember.role.r1.type" >${crewMember.role.r1.type!}&nbsp;</div>  
            </div>
            <div class="fm-opt">
                <label for="crewMember.role.r1.expiryDate">Expiry Date:</label>
                <div class="field" name="crewMember.role.r1.expiryDate" ><#if crewMember.role.r1.expiryDate??>${crewMember.role.r1.expiryDate?string('dd/MM/yyyy')}</#if>&nbsp;</div>                
            </div>
            <br/>
            </#if>
            
            <#if crewMember.role.position?if_exists == "Pilot">
            
            <#if crewMember.role.instructor.number?if_exists == "yes" >
            <div class="fm-opt">
                <label for="crewMember.role.r2.expiryDate">Expiry Date:</label>
                <div class="field" name="crewMember.role.r2.expiryDate"><#if crewMember.role.r2.expiryDate??>${crewMember.role.r2.expiryDate?string('dd/MM/yyyy')}</#if>&nbsp;</div>
            </div>
            </#if>
            
            <div class="fm-opt">
                <label for="crewMember.role.expiryDate">Medical Expiry:</label>
                <div class="field" name="crewMember.role.expiryDate"><#if crewMember.role.expiryDate??>${crewMember.role.expiryDate?string('dd/MM/yyyy')}</#if>&nbsp;</div>
            </div>
            
            <div class="fm-opt">
                <label for="crewMember.role.crm.expiryDate">CRM Expiry:</label>
                <div class="field" name="crewMember.role.crm.expiryDate" ><#if crewMember.role.crm.expiryDate??>${crewMember.role.crm.expiryDate?string('dd/MM/yyyy')}</#if>&nbsp;</div>
            </div>
            
            <div class="fm-opt">
                <label for="crewMember.role.dg.expiryDate">DG Expiry:</label>
                <div class="field" name="crewMember.role.dg.expiryDate" ><#if crewMember.role.dg.expiryDate??>${crewMember.role.dg.expiryDate?string('dd/MM/yyyy')}</#if>&nbsp;</div>
            </div>
            
            <br/>
            </#if>
            
            <div class="fm-opt">
                <label for="crewMember.payments.currency">Currency:</label>
                <div class="field" name="crewMember.payments.currency" >${crewMember.payments.currency!}&nbsp;</div>
            </div>
            <div class="fm-opt">
                <label for="crewMember.payments.monthlyBaseRate">Monthly:</label>
                <div class="field" name="crewMember.payments.monthlyBaseRate" >${crewMember.payments.monthlyBaseRate.amountAsDouble!}&nbsp;</div>
            </div>
            
            <div class="fm-opt">
                <label for="crewMember.payments.areaAllowance">Daily:</label>
                <div class="field" name="crewMember.payments.areaAllowance" >${crewMember.payments.areaAllowance.amountAsDouble!}&nbsp;</div>
            </div>


            <div class="fm-opt">
                <label for="crewMember.payments.dailyAllowance">Training:</label>
                <div class="field" name="crewMember.payments.dailyAllowance" >${crewMember.payments.areaAllowance.amountAsDouble!}&nbsp;</div>
            </div>

            <div class="fm-opt">
                <label for="crewMember.payments.flightAllowance">Travel:</label>
                <div class="field" name="crewMember.payments.flightAllowance" >${crewMember.payments.areaAllowance.amountAsDouble!}&nbsp;</div>
            </div>
            
            
            
            
            
			
  </div>	
</div>  
</body>
</html>