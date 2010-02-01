<#include "/starlite.ftl">
<html>
<head>
  <title>${aircraft.ref!}</title>
  <@enableJQuery/>
  <@enableDatePickers/>
</head>

<body>
	<@subTabs/>
	<#assign currentUser = Session.userObj>
	<#if currentUser.hasRead("aircraftInfo")>
	<#if currentUser.hasWrite("aircraftInfo")>
	  <form action="aircraftInfo!save.action" method="POST" class="smart" style="clear:left;">
	<#else>
	  <form action="#" onsubmit="return false;" method="POST" class="smart" style="clear:left;">
	<#end>
		<input type="hidden" name="id" value="${id!}"/>
		<input type="hidden" name="aircraft.id" value="${aircraft.id!}"/>
		<input type="hidden" name="tab" value="administrative"/>
		<div style="float:left; width: 500px;">
		<fieldset>
			<legend>Aircraft Info</legend>
			<div class="fm-opt">
				<label for="aircraft.ref">Reg:</label>
				<input name="aircraft.ref" type="text" value="${aircraft.ref!}"/>
			</div>
			<div class="fm-opt">
				<label for="aircraft.ownership">Ownership:</label>
				<input name="aircraft.ownership" type="text" value="${aircraft.ownership!}"/>
			</div>
			<div class="fm-opt">
				<label for="aircraft.type">Type:</label>
				<select name="aircraft.type">
				<#list aircraftTypes?if_exists as aircraftType>
				<option <#if aircraftType.name.equals(aircraft.type)>selected</#if> >${aircraftType.name!}
				</#list>
				</select>
			</div>
			<div class="fm-opt">
				<label for="aircraft.model">Model:</label>
				<input name="aircraft.model" type="text" value="${aircraft.model!}"/>
			</div>
			<div class="fm-opt">
                <label for="aircraft.engines">Engine:</label>
                <select name="aircraft.engines">
                <#if aircraft.engines?exists>
                <option <#if aircraft.engines.equals(0)>selected</#if> value="0" >Single
                <option <#if aircraft.engines gt 0>selected</#if> value="1" >Multi
                <#else>
                <option  value="0" >Single
                <option  value="1" >Multi
                </#if>
                </select>
            </div>
			<div class="fm-opt">
				<label for="aircraft.licence">Licence:</label>
				<input name="aircraft.licence" type="text" value="${aircraft.licence!}"/>
			</div>
			<div class="fm-opt">
				<label for="aircraft.aircraftMaintenanceOrganisation">AMO:</label>
				<input name="aircraft.aircraftMaintenanceOrganisation" type="text" value="${aircraft.aircraftMaintenanceOrganisation!}"/>
			</div>
			<div class="fm-opt">
				<label for="aircraft.livery">Livery:</label>
				<input name="aircraft.livery" type="text" value="${aircraft.livery!}"/>
			</div>
		</fieldset>
		</div>
		<div style="float:left; width: 500px;">
		<fieldset>
			<legend>Certificates</legend>
			<div class="fm-opt">
				<label for="aircraft.certificateOfRegistration.number">CoR:</label>
				<input name="aircraft.certificateOfRegistration.number" type="text" value="${aircraft.certificateOfRegistration.number!}"/>
			</div>
			<div class="fm-opt">
				<label for="aircraft.certificateOfRegistration.expiryDate">Expiry Date:</label>
				<input name="aircraft.certificateOfRegistration.expiryDate" class="date-pick" type="text" value="<#if aircraft.certificateOfRegistration.expiryDate??>${aircraft.certificateOfRegistration.expiryDate?string('dd/MM/yyyy')}</#if>"/>
			</div>
			
			<div class="fm-opt">
				<label for="aircraft.aircraftOperatingCertificate.number">AOC:</label>
				<input name="aircraft.aircraftOperatingCertificate.number" type="text" value="${aircraft.aircraftOperatingCertificate.number!}"/>
			</div>
			<div class="fm-opt">
				<label for="aircraft.aircraftOperatingCertificate.expiryDate">Expiry Date:</label>
				<input name="aircraft.aircraftOperatingCertificate.expiryDate" class="date-pick" type="text" value="<#if aircraft.aircraftOperatingCertificate.expiryDate??>${aircraft.aircraftOperatingCertificate.expiryDate?string('dd/MM/yyyy')}</#if>"/>
			</div>
			
			<div class="fm-opt">
				<label for="aircraft.certificateOfAirworthiness.number">CoA:</label>
				<input name="aircraft.certificateOfAirworthiness.number" type="text" value="${aircraft.certificateOfAirworthiness.number!}"/>
			</div>
			<div class="fm-opt">
				<label for="aircraft.certificateOfAirworthiness.expiryDate">Expiry Date:</label>
				<input name="aircraft.certificateOfAirworthiness.expiryDate" class="date-pick" type="text" value="<#if aircraft.certificateOfAirworthiness.expiryDate??>${aircraft.certificateOfAirworthiness.expiryDate?string('dd/MM/yyyy')}</#if>"/>
			</div>
		</fieldset>
		</div>
		<hr class="clear"/>
		<#if currentUser.hasWrite("aircraftInfo")>
		<button type="submit" class="smooth" style="float:right; margin-right:10px; margin-bottom: 4px;"><img src="images/icons/pencil.png"/>Save</button>
		</#if>
		<hr class="clear"/>
	</form>
	</#if>
</body>
</html>