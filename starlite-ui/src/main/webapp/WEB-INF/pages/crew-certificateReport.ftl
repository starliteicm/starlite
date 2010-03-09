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

<form name="crewReportForm" action="${request.contextPath}/crew!certificateReport.action">
<@jm.tableFacade
    id="tag" 
    items=crew
    maxRows=20
    exportTypes="csv,excel"
    maxRowsIncrements="20,50,100"
    stateAttr="restore"
    var="bean"
    >
    <@jm.htmlTable 
        width="850px"
        >		
        <@jm.htmlRow>
            <@jm.htmlColumn filterable=false property="code" title="id">
            <a href="crewMember!assignments.action?id=${bean.code}&fromPage=crew!certificateReport.action">${bean.code}</a>
            </@jm.htmlColumn>
            <@jm.htmlColumn filterable=false property="personal.lastName" title="Last Name"/>
            <@jm.htmlColumn filterable=false property="personal.firstName" title="First Name"/>
            <@jm.htmlColumn style="text-align:center;" filterable=false property="role.crm.number" title="crm"/>
            <@jm.htmlColumn style="text-align:center;" filterable=false cellEditor="com.itao.starlite.ui.jmesa.ExpirableDateEditor" property="role.crm.expiryDate" title="crm exp"/>
            <@jm.htmlColumn style="text-align:center;" filterable=false property="role.dg.number" title="dg"/>
            <@jm.htmlColumn style="text-align:center;" filterable=false cellEditor="com.itao.starlite.ui.jmesa.ExpirableDateEditor" property="role.dg.expiryDate" title="dg exp"/>
            <@jm.htmlColumn style="text-align:center;" filterable=false property="role.ifr.number" title="ifr"/>
            <@jm.htmlColumn style="text-align:center;" filterable=false cellEditor="com.itao.starlite.ui.jmesa.ExpirableDateEditor" property="role.ifr.expiryDate" title="ifr exp"/>
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
	location.href = '${request.contextPath}/crew!certificateReport.action?' + parameterString;
}
</script>

</body>
</html>
