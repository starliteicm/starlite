<ul>
	<li style="width:200px;height:30px;"><a href="script.action?scriptName=aircraftCharterMatrix.groovy&month=${month}&year=${year?c}">Aircraft/Charter Matrix</a>
	<li style="width:200px;height:30px;"><a href="script.action?scriptName=certificate.groovy">Certificates</a>
	<li style="width:200px;height:30px;"><a href="script.action?scriptName=crewMemberProfiles.groovy">Crew Member Profiles</a>
	<li style="width:200px;height:30px;"><a href="script.action?scriptName=crewLicences.groovy">Crew Licences</a>
	<#if notAuthorised>
	<#else>
	<li style="width:200px;height:30px;"><a href="script.action?scriptName=crewDeductions.groovy&month=${month}&year=${year?c}">Crew Deductions</a>
	<li style="width:200px;height:30px;"><a href="script.action?scriptName=crewPayments.groovy&month=${month}&year=${year?c}">Crew Payments</a>
	<li style="width:200px;height:30px;"><a href="script.action?scriptName=crewPaymentAnalysis.groovy&month=${month}&year=${year?c}">Crew Payment Analysis</a>
	</#if>
</ul>

<form name="crewDoc" id="crewDoc" action="script.action">
    <input type="hidden" name="scriptName" value="crewDocumentAnalysis.groovy" />
<li style="width:200px;height:30px;float:left;" ><A href="#" onclick="document.forms.crewDoc.submit();" >View Crew Document Report</A></li>
    <select style="float:left" name="documenttype">
        <option value="">-</option>
        <option value="passport">Passport</option>
        <option value="licence">Licence</option>
        <option value="medical">Medical</option>
        <option value="crm">CRM</option>
        <option value="dg">DG</option>
        <option value="huet">Huet</option>
    </select>
</form>
<br/><br/>
<form name="profile" id="profile" action="crewMember!profile.action">
<li style="width:200px;height:30px;float:left;"><A href="#" style="width:300px;height:20px;" onclick="document.forms.profile.submit();" >View Profile</A></li>
<select style="float:left" name="id">
<#list crewMembers as crew>
<option value="${crew.code?if_exists}" > ${crew.personal.fullName?if_exists}
</#list>
</select>
</form>