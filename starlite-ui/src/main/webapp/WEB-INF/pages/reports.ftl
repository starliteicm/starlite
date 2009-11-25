<ul>
	<li><a href="script.action?scriptName=aircraftCharterMatrix.groovy&month=${month}&year=${year?c}">Aircraft/Charter Matrix</a>
	<li><a href="script.action?scriptName=certificate.groovy">Certificates</a>
	<li><a href="script.action?scriptName=crewLicences.groovy">Crew Licences</a>
	<#if notAuthorised>
	<#else>
	<li><a href="script.action?scriptName=crewPayments.groovy&month=${month}&year=${year?c}">Crew Payments</a>
	<li><a href="script.action?scriptName=crewPaymentAnalysis.groovy&month=${month}&year=${year?c}">Crew Payment Analysis</a>
	</#if>
	<li><a href="script.action?scriptName=expiry.groovy">Expiry</a>
</ul>