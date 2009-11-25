<#include "/starlite.ftl">
<html>
<head>
  <title>${crewMember.personal.firstName!} ${crewMember.personal.lastName!}</title>
  <link rel="stylesheet" type="text/css" href="styles/forms.css">
  <@enableJQuery/>
  <@enableDatePickers/>
  <@enableCalculator/>
  <@enableHelp/>
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
	<form action="#" method="POST" class="smart readonly">
	<#else>
	<form action="crewMember!save.action" method="POST" class="smart">
	</#if>
		<input type="hidden" name="id" value="${id!}"/>
		<input type="hidden" name="crewMember.id" value="${crewMember.code!}"/>
		<input type="hidden" name="tab" value="role"/>
		<div style="float:left; width: 500px;">
		<fieldset>
			<legend>Role</legend>
			<div class="fm-opt">
				<label for="crewMember.role.position">Position:</label>
				<input name="crewMember.role.position" type="text" value="${crewMember.role.position!}"/>
			</div>
			<div class="fm-opt">
				<label for="crewMember.role.primaryLocation">Primary Location:</label>
				<input name="crewMember.role.primaryLocation" type="text" value="${crewMember.role.primaryLocation!}"/>
			</div>
			<div class="fm-opt">
				<label for="crewMember.role.initialDate">Initial Date:</label>
				<input name="crewMember.role.initialDate" type="text" class="date-pick" value="<#if crewMember.role.initialDate??>${crewMember.role.initialDate?string('dd/MM/yyyy')}</#if>"/>
			</div>
			<div class="fm-opt">
				<label for="crewMember.role.employment">Employment Status:</label>
				<select name="crewMember.role.employment"/>
					<option <#if crewMember.role.employment?if_exists == "Permanent">selected</#if>>Permanent
					<option <#if crewMember.role.employment?if_exists == "Freelance">selected</#if>>Freelance
				</select>
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
			</div>
		</fieldset>
		<fieldset>
			<legend>Certificates & Licences</legend>
			<div class="fm-opt">
				<label for="crewMember.role.drivingLicenceNumber">Driving Licence:</label>
				<input name="crewMember.role.drivingLicenceNumber" type="text" value="${crewMember.role.drivingLicenceNumber!}"/>
			</div>
			<div class="fm-opt">
				<label for="crewMember.role.drivingLicenceIssued">Driving Licence Issued:</label>
				<input name="crewMember.role.drivingLicenceIssued" type="text" value="${crewMember.role.drivingLicenceIssued!}"/>
			</div>
			<br/><br/>
			<div class="fm-opt">
				<label for="crewMember.role.instructor.number">Instructor Rated:</label>
				<input name="crewMember.role.instructor.number" type="checkbox"  value="yes"  <#if crewMember.role.instructor.number?if_exists == "yes" >checked</#if> />
			</div>
			<div class="fm-opt">
				<label for="crewMember.role.instructor.type">Instructor Type:</label>
					<select name="crewMember.role.instructor.type">
			    	<option>
					<#list aircraftTypes?if_exists as aircraftType>
						<option <#if crewMember.role.instructor.type?if_exists == (aircraftType.name)>selected</#if> >${aircraftType.name!}
					</#list>
				</select>
			</div>
			<div class="fm-opt">
				<label for="crewMember.role.instructor.quantity">Instructor Grade:</label>
				<select name="crewMember.role.instructor.quantity" >
					<option <#if crewMember.role.instructor.quantity?if_exists == "Grade 1">selected</#if>>Grade 1
					<option <#if crewMember.role.instructor.quantity?if_exists == "Grade 2">selected</#if>>Grade 2
					<option <#if crewMember.role.instructor.quantity?if_exists == "Grade 3">selected</#if>>Grade 3
				</select>
			</div>
			<div class="fm-opt">
				<label for="crewMember.role.instructor.expiryDate">Instructor Expiry:</label>
				<input name="crewMember.role.instructor.expiryDate" type="text" class="date-pick" value="<#if crewMember.role.instructor.expiryDate??>${crewMember.role.instructor.expiryDate?string('dd/MM/yyyy')}</#if>"/>
			</div>
			<br/>
			<div class="fm-opt">
				<label for="crewMember.role.crm.expiryDate">CRM Expiry:</label>
				<input name="crewMember.role.crm.expiryDate" type="text" class="date-pick" value="<#if crewMember.role.crm.expiryDate??>${crewMember.role.crm.expiryDate?string('dd/MM/yyyy')}</#if>"/>
			</div>
			<div class="fm-opt">
				<label for="crewMember.role.dg.expiryDate">DG Expiry:</label>
				<input name="crewMember.role.dg.expiryDate" type="text" class="date-pick" value="<#if crewMember.role.dg.expiryDate??>${crewMember.role.dg.expiryDate?string('dd/MM/yyyy')}</#if>"/>
			</div>
			<div class="fm-opt">
				<label for="crewMember.role.ifr.expiryDate">Instrument Rating Expiry:</label>
				<input name="crewMember.role.ifr.expiryDate" type="text" class="date-pick" value="<#if crewMember.role.ifr.expiryDate??>${crewMember.role.ifr.expiryDate?string('dd/MM/yyyy')}</#if>"/>
			</div>
			<br/>
			<div class="fm-opt">
				<label for="crewMember.role.night">Night Rated:</label>
				<input name="crewMember.role.night" type="checkbox"  value="yes"  <#if crewMember.role.night?if_exists == "yes" >checked</#if> />
			</div>
			<div class="fm-opt">
				<label for="crewMember.role.nvg">NVG Rated:</label>
				<input name="crewMember.role.nvg" type="checkbox"  value="yes"  <#if crewMember.role.nvg?if_exists == "yes" >checked</#if> />
			</div>
			<div class="fm-opt">
				<label for="crewMember.role.sling">Undersling Rated:</label>
				<input name="crewMember.role.sling" type="checkbox"  value="yes"  <#if crewMember.role.sling?if_exists == "yes" >checked</#if> />
			</div>
			<div class="fm-opt">
				<label for="crewMember.role.game">Game Rated:</label>
				<input name="crewMember.role.game" type="checkbox"  value="yes"  <#if crewMember.role.game?if_exists == "yes" >checked</#if> />
			</div>
			<div class="fm-opt">
				<label for="crewMember.role.huet">HUET Training:</label>
				<input name="crewMember.role.huet" type="checkbox"  value="yes"  <#if crewMember.role.huet?if_exists == "yes" >checked</#if> />
			</div>
			<br/>
			<div class="fm-opt">
				<label for="crewMember.role.test.number">Test Rated:</label>
				<input name="crewMember.role.test.number" type="checkbox"  value="yes"  <#if crewMember.role.test.number?if_exists == "yes" >checked</#if> />
			</div>
			<div class="fm-opt">
				<label for="crewMember.role.test.type">Test Grade:</label>
				<select name="crewMember.role.test.type" >
					<option <#if crewMember.role.test.type?if_exists == "Grade 1">selected</#if>>Grade 1
					<option <#if crewMember.role.test.type?if_exists == "Grade 2">selected</#if>>Grade 2
				</select>
			</div>
		</fieldset>
		</div>
		<div style="float:left; width: 500px;">
		<fieldset>
			<legend>Medical</legend>
			<div class="fm-opt">
				<label for="crewMember.role.medicalClass">Class:</label>
				<input name="crewMember.role.medicalClass" type="text" value="${crewMember.role.medicalClass!}"/>
			</div>
			<div class="fm-opt">
				<label for="crewMember.role.expiryDate">Expiry Date:</label>
				<input name="crewMember.role.expiryDate" type="text" class="date-pick" value="<#if crewMember.role.expiryDate??>${crewMember.role.expiryDate?string('dd/MM/yyyy')}</#if>"/>
			</div>
			<div class="fm-opt">
				<label for="crewMember.role.medicalAid">Medical Aid:</label>
				<input name="crewMember.role.medicalAid" type="text" value="${crewMember.role.medicalAid!}"/>
			</div>
			<div class="fm-opt">
				<label for="crewMember.role.medicalAidNumber">Aid Number:</label>
				<input name="crewMember.role.medicalAidNumber" type="text" value="${crewMember.role.medicalAidNumber!}"/>
			</div>
		</fieldset>
		<fieldset>
			<legend>Licence</legend>
			<div class="fm-opt">
				<label for="crewMember.role.r1.number"/>Number:</label>
				<input name="crewMember.role.r1.number" type="text" value="${crewMember.role.r1.number!}"/>
			</div>
			<div class="fm-opt">
				<label for="crewMember.role.r1.type">Type:</label>
				<select name="crewMember.role.r1.type" value="${crewMember.role.r1.type!}">
				  <option <#if crewMember.role.r1.type?if_exists == "AME">selected</#if>>      AME
				  <option <#if crewMember.role.r1.type?if_exists == "ATP">selected</#if>>      ATP
				  <option <#if crewMember.role.r1.type?if_exists == "ATPL">selected</#if>>     ATPL
				  <option <#if crewMember.role.r1.type?if_exists == "CPL">selected</#if>>      CPL
				  <option <#if crewMember.role.r1.type?if_exists == "PPL">selected</#if>>      PPL
				  <option <#if crewMember.role.r1.type?if_exists == "PPL, ATP">selected</#if>> PPL, ATP
				  <option <#if crewMember.role.r1.type?if_exists == "SPL">selected</#if>>      SPL
				</select>
			</div>
			<div class="fm-opt">
				<label for="crewMember.role.r1.expiryDate">Expiry Date:</label>
				<input name="crewMember.role.r1.expiryDate" type="text" class="date-pick" value="<#if crewMember.role.r1.expiryDate??>${crewMember.role.r1.expiryDate?string('dd/MM/yyyy')}</#if>"/>
			</div>
			<br/><br/>
		</fieldset>
		<fieldset>
			<legend>
			Hours On Aircraft Types 
			<img class="tooltip" title="To Add a Type: select a type from the dropdown and press save <br/><br/> To Remove a Type: select the empty option of the Type you wish to remove and save <br/><br/> You are unable to add more<br/> than one of the same type"  style="background-color:white;cursor:help;position:absolute;padding:10px;padding-top:0px;"  src="images/icons/info.png"/>
			</legend>
			<div class="fm-opt" style="position:relative;right:-340px;">
			Total Hours
			</div>
			<#assign i=0/> 
			<#list crewMember.role.conversions as conversion>
			<div class="fm-opt">
			    <label for="crewMember.role.conversions[${i}].number">Licence:</label>
			    <select name="crewMember.role.conversions[${i}].number">
			    <option>
				<#list aircraftTypes?if_exists as aircraftType>
				<option <#if conversion.number.equals(aircraftType.name)>selected</#if> >${aircraftType.name!}
				</#list>
				</select>
				<input class="imageCalc" style="width:50px;" name="crewMember.role.conversions[${i}].quantity" type="text" value="${conversion.quantity!}"/>
			</div>
			
			<#assign i=i+1/>
			</#list>
			<#list 0..0 as j>
			<div class="fm-opt">
				<label for="crewMember.role.conversions[${i+j}].number">Add Type:</label>
				<select name="crewMember.role.conversions[${i+j}].number">
				<option>
				<#list aircraftTypes?if_exists as aircraftType>
				<option>${aircraftType.name!}
				</#list>
				</select>
			</div>
			</#list>
			<div class="fm-opt" style="margin-left:104px">
				<span>Total Hours For All Types:</span>
			    <input class="imageCalc" style="width:50px;margin-left:70px;" name="crewMember.role.totalHours" type="text" value="${crewMember.role.totalHours!}"/>
			</div>
			
			<br/>
		</fieldset>
		<fieldset>
			<legend>Last Base Check</legend>
			<div class="fm-opt">
				<label for="crewMember.role.lastDate">Date:</label>
				<input name="crewMember.role.lastDate" type="text" class="date-pick" value="<#if crewMember.role.lastDate??>${crewMember.role.lastDate?string('dd/MM/yyyy')}</#if>"/>
			</div>
			<div class="fm-opt">
				<label for="crewMember.role.lastType">Type:</label>
					<select name="crewMember.role.lastType">
			    	<option>
					<#list aircraftTypes?if_exists as aircraftType>
						<option <#if crewMember.role.lastType?if_exists == (aircraftType.name)>selected</#if> >${aircraftType.name!}
					</#list>
				</select>
			</div>
		</fieldset>	
		</div>
		<hr class="clear"/>
		<#if !readOnly>
		<button type="submit" class="smooth" style="float:right; margin-right:10px; margin-bottom: 4px;"><img src="images/icons/pencil.png"/>Save</button>
		<hr class="clear"/>
		</#if>
	</form>
	<script>$('.imageCalc').calculator({showOn: 'button',     buttonImageOnly: true, buttonImage: 'images/calculator.png'});</script>
</body>
</html>