<#include "/starlite.ftl">
<html>
<head>
  <title>${charter.administrative.firstName!} ${charter.administrative.lastName!}</title>
</head>

<body>
	<@subTabs/>
	<#assign currentUser = Session.userObj>
    <#if currentUser.hasRead("contractAdmin")>
	<form action="charter!save.action" method="POST" class="smart" style="clear:left;">
		<input type="hidden" name="id" value="${charter.id!}"/>
		<input type="hidden" name="charter.id" value="${charter.id!}"/>
		<input type="hidden" name="tab" value="administrative"/>
		<div style="float:left; width: 500px;">
		<fieldset>
			<legend>Client Details</legend>
			<div class="fm-opt">
				<label for="charter.administrative.clientName">Name:</label>
				<input name="charter.administrative.clientName" type="text" value="${charter.administrative.clientName!}"/>
			</div>
			<div class="fm-opt">
				<label for="charter.administrative.code">Code:</label>
				<input name="charter.administrative.code" type="text" value="${charter.administrative.code!}"/>
			</div>
		</fieldset>
		<fieldset>
			<legend>Physical Address</legend>
			<div class="fm-opt">
				<label for="charter.administrative.physicalAddress1">Line 1:</label>
				<input name="charter.administrative.physicalAddress1" type="text" value="${charter.administrative.physicalAddress1!}"/>
			</div>
			<div class="fm-opt">
				<label for="charter.administrative.physicalAddress2">Line 2:</label>
				<input name="charter.administrative.physicalAddress2" type="text" value="${charter.administrative.physicalAddress2!}"/>
			</div>
			<div class="fm-opt">
				<label for="charter.administrative.physicalAddress3">Line 3:</label>
				<input name="charter.administrative.physicalAddress3" type="text" value="${charter.administrative.physicalAddress3!}"/>
			</div>
			<div class="fm-opt">
				<label for="charter.administrative.physicalAddress4">Line 4:</label>
				<input name="charter.administrative.physicalAddress4" type="text" value="${charter.administrative.physicalAddress4!}"/>
			</div>
			<div class="fm-opt">
				<label for="charter.administrative.physicalAddress5">Line 5:</label>
				<input name="charter.administrative.physicalAddress5" type="text" value="${charter.administrative.physicalAddress5!}"/>
			</div>
		</fieldset>
		<fieldset>
			<legend>Invoicing Address</legend>
			<div class="fm-opt">
				<label for="charter.administrative.invoicingAddress1">Line 1:</label>
				<input name="charter.administrative.invoicingAddress1" type="text" value="${charter.administrative.invoicingAddress1!}"/>
			</div>
			<div class="fm-opt">
				<label for="charter.administrative.invoicingAddress2">Line 2:</label>
				<input name="charter.administrative.invoicingAddress2" type="text" value="${charter.administrative.invoicingAddress2!}"/>
			</div>
			<div class="fm-opt">
				<label for="charter.administrative.invoicingAddress3">Line 3:</label>
				<input name="charter.administrative.invoicingAddress3" type="text" value="${charter.administrative.invoicingAddress3!}"/>
			</div>
			<div class="fm-opt">
				<label for="charter.administrative.invoicingAddress4">Line 4:</label>
				<input name="charter.administrative.invoicingAddress4" type="text" value="${charter.administrative.invoicingAddress4!}"/>
			</div>
			<div class="fm-opt">
				<label for="charter.administrative.invoicingAddress5">Line 5:</label>
				<input name="charter.administrative.invoicingAddress5" type="text" value="${charter.administrative.invoicingAddress5!}"/>
			</div>
		</fieldset>
		</div>
		<div style="float:left; width: 500px;">
		<fieldset>
			<legend>Charter Details</legend>
			<div class="fm-opt">
				<label for="charter.administrative.status">Status:</label>
				<#assign status=charter.administrative.status!""?string/>
				<select name="charter.administrative.status">
				
					<option value="PROSPECT"<#if status == "PROSPECT"> selected</#if>>Prospect</option>
					<option value="APPROVED"<#if status == "APPROVED"> selected</#if>>Approved</option>
					<option value="PREPARE"<#if status == "PREPARE"> selected</#if>>Prepare</option>
					<option value="POSITION"<#if status == "POSITION"> selected</#if>>Position</option>
					<option value="OPERATE"<#if status == "OPERATE"> selected</#if>>Operate</option>
					<option value="DECOMMISSION"<#if status == "DECOMMISSION"> selected</#if>>Decommission</option>
				</select>
				
				<!--input name="charter.administrative.status" type="text" value="${charter.administrative.status!}"/-->
			</div>
			<div class="fm-opt">
				<label for="charter.administrative.operatingArea">Operating Area:</label>
				<input name="charter.administrative.operatingArea" type="text" value="${charter.administrative.operatingArea!}"/>
			</div>
			<div class="fm-opt">
				<label for="charter.administrative.operatingBase">Operating Base:</label>
				<input name="charter.administrative.operatingBase" type="text" value="${charter.administrative.operatingBase!}"/>
			</div>
            <div class="fm-opt">
				<label for="charter.administrative.currency">Currency:</label>
                <#assign currency=charter.administrative.currency!/>
                <select name="charter.administrative.currency" type="text" style="width:80px;">
					<option <#if currency == 'USD'>selected</#if> value='USD'>USD ($)</option>
					<option <#if currency == 'EUR'>selected</#if> value='EUR'>EUR (&euro;)</option>
					<option <#if currency == 'GBP'>selected</#if> value='GBP'>GBP (&pound;)</option>
					<!--option <#if currency == 'ZAR'>selected</#if> value='ZAR'>ZAR (R)</option-->
				</select>
			</div>
			<div class="fm-opt">
				<label for="charter.administrative.icao">ICAO:</label>
				<input name="charter.administrative.icao" type="text" value="${charter.administrative.icao!}"/>
			</div>
		</fieldset>
		<fieldset>
			<legend>Period</legend>
			<div class="fm-opt">
				<label for="charter.startDatePlain">Start Date:</label>
				<input name="charter.startDatePlain" type="text" value="<#if charter.startDatePlain??>${charter.startDatePlain?string('dd/MM/yyyy')}</#if>"/>
			</div>
			<div class="fm-opt">
				<label for="charter.endDatePlain">End Date:</label>
				<input name="charter.endDatePlain" type="text" value="<#if charter.endDatePlain??>${charter.endDatePlain?string('dd/MM/yyyy')}</#if>"/>
			</div>
			<!--div class="fm-opt">
				<label for="charter.administrative.d">Duration (days):</label>
				<input name="charter.administrative.operatingBase" type="text" value="${charter.administrative.operatingBase!}"/>
			</div>
			<div class="fm-opt">
				<label for="charter.administrative.icao">Duration (Months):</label>
				<input name="charter.administrative.icao" type="text" value="${charter.administrative.icao!}"/>
			</div-->
		</fieldset>
		</div>
		<hr class="clear"/>
		
		<button type="submit" class="smooth" style="float:right; margin-right:10px; margin-bottom: 4px;"><img src="images/icons/pencil.png"/>Save</button>
		<hr class="clear"/>
	</form>
	</#if>
</body>
</html>