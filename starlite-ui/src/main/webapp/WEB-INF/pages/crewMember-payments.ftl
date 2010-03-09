<#include "/starlite.ftl">
<html>
<head>
  <title>${crewMember.personal.firstName!} ${crewMember.personal.lastName!}</title>
  <link rel="stylesheet" type="text/css" href="styles/forms.css">
  

<script language="javascript">

function validate(){            
      
      var currency = document.getElementById('currency').options[document.getElementById('currency').selectedIndex].value;
      var monthly  = $("#monthly").val();
      var daily    = $("#daily").val();
      var training = $("#training").val();
      var travel   = $("#travel").val();
      var errormsg = "<b> The following mandatory fields are blank: </b><br>";
      var error    = 0;
      
      if(currency== ""){ errormsg += "Currency allowance, "; error=1;}
      if(monthly == ""){ errormsg += "Monthly rate, "; error=1;}
      if(daily   == ""){ errormsg += "Daily allowance, "; error=1;}      
      if(training== ""){ errormsg += "Training allowance, "; error=1;}
      if(travel  == ""){ errormsg += "Travel allowance, "; error=1;}
      
      if(error==1){$("#msg-error").html(errormsg); return false;
      }else{
      $("#msg-error").html(""); document.forms.paymentform.submit(); 
      }                                     
}
</script>

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

	<#if notAuthorised>
	<form action="#" method="POST" style="padding:20px;" class="smart readonly">
	<fieldset>
		<legend>Not Authorised</legend>
		<br/>
	</fieldset>
	</form>
	<#else>

	<#if readOnly>
	<form action="#" method="POST" class="smart readonly" name ="paymentform">
	<#else>
	<form action="crewMember!save.action" method="POST" class="smart" name ="paymentform">
	</#if>
		<input type="hidden" name="id" value="${id!}"/>
		<input type="hidden" name="crewMember.id" value="${crewMember.code!}"/>
		<input type="hidden" name="tab" value="payments"/>
		<div style="float:left; width: 500px;">
		<fieldset>
			<legend>Base Rate</legend>
			<div class="fm-opt">
				<label for="crewMember.payments.currency"><span style="color:red;">*</span>&nbsp;Currency:</label>
				<!--input name="crewMember.banking.currency" type="text" value="${crewMember.banking.bic!}"/-->
				<#if readOnly>
					<label>${crewMember.payments.currency!'&nbsp;'}</label>
				<#else>
				<#assign currency=crewMember.payments.currency!/>
				<select name="crewMember.payments.currency" type="text" style="width:80px;" id="currency" >
					<option value="EUR" <#if currency == 'EUR'>selected</#if> value='EUR'>EUR (&euro;)</option>
					<option value="GBP" <#if currency == 'GBP'>selected</#if> value='GBP'>GBP (&pound;)</option>
					<option value="USD" <#if currency == 'USD'>selected</#if> value='USD'>USD ($)</option>
					<!--option <#if currency == 'ZAR'>selected</#if> value='ZAR'>ZAR (R)</option-->
				</select>
				</#if>
			</div>
			<div class="fm-opt">
				<label for="crewMember.payments.monthlyBaseRate"><span style="color:red;">*</span>&nbsp;Monthly:</label>
				<#if readOnly>
					<label>${crewMember.payments.monthlyBaseRate.amountAsDouble!'&nbsp;'}</label>
				<#else>
					<input name="crewMember.payments.monthlyBaseRate.amountAsDouble"  id = "monthly" type="text" style="width:76px;" value="${crewMember.payments.monthlyBaseRate.amountAsDouble!}"/>
				</#if>
			</div>
		</fieldset>
		</div>
		<div style="float:left; width: 500px;">
		<fieldset>
			<legend>Allowances</legend>
			<div class="fm-opt">
				<label for="crewMember.payments.areaAllowance"><span style="color:red;">*</span>&nbsp;Daily:</label>
				<#if readOnly>
					<label>${crewMember.payments.areaAllowance.amountAsDouble!'&nbsp;'}</label>
				<#else>
					<input name="crewMember.payments.areaAllowance.amountAsDouble" id = "daily" type="text" style="width:76px;" value="${crewMember.payments.areaAllowance.amountAsDouble!}"/>
				</#if>
			</div>

			<div class="fm-opt">
				<label for="crewMember.payments.instructorAllowance">Instructor:</label>
				<#if readOnly>
					<label>${crewMember.payments.areaAllowance.amountAsDouble!'&nbsp;'}</label>
				<#else>
					<input name="crewMember.payments.instructorAllowance.amountAsDouble" id = "instructor" type="text" style="width:76px;" value="${crewMember.payments.instructorAllowance.amountAsDouble!}"/>
				</#if>
			</div>

			<div class="fm-opt">
				<label for="crewMember.payments.dailyAllowance"><span style="color:red;">*</span>&nbsp;Training:</label>
				<#if readOnly>
					<label>${crewMember.payments.areaAllowance.amountAsDouble!'&nbsp;'}</label>
				<#else>
					<input name="crewMember.payments.dailyAllowance.amountAsDouble" id ="training" type="text" style="width:76px;" value="${crewMember.payments.dailyAllowance.amountAsDouble!}"/>
				</#if>
			</div>

			<div class="fm-opt">
				<label for="crewMember.payments.flightAllowance"><span style="color:red;">*</span>&nbsp;Travel:</label>
				<#if readOnly>
					<label>${crewMember.payments.areaAllowance.amountAsDouble!'&nbsp;'}</label>
				<#else>
					<input name="crewMember.payments.flightAllowance.amountAsDouble" id ="travel" type="text" style="width:76px;" value="${crewMember.payments.flightAllowance.amountAsDouble!}"/>
				</#if>
			</div>
		</fieldset>
		</div>
		<hr class="clear"/>
		<#if !readOnly>
		<button type="button" onclick="validate();" class="smooth" style="float:right; margin-right:10px; margin-bottom: 4px;"><img src="images/icons/pencil.png"/>Save</button>
		
  <div id="msg-error" style="color:red"></div>

		
		</#if>
		</form>
	</#if>

</body>
</html>