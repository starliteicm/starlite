<ul>
	<li><a href="script.action?scriptName=aircraftCharterMatrix.groovy&month=${month}&year=${year?c}">Aircraft/Charter Matrix</a>
	<li><a href="script.action?scriptName=certificate.groovy">Certificates</a>
	<li><a href="script.action?scriptName=crewMemberProfiles.groovy">Crew Member Profiles</a>
	<li><a href="script.action?scriptName=crewLicences.groovy">Crew Licences</a>
	<#if notAuthorised>
	<#else>
	<li><a href="script.action?scriptName=crewPayments.groovy&month=${month}&year=${year?c}">Crew Payments</a>
	<li><a href="script.action?scriptName=crewPaymentAnalysis.groovy&month=${month}&year=${year?c}">Crew Payment Analysis</a>
	<li><a href="script.action?scriptName=crewDocumentAnalysis.groovy">Crew Document Analysis</a>	
	</#if>
</ul>

<form action = "script.action">
    <input type="hidden" name="scriptName" value="crewDocumentAnalysis.groovy" />
	<select name="documenttype">
		<option value="">No Filter</option>
		<option value="passport">Passport</option>
		<option value="licence">Licence</option>
		<option value="medical">Medical</option>
		<option value="crm">CRM</option>
		<option value="dg">DG</option>
		<option value="huet">Huet</option>
	</select>
<input type="submit" value="View Crew Document Report" />
</form>

<form action = "crewMember!profile.action">
<select name="id">
<#list crewMembers as crew>
<option value="${crew.code?if_exists}" > ${crew.personal.fullName?if_exists}
</#list>
</select>
<input type="submit" value="View Profile" />
</form>