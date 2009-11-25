<#include "/starlite.ftl">
<html>
<head>
<link rel="stylesheet" type="text/css" href="${request.contextPath}/styles/jmesa.css">
<script type="text/javascript" src="${request.contextPath}/js/jquery-1.2.3.min.js"></script>
<script type="text/javascript" src="${request.contextPath}/js/jmesa.js"></script>
<script type="text/javascript">
        $(document).ready(function() {
           addDropShadow('images/table/');
        });
        
        function onSubmitWsColumn() {
			document.getElementById("saveButton").disabled = false;
		}
</script>
<title>Aircraft</title>
</head>
<body>



<#if user.hasPermission("UserAdmin")>
<div id="toolbar">
	<a href="${request.contextPath}/aircraft!addAircraft.action"><img src="${request.contextPath}/images/icons/add.png"/>Add</a>
	<hr class="clear"/>
</div>
<br/>
</#if>
 <!--div id="toolbar">
	<button id="addButton" disabled="disabled"><img src="${request.contextPath}/images/icons/add.png"/>Add</button>
	<button id="saveButton" onclick="javascript:setSaveToWorksheet('aircraftTable');onInvokeAction('aircraftTable','save_worksheet')" ${saveDisabled!}><img src="${request.contextPath}/images/icons/pencil.png"/>Save</button>
	<hr class="clear">
</div-->

<@jmesa id="aircraft"/>



</body>
</html>
