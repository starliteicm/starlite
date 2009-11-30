<#include "/starlite.ftl">
<html>
<head>
  <title>${crewMember.personal.firstName!} ${crewMember.personal.lastName!}</title>
  <@enableJQuery/>
  <@enableDatePickers/>
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
	<form action="crewMember!save.action" method="POST" class="smart" style="clear:left;" enctype="multipart/form-data">
	</#if>
		<input type="hidden" name="id" value="${id!}"/>
		<input type="hidden" name="crewMember.id" value="${crewMember.code!}"/>
		<input type="hidden" name="tab" value="personal"/>
		
		<div style="padding-top:25px;position:absolute;left:950px;"><button type="button" onclick="return false;" class="smooth" style="float:right; margin-right:10px; margin-bottom: 4px;"><img src="images/icons/printer.png"/> Print</button></div>
		
		
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
				<label for="crewMember.personal.title">Title:</label>
				<#assign title=crewMember.personal.title!/>
	    		<select name="crewMember.personal.title">
	    		    <option> 
	    			<option <#if title == 'Mr'>selected</#if> >Mr
	    			<option <#if title == 'Master'>selected</#if> >Master
	    			<option <#if title == 'Mrs'>selected</#if> >Mrs
	    			<option <#if title == 'Miss'>selected</#if> >Miss
	    			<option <#if title == 'Ms'>selected</#if> >Ms
	    			<option <#if title == 'Doctor'>selected</#if> >Doctor
	    			<option <#if title == 'Professor'>selected</#if> >Professor
	    			<option <#if title == 'Sir'>selected</#if> >Sir
	    		</select>
	    		<br/>
	    	</div>
			<div class="fm-opt">
				<label for="crewMember.personal.firstName">First Name:</label>
				<input name="crewMember.personal.firstName" type="text" value="${crewMember.personal.firstName!}"/>
			</div>
			<div class="fm-opt">
				<label for="crewMember.personal.secondName">Second Name:</label>
				<input name="crewMember.personal.secondName" type="text" value="${crewMember.personal.secondName!}"/>
			</div>
			<div class="fm-opt">
				<label for="crewMember.personal.lastName">Last Name:</label>
				<input name="crewMember.personal.lastName" type="text" value="${crewMember.personal.lastName!}"/>
			</div>
			<div class="fm-opt">
                <label for="crewMember.personal.preferedName">Prefered Name:</label>
                <input name="crewMember.personal.preferedName" type="text" value="${crewMember.personal.preferedName!}"/>
            </div>
		</fieldset>
	    <fieldset>
			<legend>Passport</legend>
			<div class="fm-opt">
				<label for="crewMember.personal.passportCountry">Country of Issue:</label>
				<input name="crewMember.personal.passportCountry" type="text" value="${crewMember.personal.passportCountry!}"/>
			</div>
			<div class="fm-opt">
				<label for="crewMember.personal.passportNumber">Passport Number:</label>
				<input name="crewMember.personal.passportNumber" type="text" value="${crewMember.personal.passportNumber!}"/>
			</div>
			<div class="fm-opt">
				<label for="crewMember.personal.passportExpiryDate">Expiry Date:</label>
				<input name="crewMember.personal.passportExpiryDate" class="date-pick" type="text" value="<#if crewMember.personal.passportExpiryDate??>${crewMember.personal.passportExpiryDate?string('dd/MM/yyyy')}</#if>"/>				
			</div>
			<div class="fm-opt">
                <label for="crewMember.personal.passportNumber">Upload:</label>
                <input name="passport" type="file" value=""/>
                <input type="hidden" name="passportTags" value="passport">                
            </div>
		</fieldset>
		<fieldset>
			<legend>Address</legend>
			<div class="fm-opt">
				<label for="crewMember.personal.address1">Line 1:</label>
				<input name="crewMember.personal.address1" type="text" value="${crewMember.personal.address1!}"/>
			</div>
			<div class="fm-opt">
				<label for="crewMember.personal.address2">Line 2:</label>
				<input name="crewMember.personal.address2" type="text" value="${crewMember.personal.address2!}"/>
			</div>
			<div class="fm-opt">
				<label for="crewMember.personal.address3">Line 3:</label>
				<input name="crewMember.personal.address3" type="text" value="${crewMember.personal.address3!}"/>
			</div>
			<div class="fm-opt">
				<label for="crewMember.personal.address4">Line 4:</label>
				<input name="crewMember.personal.address4" type="text" value="${crewMember.personal.address4!}"/>
			</div>
			<div class="fm-opt">
				<label for="crewMember.personal.address5">Line 5:</label>
				<input name="crewMember.personal.address5" type="text" value="${crewMember.personal.address5!}"/>
			</div>
			<br/>
			<div class="fm-opt">
				<label for="crewMember.personal.postalAddress">Postal Address:</label>
				<input name="crewMember.personal.postalAddress" type="text" value="${crewMember.personal.postalAddress!}"/>
			</div>
			<div class="fm-opt">
				<label for="crewMember.personal.postalTown">Postal Town:</label>
				<input name="crewMember.personal.postalTown" type="text" value="${crewMember.personal.postalTown!}"/>
			</div>
			<div class="fm-opt">
				<label for="crewMember.personal.postalCountry">Postal Country:</label>
				<input name="crewMember.personal.postalCountry" type="text" value="${crewMember.personal.postalCountry!}"/>
			</div>
			<div class="fm-opt">
				<label for="crewMember.personal.postalCode">Postal Code:</label>
				<input name="crewMember.personal.postalCode" type="text" value="${crewMember.personal.postalCode!}"/>
			</div>
		</fieldset>
		<fieldset>
			<legend>Contact Details</legend>
			<div class="fm-opt">
				<label for="crewMember.personal.mobilePhone">Mobile Phone:</label>
				<input name="crewMember.personal.mobilePhone" type="text" value="${crewMember.personal.mobilePhone!}"/>
			</div>
			<div class="fm-opt">
				<label for="crewMember.personal.mobilePhone2">Alt. Mobile Phone:</label>
				<input name="crewMember.personal.mobilePhone2" type="text" value="${crewMember.personal.mobilePhone2!}"/>
			</div>
			<div class="fm-opt">
				<label for="crewMember.personal.homePhone">Home Phone:</label>
				<input name="crewMember.personal.homePhone" type="text" value="${crewMember.personal.homePhone!}"/>
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
				<label for="crewMember.personal.email">Email:</label>
				<input name="crewMember.personal.email" type="text" value="${crewMember.personal.email!}"/>
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
				<label for="crewMember.personal.status">Gender:</label>
				<#assign gender=crewMember.personal.gender!/>
				<select name="crewMember.personal.gender">
					<option>
					<option <#if gender == 'Male'>selected</#if>>Male
					<option <#if gender == 'Female'>selected</#if>>Female
					<option <#if gender == 'Other'>selected</#if>>Other
				</select>
			</div>
			<div class="fm-opt">
				<label for="crewMember.personal.dateOfBirth">Date Of Birth:</label>
				<input name="crewMember.personal.dateOfBirth" type="text" class="date-pick" value="<#if crewMember.personal.dateOfBirth??>${crewMember.personal.dateOfBirth?string('dd/MM/yyyy')}</#if>"/>
			</div>
			<div class="fm-opt">
				<label for="crewMember.personal.nationality">Nationality:</label>
				<input name="crewMember.personal.nationality" type="text" value="${crewMember.personal.nationality!}"/>
			</div>
			<div class="fm-opt">
				<label for="crewMember.personal.status">Marital Status:</label>
				<#assign status=crewMember.personal.status!/>
				<select name="crewMember.personal.status">
					<option>
					<option <#if status == 'Divorced'>selected</#if>>Divorced
					<option <#if status == 'Engaged'>selected</#if>>Engaged
					<option <#if status == 'Married'>selected</#if>>Married
					<option <#if status == 'Single'>selected</#if>>Single
					<option <#if status == 'Widowed'>selected</#if>>Widowed
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
				<label for="crewMember.personal.nextOfKinFirstName">First Name:</label>
				<input name="crewMember.personal.nextOfKinFirstName" type="text" value="${crewMember.personal.nextOfKinFirstName!}"/>
			</div>
			<div class="fm-opt">
				<label for="crewMember.personal.nextOfKinLastName">Last Name:</label>
				<input name="crewMember.personal.nextOfKinLastName" type="text" value="${crewMember.personal.nextOfKinLastName!}"/>
			</div>
			<div class="fm-opt">
				<label for="crewMember.personal.nextOfKinMobilePhone">Mobile Phone</label>
				<input name="crewMember.personal.nextOfKinMobilePhone" type="text" value="${crewMember.personal.nextOfKinMobilePhone!}"/>
			</div>
			<div class="fm-opt">
				<label for="crewMember.personal.nextOfKinHomePhone">Home Phone:</label>
				<input name="crewMember.personal.nextOfKinHomePhone" type="text" value="${crewMember.personal.nextOfKinHomePhone!}"/>
			</div>
			<div class="fm-opt">
				<label for="crewMember.personal.nextOfKinRelation">Relation:</label>
				<input name="crewMember.personal.nextOfKinRelation" type="text" value="${crewMember.personal.nextOfKinRelation!}"/>
			</div>
			<br/>
			<div class="fm-opt">
				<label for="crewMember.personal.nextOfKinAddress1">Address Line 1:</label>
				<input name="crewMember.personal.nextOfKinAddress1" type="text" value="${crewMember.personal.nextOfKinAddress1!}"/>
			</div>
			<div class="fm-opt">
				<label for="crewMember.personal.nextOfKinAddress2">Address Line 2:</label>
				<input name="crewMember.personal.nextOfKinAddress2" type="text" value="${crewMember.personal.nextOfKinAddress2!}"/>
			</div>
			<div class="fm-opt">
				<label for="crewMember.personal.nextOfKinAddress3">Address Line 3:</label>
				<input name="crewMember.personal.nextOfKinAddress3" type="text" value="${crewMember.personal.nextOfKinAddress3!}"/>
			</div>
			<div class="fm-opt">
				<label for="crewMember.personal.nextOfKinAddress4">Address Line 4:</label>
				<input name="crewMember.personal.nextOfKinAddress4" type="text" value="${crewMember.personal.nextOfKinAddress4!}"/>
			</div>
			<div class="fm-opt">
				<label for="crewMember.personal.nextOfKinAddress5">Address Line 5:</label>
				<input name="crewMember.personal.nextOfKinAddress5" type="text" value="${crewMember.personal.nextOfKinAddress5!}"/>
			</div>
		</fieldset>
		<fieldset>
			<legend>Emergency Contact Details (Different from Next of Kin)</legend>
			<div class="fm-opt">
				<label for="crewMember.personal.emergencyContactName">Contact Name</label>
				<input name="crewMember.personal.emergencyContactName" type="text" value="${crewMember.personal.emergencyContactName!}"/>
			</div>
			<div class="fm-opt">
				<label for="crewMember.personal.emergencyContactRelationship">Contact Relation</label>
				<input name="crewMember.personal.emergencyContactRelationship" type="text" value="${crewMember.personal.emergencyContactRelationship!}"/>
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
		<button type="submit" class="smooth" style="float:right; margin-right:10px; margin-bottom: 4px;"><img src="images/icons/pencil.png"/> Save</button>
		<hr class="clear"/>
		</#if>
	</form>
</body>
</html>