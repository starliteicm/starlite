<#include "/starlite.ftl">
<#setting number_format = "######.##"/>

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
<title>Components</title>
</head>
<body>

<#if user.hasPermission("UserAdmin")>
<div id="toolbar">
    
    
    
    
    <#if tab=="componentUpload">
    <br />
    <div style="margin-left:-9px;"> 
    <@subTabs/>
    </div>
    <br />
    <br />
    <div style="float:left;padding:5px;margin-left:10px;margin-top:0px;border:1px solid silver;width:1000px;height:300px;valign:left;">
    <form action="component!upload.action" enctype="multipart/form-data" method="post">
    <div style="margin-left:11px;"> 
    <br />
    <select style="float:left;margin-left:0px;height:24px;" name="location" >
    <#list stores as store>
    <option value="${store.location}" >${store.location}</option>
    </#list>
    </select>
    </div>
    <br />
    
    <div style="margin-left:11px;">
    <br />
    <input style="margin-left:0px;margin-top:0px;" type="file" style="float:left" name="document" id="document"/>
    <input type="hidden" name="id" value="${id!}"/>
	<input type="hidden" name="component.id" value="${id!}"/>
	</div>
	<br />
	
    <div style="margin-left:11px;"> 
    <button class="smooth" style="margin-left:0px;padding:2px 10px 2px 7px;" onclick="return confirm('Are you sure you wish to upload these components to this Store?')" type="submit"><img src="images/icons/arrow_up.png"/>Upload Components</button>
    </div>
    </form>
    </div>
   
   </#if>
    
    <hr class="clear"/>
</div>
<br/>
</#if>

<#if current="active">

<@jmesa id="active"/>  
</#if>
<#if current="deactive">
<@jmesa id="deactive"/>
</#if>
 
<#if tab == "componentAdd">
<@subTabs/>
</#if>


</body>
</html>