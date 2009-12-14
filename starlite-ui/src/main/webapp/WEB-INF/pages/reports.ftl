<ul>
	<li><a href="script.action?scriptName=aircraftCharterMatrix.groovy&month=${month}&year=${year?c}">Aircraft/Charter Matrix</a>
	<li><a href="script.action?scriptName=certificate.groovy">Certificates</a>
	<li><a href="script.action?scriptName=crewMemberProfiles.groovy">Crew Member Profiles</a>
	<li><a href="script.action?scriptName=crewLicences.groovy">Crew Licences</a>
	<#if notAuthorised>
	<#else>
	<li><a href="script.action?scriptName=crewPayments.groovy&month=${month}&year=${year?c}">Crew Payments</a>
	<li><a href="script.action?scriptName=crewPaymentAnalysis.groovy&month=${month}&year=${year?c}">Crew Payment Analysis</a>
	</#if>
	<li><a href="script.action?scriptName=expiry.groovy">Crew Document Analysis</a>
</ul>

<form action = "crewMember!profile.action">
<select name="id">
<#list crewMembers as crew>
<option value="${crew.code?if_exists}" > ${crew.personal.fullName?if_exists}
</#list>
</select>
<input type="submit" value="View Profile" />
</form>