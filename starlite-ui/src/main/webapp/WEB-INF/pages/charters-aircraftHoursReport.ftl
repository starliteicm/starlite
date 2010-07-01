<#include "/starlite.ftl">
<#assign jm=JspTaglibs["/WEB-INF/jmesa.tld"] />
<html>
<head>
<link rel="stylesheet" type="text/css" href="${request.contextPath}/styles/jmesa.css">
<script type="text/javascript" src="${request.contextPath}/js/jquery.bgiframe.pack.js"></script>
<script type="text/javascript" src="${request.contextPath}/js/jmesa.js"></script>
<script type="text/javascript">
        $(document).ready(function() {
           addDropShadow('images/table/');
        });
</script>
</head>
<title>Crew Document Expiry Dates</title>
<body>

<form name="crewReportForm" action="${request.contextPath}/charters!aircraftHoursReport.action">
${html}
</form>

<script type="text/javascript">
function onInvokeAction(id) {
	setExportToLimit(id, '');
	createHiddenInputFieldsForLimitAndSubmit(id);
}
function onInvokeExportAction(id) {
	var parameterString = createParameterStringForLimit(id);
	location.href = '${request.contextPath}/charters!aircraftHoursReport.action?' + parameterString;
}
</script>

</body>
</html>
