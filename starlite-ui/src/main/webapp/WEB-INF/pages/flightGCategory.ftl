<#include "/starlite.ftl">
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
<title>Aircraft</title>
</head>
<body>

  <@enableDatePickers/>
  <@enableHelp/>
  
<#if user.hasPermission("UserAdmin")>
<div id="toolbar">
	
	  <#if edited = false>
	  <form autocomplete="off"  action="flightGCategory!save.action" enctype="multipart/form-data" method="post">
	    
	  </#if> 
	<div style="float:left;padding:10px;margin:10px;border:1px solid silver;width:500px;height:150px">	
    
	<#if tab="flight OFP">
     	<fieldset>
		<legend>G-Category</legend>
		
		<div class="fm-opt">
		  <label for="gVal">G-Category:</label>
          <input name="gVal" type="text" value="${gVal!}"/>	
		</div>
		<div style="float:left;margin-left:256px">
		<button class="smooth" style="margin-left:0;padding:2px 10px 2px 7px;" onclick="return confirm('Are you sure you want to save this G-Category?')" type="submit"><img src="images/icons/add.png"/>Save</button>
		</div>    
		    
    	</fieldset>       

	
		<hr class="clear"/>  
         
       </form>
    </#if>
    </div>
     

<br/>
</#if>

</body>
</html>
