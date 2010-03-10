<#include "/starlite.ftl">
<#setting number_format = "######.##########"/>

<html>
<head>
<link rel="stylesheet" type="text/css" href="${request.contextPath}/styles/jmesa.css">
<script type="text/javascript" src="${request.contextPath}/js/jmesa.js"></script>
<script>

function setCode(index){
    if(index == 1){
    $("#code").val("MS");
    }
    if(index == 2){
    $("#code").val("WS");
    }
    if(index == 3){
    $("#code").val("DR");
    }
}

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

<@subTabs/>
<#assign currentUser = Session.userObj>

    <form action="store!save.action" method="POST" class="smart" onsubmit="return validate();" style="clear:left;width:1150px;">
    <input type="hidden" name="id" value="${id!}"/>
    <input type="hidden" name="store.id" value="${id!}"/>
    
    <fieldset>
        <#if id?exists >
        <legend>Edit Store</legend>
        <#else>
        <legend>Add Store</legend>
        </#if>
        <div class="fm-opt">
            <label for="store.code">Primary Indentifier:</label>
            <input id="code" name="store.code" type="text" value="${store.code!}"/>
        </div>
        <div class="fm-opt">
            <label for="store.seccode">Secondary Indentifier:</label>
            <input name="store.seccode" type="text" value="${store.seccode!}"/>
        </div>
        <div class="fm-opt">
            <label for="store.description">Description:</label>
            <input name="store.description" type="text" value="${store.description!}"/>
        </div>
        <div class="fm-opt">
            <label for="store.type">Type:</label>
            <select onchange="setCode(this.selectedIndex);" name="store.type">
                <#if store.type?exists>
                <option <#if store.type.equals("Aircraft")>selected</#if> >Aircraft
                <option <#if store.type.equals("Depot")>selected</#if> >Depot
                <option <#if store.type.equals("Mobile")>selected</#if> >Mobile
                <option <#if store.type.equals("Store")>selected</#if> >Store
                <option <#if store.type.equals("Workshop")>selected</#if> >Workshop 
                <#else>
                <option >Aircraft
                <option >Depot
                <option >Mobile
                <option >Store
                <option >Workshop
                </#if>
            </select>
        </div>
        
        <button type="submit" class="smooth" style="float:right; margin-right:10px; margin-bottom: 4px;"><img src="images/icons/pencil.png"/>Save</button>
        
        </fieldset>
    
    </form>
    
    <#if id?exists>   
    <br/> 
    <form action="store!edit.action" method="POST" class="smart" style="clear:left;width:1150px;">
    <input type="hidden" name="id" value="${id!}"/>
    <input type="hidden" name="store.id" value="${id!}"/>
    
    <div style="padding-left:10px;">
    <fieldset>
        <legend>Store Components</legend>
        
        <@jmesa id="component"/>
        
    </fieldset>
    </div>
    
    </form>
    </#if>


</body>
</html>