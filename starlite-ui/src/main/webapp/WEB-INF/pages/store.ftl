<#include "/starlite.ftl">
<#setting number_format = "######.##########"/>

<html>
<head>
<link rel="stylesheet" type="text/css" href="${request.contextPath}/styles/jmesa.css">
<script type="text/javascript" src="${request.contextPath}/js/jmesa.js"></script>
<script type="text/javascript">
        $(document).ready(function() {
           addDropShadow('images/table/');
        });
        
        function onSubmitWsColumn() {
            document.getElementById("saveButton").disabled = false;
        }
        function onInvokeExportAction(id) {
            <#if tab="active">
            var parameterString = createParameterStringForLimit(id);
            location.href = '${request.contextPath}/store.action?' + parameterString;
            <#else>
            var parameterString = createParameterStringForLimit(id);
            location.href = '${request.contextPath}/store!deactive.action?' + parameterString;
            </#if>
        }
</script>
<title>Stores</title>
</head>
<body>


<#if tab != "storeAdd">
<@jmesa id="store"/>
</#if>


</body>
</html>