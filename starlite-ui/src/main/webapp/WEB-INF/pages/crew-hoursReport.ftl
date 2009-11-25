<#include "/starlite.ftl">
<#assign jm=JspTaglibs["/WEB-INF/jmesa.tld"] />
<html>
<head>
<link rel="stylesheet" type="text/css" href="${request.contextPath}/styles/jmesa.css">
<script type="text/javascript" src="${request.contextPath}/js/jquery-1.2.1.pack.js"></script>
<script type="text/javascript" src="${request.contextPath}/js/jquery.bgiframe.pack.js"></script>
<script type="text/javascript" src="${request.contextPath}/js/jmesa.js"></script>
<script type="text/javascript">
        $(document).ready(function() {
           addDropShadow('images/table/');
        });
</script>
</head>
<title>Crew Hours</title>
<body>

<form name="crewReportForm" action="${request.contextPath}/crew!hoursReport.action">
<@jm.tableFacade
    id="tag" 
    items=report
    maxRows=20
    exportTypes="csv,excel"
    maxRowsIncrements="20,50,100"
    stateAttr="restore"
    var="bean"
    >
    <@jm.htmlTable 
        width="600px"
        >		
        <@jm.htmlRow>	
            <@jm.htmlColumn filterable=false property="lastName" title="Last Name"/>
            <@jm.htmlColumn filterable=false property="firstName" title="First Name"/>
            <@jm.htmlColumn style="text-align:right;" filterable=false property="total" title="Total"/>
            <@jm.htmlColumn style="text-align:right;" filterable=false property="paid" title="Paid"/>
        </@jm.htmlRow>
    </@jm.htmlTable> 
</@jm.tableFacade>
</form>

<script type="text/javascript">
function onInvokeAction(id) {
	setExportToLimit(id, '');
	createHiddenInputFieldsForLimitAndSubmit(id);
}
function onInvokeExportAction(id) {
	var parameterString = createParameterStringForLimit(id);
	location.href = '${request.contextPath}/crew!hoursReport.action?' + parameterString;
}
</script>

</body>
</html>
