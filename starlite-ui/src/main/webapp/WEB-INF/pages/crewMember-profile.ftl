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
			<div class="fm-opt">			
			    <img name="image" src="crewMember!photo.action?id=${id!}" style="width:100px;height:100px;">
			</div>
			
		<hr class="clear"/>
  <div style="padding-top:25px;position:absolute;left:950px;" class="button"><button type="button" onclick="window.print()" class="smooth" style="float:right; margin-right:10px; margin-bottom: 4px;"><img src="images/icons/printer.png"/> Print</button></div>		
			<div class="fm-opt">											
				<label for="title">Title:</label>
				<div class="field" name="title">${crewMember.personal.title!} &nbsp;</div>
			</div>
			<div class="fm-opt">							
				<label for="fname">First name:</label>
				<div class="field" name="fname">${crewMember.personal.firstName!} &nbsp;</div>				
			</div>
   <div class="fm-opt">							
				<label for="lname">Last name:</label>
				<div class="field" name="lname">${crewMember.personal.lastName!} &nbsp; </div>				
			</div>
			
			<div class="fm-opt">							
				<label for="pname">Preferred Name:</label>
				<div class="field" name="pname">${crewMember.personal.preferredName!}&nbsp;</div>				
			</div>
			 
   <div class="fm-opt">							
				<label for="coi">Country of issue:</label>
				<div class="field" name="coi">${crewMember.personal.passportCountry!}&nbsp;</div>				
			</div>
   <div class="fm-opt">							
				<label for="fname">Passport number:</label>
				<div class="field" name="lname">${crewMember.personal.passportNumber!}&nbsp;</div>				
			</div>
   <div class="fm-opt">							
				<label for="fname">Passport expiry date:</label>
				<div class="field" name="lname">${crewMember.personal.passportExpiryDate!}&nbsp;</div>				
			</div>            
			
			<div class="fm-opt">							
				<label for="address1">Address 1:</label>
				<div class="field" name="address1">${crewMember.personal.address1!}&nbsp;</div>				
			</div>
   <div class="fm-opt">							
				<label for="address2">Address 2:</label>
				<div class="field" name="address2">${crewMember.personal.address2!}&nbsp;</div>				
			</div>	
   <div class="fm-opt">							
				<label for="address3">Address 3:</label>
				<div class="field" name="address3">${crewMember.personal.address3!}&nbsp;</div>				
			</div>	
   <div class="fm-opt">							
				<label for="address4">Address 4:</label>
				<div class="field" name="address4">${crewMember.personal.address4!}&nbsp;</div>				
			</div>   	
   <div class="fm-opt">							
				<label for="address5">Address 5:</label>
				<div class="field" name="address5">${crewMember.personal.address5!}&nbsp;</div>				
			</div>
   <br>
    <div class="fm-opt">							
				<label for="postalAddress">Postal Address:</label>
				<div class="field" name="postalAddress">${crewMember.personal.postalAddress!}&nbsp;</div>				
			</div>
   <div class="fm-opt">							
				<label for="postalTown">Postal Town:</label>
				<div class="field" name="postalTown">${crewMember.personal.postalTown!}&nbsp;</div>				
			</div>
   <div class="fm-opt">							
				<label for="postalCountry">Postal Country:</label>
				<div class="field" name="postalCountry">${crewMember.personal.postalCountry!}&nbsp;</div>				
			</div>  
   <div class="fm-opt">							
				<label for="postalCode">Postal Code:</label>
				<div class="field" name="postalCode">${crewMember.personal.postalCode!}&nbsp;</div>				
			</div>			
		
<br>	
			
		 <div class="fm-opt">							
				<label for="mobilePhone">Mobile Phone:</label>
				<div class="field" name="mobilePhone">${crewMember.personal.mobilePhone!}&nbsp;</div>				
			</div>
			<div class="fm-opt">							
				<label for="mobilePhone2">Alt. Mobile Phone:</label>
				<div class="field" name="mobilePhone2">${crewMember.personal.mobilePhone2!}&nbsp;</div>				
			</div>
			<div class="fm-opt">							
				<label for="homePhone">Home Phone:</label>
				<div class="field" name="homePhone">${crewMember.personal.homePhone!}&nbsp;</div>				
			</div>
			<div class="fm-opt">							
				<label for="homeFax">Home Fax:</label>
				<div class="field" name="homeFax">${crewMember.personal.homeFax!}&nbsp;</div>				
			</div>
			<div class="fm-opt">							
				<label for="businessPhone">Business Phone:</label>
				<div class="field" name="businessPhone">${crewMember.personal.businessPhone!}&nbsp;</div>				
			</div>
			<div class="fm-opt">							
				<label for="businessFax">Business Fax:</label>
				<div class="field" name="businessFax">${crewMember.personal.businessFax!}&nbsp;</div>				
			</div>
			<div class="fm-opt">							
				<label for="email">Email:</label>
				<div class="field" name="email">${crewMember.personal.email!}&nbsp;</div>				
			</div>
			<div class="fm-opt">							
				<label for="alternateEmail">Alt. Email:</label>
				<div class="field" name="alternateEmail">${crewMember.personal.alternateEmail!}&nbsp;</div>				
			</div>

  
<br>
  
			<div class="fm-opt">							
				<label for="medicalAid">Medical Aid:</label>
				<div class="field" name="medicalAid">${crewMember.role.medicalAid!}&nbsp;</div>				
			</div>
						<div class="fm-opt">							
				<label for="medicalAidNumber">Aid Number:</label>
				<div class="field" name="medicalAidNumber">${crewMember.role.medicalAidNumber!}&nbsp;</div>				
			</div>
   </div>
</div>
 <div class="profile-right">
 <div style="float:left; width: 500px;">			
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
			<div class="fm-opt">							
				<label for="idNumber">ID Number:</label>
				<div class="field" name="idNumber">${crewMember.personal.idNumber!}&nbsp;</div>				
			</div>
		
		 <div class="fm-opt">							
				<label for="drivingLicenceNumber">Driving Licence: </label>
				<div class="field" name="drivingLicenceNumber">${crewMember.role.drivingLicenceNumber!}&nbsp;</div>				
			</div>
			<div class="fm-opt">							
				<label for="drivingLicenceIssued">Driving Licence Issued:</label>
				<div class="field" name="drivingLicenceIssued">${crewMember.role.drivingLicenceIssued!}&nbsp;</div>				
			</div>

			
		 <div class="fm-opt">							
				<label for="nextOfKinFirstName">First Name:</label>
				<div class="field" name="nextOfKinFirstName">${crewMember.personal.nextOfKinFirstName!}&nbsp;</div>				
			</div>
			<div class="fm-opt">							
				<label for="nextOfKinLastName">Last Name:</label>
			<div class="field" name="nextOfKinLastName">${crewMember.personal.nextOfKinLastName!} </div/>				
			</div>
			<div class="fm-opt">	
    				<label for="nextOfKinMobilePhone">Mobile Phone:</label>
				<div class="field" name="nextOfKinMobilePhone">${crewMember.personal.nextOfKinMobilePhone!}&nbsp;</div>				
			</div>
			<div class="fm-opt">							
				<label for="nextOfKinHomePhone">Home Phone:</label>
				<div class="field" name="nextOfKinHomePhone">${crewMember.personal.nextOfKinHomePhone!}&nbsp;</div>				
			</div>
			<div class="fm-opt">							
				<label for="nextOfKinRelation">Relation:</label>
				<div class="field" name="nextOfKinRelation">${crewMember.personal.nextOfKinRelation!}&nbsp;</div>				
			</div>
   
   			<div class="fm-opt">							
				<label for="nextOfKinAddress1">Address Line 1:</label>
				<div class="field" name="nextOfKinAddress1">${crewMember.personal.nextOfKinAddress1!}&nbsp;</div>				
			</div>
   			<div class="fm-opt">							
				<label for="nextOfKinAddress2">Address Line 2:</label>
				<div class="field" name="nextOfKinAddress2">${crewMember.personal.nextOfKinAddress2!}&nbsp;</div>				
			</div>
   			<div class="fm-opt">							
				<label for="nextOfKinAddress3">Address Line 3:</label>
				<div class="field" name="nextOfKinAddress3">${crewMember.personal.nextOfKinAddress3!}&nbsp;</div>				
			</div>
   			<div class="fm-opt">							
				<label for="nextOfKinAddress4">Address Line 4:</label>
				<div class="field" name="nextOfKinAddress4">${crewMember.personal.nextOfKinAddress4!}&nbsp;</div>				
			</div>
   			<div class="fm-opt">							
				<label for="nextOfKinAddress5">Address Line 5:</label>
				<div class="field" name="nextOfKinAddress5">${crewMember.personal.nextOfKinAddress5!}&nbsp;</div>				
			</div>   		

		 <div class="fm-opt">							
				<label for="emergencyContactName">Contact Name:</label>
				<div class="field" name="emergencyContactName">${crewMember.personal.emergencyContactName!} </div/>				
			</div>
			<div class="fm-opt">							
				<label for="emergencyContactRelationship">Contact Relation:</label>
				<div class="field" name="emergencyContactRelationship">${crewMember.personal.emergencyContactRelationship!}&nbsp;</div>				
			</div>
			<div class="fm-opt">	
    				<label for="emergencyContactNumber">Contact Number:</label>
				<div class="field" name="emergencyContactNumber">${crewMember.personal.emergencyContactNumber!}&nbsp;</div>				
			</div>
   <br>
   			<div class="fm-opt">							
				<label for="nextOfKinAddress1">Alt. Contact Name:</label>
				<div class="field" name="alternativeEmergencyContactName">${crewMember.personal.alternativeEmergencyContactName!}&nbsp;</div>				
			</div>
   			<div class="fm-opt">							
				<label for="alternativeEmergencyContactRelationship">Alt. Contact Relation:</label>
				<div class="field" name="alternativeEmergencyContactRelationship">${crewMember.personal.alternativeEmergencyContactRelationship!}&nbsp;</div>				
			</div>
   			<div class="fm-opt">							
				<label for="alternativeEmergencyContactNumber">Alt. Contact Number:</label>
				<div class="field" name="alternativeEmergencyContactNumber">${crewMember.personal.alternativeEmergencyContactNumber!}&nbsp;</div>				
			</div>   		
  </div>	

</div>  
</body>
</html>