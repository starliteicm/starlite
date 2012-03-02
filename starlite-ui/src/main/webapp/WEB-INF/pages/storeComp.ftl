<#include "/starlite.ftl">
<#setting number_format = "######.##########"/>

<html>
<head>

<link rel="stylesheet" type="text/css" href="${request.contextPath}/styles/jmesa.css">
<script type="text/javascript" src="${request.contextPath}/js/jmesa.js"></script>
<script type="text/javascript">
    function validate(){
    return true;
    }
    
    function onInvokeExportAction(id) {
            var parameterString = createParameterStringForLimit(id);
            location.href = '${request.contextPath}/store!edit.action?id=${id!}&' + parameterString;
    }
</script>

</head>
<body>

<#if user.hasPermission("UserAdmin")>


 <#if (id?exists) && (tab=="storeViewComponents")>   
     <@jmesa2 id="components" mytableHtml="${componentTable}" />
</#if>
</#if>
</body>
</html>