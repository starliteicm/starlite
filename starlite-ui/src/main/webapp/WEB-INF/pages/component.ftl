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
            location.href = '${request.contextPath}/component.action?' + parameterString;
            <#else>
            var parameterString = createParameterStringForLimit(id);
            location.href = '${request.contextPath}/component!deactive.action?' + parameterString;
            </#if>
        }
</script>
</script>
<title>Components</title>
</head>
<body>

<#if user.hasPermission("UserAdmin")>
<div id="toolbar">
    <a href="${request.contextPath}/component!edit.action"><img src="${request.contextPath}/images/icons/add.png"/>Add</a>
    
    <#if user.hasRead("compup")>
    
    <div style="margin-left:150px;">
    <form action="component!upload.action" enctype="multipart/form-data" method="post">
    <input type="file" style="float:left" name="document" id="document"/>
    <select style="float:left;margin-left:10px;height:24px;" name="location" >
    <#list stores as store>
    <option value="${store.location}" >${store.location}</option>
    </#list>
    </select>
    <button class="smooth" style="margin-left:10px;padding:2px 10px 2px 7px;" onclick="return confirm('Are you sure you wish to upload these components to this Store?')" type="submit"><img src="images/icons/arrow_up.png"/>Upload Components</button>
    </form>
    </div>
    </#if>
    
    <hr class="clear"/>
</div>
<br/>
</#if>

<@jmesa id="componentTable"/>

</body>
</html>