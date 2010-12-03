<#include "/starlite.ftl">
<html>
<head>
<link rel="stylesheet" type="text/css" href="styles/jmesa.css">
<script type="text/javascript" src="js/jmesa.js"></script>
<script type="text/javascript">
        $(document).ready(function() {
           addDropShadow('images/table/');
        });
        function onInvokeExportAction(id) {
            var parameterString = createParameterStringForLimit(id);
            location.href = '${request.contextPath}/aircraftInfo!components.action?id=${id}&' + parameterString;
        }
</script>
</head>
<body>
	<@jmesa2 id="components" mytableHtml="${tableHtml}" />
</body>
</html>