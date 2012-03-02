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
<title>Flight Actuals Approval</title>
</head>
<style type="text/css">

.buttonStyle {
display: inline;
background-color: transparent;
background-repeat: no-repeat;
border: 0;
margin: 0px auto;
padding: 0px 0px 0 0;
text-align: center;
font-family: Helvetica, Calibri, Arial, sans-serif;
font-size: 100%;
font-weight: bold;
text-decoration: none;
}

.buttonStyle:hover{
background-position: 0 -1px;
color: #FFFFFF;
}
</style>
<body>

  <@enableDatePickers/>
  <@enableHelp/>
  
<#if user.hasPermission("UserAdmin")>
<div id="toolbar">
	
	 <div style="margin-left:10px;">
	  
	 <form action="flightActualsApproval!updateStatus.action?typeId=${typeId}" enctype="multipart/form-data" method="post">
	    
	    
	    <div style="margin-left:0px;">
	        
	    <#if (status > 0 && status < 3 )>
	        <input type="hidden" name="id" value="4"/>
		    <button class="buttonStyle" style="background-image:url(images/indigoApproval/approved_red.png);width:111px;height:48px" name="id" value="4" onclick="return confirm('Are you sure that you want to APPROVE this flight actual?')" type="submit"  />
	    </#if>
	    <#if (status > 2 ) >
            <button class="buttonStyle" disabled="disabled" style="background-image:url(images/indigoApproval/approved_green.png);width:111px;height:48px" name="id" />
        </#if>
        </div>
        
        <div style="margin-left:111px;">
        <#if (status > 0 && status < 5 )>
            <input type="hidden"  name="id" value="6"/>
	        <button class="buttonStyle" style="background-image:url(images/indigoApproval/ready_red.png);width:111px;height:48px" name="id" value="6" onclick="return confirm('Are you sure that you want to make this flight actual READY?')" type="submit"  />
	    </#if>
	    <#if (status > 4 ) >
	        <button class="buttonStyle" disabled="disabled" style="background-image:url(images/indigoApproval/ready_green.png);width:111px;height:48px" name="id"  />
	    </#if>
	    </div>
	    
	    <div style="margin-left:240px;">
	    <#if (status > 0 && status < 7 )>
	         <input type="hidden"  name="id" value="7"/>
	         <button class="buttonStyle" style="background-image:url(images/indigoApproval/released_red.png);width:111px;height:48px" name="id" value="6" onclick="return confirm('Are you sure that you want to RELEASE this flight actual?')" type="submit"  />
	    </#if>
	         <#if (status > 6 ) >
	         <button class="buttonStyle" disabled="disabled" style="background-image:url(images/indigoApproval/released_green.png);width:111px;height:48px" name="id"  />
	    </#if>
	    </div>
	    
	    <div style="margin-left:240px;">
	    <label>Action</label>
	    <input  name="id" value="${selectedFlightActual.getFlightActualStatus().getFlightActualStatusValue()}"/>
	    </div">
	    
	    
	    
	</form>
    </div>
    <form autocomplete="off" action="flightActualsApproval!save.action?typeId=${typeId}" enctype="multipart/form-data" method="post">
	<div style="float:left;padding:10px;margin:10px;border:1px solid silver;width:1100px;height:370px">	
    
	<#if tab="flight plans">
	
	
       
           
         <div style="float:left; width: 1000px;">
         
		<fieldset>
		<legend>Actuals</legend>
		
		<span>
		        
		       <input type="text" value="Flight Date"  readonly="readonly"  STYLE=" background-color: #F5F6CE;"  size="10" />
		       <input type="text" value="A/C REG"  readonly="readonly"  STYLE=" background-color: #F5F6CE;"  size="10" />
		       <input type="text" value="From Destination"  readonly="readonly"  STYLE=" background-color: #F5F6CE;"  size="10" />
		       <input type="text" value="To Destination"  readonly="readonly"  STYLE=" background-color: #F5F6CE;"  size="10" />
		       <input type="text" value="Distance"  readonly="readonly"  STYLE=" background-color: #F5F6CE;"  size="10" />
		       <input type="text" value="Captain"  readonly="readonly"  STYLE=" background-color: #F5F6CE;"  size="10" />
		       <input type="text" value="Co-Pilot"  readonly="readonly"  STYLE=" background-color: #F5F6CE;"  size="10" />
		       <input type="text" value="Flight Engineer"  readonly="readonly"  STYLE=" background-color: #F5F6CE;"  size="10" />
		       <input type="text" value="Start Fuel"  readonly="readonly"  STYLE=" background-color: #F5F6CE;"  size="10" />
		       <input type="text" value="Take Off"  readonly="readonly"  STYLE=" background-color: #F5F6CE;"  size="10" />
		       <input type="text" value="Landing"  readonly="readonly"  STYLE=" background-color: #F5F6CE;"  size="10" />
		       
	    </span>
	    <br/>
	    <span>
				<input name="flightLog.flightDateField" id="flightLog.flightDateField"  type="text" value="${flightLog.getFlightDateField()}"  readonly="readonly"  STYLE=" background-color: #F2F2F2;"  size="10" />
				<input name="flightLog.registrationField" id="flightLog.registrationField" type="text" value="${flightLog.getRegistrationField()}"  readonly="readonly" STYLE=" background-color: #F2F2F2;"  size="10"/>
				<input name="flightLog.start_LocationField" type="text" value="${flightLog.getStart_LocationField()}"  readonly="readonly" STYLE=" background-color: #F2F2F2;"  size="10"/>
				<input name="flightLog.end_LocationField" type="text" value="${flightLog.getEnd_LocationField()}" readonly="readonly" STYLE=" background-color: #F2F2F2;"  size="10"/>
				<input name="flightLog.GPS_DistanceField" type="text" value="${flightLog.getGPS_DistanceField()}" readonly="readonly" STYLE=" background-color: #F2F2F2;text-align:right;"  size="10"/>
				<input name="flightLog.captainField" id="flightLog.captainField" type="text" value="${flightLog.getCaptainField()}"  readonly="readonly" STYLE=" background-color: #F2F2F2;"  size="10"/>
				<input name="flightLog.first_OfficerField" id="flightLog.first_OfficerField" type="text" value="${flightLog.getFirst_OfficerField()}"  readonly="readonly" STYLE=" background-color: #F2F2F2;"  size="10"/>
				<input name="flightLog.cabin_CrewField" id="flightLog.cabin_CrewField" type="text" value="${flightLog.getCabin_CrewField()}"  readonly="readonly" STYLE=" background-color: #F2F2F2;"  size="10"/>
				<input name="flightLog.starting_FuelField" type="text" value="${flightLog.getStarting_FuelField()}" readonly="readonly" STYLE=" background-color: #F2F2F2;text-align:right;"  size="10"/>
				<input name="flightLog.takeoffField" type="text" value="${flightLog.getTakeoffField()}" readonly="readonly" STYLE=" background-color: #F2F2F2;"  size="10"/>
				<input name="flightLog.landingField" type="text" value="${flightLog.getLandingField()}" readonly="readonly" STYLE=" background-color: #F2F2F2;"  size="10"/>
		</span>
		<br />
		<br/>
        </fieldset>
		
		<fieldset>
		<legend>Flight Plan</legend>
		
		
		<#if (status < 3)>
	    <span>
	    
				<input  name="flightPlan.flightDateField" id="flightPlan.flightDateField"  type="text" value="${flightPlan.flightDateField}"    STYLE=" background-color: #FFFFFF;"  size="10" />
				<input  name="flightPlan.registrationField" id="flightPlan.registrationField" type="text" value="${flightPlan.registrationField}"  STYLE=" background-color: #FFFFFF;"  size="10"/>
				<input  name="flightPlan.start_LocationField" type="text" value="${flightPlan.start_LocationField}"  STYLE=" background-color: #FFFFFF;"  size="10"/>
				<input  name="flightPlan.end_LocationField" type="text" value="${flightPlan.end_LocationField}" STYLE=" background-color: #FFFFFF;"  size="10"/>
				<input  name="flightPlan.GPS_DistanceField" type="text" value="${flightPlan.GPS_DistanceField}" STYLE=" background-color: #FFFFFF;text-align:right;"  size="10"/>
				<input  name="flightPlan.captainField" id="flightPlan.captainField" type="text" value="${flightPlan.captainField}"  STYLE=" background-color: #FFFFFF;"  size="10"/>
				<input  name="flightPlan.first_OfficerField" id="flightPlan.first_OfficerField" type="text" value="${flightPlan.first_OfficerField}"  STYLE=" background-color: #FFFFFF;"  size="10"/>
				<input  name="flightPlan.flightEngineer" id="flightPlan.flightEngineer" type="text" value="${flightPlan.flightEngineer}"  STYLE=" background-color: #FFFFFF;"  size="10"/>
				<input  name="flightPlan.starting_FuelField" type="text" value="${flightPlan.starting_FuelField}" STYLE=" background-color: #FFFFFF;text-align:right;"  size="10"/>
				<input  name="flightPlan.takeoffField" type="text" value="${flightPlan.takeoffField}" STYLE=" background-color: #FFFFFF;"  size="10"/>
				<input  name="flightPlan.landingField" type="text" value="${flightPlan.landingField}" STYLE=" background-color: #FFFFFF;"  size="10"/>
				
		</span>
		
		<div style="margin-left:900px;">
		<br />
		<button class="smooth" style="margin-left:10px;padding:2px 15px 2px 7px;" onclick="return confirm('Are you sure that you want to save these changes?')" type="submit"><img src="images/icons/pencil.png"/>Save</button>
		</div>
		</#if>
		<#if (status > 3)>
		
		        <input readonly="readonly" name="flightPlan.flightDateField" id="flightPlan.flightDateField"  type="text" value="${flightPlan.flightDateField}"    STYLE=" background-color: #F2F2F2;"  size="10" />
				<input readonly="readonly" name="flightPlan.registrationField" id="flightPlan.registrationField" type="text" value="${flightPlan.registrationField}"  STYLE=" background-color: #F2F2F2;"  size="10"/>
				<input readonly="readonly" name="flightPlan.start_LocationField" type="text" value="${flightPlan.start_LocationField}"  STYLE=" background-color: #F2F2F2;"  size="10"/>
				<input readonly="readonly" name="flightPlan.end_LocationField" type="text" value="${flightPlan.end_LocationField}" STYLE=" background-color: #F2F2F2;"  size="10"/>
				<input readonly="readonly" name="flightPlan.GPS_DistanceField" type="text" value="${flightPlan.GPS_DistanceField}" STYLE=" background-color: #F2F2F2;text-align:right;"  size="10"/>
				<input readonly="readonly" name="flightPlan.captainField" id="flightPlan.captainField" type="text" value="${flightPlan.captainField}"  STYLE=" background-color: #F2F2F2;"  size="10"/>
				<input readonly="readonly" name="flightPlan.first_OfficerField" id="flightPlan.first_OfficerField" type="text" value="${flightPlan.first_OfficerField}"  STYLE=" background-color: #F2F2F2;"  size="10"/>
				<input readonly="readonly" name="flightPlan.flightEngineer" id="flightPlan.flightEngineer" type="text" value="${flightPlan.flightEngineer}"  STYLE=" background-color: #F2F2F2;"  size="10"/>
				<input readonly="readonly" name="flightPlan.starting_FuelField" type="text" value="${flightPlan.starting_FuelField}" STYLE=" background-color: #F2F2F2;text-align:right;"  size="10"/>
				<input readonly="readonly" name="flightPlan.takeoffField" type="text" value="${flightPlan.takeoffField}" STYLE=" background-color: #F2F2F2;"  size="10"/>
				<input readonly="readonly" name="flightPlan.landingField" type="text" value="${flightPlan.landingField}" STYLE=" background-color: #F2F2F2;"  size="10"/>
				
		
		</#if>
		<br />
		<br/>
        </fieldset>
		
		<fieldset>
		<legend>Variance</legend>
		
		<span>
				<input name="flightVariance.flightDateField" id="flightVariance.flightDateField"  type="text" value="${flightVariance.getFlightDateField()}"  readonly="readonly"  STYLE=" background-color: #F2F2F2;"  size="10" />
				<input name="flightVariance.registrationField" id="flightVariance.registrationField" type="text" value="${flightVariance.getRegistrationField()}"  readonly="readonly" STYLE=" background-color: #F2F2F2;"  size="10"/>
				<input name="flightVariance.start_LocationField" type="text" value="${flightVariance.getStart_LocationField()}"  readonly="readonly" STYLE=" background-color: #F2F2F2;"  size="10"/>
				<input name="flightVariance.end_LocationField" type="text" value="${flightVariance.getEnd_LocationField()}" readonly="readonly" STYLE=" background-color: #F2F2F2;"  size="10"/>
				<input name="flightVariance.GPS_DistanceField" type="text" value="${flightVariance.getGPS_DistanceField()}" readonly="readonly" STYLE=" background-color: #F2F2F2;text-align:right;"  size="10"/>
				<input name="flightVariance.captainField" id="flightVariance.captainField" type="text" value="${flightVariance.getCaptainField()}"  readonly="readonly" STYLE=" background-color: #F2F2F2;"  size="10"/>
				<input name="flightVariance.first_OfficerField" id="flightVariance.first_OfficerField" type="text" value="${flightVariance.getFirst_OfficerField()}"  readonly="readonly" STYLE=" background-color: #F2F2F2;"  size="10"/>
				<input name="flightVariance.flightEngineer" id="flightVariance.flightEngineer" type="text" value="${flightVariance.getFlightEngineer()}"  readonly="readonly" STYLE=" background-color: #F2F2F2;"  size="10"/>
				<input name="flightVariance.starting_FuelField" type="text" value="${flightVariance.getStarting_FuelField()}" readonly="readonly" STYLE=" background-color: #F2F2F2;text-align:right;"  size="10"/>
				<input name="flightVariance.takeoffField" type="text" value="${flightVariance.getTakeoffField()}" readonly="readonly" STYLE=" background-color: #F2F2F2;"  size="10"/>
				<input name="flightVariance.landingField" type="text" value="${flightVariance.getLandingField()}" readonly="readonly" STYLE=" background-color: #F2F2F2;"  size="10"/>
				
		</span>
		<br />
		<br/>
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
