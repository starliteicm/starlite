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

<form name="crewReportForm" action="${request.contextPath}/crew!expiryReport.action">
<@jm.tableFacade
    id="expiry"
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
            <a href="crewMember!assignments.action?id=${bean.code}&fromPage=crew!expiryReport.action">${bean.code}</a>
            </@jm.htmlColumn>
            <@jm.htmlColumn filterable=false property="personal.lastName" title="Last Name"/>
            <@jm.htmlColumn filterable=false property="personal.firstName" title="First Name"/>
            <@jm.htmlColumn style="text-align:center;" filterable=false cellEditor="com.itao.starlite.ui.jmesa.ExpirableDateEditor" property="personal.passportExpiryDate" title="Passport"/>
            <@jm.htmlColumn style="text-align:center;" filterable=false cellEditor="com.itao.starlite.ui.jmesa.ExpirableDateEditor" property="role.expiryDate" title="Medical"/>
            <@jm.htmlColumn style="text-align:center;" filterable=false cellEditor="com.itao.starlite.ui.jmesa.ExpirableDateEditor" property="role.r1.expiryDate" title="r1"/>
            <@jm.htmlColumn style="text-align:center;" filterable=false cellEditor="com.itao.starlite.ui.jmesa.ExpirableDateEditor" property="role.r2.expiryDate" title="r2"/>
            <@jm.htmlColumn style="text-align:center;" filterable=false cellEditor="com.itao.starlite.ui.jmesa.ExpirableDateEditor" property="role.crm.expiryDate" title="crm"/>
            <@jm.htmlColumn style="text-align:center;" filterable=false cellEditor="com.itao.starlite.ui.jmesa.ExpirableDateEditor" property="role.dg.expiryDate" title="dg"/>
            <@jm.htmlColumn style="text-align:center;" filterable=false cellEditor="com.itao.starlite.ui.jmesa.ExpirableDateEditor" property="role.ifr.expiryDate" title="ifr"/>
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
	location.href = '${request.contextPath}/crew!expiryReport.action?' + parameterString;
}
</script>

</body>
</html>
