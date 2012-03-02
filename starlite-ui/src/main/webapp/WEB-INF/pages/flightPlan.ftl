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
	
	 <div style="margin-left:550px;">
	 
	  <#if edited = true>
		    <form autocomplete="off"  action="flightPlan!editedSave.action?id=${id}&edited=true" enctype="multipart/form-data" method="post">
		    <button class="smooth" style="margin-left:0;padding:2px 10px 2px 7px;" onclick="return confirm('Are you sure you want to save this Flight Plan?')" type="submit"><img src="images/icons/add.png"/>Update</button>
      </#if>
      <#if edited = false>
	  <form autocomplete="off"  action="flightPlan!save.action" enctype="multipart/form-data" method="post">
	    <button class="smooth" style="margin-left:0;padding:2px 10px 2px 7px;" onclick="return confirm('Are you sure you want to save this Flight Plan?')" type="submit"><img src="images/icons/add.png"/>Save</button>
	  </#if>
     </div>
    
	<div style="float:left;padding:10px;margin:10px;border:1px solid silver;width:1100px;height:370px">	
    
	<#if tab="flight plans">
	
	
       
           
         <div style="float:left; width: 360px;">
         
		<fieldset>
		<legend>General</legend>
		<div class="fm-opt">
				<label for="flightPlan.customer">Customer:</label>
				<input name="flightPlan.customer" type="text" value="${flightPlan.customer}"/>	
		</div>
		<div class="fm-opt">
				<label for="flightPlan.flightType">Flight Type:</label>
				<input name="flightPlan.flightType" type="text" value="${flightPlan.flightType}"/>	
		</div>
	    <div class="fm-opt">
				<label for="flightPlan.flightDateField">Flight Date:</label>
				<input name="flightPlan.flightDateField" id="flightPlan.flightDateField" class="date-pick" type="text" value="${flightPlan.flightDateField}"/>
		</div>
		<div class="fm-opt">
				<label for="flightPlan.invoiceNo">Invoice No:</label>
				<input name="flightPlan.invoiceNo" type="text" value="${flightPlan.invoiceNo}"/>	
		</div>
		<div class="fm-opt">
				<label for="flightPlan.ETD">ETD:</label>
				<input name="flightPlan.ETD" type="text" value="${flightPlan.ETD}"/>	
		</div>
	    <div class="fm-opt">
				<label for="flightPlan.flightNumber">Flight No:</label>
				<input name="flightPlan.flightNumber" type="text" value="${flightPlan.flightNumber}"/>	
		</div>
		<div class="fm-opt">
                <label for="flightPlan.registrationField">A/C REG:</label>
	    		<select name="flightPlan.registrationField">
	    		   <#list aircrafts as ac> 	
	    		   <#if ac = flightPlan.registrationField>	  
	    		   	<option SELECTED>${ac}
	    		   </#if>
	    		   <#if ac != flightPlan.registrationField>
	    		    <option>${ac}
	    		    </#if>
	    		   </#list>
	    		</select>
	    		<br/>
        </div>
        <div class="fm-opt">
				<label for="flightPlan.flight_TypeField">A/C Type:</label>
	    		<select name="flightPlan.flight_TypeField">
	    		   <#list aircraftTypes as at>
	    			<#if at = flightPlan.flight_TypeField>	  
	    		   	<option SELECTED>${at}
	    		   </#if>
	    		   <#if at != flightPlan.flight_TypeField>
	    		    <option>${at}
	    		    </#if>
	    		   </#list>
	    		</select>
	    		<br/>	
		</div>
		<div class="fm-opt">
			    <div class="fm-opt">
				<div class="fm-opt">
				<label for="flightPlan.captainField">Captain:</label>
	    		<select name="flightPlan.captainField">
	    		   <#list pilots as person >
  	    			<#if person = flightPlan.captainField>	  
	    		   	<option SELECTED>${person}
	    		    </#if>
	    		    <#if person != flightPlan.captainField>
	    		    <option>${person}
	    		    </#if>
	    		   </#list>
	    		</select>
	            </div>
			</div>
		</div>
		<div class="fm-opt">
			    <div class="fm-opt">
				<div class="fm-opt">
				<label for="flightPlan.first_OfficerField">Co-Pilot:</label>
	    		<select name="flightPlan.first_OfficerField">
	    		   <#list pilots as person >
	    			<#if person = flightPlan.first_OfficerField>	  
	    		   	<option SELECTED>${person}
	    		    </#if>
	    		    <#if person != flightPlan.first_OfficerField>
	    		    <option>${person}
	    		    </#if>
	    		   </#list>
	    		</select>
	            </div>
			</div>
		</div>
		<div class="fm-opt">
			    <div class="fm-opt">
				<div class="fm-opt">
				<label for="flightPlan.flightEngineer">Flight Engineer:</label>
	    		<select name="flightPlan.flightEngineer">
	    		   <#list engineers as person >
	    			<#if person = flightPlan.flightEngineer>	  
	    		   	<option SELECTED>${person}
	    		    </#if>
	    		    <#if person != flightPlan.flightEngineer>
	    		    <option>${person}
	    		    </#if>
	    		   </#list>
	    		</select>
	            </div>
			</div>
		</div>
	    
		<br/>
		<br/>
		<br/>
    	</fieldset>       
		</div>
	
		
		<div style="float:left; width: 360px;">
		<fieldset>
			<legend>Navigation Planning</legend>
		    <div class="fm-opt">
                <label for="flightPlan.start_LocationField">From Destination:</label>
				<input name="flightPlan.start_LocationField" type="text" value="${flightPlan.start_LocationField}"/>
            </div>
            <div class="fm-opt">
                <label for="flightPlan.end_LocationField">To Destination:</label>
				<input name="flightPlan.end_LocationField" type="text" value="${flightPlan.end_LocationField}"/>
            </div>
            <div class="fm-opt">
                <label for="flightPlan.GPS_DistanceField">Distance:</label>
				<input name="flightPlan.GPS_DistanceField" type="text" value="${flightPlan.GPS_DistanceField}"/>
            </div>
		</fieldset>
				
		<fieldset>
		    <legend>Fuel Plan</legend>
		    <div class="fm-opt">
                <label for="flightPlan.starting_FuelField">Start Fuel:</label>
				<input name="flightPlan.starting_FuelField" type="text" value="${flightPlan.starting_FuelField}"/>
            </div>
        </fieldset>     
        
        <fieldset>
		    <legend>Schedule</legend>
		    <div class="fm-opt">
                <label for="flightPlan.takeoffField">Take Off Time:</label>
				<input name="flightPlan.takeoffField" type="text" value="${flightPlan.takeoffField}"/>
            </div>
             <div class="fm-opt">
                <label for="flightPlan.landingField">Landing Time:</label>
				<input name="flightPlan.landingField" type="text" value="${flightPlan.landingField}"/>
            </div>
        </fieldset>     
		</div>
		
		<div style="float:left; width: 360px;">
		<fieldset>
			<legend>Technical</legend>
			<div class="fm-opt">
                <label for="flightPlan.ACServiceType">Type of Service</label>
	    		<select name="flightPlan.ACServiceType">
	    		   <#list typeOfService as service>
	    			<#if service = flightPlan.ACServiceType>	  
	    		   	<option SELECTED>${service}
	    		    </#if>
	    		    <#if service != flightPlan.ACServiceType>
	    		    <option>${service}
	    		    </#if>
	    		   </#list>
	    		</select>	
            </div>
            <div class="fm-opt">
                <label for="flightPlan.ACHrsToNextService">HRS to next Service:</label>
				<input name="flightPlan.ACHrsToNextService" type="text" value="${flightPlan.ACHrsToNextService}"/>
            </div>
            
            <br />
            		
		</fieldset>
		<fieldset>
			<legend>Loading</legend>
			<div class="fm-opt">
                <label for="flightPlan.PAX">PAX (KG):</label>
				<input name="flightPlan.PAX" type="text" value="${flightPlan.PAX}"/>
            </div>
            <div class="fm-opt">
                <label for="flightPlan.freight">Freight (KG):</label>
				<input name="flightPlan.freight" type="text" value="${flightPlan.freight}"/>
            </div>
            <div class="fm-opt">
                <label for="flightPlan.totalLoad">Total Load (KG):</label>
				<input name="flightPlan.totalLoad" type="text" value="${flightPlan.totalLoad}"/>
            </div>
		</fieldset>
		
		</div>
	
		<hr class="clear"/>  
         
       </form>
    </#if>
    </div>
     
</div>
<br/>
</#if>

</body>
</html>
