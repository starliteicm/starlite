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
<title>Contract</title>
</head>
<body>
<#if user.hasPermission("UserAdmin")>
<div id="toolbar">
	<a href="${request.contextPath}/charters!addCharter.action"><img src="${request.contextPath}/images/icons/add.png"/>Add</a>
	<hr class="clear"/>
</div>
<br/>
</#if>
<@jmesa id="charters"/>
</body>
</html>
