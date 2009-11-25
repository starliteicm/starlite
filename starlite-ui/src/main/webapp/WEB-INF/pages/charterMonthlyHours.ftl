<#include "/starlite.ftl">
<html>
<head>
	<link rel="stylesheet" type="text/css" href="styles/jmesa.css">
	<script type="text/javascript" src="js/jquery-1.2.3.min.js"></script>
	<script type="text/javascript" src="js/jmesa.js"></script>
	<script type="text/javascript">
	        $(document).ready(function() {
	           addDropShadow('images/table/');
	        });
	</script>
	<title>Monthly Hours - ${monthName}, ${year?string("####")}</title>
</head>

<body>
	<#assign id="monthlyHours"+monthName+year?string('####')/>
	<div id="toolbar">
		<a href="${previousLink}"><img src="images/icons/resultset_previous.png"/>Previous</a>
		<a href="${nextLink}"><img src="images/icons/resultset_next.png"/>Next</a>
		<hr class="clear"/>
	</div>
	<!--a href="${previousLink}">Previous</a> <a href="${nextLink}">Next</a-->
	<h1>${monthName}, ${year?string("####")}</h1>
	<h2>${charter.code}</h2>
	<br/>
	<form name="${id}Form" action="${request.requestURL}${params!}">
		<div id="${id}">
			${tableHtml}
		</div>
		<input type="hidden" name="charterId" value="${charterId}"/>
		<input type="hidden" name="month" value="${month}"/>
		<input type="hidden" name="year" value="${year?string('####')}"/>
	</form>

	<form name="${id}Form" action="${request.contextPath}/charterMonthlyHours!saveHoursInvoiced.action">
		<input type="hidden" name="year" value="${year}"/>
		<input type="hidden" name="month" value="${month}"/>
		<input type="hidden" name="charterId" value="${charterId}"/>
		<label>Hours Invoiced</label><input type="text" name="hoursInvoiced" value="${hoursInvoiced}"/>
		<#if user.hasPermission("InvoiceHours")>
		<input type="submit" value="save"/>
		</#if>
	</form>

	<script type="text/javascript">
	function onInvokeAction(id) {
		setExportToLimit(id, '');
	    createHiddenInputFieldsForLimitAndSubmit(id);
	}
	</script>
</body>
</html>
