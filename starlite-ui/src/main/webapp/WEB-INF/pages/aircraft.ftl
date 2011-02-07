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
<style type="text/css"> 
span.oneRow 
{ 
display: inline; 
width: 1000px; 
}

label.small
{
float:left; 
margin-left:0px;
height:25px;
width: 70px; 
} 
label.vsmall
{
float:left; 
margin-left:0px;
padding-right:0px;
height:25px;
width: 40px; 
} 
select.small
{

float:left; 
margin-left:0px;
margin-right:0px;
height:20px;
width: 35px; 
} 
</style>



<body>



<#if user.hasPermission("UserAdmin")>
<div id="toolbar">
	<a href="${request.contextPath}/aircraft!addAircraft.action"><img src="${request.contextPath}/images/icons/add.png"/>Add</a>
		
	<#if tab="flight plans">
       <form action="flightPlan.action" enctype="multipart/form-data" method="post">
          <button class="smooth" style="margin-left:10px;padding:2px 10px 2px 7px;" onclick="return confirm('Would you like to create a new Flight Plan?')" type="submit"><img src="images/icons/add.png"/>Add New Flight Plan</button>
       </form>
    </#if>
    
    <#if tab="flight mismatches">
       <form action="aircraft!link.action" enctype="multipart/form-data" method="post">
          
          <#if flightActuals.isEmpty() = false>
          
		        <div style="margin-left:50px;">
		        <div style="float:left;padding:5px;margin:0px;border:1px solid silver;width:530px;height:20px;valign:center;">	
		          
                
                <label class="small" for="flightPlan">Flight Plan</label>	
	    		<select class="small" name="flightPlan">
	    		   <#list flightPlans as plans>
	    			  <option>${plans.flightPlanID}
	    		   </#list>
	    		</select>
	    		
	    		
	    		<label class="small"> link with </label>
	    	
	    		<label class="small" for="flightLog">Flight Log</label>	
	    		<select class="small" name="flightLog">
	    		   <#list flightLogs as logs>
	    			  <option>${logs.flightIDField}
	    		   </#list>
	    		</select>
	    		
	         
	     
	      <button class="smooth" style="margin-left:10px;padding:2px 10px 2px 7px;" onclick="return confirm('Would you like to link these two records to form a Flight Actual Record (Approvals)?')" type="submit"><img src="images/icons/add.png"/>Link Together</button>     
          </div>
          
         
          </div>
           </#if>
       </form>
    </#if>
    
	<hr class="clear"/>
   
</div>
<br/>
</#if>
 <!--div id="toolbar">
	<button id="addButton" disabled="disabled"><img src="${request.contextPath}/images/icons/add.png"/>Add</button>
	<button id="saveButton" onclick="javascript:setSaveToWorksheet('aircraftTable');onInvokeAction('aircraftTable','save_worksheet')" ${saveDisabled!}><img src="${request.contextPath}/images/icons/pencil.png"/>Save</button>
	<hr class="clear">
</div-->

<#if current = "aircraft">
<@jmesa id="aircraft"/>
</#if>

<#if current = "flight plans">
<@jmesa id="FlightPlan"/>
</#if>

<#if current = "flight actuals">
<@jmesa id="FlightActuals"/>
</#if>

<div style="margin-left:0px;"> 
<#if current = "flight mismatches">
<@jmesa id="FlightActuals"/>
</#if>
</div>

</body>
</html>
