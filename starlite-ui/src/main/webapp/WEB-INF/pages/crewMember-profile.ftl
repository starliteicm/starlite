<#include "/starlite.ftl">
<html>
<head>  
<link rel="stylesheet" type="text/css" href="styles/forms.css">  
<style type="text/css">
   
.personal{
	border:none;	
	color:black;
	}
   
</style>

</head>
<body>
       <div style="float:left; width: 500px;">
		<fieldset>
			<legend>Photo</legend>
			<div class="fm-opt">			
			    <img name="image" src="crewMember!photo.action?id=${id!}" style="width:100px;height:100px;">
			</div>
		</fieldset>	
		<hr class="clear"/>	
		<fieldset>	
			<legend>Name</legend>
			<div class="fm-opt">											
				<label for="title">Title:</label>
				<input type="text" name="title" class="personal" value="${crewMember.personal.title!}" />
			</div>
			<div class="fm-opt">							
				<label for="fname">First name:</label>
				<input type="text" name="fname" class="personal" value="${crewMember.personal.firstName!}" />				
			</div>
            <div class="fm-opt">							
				<label for="fname">Last name:</label>
				<input type="text" name="lname" class="personal" value="${crewMember.personal.lastName!}" />				
			</div>
            <div class="fm-opt">							
				<label for="pname">Preferred name:</label>
				<input type="text" name="pname" class="personal" value="${crewMember.personal.preferredName!}" />				
			</div>
            <div class="fm-opt">							
				<label for="coi">Country of issue:</label>
				<input type="text" name="coi" class="personal" value="${crewMember.personal.passportCountry!}" />				
			</div>
            <div class="fm-opt">							
				<label for="fname">Passport number:</label>
				<input type="text" name="lname" class="personal" value="${crewMember.personal.passportNumber!}" />				
			</div>
            <div class="fm-opt">							
				<label for="fname">Passport expiry date:</label>
				<input type="text" name="lname" class="personal" value="${crewMember.personal.passportExpiryDate!}" />				
			</div>            
		</fieldset>		
		<fieldset>	
			<legend>Address</legend>
			<div class="fm-opt">							
				<label for="address1">Address 1:</label>
				<input type="text" name="address1" class="personal" value="${crewMember.personal.address1!}" />				
			</div>
   <div class="fm-opt">							
				<label for="address2">Address 2:</label>
				<input type="text" name="address2" class="personal" value="${crewMember.personal.address2!}" />				
			</div>	
   <div class="fm-opt">							
				<label for="address3">Address 3:</label>
				<input type="text" name="address3" class="personal" value="${crewMember.personal.address3!}" />				
			</div>	
   <div class="fm-opt">							
				<label for="address4">Address 4:</label>
				<input type="text" name="address4" class="personal" value="${crewMember.personal.address4!}" />				
			</div>   	
   <div class="fm-opt">							
				<label for="address5">Address 5:</label>
				<input type="text" name="address5" class="personal" value="${crewMember.personal.address5!}" />				
			</div>
   <br>
    <div class="fm-opt">							
				<label for="postalAddress">Postal Address:</label>
				<input type="text" name="postalAddress" class="personal" value="${crewMember.personal.postalAddress!}" />				
			</div>
   <div class="fm-opt">							
				<label for="postalTown">Postal Town:</label>
				<input type="text" name="postalTown" class="personal" value="${crewMember.personal.postalTown!}" />				
			</div>
   <div class="fm-opt">							
				<label for="postalCountry">Postal Country:</label>
				<input type="text" name="postalCountry" class="personal" value="${crewMember.personal.postalCountry!}" />				
			</div>

   <div class="fm-opt">							
				<label for="address5">Address 5:</label>
				<input type="text" name="address5" class="personal" value="${crewMember.personal.address5!}" />				
			</div>
   <div class="fm-opt">							
				<label for="postalCode">Postal Code:</label>
				<input type="text" name="postalCode" class="personal" value="${crewMember.personal.postalCode!}" />				
			</div>			
		</fieldset>
		 <fieldset>
			<legend>Contact Details</legend>
		 <div class="fm-opt">							
				<label for="mobilePhone">Mobile Phone:</label>
				<input type="text" name="mobilePhone" class="personal" value="${crewMember.personal.mobilePhone!}" />				
			</div>
			<div class="fm-opt">							
				<label for="mobilePhone">Alt. Mobile Phone:</label>
				<input type="text" name="mobilePhone" class="personal" value="${crewMember.personal.mobilePhone2!}" />				
			</div>
			<div class="fm-opt">							
				<label for="mobilePhone">Home Phone:</label>
				<input type="text" name="mobilePhone" class="personal" value="${crewMember.personal.homePhone!}" />				
			</div>
			<div class="fm-opt">							
				<label for="mobilePhone">Home Fax:</label>
				<input type="text" name="mobilePhone" class="personal" value="${crewMember.personal.homeFax!}" />				
			</div>
			<div class="fm-opt">							
				<label for="mobilePhone">Business Phone:</label>
				<input type="text" name="mobilePhone" class="personal" value="${crewMember.personal.businessPhone!}" />				
			</div>
			<div class="fm-opt">							
				<label for="mobilePhone">Business Fax:</label>
				<input type="text" name="mobilePhone" class="personal" value="${crewMember.personal.businessFax!}" />				
			</div>
			<div class="fm-opt">							
				<label for="email">Email:</label>
				<input type="text" name="email" class="personal" value="${crewMember.personal.email!}" />				
			</div>
			<div class="fm-opt">							
				<label for="alternateEmail">Alt. Email:</label>
				<input type="text" name="alternateEmail" class="personal" value="${crewMember.personal.alternateEmail!}" />				
			</div>
		</fieldset>	
  
  <fieldset>
  <legend>Medical</legend>
			<div class="fm-opt">							
				<label for="medicalAid">Medical Aid:</label>
				<input type="text" name="medicalAid" class="personal" value="${crewMember.role.medicalAid!}" />				
			</div>
						<div class="fm-opt">							
				<label for="medicalAidNumber">Aid Number:</label>
				<input type="text" name="medicalAidNumber" class="personal" value="${crewMember.role.medicalAidNumber!}" />				
			</div>

  </fieldset>
    </div
 <div style="float:left; width: 500px;">
  <fieldset>
			<legend>Personal</legend>
		 <div class="fm-opt">							
				<label for="gender">Gender:</label>
				<input type="text" name="gender" class="personal" value="${crewMember.personal.gender!}" />				
			</div>
			<div class="fm-opt">							
				<label for="dateOfBirth">Date Of Birth:</label>
				<input type="text" name="dateOfBirth" class="personal" value="${crewMember.personal.dateOfBirth!}" />				
			</div>
			<div class="fm-opt">							
				<label for="nationality">Nationality:</label>
				<input type="text" name="nationality" class="personal" value="${crewMember.personal.nationality!}" />				
			</div>
			<div class="fm-opt">							
				<label for="status">Marital Status:</label>
				<input type="text" name="status" class="personal" value="${crewMember.personal.status!}" />				
			</div>
			<div class="fm-opt">							
				<label for="idNumber">ID Number:</label>
				<input type="text" name="idNumber" class="personal" value="${crewMember.personal.idNumber!}" />				
			</div>
		</fieldset>	

  <fieldset>
			<legend>Driving Licence</legend>
		 <div class="fm-opt">							
				<label for="drivingLicenceNumber">Driving Licence:</label>
				<input type="text" name="drivingLicenceNumber" class="personal" value="${crewMember.role.drivingLicenceNumber!}" />				
			</div>
			<div class="fm-opt">							
				<label for="drivingLicenceIssued">Driving Licence Issued:</label>
				<input type="text" name="drivingLicenceIssued" class="personal" value="${crewMember.role.drivingLicenceIssued!}" />				
			</div>
		</fieldset>  
    <fieldset>
			<legend>Next Of Kin</legend>
		 <div class="fm-opt">							
				<label for="nextOfKinFirstName">First Name:</label>
				<input type="text" name="nextOfKinFirstName" class="personal" value="${crewMember.personal.nextOfKinFirstName!}" />				
			</div>
			<div class="fm-opt">							
				<label for="nextOfKinLastName">Last Name:</label>
				<input type="text" name="nextOfKinLastName" class="personal" value="${crewMember.personal.nextOfKinLastName!}" />				
			</div>
			<div class="fm-opt">	
    				<label for="nextOfKinMobilePhone">Mobile Phone:</label>
				<input type="text" name="nextOfKinMobilePhone" class="personal" value="${crewMember.personal.nextOfKinMobilePhone!}" />				
			</div>
			<div class="fm-opt">							
				<label for="nextOfKinHomePhone">Home Phone:</label>
				<input type="text" name="nextOfKinHomePhone" class="personal" value="${crewMember.personal.nextOfKinHomePhone!}" />				
			</div>
			<div class="fm-opt">							
				<label for="nextOfKinRelation">Relation:</label>
				<input type="text" name="nextOfKinRelation" class="personal" value="${crewMember.personal.nextOfKinRelation!}" />				
			</div>
   <br>
   			<div class="fm-opt">							
				<label for="nextOfKinAddress1">Address Line 1:</label>
				<input type="text" name="nextOfKinAddress1" class="personal" value="${crewMember.personal.nextOfKinAddress1!}" />				
			</div>
   			<div class="fm-opt">							
				<label for="nextOfKinAddress2">Address Line 2:</label>
				<input type="text" name="nextOfKinAddress2" class="personal" value="${crewMember.personal.nextOfKinAddress2!}" />				
			</div>
   			<div class="fm-opt">							
				<label for="nextOfKinAddress3">Address Line 3:</label>
				<input type="text" name="nextOfKinAddress3" class="personal" value="${crewMember.personal.nextOfKinAddress3!}" />				
			</div>
   			<div class="fm-opt">							
				<label for="nextOfKinAddress4">Address Line 4:</label>
				<input type="text" name="nextOfKinAddress4" class="personal" value="${crewMember.personal.nextOfKinAddress4!}" />				
			</div>
   			<div class="fm-opt">							
				<label for="nextOfKinAddress5">Address Line 5:</label>
				<input type="text" name="nextOfKinAddress5" class="personal" value="${crewMember.personal.nextOfKinAddress5!}" />				
			</div>   		
		</fieldset>
    <fieldset>
			<legend>Emergency Contact Details (Different from Next of Kin)</legend>
		 <div class="fm-opt">							
				<label for="emergencyContactName">Contact Name:</label>
				<input type="text" name="emergencyContactName" class="personal" value="${crewMember.personal.emergencyContactName!}" />				
			</div>
			<div class="fm-opt">							
				<label for="emergencyContactRelationship">Contact Relation:</label>
				<input type="text" name="emergencyContactRelationship" class="personal" value="${crewMember.personal.emergencyContactRelationship!}" />				
			</div>
			<div class="fm-opt">	
    				<label for="emergencyContactNumber">Contact Number:</label>
				<input type="text" name="emergencyContactNumber" class="personal" value="${crewMember.personal.emergencyContactNumber!}" />				
			</div>
   <br>
   			<div class="fm-opt">							
				<label for="nextOfKinAddress1">Alt. Contact Name:</label>
				<input type="text" name="alternativeEmergencyContactName" class="personal" value="${crewMember.personal.alternativeEmergencyContactName!}" />				
			</div>
   			<div class="fm-opt">							
				<label for="alternativeEmergencyContactRelationship">Alt. Contact Relation:</label>
				<input type="text" name="alternativeEmergencyContactRelationship" class="personal" value="${crewMember.personal.alternativeEmergencyContactRelationship!}" />				
			</div>
   			<div class="fm-opt">							
				<label for="alternativeEmergencyContactNumber">Alt. Contact Number:</label>
				<input type="text" name="alternativeEmergencyContactNumber" class="personal" value="${crewMember.personal.alternativeEmergencyContactNumber!}" />				
			</div>   		
		</fieldset>

  </div>	
</body>
</html>