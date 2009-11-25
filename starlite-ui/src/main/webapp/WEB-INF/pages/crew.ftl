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
</script>
</head>
<title>Crew</title>
<body>

<#if user.hasPermission("UserAdmin")>
<div id="toolbar">
	<a href="${request.contextPath}/crew!addCrewMember.action"><img src="${request.contextPath}/images/icons/add.png"/>Add</a>
	<hr class="clear"/>
</div>
<br/>
</#if>
<@jmesa id="crewTable"/>
</body>
</html>