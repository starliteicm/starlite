<#include "/starlite.ftl">
<#assign jm=JspTaglibs["/WEB-INF/jmesa.tld"] />
<html>
<head>
<link rel="stylesheet" type="text/css" href="${request.contextPath}/styles/jmesa.css">

<@enableJQuery/>
<script type="text/javascript" src="${request.contextPath}/js/jmesa.js"></script>
<script type="text/javascript">
        $(document).ready(function() {
           addDropShadow('images/table/');
        });
</script>
</head>
<title>${pageContext.title!}</title>
<body>

<#assign paramstring="?scriptName="+scriptName>
<#if request.getParameter("view")??>
<#assign paramstring = paramString+"&view="+request.getParameter("view")>
</#if>

<#if pageContext["extraParams"]["month"]??>
	<#assign currentDateParams="&month="+pageContext["extraParams"]["month"]+"&year="+pageContext["extraParams"]["year"]/>
</#if>

<div id="toolbar">
	<a href="${request.requestURL}?scriptName=${scriptName}&view=CSV${currentDateParams!}" style="float: left;"><img src="images/icons/page_white_text.png"/>Export Csv</a>
	<a href="${request.requestURL}?scriptName=${scriptName}&view=EXCEL${currentDateParams!}" style="float: left;"><img src="images/icons/page_white_excel.png"/>Export Excel</a>
	<a href="${request.requestURL}?scriptName=${scriptName}&view=PDF${currentDateParams!}" style="float: left;"><img src="images/icons/page_white_acrobat.png"/>Export Pdf</a>
	<div class="separator"> </div>
	<#if pageContext["extraParams"]["previousMonth"]?? && pageContext["extraParams"]["previousYear"]??>
	<a href="${request.requestURL}${paramstring}&month=${pageContext["extraParams"]["previousMonth"]}&year=${pageContext["extraParams"]["previousYear"]?c}"><img src="images/icons/resultset_previous.png"/>Previous</a>
	</#if>
	<#if pageContext["extraParams"]["nextMonth"]?? && pageContext["extraParams"]["nextYear"]??>
	<a href="${request.requestURL}${paramstring}&month=${pageContext["extraParams"]["nextMonth"]}&year=${pageContext["extraParams"]["nextYear"]?c}"><img src="images/icons/resultset_next.png"/>Next</a>
	</#if>	
	<hr class="clear"/>
</div>

<form name="crewReportForm" action="${request.requestURL}">
<input type="hidden" name="scriptName" value="${scriptName}"/>
<#if pageContext["extraParams"]["month"]??>
<input type="hidden" name="month" value="${pageContext["extraParams"]["month"]}"/>
</#if>
<#if pageContext["extraParams"]["year"]??>
<input type="hidden" name="year" value="${pageContext["extraParams"]["year"]}"/>
</#if>
${html!}
</form>

<script type="text/javascript">
function onInvokeAction(id) {
	setExportToLimit(id, '');
	createHiddenInputFieldsForLimitAndSubmit(id);
}
function onInvokeExportAction(id) {
	var parameterString = createParameterStringForLimit(id);
	location.href = '${request.contextPath}/script.action?scriptName=${scriptName}&' + parameterString;
}
</script>

</body>
</html>
