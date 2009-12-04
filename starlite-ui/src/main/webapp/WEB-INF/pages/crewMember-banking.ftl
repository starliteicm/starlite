<#include "/starlite.ftl">
<html>
<head>
  <title>${crewMember.personal.firstName!} ${crewMember.personal.lastName!}</title>
  <link rel="stylesheet" type="text/css" href="styles/forms.css">
  <@enableJQuery/>
  
<style type="text/css">

.star{
   color:red;
     }

#msg-error{
   margin: 0 auto;   
   width:90%;
   padding:5px;
   color:red;
   }

</style>

<script language="javascript">

   function validate(){            
      var bankname   = $("#bankname").val();
      var branchcode = $("#branchcode").val();
      var address1   = $("#address1").val();
      var address2   = $("#address2").val();
      var accountname= $("#accountname").val();
      var accountnumber= $("#accountnumber").val();
      var swift      = $("#swift").val();
      var error      = 0;
      var errormsg = "<b> The following mandatory fields are blank: </b><br>";
      if(bankname == "")   { errormsg += "bank name, "; error=1;}
      if(branchcode == "") { errormsg += "branch code, "; error=1;}      
      if(address1 == "")   { errormsg += "address 1, "; error=1;}
      if(address2 == "")   { errormsg += "address 2, "; error=1;}
      if(accountname == ""){ errormsg += "account name, "; error=1;}
      if(accountnumber == ""){ errormsg += "account number, "; error=1;}
      if(swift == "")      { errormsg += "SWIFT code, "; error=1;}
      
      if(error==1){$("#msg-error").html(errormsg); return false;
      }else{
      $("#msg-error").html(""); document.forms.bankingform.submit(); 
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
	<#if readOnly>
	<form action="#" method="POST" class="smart readonly">
	<#else>
	<form action="crewMember!save.action" name="bankingform" method="POST" class="smart">
	</#if>
		<input type="hidden" name="id" value="${id!}"/>
		<input type="hidden" name="crewMember.id" value="${crewMember.code!}"/>
		<input type="hidden" name="tab" value="banking"/>
		<div style="float:left; width: 500px;">
		<fieldset>
			<legend>Bank Details</legend>
			<div class="fm-opt">
				<label for="crewMember.banking.bankName"><span class="star">*</span>Bank Name:</label>
				<input name="crewMember.banking.bankName" id="bankname" type="text" value="${crewMember.banking.bankName!}" />
			</div>
			<div class="fm-opt">
				<label for="crewMember.banking.branchCode"><span class="star">*</span>Branch Code:</label>
				<input name="crewMember.banking.branchCode" id="branchcode" type="text" value="${crewMember.banking.branchCode!}" />
			</div>
			<div class="fm-opt">
				<label for="crewMember.banking.address1"><span class="star">*</span>Address 1:</label>
				<input name="crewMember.banking.address1" id="address1" type="text" value="${crewMember.banking.address1!}" />
			</div>
			<div class="fm-opt">
				<label for="crewMember.banking.address2"><span class="star">*</span>Address 2:</label>
				<input name="crewMember.banking.address2" id="address2" type="text" value="${crewMember.banking.address2!}" />
			</div>
			<div class="fm-opt">
				<label for="crewMember.banking.address3">Address 3:</label>
				<input name="crewMember.banking.address3" type="text" value="${crewMember.banking.address3!}"/>
			</div>
			<div class="fm-opt">
				<label for="crewMember.banking.address4">Address 4:</label>
				<input name="crewMember.banking.address4" type="text" value="${crewMember.banking.address4!}"/>
			</div>
			<div class="fm-opt">
				<label for="crewMember.banking.address5">Address 5:</label>
				<input name="crewMember.banking.address5" type="text" value="${crewMember.banking.address5!}"/>
			</div>
		</fieldset>
		</div>
		<div style="float:left; width: 500px;">
		<fieldset>
			<legend>Account Details</legend>
			<div class="fm-opt">
				<label for="crewMember.banking.accountName"><span class="star">*</span>Account Name:</label>
				<input name="crewMember.banking.accountName" id="accountname" type="text" value="${crewMember.banking.accountName!}" />
			</div>
			<div class="fm-opt">
				<label for="crewMember.banking.accountNumber"><span class="star">*</span>Account Number:</label>
				<input name="crewMember.banking.accountNumber" id="accountnumber" type="text" value="${crewMember.banking.accountNumber!}" />
			</div>
			<div class="fm-opt">
				<label for="crewMember.banking.swift"><span class="star">*</span>SWIFT:</label>
				<input name="crewMember.banking.swift" id="swift" type="text" value="${crewMember.banking.swift!}" />
			</div>
			<div class="fm-opt">
				<label for="crewMember.banking.iban">IBAN:</label>
				<input name="crewMember.banking.iban" type="text" value="${crewMember.banking.iban!}" />
			</div>
			<div class="fm-opt">
				<label for="crewMember.banking.bic">BIC:</label>
				<input name="crewMember.banking.bic" type="text" value="${crewMember.banking.bic!}"/>
			</div>
		</fieldset>
		</div>
		<hr class="clear"/>
		<#if !readOnly>
		<button type="button" onclick="validate();" class="smooth" style="float:right; margin-right:10px; margin-bottom: 4px;"><img src="images/icons/pencil.png"/>Save</button>
		<div id="msg-error"></div>
  <hr class="clear"/>
		</#if>
	</form>
</body>
</html>