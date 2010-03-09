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
</script>
<title>Stores</title>
</head>
<body>

<#if user.hasPermission("UserAdmin")>
<div id="toolbar">
    <a href="${request.contextPath}/store!edit.action"><img src="${request.contextPath}/images/icons/add.png"/>Add</a>
    <hr class="clear"/>
</div>
<br/>
</#if>

<@jmesa id="store"/>

</body>
</html>